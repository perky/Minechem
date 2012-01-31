package net.minecraft.minechem;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.ItemStack;

public class MinechemCraftingRecipe {
	
	public static List<MinechemCraftingRecipe> recipes = new ArrayList();
	
	public ItemStack output;
	public Molecule input1;
	public Molecule input2;
	
	public MinechemCraftingRecipe(ItemStack out, Molecule in1, Molecule in2) {
		this.output = out;
		this.input1 = in1;
		this.input2 = in2;
	}
	
	public static void addRecipe(ItemStack out, Molecule in1, Molecule in2) {
		if(out == null)
			return;
		MinechemCraftingRecipe recipe = new MinechemCraftingRecipe(out, in1, in2);
		recipes.add(recipe);
	}

}
