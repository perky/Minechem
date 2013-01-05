package ljdp.minechem.common.containers;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ljdp.minechem.client.gui.GuiChemistJournal;
import ljdp.minechem.client.gui.GuiFakeSlot;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
		return null;
	}
	
	@Override
	public ItemStack slotClick(int par1, int par2, int par3,
			EntityPlayer par4EntityPlayer) {
		return null;
	}
	
	@Override
	public void putStackInSlot(int par1, ItemStack par2ItemStack) {
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void putStacksInSlots(ItemStack[] par1ArrayOfItemStack) {
	}
	
	@Override
	public Slot getSlotFromInventory(IInventory iInventory, int slot) {
		Slot aSlot = new Slot(iInventory, slot, 0, 0);
		aSlot.slotNumber = 0;
		return aSlot;
	}

}
