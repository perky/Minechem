package ljdp.minechem.common;

import java.util.ArrayList;

import net.minecraft.src.ItemStack;

public class SynthesisRecipe {
	
	private ItemStack output;
	private ArrayList<ItemStack> recipe;

	public SynthesisRecipe(ItemStack output, ArrayList inputStacks) {
		this.output = output;
		this.recipe = inputStacks;
	}
	
	public boolean matches(ArrayList<ItemStack> inputStacks) {
		return recipe.containsAll(inputStacks);
	}
	
	public boolean hasOutputStack(ItemStack outputStack) {
		return this.output.itemID == outputStack.itemID && this.output.getItemDamage() == outputStack.getItemDamage();
	}
	
	public ItemStack getOutputStack() {
		return output;
	}

	public ArrayList<ItemStack> getInputStacks() {
		return recipe;
	}
}
