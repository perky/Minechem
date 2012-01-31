package net.minecraft.minechem;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class ItemEmptyTestTube extends Item {

	public ItemEmptyTestTube(int i)
	{
		super(i);
		setMaxStackSize(64);
		setHasSubtypes(false);
		System.out.println(this.shiftedIndex);
	}
	
	@Override
	public int getItemStackLimit() {
		return 64;
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return "Empty Test Tube";
	}
}
