package net.minecraft.minechem;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.ic2.api.Direction;
import net.minecraft.src.ic2.api.IEnergySink;

public class TileEntityMinechemCrafting extends TileEntityMinechemMachine {
	
	public TileEntityMinechemCrafting() {
		super();
		timerDuration = 225;
		maxIC2Energy = 512;
		maxIC2EnergyInput = 512;
		consumeIC2EnergyPerTick = 80;
		
		inventoryStack = new ItemStack[3];
	}
	
	public void updateEntity() {
		boolean canCraft = canCraft();
		if((timer > 0 && !mod_Minechem.requireIC2Power)
		|| (timer > 0 && mod_Minechem.requireIC2Power && didConsumePower())){
			timer--;
			if(timer <= 0 && canCraft){
				craftingComplete();
				onInventoryChanged();
			} else if(!canCraft) {
				timer = 0;
				onInventoryChanged();
			}
		} else if(canCraft) {
			timer = timerDuration;
		}
	}
	
	public boolean canCraft() {
		boolean foundIn1 = false;
		boolean foundIn2 = false;
		MinechemCraftingRecipe foundRecipe = null;
		if(isTube(inventoryStack[0]) && isTube(inventoryStack[1])) {
			Molecule in1 = Molecule.moleculeByItemStack( inventoryStack[0] );
			Molecule in2 = Molecule.moleculeByItemStack( inventoryStack[1] );
			for(int i = 0; i < MinechemCraftingRecipe.recipes.size(); i++) {
				MinechemCraftingRecipe recipe = MinechemCraftingRecipe.recipes.get(i);
				foundIn1 = false;
				foundIn2 = false;
				
				if(in1.name.equals( recipe.input1.name )) {
					foundIn1 = true;
				}
				
				if(in2.name.equals( recipe.input2.name )) {
					foundIn2 = true;
				}
				
				if(!foundIn1) {
					if(in1.name.equals( recipe.input2.name )) {
						foundIn1 = true;
					}
				}
				
				if(!foundIn2) {
					if(in2.name.equals( recipe.input1.name )) {
						foundIn2 = true;
					}
				}
				
				if(foundIn1 && foundIn2) {
					foundRecipe = recipe;
					break;
				}
			}
		}
		
		if(foundRecipe != null) {
			ItemStack output = foundRecipe.output;
			if(output.itemID == Item.potion.shiftedIndex) {
				ItemStack bottleStack = new ItemStack(Item.glassBottle, 3);
				if(inventoryStack[2] != null && inventoryStack[2].isStackEqual(bottleStack)) {
					return true;
				}
			}
			else if(foundRecipe.output.getItem().hasContainerItem()) 
			{
				Item containerItem = foundRecipe.output.getItem().getContainerItem();
				ItemStack containerStack = new ItemStack(containerItem, 3);
				if(inventoryStack[2] != null && inventoryStack[2].isItemEqual(containerStack)) {
					return true;
				}
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	public void craftingComplete() {
		boolean foundIn1 = false;
		boolean foundIn2 = false;
		Molecule[] recipeIns = new Molecule[2];
		Molecule in1 = Molecule.moleculeByItemStack( inventoryStack[0] );
		Molecule in2 = Molecule.moleculeByItemStack( inventoryStack[1] );
		if(in1 == null || in2 == null)
			return;
		MinechemCraftingRecipe foundRecipe = null;
		for(int i = 0; i < MinechemCraftingRecipe.recipes.size(); i++) {
			MinechemCraftingRecipe recipe = MinechemCraftingRecipe.recipes.get(i);
			foundIn1 = false;
			foundIn2 = false;
			
			if(in1.name.equals( recipe.input1.name )) {
				foundIn1 = true;
				recipeIns[0] = recipe.input1;
			}
			
			if(in2.name.equals( recipe.input2.name )) {
				foundIn2 = true;
				recipeIns[1] = recipe.input2;
			}
			
			if(!foundIn1) {
				if(in1.name.equals( recipe.input2.name )) {
					foundIn1 = true;
					recipeIns[0] = recipe.input2;
				}
			}
			
			if(!foundIn2) {
				if(in2.name.equals( recipe.input1.name )) {
					foundIn2 = true;
					recipeIns[1] = recipe.input1;
				}
			}
			
			if(foundIn1 && foundIn2) {
				foundRecipe = recipe;
				break;
			}
		}
		
		
		if(foundRecipe != null) {
			ItemStack output = foundRecipe.output.copy();
			ItemStack stack = inventoryStack[2];
			boolean outputIsValid = false;
			
			if(output.itemID == Item.potion.shiftedIndex) {
				ItemStack bottleStack = new ItemStack(Item.glassBottle, 3);
				if(inventoryStack[2] != null && inventoryStack[2].isStackEqual(bottleStack)) {
					setInventorySlotContents(2, output);
					outputIsValid = true;
				}
			}
			else if(output.getItem().hasContainerItem()) 
			{
				Item containerItem = output.getItem().getContainerItem();
				ItemStack containerStack = new ItemStack(containerItem, 1);
				if(stack != null && stack.isItemEqual(containerStack)) {
					setInventorySlotContents(2, output);
					outputIsValid = true;
				}
			} else {
				if(stack == null) {
					setInventorySlotContents(2, output);
					inventoryStack[2] = output;
					outputIsValid = true;
				} else if( stack.itemID == output.itemID 
						&& stack.getItemDamage() == output.getItemDamage() 
						&& stack.stackSize + output.stackSize < stack.getMaxStackSize())
				{
					stack.stackSize += output.stackSize;
					setInventorySlotContents(2, stack);
					outputIsValid = true;
				}
			}
			
			if(outputIsValid) {
				inventoryStack[0] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
				inventoryStack[1] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
				
				dumpEmptyTubeSlotToChest(0);
				dumpEmptyTubeSlotToChest(1);
				dumpSlotToChest(2);
			}
		}
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean addItem(ItemStack stack, boolean doAdd, Orientations from) {
		if(from == Orientations.YPos) {
			if(tryAddingStack(stack, 0, doAdd)) return true;
			if(tryAddingStack(stack, 1, doAdd)) return true;
		}
		
		return false;
	}

	@Override
	public ItemStack extractItem(boolean doRemove, Orientations from) {
		if(from != Orientations.YPos) {
			if(inventoryStack[2] != null) {
				if(doRemove)
					return decrStackSize(2, inventoryStack[2].stackSize);
				else
					return inventoryStack[2];
			}
			if(isEmptyTube(inventoryStack[0])) {
				if(doRemove)
					return decrStackSize(0, 1);
				else
					return inventoryStack[0];
			}
			if(isEmptyTube(inventoryStack[1])) {
				if(doRemove)
					return decrStackSize(1, 1);
				else
					return inventoryStack[1];
			}
		}
		
		return null;
	}

	@Override
	public int getStartInventorySide(int side) {
		return side == 1 ? 0 : 2;
	}

	@Override
	public int getSizeInventorySide(int side) {
		return side == 1 ? 2 : 1;
	}
	
}
