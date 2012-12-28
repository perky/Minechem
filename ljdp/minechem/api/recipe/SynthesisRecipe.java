package ljdp.minechem.api.recipe;

import java.util.ArrayList;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.inventory.IInventory;
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

    /**
     * Takes 1 recipe's worth of items from the inventory, making sure to leave
     * behind at least 1 recipe's worth of items.
     * 
     * @return False if taking the items would result in too few items
     *   remaining.
     */
    public boolean takeFromCraftingInventory(final IInventory inv) {
        ItemStack[] stacks = new ItemStack[inv.getSizeInventory()];
        for (int slot = 0; slot < inv.getSizeInventory(); slot++)
            stacks[slot] = inv.getStackInSlot(slot);
        if (!SynthesisRecipeHandler.instance.itemStacksMatchesRecipe(stacks, this, 2))
            return false;
        if (isShaped)
            takeShapedFromCraftingInventory(stacks);
        else
            takeUnshapedFromCraftingInventory(stacks);
        return true;
    }

    /**
     * No checking; assumes the stacks match the recipe.
     */
    private void takeShapedFromCraftingInventory(ItemStack[] stacks) {
        for (int slot = 0; slot < shapedRecipe.length; slot++)
            stacks[slot].stackSize -= shapedRecipe[slot].amount;
    }

    /**
     * No checking; assumes the stacks match the recipe.
     */
    private void takeUnshapedFromCraftingInventory(ItemStack[] stacks) {
        for (Chemical c : unshapedRecipe) {
            int remaining = c.amount;
            for (int slot = 0; slot < stacks.length; slot++) {
                if (MinechemHelper.itemStackMatchesChemical(stacks[slot], c)) {
                    int decrement = Math.min(remaining, stacks[slot].stackSize);
                    stacks[slot].stackSize -= decrement;
                    remaining -= decrement;
                    if (remaining == 0) break;
                }
            }
        }
    }
}
