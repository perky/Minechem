package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerMinechem extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	protected boolean mergeItemStack2(ItemStack itemstack, int i, int j, boolean flag, boolean single) {
		boolean flag1 = false;
        int k = i;
        if (flag)
        {
            k = j - 1;
        }
        if (itemstack.isStackable() && !single)
        {
            while (itemstack.stackSize > 0 && (!flag && k < j || flag && k >= i))
            {
                Slot slot = (Slot)inventorySlots.get(k);
                ItemStack itemstack1 = slot.getStack();
                if (itemstack1 != null && itemstack1.itemID == itemstack.itemID && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.func_46154_a(itemstack, itemstack1))
                {
                    int i1 = itemstack1.stackSize + itemstack.stackSize;
                    if (i1 <= itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize = 0;
                        itemstack1.stackSize = i1;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize -= itemstack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = itemstack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }
                if (flag)
                {
                    k--;
                }
                else
                {
                    k++;
                }
            }
        }
        if (itemstack.stackSize > 0)
        {
            int l;
            if (flag)
            {
                l = j - 1;
            }
            else
            {
                l = i;
            }
            do
            {
                if ((flag || l >= j) && (!flag || l < i))
                {
                    break;
                }
                Slot slot1 = (Slot)inventorySlots.get(l);
                ItemStack itemstack2 = slot1.getStack();
                if (itemstack2 == null)
                {
                	if(single) {
                		ItemStack itemstack3 = itemstack.splitStack(1);
                		slot1.putStack(itemstack3);
                		flag1 = true;
                		return true;
                	} else {
                		slot1.putStack(itemstack.copy());
                        slot1.onSlotChanged();
                        itemstack.stackSize = 0;
                        flag1 = true;
                        break;
                	}
                }
                if (flag)
                {
                    l--;
                }
                else
                {
                    l++;
                }
            }
            while (true);
        }
        return flag1;
	}
	
	@Override
	protected boolean mergeItemStack(ItemStack itemstack, int i, int j, boolean flag)
    {
        boolean flag1 = false;
        int k = i;
        if (flag)
        {
            k = j - 1;
        }
        if (itemstack.isStackable())
        {
            while (itemstack.stackSize > 0 && (!flag && k < j || flag && k >= i))
            {
                Slot slot = (Slot)inventorySlots.get(k);
                ItemStack itemstack1 = slot.getStack();
                if (itemstack1 != null && itemstack1.itemID == itemstack.itemID && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.func_46154_a(itemstack, itemstack1))
                {
                    int i1 = itemstack1.stackSize + itemstack.stackSize;
                    if (i1 <= itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize = 0;
                        itemstack1.stackSize = i1;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize -= itemstack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = itemstack.getMaxStackSize();
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }
                if (flag)
                {
                    k--;
                }
                else
                {
                    k++;
                }
            }
        }
        if (itemstack.stackSize > 0)
        {
            int l;
            if (flag)
            {
                l = j - 1;
            }
            else
            {
                l = i;
            }
            do
            {
                if ((flag || l >= j) && (!flag || l < i))
                {
                    break;
                }
                Slot slot1 = (Slot)inventorySlots.get(l);
                ItemStack itemstack2 = slot1.getStack();
                if (itemstack2 == null)
                {
                    slot1.putStack(itemstack.copy());
                    slot1.onSlotChanged();
                    itemstack.stackSize = 0;
                    flag1 = true;
                    break;
                }
                if (flag)
                {
                    l--;
                }
                else
                {
                    l++;
                }
            }
            while (true);
        }
        return flag1;
    }

}
