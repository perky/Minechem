package net.minecraft.minechem;

import java.util.List;
import java.util.Map;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.ISpecialInventory;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.forge.ISidedInventory;
import net.minecraft.src.ic2.api.Direction;
import net.minecraft.src.ic2.api.IEnergySink;
import net.minecraft.src.ic2.api.Ic2Recipes;

public class TileEntityMinechemMachine extends TileEntity implements IInventory, ISpecialInventory, ISidedInventory, IEnergySink {

	protected ItemStack inventoryStack[];
	protected int timer;
	public int timerDuration;
	public boolean isPowering;
	public boolean isUsingIC2Power;
	
	public int IC2Energy;
	public int maxIC2Energy;
	public int maxIC2EnergyInput;
	public int consumeIC2EnergyPerTick;

	public TileEntityMinechemMachine() {
		super();
		timerDuration = 475;
		isPowering = false;
		isUsingIC2Power = false;
		IC2Energy = 0;
		maxIC2Energy = 100;
		consumeIC2EnergyPerTick = 1;
	}
	
	public ItemStack[] getInventoryStack() {
		return inventoryStack;
	}

	public boolean isElementTube(ItemStack itemstack) {
		return itemstack != null && itemstack.itemID == mod_Minechem.itemTesttube.shiftedIndex && itemstack.getItemDamage() > 0;
	}
	
	public boolean isTube(ItemStack itemstack) {
		return itemstack != null && itemstack.itemID == mod_Minechem.itemTesttube.shiftedIndex;
	}

	public boolean isEmptyTube(ItemStack itemstack) {
		return itemstack != null && itemstack.itemID == mod_Minechem.itemTesttubeEmpty.shiftedIndex;
	}
	
	public boolean isSingleElementTube(ItemStack itemstack) {
		if(itemstack == null)
			return false;
		
		NBTTagCompound tag = itemstack.getTagCompound();
		if(tag != null) {
			int atoms = tag.getInteger("atoms");
			return isElementTube(itemstack) && atoms == 1;
		} else
			return isElementTube(itemstack); 
	}

	public int getTimer() {
		return timer;
	}
	
	// 0 = input.
	// 1 = output.
	// 2 = both.
	public int getInputOrOutputForSide(int side) {
		return 2;
	}
	
	public boolean addItem (ItemStack stack, boolean doAdd, Orientations from){
		int startpos = getStartInventorySide(from.ordinal());
		int endpos = startpos + getSizeInventorySide(from.ordinal());
		
		for(int i = startpos; i < endpos; i++) {
			ItemStack stack1 = inventoryStack[i];
			if(stack1 == null) {
				if(tryAddingStack(stack, 0, doAdd)) return true;
			}
		}
		
		return false;
	}
	
	public ItemStack extractItem(boolean doRemove, Orientations from){
		int startpos = getStartInventorySide(from.ordinal());
		int endpos = startpos + getSizeInventorySide(from.ordinal());
		
		for(int i = startpos; i < endpos; i++) {
			ItemStack out = inventoryStack[i];
			if(out != null){
				if(doRemove){
					return decrStackSize(i, 1);
				}else
					return out;
			}
		}
		
		return null;
	}
	
	public boolean tryAddingStack(ItemStack newStack, int slotnumber, boolean doAdd) {
		ItemStack currentStack = getStackInSlot(slotnumber);
		if(currentStack != null) {
			if(currentStack.getItem() == newStack.getItem()
					&& currentStack.getItemDamage() == newStack.getItemDamage()
					&& currentStack.stackSize + 1 <= newStack.getMaxStackSize()
					&& currentStack.stackSize + 1 <= getInventoryStackLimit())
			{
				if(doAdd) {
					currentStack.stackSize++;
					newStack.stackSize--;
				}
				return true;
			}
		} else {
			if(doAdd) {
				currentStack = newStack.copy();
				currentStack.stackSize = 1;
				newStack.stackSize--;
				setInventorySlotContents(slotnumber, currentStack);
			}
			return true;
		}
		
		return false;
	}
	
	protected void takeEmptyTubeFromChest(int slotnumber) {
		if(inventoryStack[slotnumber] != null)
			return;
		BlockMinechem blockMinechem = (BlockMinechem)getBlockType();
		TileEntityChest chest = blockMinechem.findAdjacentChest( worldObj, xCoord, yCoord, zCoord );
		
		if(chest != null) {
			for(int i = 0; i < chest.getSizeInventory(); i++) {
				ItemStack stack = chest.getStackInSlot(i);
				if(isEmptyTube(stack)) {
					ItemStack newStack = stack.copy();
					newStack.stackSize = 1;
					stack.stackSize--;
					if(stack.stackSize == 0) stack = null;
					chest.setInventorySlotContents(i, stack);
					setInventorySlotContents(slotnumber, newStack);
					break;
				}
			}
		}
	}
	
	protected void takeTestTubeFromSorter(int slotStart, int slotEnd) {
		BlockMinechem blockMinechem = (BlockMinechem)getBlockType();
		List<TileEntityMinechemSorter> sorters = blockMinechem.findAdjacentSorters(worldObj, xCoord, yCoord, zCoord);
		List<Orientations> sorterOrientations = blockMinechem.findAdjacentSorterOrientations(worldObj, xCoord, yCoord, zCoord);
		
		if(sorters.size() != 0) {
			for(int i = 0; i < sorters.size(); i++) {
				TileEntityMinechemSorter sorter = sorters.get(i);
				Orientations sorterOrientation = sorterOrientations.get(i);
				sorterOrientation = sorterOrientation.reverse();
				
				ItemStack possibleStack = sorter.extractItem(false, sorterOrientation);
				if(possibleStack != null && isTube(possibleStack)) {
					for(int i1 = slotStart; i1 < slotEnd; i1++) {
						if(inventoryStack[i1] == null) {
							ItemStack itemstack = sorter.extractItem(true, sorterOrientation);
							setInventorySlotContents(i1, itemstack);
							return;
						}
					}
				}
			}
		}
	}
	
	protected void dumpEmptyTubeSlotToChest(int slotnumber) {
		if(!isEmptyTube( inventoryStack[slotnumber] ))
			return;
		
		BlockMinechem blockMinechem = (BlockMinechem)getBlockType();
		TileEntityChest chest = blockMinechem.findAdjacentChest( worldObj, xCoord, yCoord, zCoord );
		
		if(chest != null) {
			ItemStack emptyTube = new ItemStack(mod_Minechem.itemTesttubeEmpty);
			for(int i = 0; i < chest.getSizeInventory(); i++) {
				ItemStack stack = chest.getStackInSlot(i);
				if(stack != null && stack.isItemEqual(emptyTube) && stack.stackSize != 64) {
					stack.stackSize++;
					decrStackSize(slotnumber, 1);
					break;
				}
				else if(stack == null) {
					chest.setInventorySlotContents(i, decrStackSize(slotnumber, 1));
					break;
				}
			}
		}
	}
	
	protected void dumpSlotToChest(int slotnumber) {
		if(isEmptyTube(inventoryStack[slotnumber])){
			dumpEmptyTubeSlotToChest(slotnumber);
			return;
		}
		
		BlockMinechem blockMinechem = (BlockMinechem)getBlockType();
		List<TileEntityMinechemSorter> sorters = blockMinechem.findAdjacentSorters( worldObj, xCoord, yCoord, zCoord );
		
		if(sorters.size() != 0) {
			
			for(TileEntityMinechemSorter sorter : sorters) {
				ItemStack itemToSort = getStackInSlot(slotnumber);
				List<Integer> possibleDirs = sorter.getPossibleSortDirs(itemToSort);
				if(possibleDirs.size() != 0) {
					sorter.setInventorySlotContents(0, decrStackSize(slotnumber, 1));
					sorter.doSort(possibleDirs);
					return;
				}
			}
			
		}
		
		TileEntityChest chest = blockMinechem.findAdjacentChest( worldObj, xCoord, yCoord, zCoord );
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

	@Override
	public int getSizeInventory() {
		return inventoryStack.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(i >= 0 && i < inventoryStack.length)
	    {
	        return inventoryStack[i];
	    } else
	    {
	        return null;
	    }
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
    {
        if (inventoryStack[i] != null)
        {
            if (inventoryStack[i].stackSize <= j)
            {
                ItemStack itemstack = inventoryStack[i];
                inventoryStack[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = inventoryStack[i].splitStack(j);
            if (inventoryStack[i].stackSize == 0)
            {
            	inventoryStack[i] = null;
            }
            return itemstack1;
        }
        else
        {
            return null;
        }
    }

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(i >= 0 && i < inventoryStack.length)
	    {
			inventoryStack[i] = itemstack;
	    }
	}

	@Override
	public String getInvName() {
		return "Minechem Machine";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
	        return false;
		
	    return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		timer = nbttagcompound.getInteger("timer");
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        inventoryStack = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte byte0 = nbttagcompound1.getByte("Slot");
            if (byte0 >= 0 && byte0 < inventoryStack.length)
            {
            	inventoryStack[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("timer", timer);
		NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < inventoryStack.length; i++)
        {
            if (inventoryStack[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                inventoryStack[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }
        nbttagcompound.setTag("Items", nbttaglist);
	}

	@Override
	public int getStartInventorySide(int side) {
		return 0;
	}

	@Override
	public int getSizeInventorySide(int side) {
		return 0;
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
		if(mod_Minechem.requireIC2Power) {
			return IC2Energy < maxIC2Energy;
		} else 
			return false;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
		if(amount > maxIC2EnergyInput) {
			float f = 2F;
	        worldObj.createExplosion(null, xCoord, yCoord, zCoord, f);
	        worldObj.setBlockWithNotify(xCoord, yCoord, zCoord, 0);
	        return 0;
		}
		
		int returnEnergy = 0;
		IC2Energy += amount;
		if(IC2Energy > maxIC2Energy) {
			returnEnergy = IC2Energy - maxIC2Energy;
			IC2Energy = maxIC2Energy;
		}
		
		return returnEnergy;
	}
	
	protected boolean didConsumePower() {
		if(IC2Energy >= consumeIC2EnergyPerTick) {
			IC2Energy -= consumeIC2EnergyPerTick;
			return true;
		} else {
			IC2Energy = 0;
			return false;
		}
	}

}