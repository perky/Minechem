package net.minecraft.minechem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.buildcraft.api.Orientations;

public class TileEntityMinechemSorter extends TileEntityMinechemMachine {
	
	public String[] formulas;
	private static final String greaterThanPattern = "([A-z]*)>([0-9]*)";
	private static final String lesserThanPattern = "([A-z]*)<([0-9]*)";
	private static final String singleElementPattern = "^([A-z]*)([0-9]*)$";
	private Random random;
	private List<Integer> defaultDirs;
	
	public TileEntityMinechemSorter() {
		super();
		int size = (5*5)+1;
		formulas = new String[5];
		for(int i = 0; i < 5; i++){
			formulas[i] = "";
		}
		inventoryStack = new ItemStack[size];
		random = new Random();
		defaultDirs = new ArrayList();
	}
	
	public void onFormulasChanged(String[] changedFormulas) {
		formulas = changedFormulas.clone();
		defaultDirs.clear();
		for(int i = 0; i < 5; i++) {
			if(formulas[i].equals("default")) {
				defaultDirs.add(i);
			}
		}
	}

	@Override
	public void updateEntity() {
		List<Integer> possibleDirs = getPossibleSortDirs( inventoryStack[0] );
		doSort( possibleDirs );
		
		for(int i = 1; i < 6; i++){
			dumpToSpecificChest(Orientations.YNeg, i);
		}
		for(int i = 6; i < 11; i++){
			dumpToSpecificChest(Orientations.ZNeg, i);
		}
		for(int i = 11; i < 16; i++){
			dumpToSpecificChest(Orientations.ZPos, i);
		}
		for(int i = 16; i < 21; i++){
			dumpToSpecificChest(Orientations.XNeg, i);
		}
		for(int i = 21; i < 26; i++){
			dumpToSpecificChest(Orientations.XPos, i);
		}
	}
	
	public void doSort(List<Integer> possibleDirs) {
		ItemStack itemToSort = inventoryStack[0];
		
		if(itemToSort != null && isTube(itemToSort)) {
			if(possibleDirs.size() != 0) {
				int chosenIndex = random.nextInt(possibleDirs.size());
				int chosenDir = possibleDirs.get(chosenIndex);
				moveStackToDir(itemToSort, chosenDir);
			} 
			else if(defaultDirs.size() != 0) {
				int chosenIndex = random.nextInt(defaultDirs.size());
				int chosenDir = defaultDirs.get(chosenIndex);
				moveStackToDir(itemToSort, chosenDir);
			} else {
				dispenceStack(itemToSort);
				setInventorySlotContents(0, null);
			}
		} else if(itemToSort != null && !isTube(itemToSort)) {
			dispenceStack(itemToSort);
			setInventorySlotContents(0, null);
		}
	}
	
	public List<Integer> getPossibleSortDirs(ItemStack itemToSort) {
		List<Integer> possibleDirs = new ArrayList();
		if(itemToSort != null && isTube(itemToSort)) {
			for(int i = 0; i < 5; i++) {
				Molecule m = Molecule.moleculeByItemStack(itemToSort);
				if(m == null) continue;
				if(canSortTube(m.name, formulas[i], i)) {
					possibleDirs.add(i);
				}
			}
		}
		
		return possibleDirs;
	}
	
	private void moveStackToDir(ItemStack itemstack, int dir) {
		for(int i = 0; i < 5; i++) {
			int pos = ((5*dir)+1)+i;
			ItemStack existingStack = inventoryStack[pos];
			if(existingStack == null) {
				setInventorySlotContents(pos, itemstack);
				setInventorySlotContents(0, null);
				return;
			}
		}
		
		// If we can't move stack, chuck it out.
		dispenceStack(itemstack);
		setInventorySlotContents(0, null);
	}
	
	public void dumpToSpecificChest(Orientations or, int slotnumber) {
		if(inventoryStack[slotnumber] != null) {
			BlockMinechem blockMinechem = (BlockMinechem)getBlockType();
			TileEntityChest chest = null;
			if(or == Orientations.YNeg) {
				chest = blockMinechem.getAdjacentChest(worldObj, xCoord, yCoord-1, zCoord);
			}
			if(or == Orientations.ZNeg) {
				chest = blockMinechem.getAdjacentChest(worldObj, xCoord, yCoord, zCoord-1);
			}
			if(or == Orientations.ZPos) {
				chest = blockMinechem.getAdjacentChest(worldObj, xCoord, yCoord, zCoord+1);
			}
			if(or == Orientations.XNeg) {
				chest = blockMinechem.getAdjacentChest(worldObj, xCoord-1, yCoord, zCoord);
			}
			if(or == Orientations.XPos) {
				chest = blockMinechem.getAdjacentChest(worldObj, xCoord+1, yCoord, zCoord);
			}
			
			if(chest != null) {
				for(int i = 0; i < chest.getSizeInventory(); i++) {
					ItemStack stack = chest.getStackInSlot(i);
					if(stack == null) {
						chest.setInventorySlotContents(i, decrStackSize(slotnumber, 1));
						break;
					}
				}
			}
		}
	}
	
	private void dispenceStack(ItemStack itemstack) {
		EntityItem entityitem = new EntityItem(worldObj, xCoord, yCoord + 0.5D, zCoord, itemstack);
        double d3 = random.nextDouble() * 0.10000000000000001D + 0.20000000000000001D;
        entityitem.motionX = (double)1 * d3;
        entityitem.motionY = 0.20000000298023224D;
        entityitem.motionZ = (double)1 * d3;
        entityitem.motionX += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.motionY += random.nextGaussian() * 0.0074999998323619366D * 6D;
        entityitem.motionZ += random.nextGaussian() * 0.0074999998323619366D * 6D;
        worldObj.spawnEntityInWorld(entityitem);
        worldObj.playAuxSFX(1000, xCoord, yCoord, zCoord, 0);
	}
	
	private boolean canSortTube(String suppliedFormula, String requestedFormula, int sortDir) {
		if(!requestedFormula.equals("")){
			if(requestedFormula.substring(0, 1).equals("!")) {
				requestedFormula = requestedFormula.replaceFirst("!", "");
			} else {
				requestedFormula = requestedFormula.replaceAll("\\*", "[0-9]*");
			}
		}
		if(requestedFormula.equals("")) {
			return false;
		}
		else if(requestedFormula.matches(greaterThanPattern) && suppliedFormula.matches(singleElementPattern)) {
			Matcher match1 = Pattern.compile(greaterThanPattern).matcher(requestedFormula);
			Matcher match2 = Pattern.compile(singleElementPattern).matcher(suppliedFormula);
			if(match1.find() && match2.find()) {
				String requestedElement = match1.group(1);
				int requestedAtoms = Integer.valueOf(match1.group(2));
				
				String suppliedElement = match2.group(1);
				int suppliedAtoms = Integer.valueOf(match2.group(2));
				
				if( suppliedElement.equals(requestedElement) && suppliedAtoms > requestedAtoms) {
					return true;
				}
			}
		}
		else if(requestedFormula.matches(lesserThanPattern) && suppliedFormula.matches(singleElementPattern)) {
			Matcher match1 = Pattern.compile(lesserThanPattern).matcher(requestedFormula);
			Matcher match2 = Pattern.compile(singleElementPattern).matcher(suppliedFormula);
			if(match1.find() && match2.find()) {
				String requestedElement = match1.group(1);
				int requestedAtoms = Integer.valueOf(match1.group(2));
				
				String suppliedElement = match2.group(1);
				int suppliedAtoms = Integer.valueOf(match2.group(2));
				
				if( suppliedElement.equals(requestedElement) && suppliedAtoms < requestedAtoms) {
					return true;
				}
			}
		} 
		else if(suppliedFormula.matches(requestedFormula)){
			return true;
		}
		
		return false;
	}	

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList tagList = new NBTTagList();
		for(int i = 0; i < 5; i++) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setString("formula", formulas[i]);
			tagList.setTag(nbttagcompound1);
		}
		nbttagcompound.setTag("formulas", tagList);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList tagList = nbttagcompound.getTagList("formulas");
		String[] tempFormulas = new String[5];
		for(int i = 0; i < 5; i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)tagList.tagAt(i);
			tempFormulas[i] = nbttagcompound1.getString("formula");
		}
		onFormulasChanged(tempFormulas);
	}

	@Override
	public int getStartInventorySide(int side) {
		switch(side) {
		case 0:
			return (5*0)+1;
		case 1:
			return 0;
		case 2:
			return (5*1)+1;
		case 3:
			return (5*2)+1;
		case 4:
			return (5*3)+1;
		case 5:
			return (5*4)+1;
		}
		return 0;
	}

	@Override
	public int getSizeInventorySide(int side) {
		if(side == 1)
			return 1;
		else
			return 5;
	}
	
	
}
