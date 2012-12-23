package ljdp.minechem.common.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.client.FMLClientHandler;

import buildcraft.api.core.Position;
import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.blueprint.BlueprintBlock;
import ljdp.minechem.common.blueprint.BlueprintBlock.Type;
import ljdp.minechem.common.blueprint.BlueprintFusion;
import ljdp.minechem.common.blueprint.MinechemBlueprint;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityBlueprintProjector extends TileEntity {
	
	private static int air;
	MinechemBlueprint blueprint;
	
	enum BlockStatus {
		CORRECT, INCORRECT
	}
	
	boolean isComplete = false;
	Integer[][][] structure;
	
	public TileEntityBlueprintProjector() {
		this.blueprint = new BlueprintFusion();
		this.structure = this.blueprint.getStructure();
	}
	
	@Override
	public void updateEntity() {
		if(!isComplete)
			projectBlueprint();
	}
	
	private void projectBlueprint() {
		int facing = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection direction = ForgeDirection.NORTH;
		Position position = new Position(xCoord, yCoord, zCoord, direction);
		position.moveForwards(blueprint.zSize + 1);
		position.moveLeft(Math.floor(blueprint.xSize / 2));
		
		boolean shouldProjectGhostBlocks = true;
		
		int totalIncorrectCount = blueprint.getTotalSize();
		for(int y = 0; y < blueprint.ySize; y++) {
			int horizontalIncorrectCount = blueprint.getHorizontalSliceSize();
			Integer[][] blueprintSlice   = blueprint.getHorizontalSlice(y);
			
			for(int x = 0; x < blueprint.xSize; x++) {
				for(int z = 0; z < blueprint.zSize; z++) {
					if(shouldProjectGhostBlocks) {
						BlockStatus blockStatus = projectGhostBlock(x, y, z, position);
						if(blockStatus == BlockStatus.CORRECT) {
							horizontalIncorrectCount--;
							totalIncorrectCount--;
						}
					} else {
						destroyGhostBlock(x, y, z, position);
					}
				}
			}
			if(horizontalIncorrectCount != 0)
				shouldProjectGhostBlocks = false;
		}
		
		if(totalIncorrectCount == 0 && !isComplete) {
			isComplete = true;
			buildStructure(position);
		}
	}

	private void buildStructure(Position position) {
		Integer[][][] resultStructure = blueprint.getResultStructure();
		HashMap<Integer,BlueprintBlock> blockLookup = blueprint.getBlockLookup();
		
		TileEntity managerTileEntity = buildManagerBlock(position);
		
		for(int x = 0; x < blueprint.xSize; x++) {
			for(int y = 0; y < blueprint.ySize; y++) {
				for(int z = 0; z < blueprint.zSize; z++) {
					int structureId = resultStructure[y][x][z];
					setBlock(x, y, z, position, structureId, blockLookup, managerTileEntity);
				}
			}
		}
		
		if(worldObj.isRemote)
			FMLClientHandler.instance().getClient().thePlayer.addChatMessage("Structure Built.");
	}

	private TileEntity buildManagerBlock(Position position) {
		int x = (int) (blueprint.getManagerPosX() + position.x);
		int y = (int) (blueprint.getManagerPosY() + position.y);
		int z = (int) (blueprint.getManagerPosZ() + position.z);
		BlueprintBlock managerBlock = blueprint.getManagerBlock();
		worldObj.setBlockAndMetadataWithNotify(x, y, z, managerBlock.block.blockID, managerBlock.metadata);
		return worldObj.getBlockTileEntity(x, y, z);
	}

	private void setBlock(int x, int y, int z, Position position,
			int structureId, HashMap<Integer, BlueprintBlock> blockLookup, TileEntity managerTileEntity)
	{
		x = (int) (x + position.x);
		y = (int) (y + position.y);
		z = (int) (z + position.z);
		if(structureId == air) {
			worldObj.setBlock(x, y, z, 0);
		} else {
			BlueprintBlock blueprintBlock = blockLookup.get(structureId);
			if(blueprintBlock.type == Type.MANAGER)
				return;
			worldObj.setBlockAndMetadata(x, y, z, blueprintBlock.block.blockID, blueprintBlock.metadata);
			if(blueprintBlock.type == Type.PROXY) {
				TileEntityProxy proxy = (TileEntityProxy) worldObj.getBlockTileEntity(x, y, z);
				proxy.setManager(managerTileEntity);
			}
		}
	}

	private BlockStatus projectGhostBlock(int x, int y, int z, Position position) {
		int worldX = (int) (position.x + x);
		int worldY = (int) (position.y + y);
		int worldZ = (int) (position.z + z);
		Integer structureID = structure[y][x][z];
		int blockID = worldObj.getBlockId(worldX, worldY, worldZ);
		if(structureID == air) {
			if(blockID == air)
				return BlockStatus.CORRECT;
			else
				return BlockStatus.INCORRECT;
		} else {
			HashMap<Integer,BlueprintBlock> lut = blueprint.getBlockLookup();
			BlueprintBlock blueprintBlock = lut.get(structureID);
			if(blockID == air) {
				worldObj.setBlockAndMetadata(worldX, worldY, worldZ, MinechemBlocks.ghostBlock.blockID, 1);
				return BlockStatus.INCORRECT;
			} else if(blockID == blueprintBlock.block.blockID
					&& worldObj.getBlockMetadata(worldX, worldY, worldZ) == blueprintBlock.metadata) {
				return BlockStatus.CORRECT;
			} else {
				return BlockStatus.INCORRECT;
			}
		}
	}
	
	private void destroyGhostBlock(int x, int y, int z, Position position) {
		int worldX = (int) (position.x + x);
		int worldY = (int) (position.y + y);
		int worldZ = (int) (position.z + z);
		int blockID = worldObj.getBlockId(worldX, worldY, worldZ);
		if(blockID == MinechemBlocks.ghostBlock.blockID) {
			worldObj.setBlock(worldX, worldY, worldZ, 0);
		}
	}
	
	private int getGhostBlockType(int blockID) {
		return 0;
	}

	public boolean isPowered() {
		//TODO: stub method.
		return true;
	}
}
