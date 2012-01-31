package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.mod_Minechem;

/**
 * @author lukeperkin
 */
public class SlotElectrolysisOutput extends Slot {

	private EntityPlayer entityplayer;
	final ContainerElectrolysis containerElectrolysis;
	
	public SlotElectrolysisOutput(ContainerElectrolysis containerelectrolysis, EntityPlayer player, 
			IInventory iinventory, int i, int j, int k) 
	{
		super(iinventory, i, j, k);
		containerElectrolysis = containerelectrolysis;
		entityplayer = player;
	}
	
	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return (itemstack.itemID == mod_Minechem.itemTesttubeEmpty.shiftedIndex);
	}
	
	@Override
	public int getSlotStackLimit() {
		return 1;
	}

}
