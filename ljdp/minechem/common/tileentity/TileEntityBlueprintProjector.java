package ljdp.minechem.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

import buildcraft.api.core.Position;
import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.blueprint.BlueprintBlock;
import ljdp.minechem.common.blueprint.BlueprintBlock.Type;
import ljdp.minechem.common.blueprint.BlueprintFusion;
import ljdp.minechem.common.blueprint.MinechemBlueprint;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.sound.LoopingSound;
import ljdp.minechem.common.utils.DirectionMultiplier;
import ljdp.minechem.common.utils.LocalPosition;
import ljdp.minechem.common.utils.LocalPosition.Pos3;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityBlueprintProjector extends TileEntity implements IInventory {
	
	private static int air;
	MinechemBlueprint blueprint;
	
	enum BlockStatus {
		CORRECT, INCORRECT
	}
	
	boolean isComplete = false;
	Integer[][][] structure;
	LoopingSound projectorSound;
	private ItemStack[] inventory;
	
	public TileEntityBlueprintProjector() {
		this.projectorSound = new LoopingSound("ljdp.minechem.projector", 20);
		this.projectorSound.setVolume(.2F);
		this.inventory = new ItemStack[getSizeInventory()];
	}
	
	@Override
	public void updateEntity() {
		if(blueprint != null && !isComplete) {
			projectBlueprint();
			this.projectorSound.play(worldObj, xCoord, yCoord, zCoord);
		}
	}
	
	private void projectBlueprint() {
		int facing = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection direction = MinechemHelper.getDirectionFromFacing(facing);
		LocalPosition position = new LocalPosition(xCoord, yCoord, zCoord, direction);
		position.moveForwards(blueprint.zSize + 1);
		position.moveLeft(Math.floor(blueprint.xSize / 2));
		boolean shouldProjectGhostBlocks = true;
		int totalIncorrectCount = blueprint.getTotalSize();
		
		for(int x = 0; x < blueprint.xSize; x ++) {
			int verticalIncorrectCount = blueprint.getVerticalSliceSize();
			Integer[][] verticalSlice = blueprint.getVerticalSlice(x);
			for(int y = 0; y < blueprint.ySize; y++) {
				for(int z = 0; z < blueprint.zSize; z++) {
					if(shouldProjectGhostBlocks) {
						BlockStatus blockStatus;
						if(isManagerBlock(x, y, z)) 
							blockStatus = BlockStatus.CORRECT;
						else
							blockStatus = projectGhostBlock(x, y, z, position);
						if(blockStatus == BlockStatus.CORRECT) {
							verticalIncorrectCount--;
							totalIncorrectCount--;
						}
					} else {
						destroyGhostBlock(x, y, z, position);
					}
				}
			}
			if(verticalIncorrectCount != 0)
				shouldProjectGhostBlocks = false;
		}
		
		if(totalIncorrectCount == 0 && !isComplete) {
			isComplete = true;
			buildStructure(position);
		}
	}

	private void buildStructure(LocalPosition position) {
		Integer[][][] resultStructure = blueprint.getResultStructure();
		HashMap<Integer,BlueprintBlock> blockLookup = blueprint.getBlockLookup();
		
		TileEntity managerTileEntity = buildManagerBlock(position);
		
		for(int x = 0; x < blueprint.xSize; x++) {
			for(int y = 0; y < blueprint.ySize; y++) {
				for(int z = 0; z < blueprint.zSize; z++) {
					if(isManagerBlock(x, y, z))
						continue;
					int structureId = resultStructure[y][x][z];
					setBlock(x, y, z, position, structureId, blockLookup, managerTileEntity);
				}
			}
		}
		
		if(worldObj.isRemote)
			FMLClientHandler.instance().getClient().thePlayer.addChatMessage("Structure Built.");
	}
	
	private boolean isManagerBlock(int x, int y, int z) {
		return x == blueprint.getManagerPosX() && y == blueprint.getManagerPosY() && z == blueprint.getManagerPosZ();
	}

	private TileEntity buildManagerBlock(LocalPosition position) {
		BlueprintBlock managerBlock = blueprint.getManagerBlock();
		if(managerBlock != null) {
			DirectionMultiplier multiplier = DirectionMultiplier.map.get(position.orientation);
			Pos3 worldPos = position.getLocalPos(blueprint.getManagerPosX(), blueprint.getManagerPosY(), blueprint.getManagerPosZ());
			worldObj.setBlockAndMetadataWithNotify(worldPos.x, worldPos.y, worldPos.z, managerBlock.block.blockID, managerBlock.metadata);
			return worldObj.getBlockTileEntity(worldPos.x, worldPos.y, worldPos.z);
		} else {
			return null;
		}
	}

	private void setBlock(int x, int y, int z, LocalPosition position,
			int structureId, HashMap<Integer, BlueprintBlock> blockLookup, TileEntity managerTileEntity)
	{
		Pos3 worldPos = position.getLocalPos(x, y, z);
		if(structureId == MinechemBlueprint.wildcard) {
			return;
		} if(structureId == air) {
			worldObj.setBlock(worldPos.x, worldPos.y, worldPos.z, 0);
		} else {
			BlueprintBlock blueprintBlock = blockLookup.get(structureId);
			if(blueprintBlock.type == Type.MANAGER)
				return;
			worldObj.setBlockAndMetadata(worldPos.x, worldPos.y, worldPos.z, blueprintBlock.block.blockID, blueprintBlock.metadata);
			if(blueprintBlock.type == Type.PROXY) {
				TileEntityProxy proxy = (TileEntityProxy) worldObj.getBlockTileEntity(worldPos.x, worldPos.y, worldPos.z);
				if(proxy != null)
					proxy.setManager(managerTileEntity);
			}
		}
	}

	private BlockStatus projectGhostBlock(int x, int y, int z, LocalPosition position) {
		Pos3 worldPos = position.getLocalPos(x, y, z);
		Integer structureID = structure[y][x][z];
		int blockID       = worldObj.getBlockId(worldPos.x, worldPos.y, worldPos.z);
		int blockMetadata = worldObj.getBlockMetadata(worldPos.x, worldPos.y, worldPos.z);
		if(structureID == MinechemBlueprint.wildcard) {
			return BlockStatus.CORRECT;
		} else if(structureID == air) {
			if(blockID == air)
				return BlockStatus.CORRECT;
			else
				return BlockStatus.INCORRECT;
		} else {
			HashMap<Integer,BlueprintBlock> lut = blueprint.getBlockLookup();
			BlueprintBlock blueprintBlock = lut.get(structureID);
			if(blockID == air) {
				createGhostBlock(worldPos.x, worldPos.y, worldPos.z, structureID);
				return BlockStatus.INCORRECT;
			} else if(blockID == blueprintBlock.block.blockID
					&& (blockMetadata == blueprintBlock.metadata || blueprintBlock.metadata == -1)) {
				return BlockStatus.CORRECT;
			} else {
				return BlockStatus.INCORRECT;
			}
		}
	}
	
	private void createGhostBlock(int x, int y, int z, int blockID) {
		worldObj.setBlockAndMetadata(x, y, z, MinechemBlocks.ghostBlock.blockID, 0);
		TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityGhostBlock) {
			TileEntityGhostBlock ghostBlock = (TileEntityGhostBlock) tileEntity;
			ghostBlock.setBlueprintAndID(blueprint, blockID);
		}
	}
	
	public void destroyProjection() {
		if(this.blueprint == null)
			return;
		int facing = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection direction = MinechemHelper.getDirectionFromFacing(facing);
		LocalPosition position = new LocalPosition(xCoord, yCoord, zCoord, direction);
		position.moveForwards(blueprint.zSize + 1);
		position.moveLeft(Math.floor(blueprint.xSize / 2));
		for(int x = 0; x < blueprint.xSize; x++) {
			for(int y = 0; y < blueprint.ySize; y++) {
				for(int z = 0; z < blueprint.zSize; z++) {
					destroyGhostBlock(x, y, z, position);
				}
			}
		}
	}
	
	private void destroyGhostBlock(int x, int y, int z, LocalPosition position) {
		Pos3 worldPos = position.getLocalPos(x, y, z);
		int blockID = worldObj.getBlockId(worldPos.x, worldPos.y, worldPos.z);
		if(blockID == MinechemBlocks.ghostBlock.blockID) {
			worldObj.setBlock(worldPos.x, worldPos.y, worldPos.z, 0);
		}
	}
	
	private int getGhostBlockType(int blockID) {
		return 0;
	}

	public boolean isPowered() {
		//TODO: stub method.
		return true;
	}
	
	public int getFacing() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

	public void setBlueprint(MinechemBlueprint blueprint) {
		if(blueprint != null) {
			this.blueprint = blueprint;
			this.structure = blueprint.getStructure();
		} else {
			destroyProjection();
			this.blueprint = null;
			this.structure = null;
			this.isComplete = false;
		}
	}
	
	public MinechemBlueprint takeBlueprint() {
		MinechemBlueprint blueprint = this.blueprint;
		setBlueprint(null);
		return blueprint;
	}

	public boolean hasBlueprint() {
		return this.blueprint != null;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return this.inventory[0];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack blueprintItem = this.inventory[0];
		this.inventory[0] = null;
		setBlueprint(null);
		return blueprintItem;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		this.inventory[0] = itemstack;
		if(itemstack != null) {
			MinechemBlueprint blueprint = MinechemItems.blueprint.getBlueprint(itemstack);
			setBlueprint(blueprint);
		}
	}

	@Override
	public String getInvName() {
		return "container.blueprintProjector";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		ItemStack blueprintStack = inventory[0];
		if(blueprintStack != null) {
			NBTTagCompound blueprintNBT = new NBTTagCompound();
			blueprintStack.writeToNBT(blueprintNBT);
			nbtTagCompound.setTag("blueprint", blueprintNBT);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		this.inventory = new ItemStack[getSizeInventory()];
		this.isComplete = false;
		NBTTagCompound blueprintNBT = (NBTTagCompound) nbtTagCompound.getTag("blueprint");
		if(blueprintNBT != null) {
			ItemStack blueprintStack = ItemStack.loadItemStackFromNBT(blueprintNBT);
			MinechemBlueprint blueprint = MinechemItems.blueprint.getBlueprint(blueprintStack);
			setBlueprint(blueprint);
			this.inventory[0] = blueprintStack;
		}
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tagCompound);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		this.readFromNBT(pkt.customParam1);
	}

	public MinechemBlueprint getBlueprint() {
		return this.blueprint;
	}
}
