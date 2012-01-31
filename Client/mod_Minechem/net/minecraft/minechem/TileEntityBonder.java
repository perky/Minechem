package net.minecraft.minechem;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.ic2.api.Direction;
import net.minecraft.src.ic2.api.IEnergySink;

public class TileEntityBonder extends TileEntityMinechemMachine implements IEnergySink {

	public static int timerDuration = 300;
	private boolean isProcessComplete;
	private boolean isRunning;
	private boolean hasEnoughPower;
	public static int IC2PowerPerTick = 4;
	
	public TileEntityBonder() {
		inventoryStack = new ItemStack[5];
		isProcessComplete = false;
	}
	
	public void updateEntity()
	{
		if((timer > 0 && !mod_Minechem.requireIC2Power)
		|| (timer > 0 && mod_Minechem.requireIC2Power && hasEnoughPower))
		{
			timer--;
			isRunning = true;
			if(timer <= 0)
			{
				bondingComplete();
				onInventoryChanged();
				isProcessComplete = true;
				isRunning = false;
			} else if(!canBond()) {
				timer = 0;
				onInventoryChanged();
				isRunning = false;
			}
		} else if(canBond()) {
			timer = timerDuration;
			isProcessComplete = false;
			isRunning = true;
		} else {
			takeEmptyTubeFromChest(0);
			isRunning = false;
		}
	}
	
	public void bondingComplete()
	{
		String chemicalname = "";
		int atoms = 0;
		int lastatoms = 0;
		int lastElementId = -1;
		boolean isCompound = false;
		String lastname = null;
		
		for(int i = 1; i < inventoryStack.length; i++) {
			ItemStack itemstack = inventoryStack[i];
			if(isElementTube(itemstack))
			{
				Molecule molecule = new Molecule( itemstack );
				if(lastElementId != -1) {
					if(lastElementId == molecule.elementId) {
						lastatoms += molecule.atoms;
					} else {
						String satoms = Integer.toString(lastatoms);
						if(lastatoms == 1)
							satoms = "";
						chemicalname += lastname + satoms;
						lastatoms = molecule.atoms;
						lastname = molecule.elementName;
						isCompound = true;
					}
				} else {
					lastname = molecule.elementName;
					lastatoms = molecule.atoms;
					lastElementId = molecule.elementId;
				}
				
				atoms += molecule.atoms;
				inventoryStack[i] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
			}
		}
		
		if(lastElementId != -1) {
			String satoms = Integer.toString(lastatoms);
			if(lastatoms == 1)
				satoms = "";
			chemicalname += lastname + satoms;
			Molecule molecule1;
			if(isCompound)
				molecule1 = new Molecule(0, atoms, chemicalname);
			else
				molecule1 = new Molecule(lastElementId, atoms);
			inventoryStack[0] = molecule1.stack;
		}
		
		for(int i = 0; i < getSizeInventory(); i++) {
			dumpSlotToChest(i);
		}
	}
	
	public boolean canBond()
	{
		int tubeCount = 0;
		for(int i = 1; i < inventoryStack.length; i++) {
			ItemStack itemstack = inventoryStack[i];
			if(isElementTube(itemstack))
			{
				tubeCount++;
			}
		}
		
		return isEmptyTube(inventoryStack[0]) && tubeCount > 1;
	}
	
	private boolean inputIsEmptyTubes() {
		boolean isempty = false;
		for(int i = 1; i < 5; i++) {
			if(isEmptyTube(inventoryStack[i]))
				isempty = true;
		}
		
		return isempty;
	}
	
	private boolean canRemoveTube() {
		boolean canremove = false;
		for(int i = 1; i < 5; i++) {
			if(inventoryStack[i] != null && inventoryStack[i].itemID != mod_Minechem.itemTesttubeEmpty.shiftedIndex)
				canremove = true;
		}
		
		return canremove;
	}
	
	@Override
	public boolean addItem(ItemStack stack, boolean doAdd, Orientations from) {
		if(from == Orientations.YPos) {
			if(isEmptyTube(stack) && inventoryStack[0] == null) {
				if(tryAddingStack(stack, 0, doAdd)) return true;
			}
		} else {
			for(int i = 1; i < getSizeInventory(); i++) {
				if(tryAddingStack(stack, i, doAdd)) return true;
			}
		}
		
		return false;
	}

	@Override
	public ItemStack extractItem(boolean doRemove, Orientations from) {
		if(from == Orientations.YPos) {
			if(!isEmptyTube(inventoryStack[0])) {
				if(doRemove)
					return decrStackSize(0, 1);
				else
					return inventoryStack[0];
			}
			for(int i = 1; i < getSizeInventory(); i++) {
				if(inventoryStack[i] != null) {
					if(doRemove)
						return decrStackSize(i, 1);
					else
						return inventoryStack[i];
				}
			}
		}
		
		return null;
	}

	public int getStartInventorySide(int side) {
		return side == 1 ? 1 : 0;
	}
	
	public int getSizeInventorySide(int side) {
		return side == 1 ? 4 : 1;
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
