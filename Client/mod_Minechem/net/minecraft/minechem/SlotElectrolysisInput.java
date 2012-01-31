package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class SlotElectrolysisInput extends Slot {
	
	private EntityPlayer entityplayer;
	final ContainerElectrolysis containerElectroylsis;

	public SlotElectrolysisInput(ContainerElectrolysis containerelectrolysis, EntityPlayer player, IInventory iinventory, int i, int j, int k)
	{
		super(iinventory, i, j, k);
		entityplayer = player;
		containerElectroylsis = containerelectrolysis;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return true;
	}
	
	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
