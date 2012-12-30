package ljdp.minechem.common.tileentity;

import ljdp.minechem.common.blueprint.BlueprintBlock;
import ljdp.minechem.common.blueprint.MinechemBlueprint;
import ljdp.minechem.common.network.PacketGhostBlock;
import ljdp.minechem.common.network.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityGhostBlock extends TileEntity {
	
	private MinechemBlueprint blueprint;
	private int blockID;
	
	public void setBlueprintAndID(MinechemBlueprint blueprint, int blockID) {
		setBlueprint(blueprint);
		setBlockID(blockID);
		if(worldObj != null && !worldObj.isRemote)
			sendUpdatePacket();
	}
	
	public void setBlueprint(MinechemBlueprint blueprint) {
		this.blueprint = blueprint;
	}
	
	public MinechemBlueprint getBlueprint() {
		return this.blueprint;
	}
	
	public void setBlockID(int blockID) {
		this.blockID = blockID;
	}
	
	public int getBlockID() {
		return this.blockID;
	}

	public ItemStack getBlockAsItemStack() {
		BlueprintBlock blueprintBlock = this.blueprint.getBlockLookup().get(this.blockID);
		return new ItemStack(blueprintBlock.block, 1, blueprintBlock.metadata);
	}
	
	private void sendUpdatePacket() {
		PacketGhostBlock packet = new PacketGhostBlock(this);
		PacketHandler.sendPacket(packet);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		nbtTagCompound.setInteger("blueprintID", blueprint.id);
		nbtTagCompound.setInteger("blockID", blockID);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		this.blockID    = nbtTagCompound.getInteger("blockID");
		int blueprintID = nbtTagCompound.getInteger("blueprintID");
		this.blueprint  = MinechemBlueprint.blueprints.get(blueprintID);
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

}
