package ljdp.minechem.common.recipe;

import java.util.ArrayList;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.item.ItemStack;

public class SynthesisRecipeHandler {
	
	public static SynthesisRecipeHandler instance = new SynthesisRecipeHandler();
	
	public SynthesisRecipeHandler() {
		
	}
	
	public SynthesisRecipe getRecipeFromOutput(ItemStack output) {
		for(SynthesisRecipe recipe : SynthesisRecipe.recipes) {
			if(Util.stacksAreSameKind(output, recipe.getOutput()))
				return recipe;
		}
		return null;
	}
	
	public SynthesisRecipe getRecipeFromInput(ItemStack[] input) {
		for(SynthesisRecipe recipe : SynthesisRecipe.recipes) {
			if(itemStacksMatchesRecipe(input, recipe))
				return recipe;
		}
		return null;
	}
	
	public boolean itemStacksMatchesRecipe(ItemStack[] stacks, SynthesisRecipe recipe) {
		if(recipe.isShaped())
			return itemStacksMatchesShapedRecipe(stacks, recipe);
		else
			return itemStacksMatchesShapelessRecipe(stacks, recipe);
	}

	private boolean itemStacksMatchesShapelessRecipe(ItemStack[] stacks, SynthesisRecipe recipe) {
		ArrayList<ItemStack> stacksList = new ArrayList();
		ArrayList<ItemStack> shapelessRecipe = MinechemHelper.convertChemicalsIntoItemStacks(recipe.getShapelessRecipe());
		for(ItemStack itemstack : stacks) {
			if(itemstack != null)
				stacksList.add(itemstack.copy());
		}
		for(ItemStack itemstack : stacksList) {
			int ingredientSlot = getIngredientSlotThatMatchesStack(shapelessRecipe, itemstack);
			if(ingredientSlot != -1)
				shapelessRecipe.remove(ingredientSlot);
			else
				return false;
		}
		return shapelessRecipe.size() == 0;
	}

	private boolean itemStacksMatchesShapedRecipe(ItemStack[] stacks, SynthesisRecipe recipe) {
		Chemical[] chemicals = recipe.getShapedRecipe();
		for(int i = 0; i < chemicals.length; i++) {
			if(stacks[i] == null && chemicals[i] != null)
				return false;
			if(chemicals[i] == null && stacks[i] != null)
				return false;
			if(stacks[i] == null || chemicals[i] == null)
				continue;
			if(MinechemHelper.itemStackMatchesChemical(stacks[i], chemicals[i]))
				continue;
			else
				return false;
		}
		return true;
	}
	
	private int getIngredientSlotThatMatchesStack(ArrayList<ItemStack> ingredients, ItemStack itemstack) {
		for(int slot = 0; slot < ingredients.size(); slot++) {
			ItemStack ingredientStack = ingredients.get(slot);
			if(ingredientStack != null && Util.stacksAreSameKind(itemstack, ingredientStack) 
					&& itemstack.stackSize >= ingredientStack.stackSize)
				return slot;
		}
		return -1;
	}
}
