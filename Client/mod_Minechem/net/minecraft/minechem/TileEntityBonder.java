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

public class TileEntityBonder extends TileEntityMinechemMachine implements IEnergySink {

	public static int timerDuration = 120;
	private boolean isProcessComplete;
	private boolean isRunning;
	private boolean hasEnoughPower;
	public static int IC2PowerPerTick = 4;
	public String currentBondFormula;
	
	public TileEntityBonder() {
		inventoryStack = new ItemStack[5];
		isProcessComplete = false;
		currentBondFormula = "";
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
	
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		String formula = getBondFormula();
		String fullName = Util.getFullChemicalName(formula);
		currentBondFormula = Util.convertNumbersToSuperscript( formula ) + "  " + fullName;
	}

	public void bondingComplete()
	{
		String bondFormula = getBondFormula();
		
		for(int i = 1; i < inventoryStack.length; i++) {
			ItemStack itemstack = inventoryStack[i];
			if(isTube(itemstack))
				inventoryStack[i] = new ItemStack(mod_Minechem.itemTesttubeEmpty);
		}
		
		if(!bondFormula.equals("")) {
			Molecule m = Molecule.moleculeOrElementByFormula(bondFormula);
			if(m != null) {
				inventoryStack[0] = m.stack;
			}
		}
		
		dumpSlotToChest(0);
		for(int i = 1; i < getSizeInventory(); i++) {
			dumpEmptyTubeSlotToChest(i);
		}
	}
	
	public String getBondFormula() {
		String bondFormula = "";
		Molecule lastMolecule = null;
		
		for(int i = 1; i < inventoryStack.length; i++) {
			ItemStack itemstack = inventoryStack[i];
			if(isTube(itemstack)) {
				NBTTagCompound tagCompound = itemstack.getTagCompound();
				if(tagCompound == null)
					continue;
				
				String formula = tagCompound.getString("chemicalname");
				
				if(isElementTube(itemstack)) {
					int atoms = 1;
					Matcher matcher = Pattern.compile("([A-Z][a-z]*)([0-9]*)").matcher(formula);
					if(matcher.find()) {
						String element = matcher.group(1);
						if( !matcher.group(2).equals("") )
							atoms = Integer.valueOf(matcher.group(2));
						Molecule m = Molecule.elementByFormula(element, atoms);
						
						if(lastMolecule != null) {
							if(lastMolecule.elementId == m.elementId)
								m.setAtoms( m.atoms + lastMolecule.atoms );
							else
								bondFormula += lastMolecule.name;
						}
						
						lastMolecule = m;
					}
				} else {
					if(lastMolecule != null)
						bondFormula += lastMolecule.name;
					lastMolecule = null;
					bondFormula += formula;
				}
			}
		}
		
		if(lastMolecule != null) {
			bondFormula += lastMolecule.name;
		}
		
		return bondFormula;
	}
	
	public boolean canBond()
	{
		int tubeCount = 0;
		for(int i = 1; i < inventoryStack.length; i++) {
			ItemStack itemstack = inventoryStack[i];
			if(isTube(itemstack))
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
