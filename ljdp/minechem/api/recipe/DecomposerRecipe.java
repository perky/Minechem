package ljdp.minechem.api.recipe;

import java.util.ArrayList;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.core.EnumElement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DecomposerRecipe {
	
	public static ArrayList<DecomposerRecipe> recipes = new ArrayList();
	
	ItemStack input;
	ArrayList<Chemical> output;
	
	public static DecomposerRecipe add(DecomposerRecipe recipe) {
		recipes.add(recipe);
		return recipe;
	}
	
	public DecomposerRecipe(ItemStack input, Chemical...chemicals) {
		this(chemicals);
		this.input = input;
	}
	
	public DecomposerRecipe(Chemical...chemicals) {
		this.output = new ArrayList();
		for(Chemical chemical : chemicals) {
			this.output.add(chemical);
		}
	}
	
	public ItemStack getInput() {
		return this.input;
	}
	
	public ArrayList<Chemical> getOutput() {
		return this.output;
	}
	
	public ArrayList<Chemical> getOutputRaw() {
		return this.output;
	}
}
