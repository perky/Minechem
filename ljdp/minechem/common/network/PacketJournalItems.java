package ljdp.minechem.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import cpw.mods.fml.common.network.Player;

import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.containers.ContainerChemistJournal;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketJournalItems extends PacketMinechem {
	
	NBTTagCompound itemListCompound;
	
	public PacketJournalItems(NBTTagList itemTagList) {
		super(true);
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setTag("itemlist", itemTagList);
		this.itemListCompound = tagCompound;
	}
	
	public PacketJournalItems() {
		super();
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		CompressedStreamTools.writeCompressed(itemListCompound, outputStream);
	}
	
	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		itemListCompound = CompressedStreamTools.readCompressed(inputStream);
	}
	
	@Override
	public void onReceive(INetworkManager manager, Player player) {
		NBTTagList tagList = itemListCompound.getTagList("itemlist");
		EntityPlayer entityPlayer = (EntityPlayer) player;
		NBTTagCompound entityData = entityPlayer.getEntityData();
		NBTTagCompound persistedData = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		if(persistedData != null) {
			persistedData.setTag(MinechemItems.journal.ITEMS_TAG_NAME, tagList);
		}
		if(entityPlayer.openContainer != null && entityPlayer.openContainer instanceof ContainerChemistJournal){
			List<ItemStack> items = MinechemHelper.readTagListToItemStackList(tagList);
			((ContainerChemistJournal)entityPlayer.openContainer).gui.populateItemList(items, entityPlayer);
		}
	}
}
