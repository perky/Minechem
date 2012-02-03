package net.minecraft.minechem;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.ic2.api.Direction;
import net.minecraft.src.ic2.api.EnergyNet;
import net.minecraft.src.ic2.api.IEnergySink;

public class TileEntityFusion extends TileEntityMinechemMachine {
	
	public TileEntityFusion() {
		super();
		
		consumeIC2EnergyPerTick = 40;
		maxIC2Energy = 256;
		maxIC2EnergyInput = 512;
		
		inventoryStack = new ItemStack[3];
	}
	
	public void updateEntity() {
		if((timer > 0 && !mod_Minechem.requireIC2Power)
		|| (timer > 0 && mod_Minechem.requireIC2Power && didConsumePower()))
		{
			if(timer < 450)
			{
				timer -= 3;
			} else {
				timer--;
			}
			
			if(timer <= 0)
			{
				fusionComplete();
				onInventoryChanged();
			}
			else if(!canFuse())
			{
				timer = 0;
				onInventoryChanged();
			}
		}
		else if(canFuse())
		{
			timer = timerDuration;
		} else {
			takeEmptyTubeFromChest(2);
		}
	}
	
	public boolean canFuse() {
		return (isElementTube(inventoryStack[0]) && isElementTube(inventoryStack[1]) && isEmptyTube(inventoryStack[2]));
	}
	
	public void fusionComplete() {
		if(!canFuse())
			return;
		
		int a = inventoryStack[0].getItemDamage();
		int b = inventoryStack[1].getItemDamage();
		int c = a + b;
		if(c <= 118)
		{
			Molecule mA = Molecule.moleculeByItemStack(inventoryStack[0]);
			Molecule mB = Molecule.moleculeByItemStack(inventoryStack[1]);
			if(mA == null || mB == null)
				return;
			int atomsA = mA.atoms;
			int atomsB = mB.atoms;
			int atomsC = Math.min(atomsA, atomsB);
			Molecule m = new Molecule(c, atomsC);
			inventoryStack[2] = m.stack;
		}
		inventoryStack[0] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
		inventoryStack[1] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
		
		dumpSlotToChest(0);
		dumpSlotToChest(1);
		dumpSlotToChest(2);
	}
	
	public boolean canRemoveTube() {
		return (isEmptyTube(inventoryStack[0]) || isEmptyTube(inventoryStack[1]) || isElementTube(inventoryStack[2]));
	}
	
	@Override
	public boolean addItem(ItemStack stack, boolean doAdd, Orientations from) {
		if(from == Orientations.YPos) {
			if(isEmptyTube(stack)) {
				if(tryAddingStack(stack, 2, doAdd)) return true;
			} else {
				if(tryAddingStack(stack, 0, doAdd)) return true;
				if(tryAddingStack(stack, 1, doAdd)) return true;
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
			if(isEmptyTube(inventoryStack[1])) {
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
		return side == 1 ? 0 : 2;
	}
	
	public int getSizeInventorySide(int side) {
		return side == 1 ? 2 : 1;
	}
	
	@Override
	public String getInvName() {
		return "Fusion Reactor";
	}

}
