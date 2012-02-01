package net.minecraft.minechem;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemMinechem extends ItemBlock {
	
	public static final int electrolysis = 0;
	public static final int fusion = 1;
	public static final int bonder = 2;
	public static final int unbonder = 3;
	public static final int fission = 4;
	public static final int crafting = 5;
	public static final int thermite = 6;
	private static String[][] names = {
		{"electrolysis", "Electrolysis Kit"},
		{"fusion", "Fusion Reactor"},
		{"bonder", "Bonder"},
		{"unbonder", "Un-Bonder"},
		{"fission", "Fission Reactor"},
		{"crafting", "Molecular Crafting Table"},
		{"thermite", "Thermite"}
	};

	public ItemMinechem(int i, Block block) {
		super(i);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int i) {
		return i;
	}

	public int getPlacedBlockMetadata(int i)
    {
        return i;
    }
	
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return names[itemstack.getItemDamage()][1];
	}
	
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return getItemName() + "." + names[itemstack.getItemDamage()][0];
	}

}
