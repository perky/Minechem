package ljdp.minechem.api.recipe;

import java.util.ArrayList;

import ljdp.minechem.api.core.Chemical;

import net.minecraft.item.ItemStack;

public class DecomposerRecipeSelect extends DecomposerRecipeChance {
	
	ArrayList<DecomposerRecipe> possibleRecipes;
	
	public DecomposerRecipeSelect(ItemStack input, float chance, DecomposerRecipe...possibleRecipes) {
		super(input, chance);
		this.possibleRecipes = new ArrayList();
		for(DecomposerRecipe recipe : possibleRecipes) {
			this.possibleRecipes.add(recipe);
		}
	}
	
	@Override
	public ArrayList<Chemical> getOutput() {
		if(random.nextFloat() < this.chance) {
			DecomposerRecipe selectedRecipe = possibleRecipes.get(random.nextInt(possibleRecipes.size()));
			return selectedRecipe.getOutput();
		} else {
			return null;
		}
	}

}
