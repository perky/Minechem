package net.minecraft.minechem;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.ic2.api.Direction;
import net.minecraft.src.ic2.api.IEnergySink;

public class TileEntityMinechemCrafting extends TileEntityMinechemMachine implements IEnergySink {
	
	public static int timerDuration = 200;
	private boolean isRunning;
	private boolean hasEnoughPower;
	public static int IC2PowerPerTick = 6;
	
	public TileEntityMinechemCrafting() {
		inventoryStack = new ItemStack[3];
	}
	
	public void updateEntity() {
		boolean canCraft = canCraft();
		if((timer > 0 && !mod_Minechem.requireIC2Power)
		|| (timer > 0 && mod_Minechem.requireIC2Power && hasEnoughPower)){
			timer--;
			isRunning = true;
			if(timer <= 0 && canCraft){
				craftingComplete();
				onInventoryChanged();
				isRunning = false;
			} else if(!canCraft) {
				timer = 0;
				onInventoryChanged();
				isRunning = false;
			}
		} else if(canCraft) {
			timer = timerDuration;
			isRunning = true;
		}
	}
	
	public boolean canCraft() {
		return isTube(inventoryStack[0]) && isTube(inventoryStack[1]);
	}
	
	public void craftingComplete() {
		boolean foundIn1 = false;
		boolean foundIn2 = false;
		Molecule[] recipeIns = new Molecule[2];
		Molecule in1 = new Molecule( inventoryStack[0] );
		Molecule in2 = new Molecule( inventoryStack[1] );
		MinechemCraftingRecipe foundRecipe = null;
		for(int i = 0; i < MinechemCraftingRecipe.recipes.size(); i++) {
			MinechemCraftingRecipe recipe = MinechemCraftingRecipe.recipes.get(i);
			if(in1.elementId == recipe.input1.elementId && in1.atoms >= recipe.input1.atoms) {
				foundIn1 = true;
				recipeIns[0] = recipe.input1;
			}
			
			if(in2.elementId == recipe.input2.elementId && in2.atoms >= recipe.input2.atoms) {
				foundIn2 = true;
				recipeIns[1] = recipe.input2;
			}
			
			if(!foundIn1 && !foundIn2) {
				if(in1.elementId == recipe.input2.elementId && in1.atoms >= recipe.input2.atoms) {
					foundIn1 = true;
					recipeIns[0] = recipe.input2;
				}
				if(in2.elementId == recipe.input1.elementId && in2.atoms >= recipe.input1.atoms) {
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
			
			if(outputIsValid) {
				if(in1.isCompound())
					inventoryStack[0] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
				else
					inventoryStack[0] = in1.decrAtoms( recipeIns[0].atoms ).copy();
				
				if(in2.isCompound())
					inventoryStack[1] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
				else
					inventoryStack[1] = in2.decrAtoms( recipeIns[1].atoms ).copy();
				
				dumpSlotToChest(0);
				dumpSlotToChest(1);
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
	
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) {
		return true;
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return true;
	}

	@Override
	public boolean demandsEnergy() {
		if(mod_Minechem.requireIC2Power)
			return isRunning;
		else 
			return false;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
		if( amount >= IC2PowerPerTick ) {
			hasEnoughPower = true;
			return amount - IC2PowerPerTick;
		} else {
			hasEnoughPower = false;
			return amount;
		}
	}
	
}
