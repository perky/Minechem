package net.minecraft.minechem;

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

public class TileEntityMinechemMachine extends TileEntity implements IInventory, ISpecialInventory, ISidedInventory {

	protected ItemStack inventoryStack[];
	protected int timer;
	public static int timerDuration = 600;
	public boolean isPowering;
	public boolean isUsingIC2Power;
	public static int IC2PowerPerTick = 0;

	public TileEntityMinechemMachine() {
		super();
		isPowering = false;
		isUsingIC2Power = false;
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
		int startpos = 0;
		int endpos = 1;
		int inputouput = getInputOrOutputForSide(from.ordinal());
		boolean didAdd = false;
		
		for(int i = startpos; i < endpos; i++) {
			if(doAdd){
				inventoryStack[i] = stack;
				didAdd = true;
			}
		}
		
		return didAdd;
	}
	
	public ItemStack extractItem(boolean doRemove, Orientations from){
		int start = 0;
		int end = 1;
		
		for(int i = start; i < end; i++) {
			if(inventoryStack[i] != null){
				ItemStack out = inventoryStack[i];
				if(doRemove)
					inventoryStack[i] = null;
				return inventoryStack[i];
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
			for(int i = 0; i < chest.getSizeInventory(); i++) {
				ItemStack stack = chest.getStackInSlot(i);
				if(stack == null) {
					chest.setInventorySlotContents(i, decrStackSize(slotnumber, 1));
				}
			}
		}
	}
	
	protected void dumpSlotToChest(int slotnumber) {
		BlockMinechem blockMinechem = (BlockMinechem)getBlockType();
		TileEntityChest chest = blockMinechem.findAdjacentChest( worldObj, xCoord, yCoord, zCoord );
		
		if(chest != null) {
			for(int i = 0; i < chest.getSizeInventory(); i++) {
				ItemStack stack = chest.getStackInSlot(i);
				if(stack == null) {
					chest.setInventorySlotContents(i, decrStackSize(slotnumber, 1));
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

}