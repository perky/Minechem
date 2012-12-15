package ljdp.minechem.common;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.src.ItemStack;

public class SynthesisRecipe {
	
	private ItemStack output;
	private ItemStack[] shapedRecipe;
	private ArrayList<ItemStack> unshapedRecipe;
	private boolean isShaped;

	public SynthesisRecipe(ItemStack output, ItemStack[] recipe, boolean isShaped) {
		this.output = output;
		this.shapedRecipe = recipe;
		this.unshapedRecipe = new ArrayList<ItemStack>(Arrays.asList(recipe));
		this.isShaped = isShaped;
	}
	
	public boolean matches(ItemStack[] inputStacks) {
		if(this.isShaped)
			return matchesShaped(inputStacks);
		else
			return matchesUnshaped(inputStacks);
	}
	
	private boolean matchesShaped(ItemStack[] inputStacks) {
		for(int i = 0; i < shapedRecipe.length; i++) {
			if(inputStacks[i] == null && shapedRecipe[i] != null)
				return false;
			if(inputStacks[i] == null)
				continue;
			if(inputStacks[i].itemID != shapedRecipe[i].itemID)
				return false;
			if(inputStacks[i].stackSize >= shapedRecipe[i].stackSize)
				return false;
			if(inputStacks[i].getItemDamage() != shapedRecipe[i].getItemDamage())
				return false;
		}
		return true;
	}
	
	private boolean matchesUnshaped(ItemStack[] inputStacks) {
		ArrayList<ItemStack> inputStackList = new ArrayList(Arrays.asList(inputStacks));
		return unshapedRecipe.containsAll(inputStackList);
	}
	
	public boolean hasOutputStack(ItemStack outputStack) {
		return this.output.itemID == outputStack.itemID && this.output.getItemDamage() == outputStack.getItemDamage();
	}
	
	public ItemStack getOutputStack() {
		return output;
	}
	
	public boolean isShaped() {
		return isShaped;
	}
	
	public ItemStack[] getShapedRecipe() {
		return shapedRecipe;
	}
	
	public ArrayList<ItemStack> getUnshapedRecipe() {
		return unshapedRecipe;
	}
}
