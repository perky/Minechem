package net.minecraft.minechem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.ic2.api.Direction;
import net.minecraft.src.ic2.api.IEnergySink;

public class TileEntityUnbonder extends TileEntityMinechemMachine implements IEnergySink {
	
	public static int timerDuration = 300;
	private boolean isProcessComplete;
	private boolean isRunning;
	private boolean hasEnoughPower;
	public static int IC2PowerPerTick = 5;
	private ItemStack incompatableStack;
	
	public TileEntityUnbonder() {
		inventoryStack = new ItemStack[5];
		isProcessComplete = false;
	}
	
	public void updateEntity() {
		if((timer > 0 && !mod_Minechem.requireIC2Power)
		|| (timer > 0 && mod_Minechem.requireIC2Power && hasEnoughPower)) {
			timer--;
			isRunning = true;
			if(timer <= 0 && canUnbond()) {
				isProcessComplete = true;
				unbondingComplete();
				onInventoryChanged();
				isRunning = false;
			} else if(!canUnbond()) {
				timer = 0;
				onInventoryChanged();
				isRunning = false;
			}
		} else if(canUnbond()) {
			timer = timerDuration;
			isProcessComplete = false;
			isRunning = true;
		} else {
			isRunning = false;
			for(int i = 1; i < getSizeInventory(); i++) {
				takeEmptyTubeFromChest(i);
			}
		}
	}
	
	public boolean canUnbond() {
		if(incompatableStack != null) {
			if(inventoryStack[0] == incompatableStack) {
				return false;
			} else {
				incompatableStack = null;
			}
		}
		
		if(isSingleElementTube(inventoryStack[0]))
			return false;
		
		int tubeCount = 0;
		for(int i =1; i < inventoryStack.length; i++) {
			if(isEmptyTube(inventoryStack[i]))
				tubeCount++;
		}
		return isTube(inventoryStack[0]) && tubeCount > 0;
	}
	
	public void unbondingComplete() {
		int tubeCount = 0;
		for(int i =1; i < inventoryStack.length; i++) {
			if(isEmptyTube(inventoryStack[i]))
				tubeCount++;
		}
		
		ItemStack itemstack = inventoryStack[0];
		NBTTagCompound tagCompound = itemstack.getTagCompound();
		boolean leftOverExists = false;
		String leftOver = "";
		if(itemstack.getItemDamage() == 0) {
			if(tagCompound != null) {
				String formula = tagCompound.getString("chemicalname");
				Pattern pattern = Pattern.compile("([A-Z][a-z]*)([0-9]*)");
				Matcher matcher = pattern.matcher(formula);
				String unbonded = "";
				int count = 0;
				while(matcher.find()) {
					if(count < tubeCount) {
						String element = matcher.group(1);
						String atomsStr = matcher.group(2);
						unbonded += matcher.group(0);
						int atoms = 1;
						if(!atomsStr.equals(""))
							atoms = Integer.valueOf(atomsStr);
						System.out.println(element + ":" + atoms);
						Molecule outputMolecule = Molecule.elementByFormula(element, atoms);
						
						if(outputMolecule != null && outputMolecule.stack != null) {
							for(int i =1; i < inventoryStack.length; i++) {
								if(isEmptyTube(inventoryStack[i])) {
									inventoryStack[i] = outputMolecule.stack;
									break;
								}
							}
						}
					} else {
						leftOver = formula.replaceFirst(unbonded, "");
						leftOverExists = true;
						Molecule m = Molecule.moleculeOrElementByFormula(leftOver);
						if(m != null)
							inventoryStack[0] = m.stack;
						else
							inventoryStack[0] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
						break;
					}
					count++;
				}
			}
		} else {
			if(tagCompound != null){
				int atoms = tagCompound.getInteger("atoms");
				int element = itemstack.getItemDamage();
				if(tubeCount >= atoms) {
					int count = 0;
					for(int i =1; i < inventoryStack.length; i++) {
						if(count >= atoms) break;
						if(isEmptyTube(inventoryStack[i])) {
							inventoryStack[i] = new Molecule(element, 1).stack;
							count++;
						}
					}
				} else {
					int atomDiv = atoms / tubeCount;
					int atomRemainder = atoms % tubeCount;
					if(atomDiv > 0) {
						for(int i =1; i < inventoryStack.length; i++) {
							if(isEmptyTube(inventoryStack[i]))
								inventoryStack[i] = new Molecule(element, atomDiv).stack;
						}
					}
					
					if(atomRemainder > 0) {
						leftOverExists = true;
						Molecule m = new Molecule(element, atomRemainder);
						inventoryStack[0] = m.stack;
					}
				}
			}
		}
		
		if(!leftOverExists)
			inventoryStack[0] = new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
		
		for(int i = 0; i < getSizeInventory(); i++) {
			takeEmptyTubeFromChest(i);
		}
	}
	
	private boolean canRemoveTube() {
		boolean canremove = false;
		for(int i = 1; i < 5; i++) {
			if(inventoryStack[i] != null && inventoryStack[i].itemID != mod_Minechem.itemTesttube.shiftedIndex)
				canremove = true;
		}
		
		return canremove;
	}
	
	@Override
	public boolean addItem(ItemStack stack, boolean doAdd, Orientations from) {
		if(from == Orientations.YPos) {
			if(isEmptyTube(stack)) {
				for(int i = 1; i < getSizeInventory(); i++) {
					if(tryAddingStack(stack, i, doAdd)) return true;
				}
			} else {
				if(tryAddingStack(stack, 0, doAdd)) return true;
			}
		}
		
		return false;
	}

	@Override
	public ItemStack extractItem(boolean doRemove, Orientations from) {
		if(from != Orientations.YPos) {
			for(int i = 0; i < getSizeInventory(); i++) {
				if(!isEmptyTube(inventoryStack[i])) {
					if(doRemove)
						return decrStackSize(i, 1);
					else
						return inventoryStack[i];
				}
			}
			if(isEmptyTube(inventoryStack[0])) {
				if(doRemove)
					return decrStackSize(0, 1);
				else
					return inventoryStack[0];
			}
		}
		
		return null;
	}

	public int getStartInventorySide(int side) {
		return side == 1 ? 0 : 1;
	}
	
	public int getSizeInventorySide(int side) {
		return side == 1 ? 1 : 4;
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
