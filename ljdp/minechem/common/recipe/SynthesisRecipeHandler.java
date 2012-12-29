package ljdp.minechem.common.recipe;

import java.util.ArrayList;
import java.util.Collection;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.inventory.IInventory;
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
        return itemStacksMatchesRecipe(stacks, recipe, 1);
    }

	public boolean itemStacksMatchesRecipe(ItemStack[] stacks, SynthesisRecipe recipe, int factor) {
		if(recipe.isShaped())
			return itemStacksMatchesShapedRecipe(stacks, recipe, factor);
		else
			return itemStacksMatchesShapelessRecipe(stacks, recipe, factor);
	}

	private boolean itemStacksMatchesShapelessRecipe(ItemStack[] stacks, SynthesisRecipe recipe) {
        return itemStacksMatchesShapelessRecipe(stacks, recipe, 1);
    }

	private boolean itemStacksMatchesShapelessRecipe(ItemStack[] stacks, SynthesisRecipe recipe, int factor) {
		ArrayList<ItemStack> stacksList = new ArrayList();
		ArrayList<ItemStack> shapelessRecipe = MinechemHelper.convertChemicalsIntoItemStacks(recipe.getShapelessRecipe());
		for(ItemStack itemstack : stacks) {
			if(itemstack != null)
				stacksList.add(itemstack.copy());
		}
		for(ItemStack itemstack : stacksList) {
			int ingredientSlot = getIngredientSlotThatMatchesStack(shapelessRecipe, itemstack, factor);
			if(ingredientSlot != -1)
				shapelessRecipe.remove(ingredientSlot);
			else
				return false;
		}
		return shapelessRecipe.size() == 0;
	}

	private boolean itemStacksMatchesShapedRecipe(ItemStack[] stacks, SynthesisRecipe recipe) {
        return itemStacksMatchesShapedRecipe(stacks, recipe, 1);
    }

	private boolean itemStacksMatchesShapedRecipe(ItemStack[] stacks, SynthesisRecipe recipe, int factor) {
		Chemical[] chemicals = recipe.getShapedRecipe();
		for(int i = 0; i < chemicals.length; i++) {
			if(stacks[i] == null && chemicals[i] != null)
				return false;
			if(chemicals[i] == null && stacks[i] != null)
				return false;
			if(stacks[i] == null || chemicals[i] == null)
				continue;
			if(MinechemHelper.itemStackMatchesChemical(stacks[i], chemicals[i], factor))
				continue;
			else
				return false;
		}
		return true;
	}
	
	private int getIngredientSlotThatMatchesStack(ArrayList<ItemStack> ingredients, ItemStack itemstack) {
        return getIngredientSlotThatMatchesStack(ingredients, itemstack, 1);
    }

	private int getIngredientSlotThatMatchesStack(ArrayList<ItemStack> ingredients, ItemStack itemstack, int factor) {
		for(int slot = 0; slot < ingredients.size(); slot++) {
			ItemStack ingredientStack = ingredients.get(slot);
			if(ingredientStack != null && Util.stacksAreSameKind(itemstack, ingredientStack) 
					&& itemstack.stackSize >= ingredientStack.stackSize * factor)
				return slot;
		}
		return -1;
	}


    /**
     * Takes 1 recipe's worth of items from the inventory, making sure to leave
     * behind at least 1 recipe's worth of items.
     *
     * @return False if taking the items would result in too few items
     *   remaining.
     */
    public static boolean takeFromCraftingInventory(SynthesisRecipe recipe, final IInventory inv) {
        ItemStack[] stacks = new ItemStack[inv.getSizeInventory()];
        for (int slot = 0; slot < inv.getSizeInventory(); slot++)
            stacks[slot] = inv.getStackInSlot(slot);
        if (!SynthesisRecipeHandler.instance.itemStacksMatchesRecipe(stacks, recipe, 2))
            return false;
        if (recipe.isShaped())
            takeShapedFromCraftingInventory(recipe, stacks);
        else
            takeUnshapedFromCraftingInventory(recipe, stacks);
        return true;
    }

    /**
     * No checking; assumes the stacks match the recipe.
     */
    private static void takeShapedFromCraftingInventory(SynthesisRecipe recipe, ItemStack[] stacks) {
        Chemical[] shapedRecipe = recipe.getShapedRecipe();
        for (int slot = 0; slot < shapedRecipe.length; slot++)
            stacks[slot].stackSize -= shapedRecipe[slot].amount;
    }

    /**
     * No checking; assumes the stacks match the recipe.
     */
    private static void takeUnshapedFromCraftingInventory(SynthesisRecipe recipe, ItemStack[] stacks) {
        Collection<Chemical> unshapedRecipe = recipe.getShapelessRecipe();
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
