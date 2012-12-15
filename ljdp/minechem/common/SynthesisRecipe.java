package ljdp.minechem.common;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.src.ItemStack;

public class SynthesisRecipe {
	
	private ItemStack output;
	private ItemStack[] shapedRecipe;
	private ArrayList<ItemStack> unshapedRecipe;
	private boolean isShaped;

	public SynthesisRecipe(ItemStack output, ItemStack[] ingredients, boolean isShaped) {
		this.output = output;
		this.shapedRecipe = ingredients;
		this.unshapedRecipe = copyItemArrayIntoList(ingredients);
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
			if(shapedRecipe[i] == null && inputStacks[i] != null)
				return false;
			if(inputStacks[i] == null || shapedRecipe[i] == null)
				continue;
			if(inputStacks[i].itemID != shapedRecipe[i].itemID)
				return false;
			if(inputStacks[i].stackSize < shapedRecipe[i].stackSize)
				return false;
			if(inputStacks[i].getItemDamage() != shapedRecipe[i].getItemDamage())
				return false;
		}
		return true;
	}
	
	private boolean matchesUnshaped(ItemStack[] inputStacks) {
		ArrayList<ItemStack> inputStackList = copyItemArrayIntoList(inputStacks);
		ArrayList<ItemStack> ingredients = copyItemList(this.unshapedRecipe);
		for(ItemStack inputStack : inputStackList) {
			int ingredientSlot = getIngredientSlotThatMatchesStack(ingredients, inputStack);
			if(ingredientSlot != -1)
				ingredients.remove(ingredientSlot);
			else
				return false;
		}
		return ingredients.size() == 0;
	}
	
	private int getIngredientSlotThatMatchesStack(ArrayList<ItemStack> ingredients, ItemStack itemStack) {
		for(int slot = 0; slot < ingredients.size(); slot++) {
			ItemStack ingredientStack = ingredients.get(slot);
			if(ingredientStack != null && ingredientStack.itemID == itemStack.itemID 
					&& ingredientStack.getItemDamage() == itemStack.getItemDamage() 
					&& itemStack.stackSize >= ingredientStack.stackSize)
				return slot;
		}
		return -1;
	}
	
	private ArrayList<ItemStack> copyItemList(ArrayList<ItemStack> itemList) {
		ArrayList<ItemStack> itemListCopy = new ArrayList();
		for(ItemStack itemstack : itemList) {
			itemListCopy.add(itemstack.copy());
		}
		return itemListCopy;
	}
	
	private ArrayList<ItemStack> copyItemArrayIntoList(ItemStack[] itemstacks) {
		ArrayList<ItemStack> itemList = new ArrayList();
		for(int i = 0; i < itemstacks.length; i++) {
			if(itemstacks[i] != null)
				itemList.add(itemstacks[i]);
		}
		return itemList;
	}
	
	public boolean hasOutputStack(ItemStack outputStack) {
		return this.output.itemID == outputStack.itemID && this.output.getItemDamage() == outputStack.getItemDamage();
	}
	
	public ItemStack getOutputStack() {
		return output.copy();
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
	
	public int getIngredientCount() {
		int count = 0;
		for(ItemStack ingredient : unshapedRecipe) {
			count += ingredient.stackSize;
		}
		return count;
	}
}
