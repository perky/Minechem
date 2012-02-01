package net.minecraft.minechem;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.ic2.api.Direction;
import net.minecraft.src.ic2.api.IEnergySink;

public class TileEntityFission extends TileEntityMinechemMachine implements IEnergySink {
	
	private boolean isRunning;
	private boolean hasEnoughPower;
	public static int IC2PowerPerTick = 8;
	
	public TileEntityFission() {
		inventoryStack = new ItemStack[3];
	}
	
	public void updateEntity()
	{
		if((timer > 0 && !mod_Minechem.requireIC2Power)
		|| (timer > 0 && mod_Minechem.requireIC2Power && hasEnoughPower)) {
			timer--;
			isRunning = true;
			if(timer <= 0 && canFissure()) {
				fissionComplete();
				onInventoryChanged();
				isRunning = false;
			} else if(!canFissure()) {
				timer = 0;
				onInventoryChanged();
				isRunning = false;
			}
		} else if(canFissure()) {
			timer = timerDuration;
			isRunning = true;
		} else {
			isRunning = false;
			takeEmptyTubeFromChest(1);
			takeEmptyTubeFromChest(2);
		}
	}
	
	public boolean canFissure()
	{
		return isElementTube(inventoryStack[0]) && isEmptyTube(inventoryStack[1]) && isEmptyTube(inventoryStack[2]);
	}
	
	public void fissionComplete()
	{
		ItemStack input = inventoryStack[0];
		Molecule mInput = Molecule.moleculeByItemStack(input);
		int element = input.getItemDamage();
		int elementDiv = element / 2;
		if(inventoryStack[1] != null && elementDiv > 0)
			inventoryStack[1] = new Molecule(elementDiv, mInput.atoms).stack;
		if(inventoryStack[2] != null && elementDiv > 0)
			inventoryStack[2] = new Molecule(elementDiv, mInput.atoms).stack;
		
		inventoryStack[0] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
		dumpSlotToChest(0);
		dumpSlotToChest(1);
		dumpSlotToChest(2);
	}
	
	@Override
	public boolean addItem(ItemStack stack, boolean doAdd, Orientations from) {
		if(from == Orientations.YPos) {
			if(isEmptyTube(stack)) {
				if(tryAddingStack(stack, 1, doAdd)) return true;
				if(tryAddingStack(stack, 2, doAdd)) return true;
			} else {
				if(tryAddingStack(stack, 0, doAdd)) return true;
			}
		}
		
		return false;
	}

	@Override
	public ItemStack extractItem(boolean doRemove, Orientations from) {
		if(from != Orientations.YPos) {
			if(isEmptyTube(inventoryStack[0])) {
				if(doRemove)
					return decrStackSize(0, 1);
				else
					return inventoryStack[0];
			}
			if(!isEmptyTube(inventoryStack[1])) {
				if(doRemove)
					return decrStackSize(1, 1);
				else
					return inventoryStack[1];
			}
			if(!isEmptyTube(inventoryStack[2])) {
				if(doRemove)
					return decrStackSize(2, 1);
				else
					return inventoryStack[2];
			}
		}
		
		return null;
	}

	public int getStartInventorySide(int side) {
		return side == 1 ? 0 : 1;
	}
	
	public int getSizeInventorySide(int side) {
		return side == 1 ? 1 : 2;
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
