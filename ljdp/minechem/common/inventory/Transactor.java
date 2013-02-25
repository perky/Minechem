package ljdp.minechem.common.inventory;

import java.util.ArrayList;
import java.util.List;

import ljdp.minechem.api.util.Util;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;

public class Transactor {
	
	BoundedInventory inventory;
	int maxStackSize = 64;
	
	public Transactor(BoundedInventory inventory) {
		this.inventory = inventory;
	}
	
	public Transactor(BoundedInventory inventory, int maxStackSize) {
		this(inventory);
		this.maxStackSize = maxStackSize;
	}

	public ItemStack add(ItemStack stack, boolean doAdd) {
		int totalAmountAdded = 0;
		int totalAmountToAdd = stack.stackSize;
		int slot = 0;
		while(totalAmountToAdd > 0 && slot < inventory.getSizeInventory()) {
			int amountAdded   = putStackInSlot(stack, totalAmountToAdd, slot, doAdd);
			totalAmountAdded += amountAdded;
			totalAmountToAdd -= amountAdded;
			slot++;
		}
		ItemStack remainingStack = stack.copy();
		remainingStack.stackSize -= totalAmountAdded;
		if(remainingStack.stackSize <= 0)
			remainingStack = null;
		return remainingStack;
	}
	
	public ItemStack[] remove(int amount, boolean doRemove) {
		int totalAmountRemoved = 0;
		int totalAmountToRemove = amount;
		List<ItemStack> removed = new ArrayList();
		int slot = 0;
		while(totalAmountToRemove > 0 && slot < inventory.getSizeInventory()) {
			ItemStack stackRemoved;
			if(doRemove)
				stackRemoved = inventory.decrStackSize(slot, totalAmountToRemove);
			else {
				stackRemoved = inventory.getStackInSlot(slot).copy();
				if(stackRemoved != null)
					stackRemoved.stackSize = Math.min(amount, stackRemoved.stackSize);
			}
			if(stackRemoved != null) {
				totalAmountRemoved  += stackRemoved.stackSize;
				totalAmountToRemove -= stackRemoved.stackSize;
				removed.add(stackRemoved);
			}
			slot++;
		}
		return removed.toArray(new ItemStack[removed.size()]);
	}
	
	public ItemStack removeItem(boolean doRemove) {
		for(int slot = 0; slot < inventory.getSizeInventory(); slot++) {
			ItemStack stackInSlot = inventory.getStackInSlot(slot);
			if(stackInSlot != null) {
				if(doRemove) {
					return inventory.decrStackSize(slot, 1);
				} else {
					ItemStack returnStack = stackInSlot.copy();
					returnStack.stackSize = 1;
					return returnStack;
				}
			}
		}
		return null;
	}
	
	public int putStackInSlot(ItemStack stack, int amount, int slot, boolean doAdd) {
		ItemStack stackInSlot = inventory.getStackInSlot(slot);
		if(stackInSlot == null) {
			if(stack.stackSize > getMaxStackSize(stack))
				stack.stackSize = getMaxStackSize(stack);
			if(doAdd)
				inventory.setInventorySlotContents(slot, stack);
			return stack.stackSize;
		}
		if(Util.stacksAreSameKind(stack, stackInSlot)) {
			return appendStackToSlot(stack, amount, slot, doAdd);
		} else {
			return 0;
		}
	}
	
	public int appendStackToSlot(ItemStack stack, int amount, int slot, boolean doAdd) {
		ItemStack stackInSlot = inventory.getStackInSlot(slot);
		if(stackInSlot.stackSize + amount > getMaxStackSize(stackInSlot)) {
			int partialAmount = (getMaxStackSize(stackInSlot) - stackInSlot.stackSize);
			if(doAdd)
				stackInSlot.stackSize += partialAmount;
			return partialAmount;
		} else {
			if(doAdd)
				stackInSlot.stackSize += amount;
			return amount;
		}
	}
	
	public int getMaxStackSize(ItemStack itemstack) {
		return Math.min(itemstack.getMaxStackSize(), this.maxStackSize);
	}
	
	
}
