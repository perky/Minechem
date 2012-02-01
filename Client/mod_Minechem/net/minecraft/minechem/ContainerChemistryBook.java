package net.minecraft.minechem;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainerCreative;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Slot;

public class ContainerChemistryBook extends Container {
	
	public List recipes;
	public int currentPos;
	
	public ContainerChemistryBook(EntityPlayer entityplayer) {
		// Add player's inventory slots.
		InventoryPlayer inventoryplayer = entityplayer.inventory;
		for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(inventoryplayer, i1 + j * 9 + 9, 8 + i1 * 18, 125 + j * 18));
            }
        }
        
        for(int i3 = 0; i3 < 9; i3++)
        {
            addSlot(new Slot(inventoryplayer, i3, 8 + i3 * 18, 184));
        }
        
        recipes = MinechemCraftingRecipe.recipes;
        currentPos = 0;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	public List<String> getRecipes() {
		List<String> recipelist = new ArrayList();
		
		for(int n = 0; n < 8; n++) {
        	int n1 = currentPos + n;
        	MinechemCraftingRecipe recipe = (MinechemCraftingRecipe)recipes.get(n1);
        	String formulas = "";
        	if(recipe.input1.name.equals( recipe.input2.name )) {
        		formulas = Util.convertNumbersToSuperscript(recipe.input1.name) + " x 2";
        	} else {
        		formulas = Util.convertNumbersToSuperscript(recipe.input1.name) + " ¤7+¤f " + Util.convertNumbersToSuperscript(recipe.input2.name);
        	}
        	
        	String s =  formulas + " ¤7=¤f " + recipe.output.getItem().getItemDisplayName(recipe.output);
        	recipelist.add(s);
        }
		
		return recipelist;
	}
	
	public void scrollTo(float currentScroll)
    {
		currentPos = MathHelper.floor_float( currentScroll * (recipes.size() - 8) );
    }

}
