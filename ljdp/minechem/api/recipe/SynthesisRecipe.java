package ljdp.minechem.api.recipe;

import java.util.ArrayList;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.core.Molecule;
import ljdp.minechem.api.util.Util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class SynthesisRecipe {
	
	public static ArrayList<SynthesisRecipe> recipes = new ArrayList();
	
	private ItemStack output;
	private Chemical[] shapedRecipe;
	private ArrayList<Chemical> unshapedRecipe;
	private boolean isShaped;
	
	public static SynthesisRecipe add(SynthesisRecipe recipe) {
		recipes.add(recipe);
		return recipe;
	}

	public SynthesisRecipe(ItemStack output, boolean isShaped, Chemical...recipe) {
		this.output = output;
		this.isShaped = isShaped;
		this.shapedRecipe = recipe;
		this.unshapedRecipe = new ArrayList();
		for(Chemical ingredient : recipe) {
			if(ingredient != null)
				this.unshapedRecipe.add(ingredient);
		}
	}
	
	public SynthesisRecipe(ItemStack output, boolean isShaped, ArrayList<Chemical> recipe) {
		this.output = output;
		this.isShaped = isShaped;
		this.shapedRecipe = recipe.toArray(new Chemical[recipe.size()]);
		this.unshapedRecipe = recipe;
	}

	public ItemStack getOutput() {
		return this.output;
	}
	
	public boolean isShaped() {
		return this.isShaped;
	}
	
	public Chemical[] getShapedRecipe() {
		return this.shapedRecipe;
	}
	
	public ArrayList<Chemical> getShapelessRecipe() {
		return this.unshapedRecipe;
	}
	
	public int getIngredientCount() {
		int count = 0;
		for(Chemical chemical : unshapedRecipe) {
			count += chemical.amount;
		}
		return count;
	}
}
