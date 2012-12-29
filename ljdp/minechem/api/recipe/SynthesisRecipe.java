package ljdp.minechem.api.recipe;

import java.util.ArrayList;

import ljdp.minechem.api.core.Chemical;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SynthesisRecipe {
	
	public static ArrayList<SynthesisRecipe> recipes = new ArrayList();
	
	private ItemStack output;
	private Chemical[] shapedRecipe;
	private ArrayList<Chemical> unshapedRecipe;
	private int energyCost;
	private boolean isShaped;
	
	public static SynthesisRecipe add(SynthesisRecipe recipe) {
		recipes.add(recipe);
		return recipe;
	}

	public SynthesisRecipe(ItemStack output, boolean isShaped, int energyCost, Chemical...recipe) {
		this.output = output;
		this.isShaped = isShaped;
		this.energyCost = energyCost;
		this.shapedRecipe = recipe;
		this.unshapedRecipe = new ArrayList();
		for(Chemical ingredient : recipe) {
			if(ingredient != null)
				this.unshapedRecipe.add(ingredient);
		}
	}
	
	public SynthesisRecipe(ItemStack output, boolean isShaped, int energyCost, ArrayList<Chemical> recipe) {
		this.output = output;
		this.isShaped = isShaped;
		this.energyCost = energyCost;
		this.shapedRecipe = recipe.toArray(new Chemical[recipe.size()]);
		this.unshapedRecipe = recipe;
	}

	public ItemStack getOutput() {
		return this.output;
	}
	
	public boolean isShaped() {
		return this.isShaped;
	}
	
	public int energyCost() {
		return this.energyCost;
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
