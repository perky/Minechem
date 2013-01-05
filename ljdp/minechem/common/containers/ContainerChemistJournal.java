package ljdp.minechem.common.containers;

import java.util.ArrayList;
import java.util.List;

import ljdp.minechem.client.gui.GuiChemistJournal;
import ljdp.minechem.client.gui.GuiFakeSlot;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ContainerChemistJournal extends Container {
	
	public GuiChemistJournal gui;
	
	public ContainerChemistJournal(InventoryPlayer inventoryPlayer) {
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer) {
		return true;
	}
	
	private List<ItemStack> getItemListOffPlayer(EntityPlayer player) {
		NBTTagCompound entityData = player.getEntityData();
		NBTTagCompound persistedData = (NBTTagCompound) entityData.getCompoundTag(player.PERSISTED_NBT_TAG);
		if(persistedData != null) {
			NBTTagList itemList = persistedData.getTagList(MinechemItems.journal.ITEMS_TAG_NAME);
			if(itemList != null) {
				return MinechemHelper.readTagListToItemStackList(itemList);
			}
		}
		return null;
	}

}
