package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerBonder extends ContainerMinechem {

	private int brewTime;
	private TileEntityBonder tileBonder;
	
	public ContainerBonder(InventoryPlayer inventoryplayer, TileEntityBonder tileentityfusion) {
		tileBonder = tileentityfusion;
		
		addSlot(new SlotMinechemEmptyTube(this, inventoryplayer.player, tileBonder, 0, 36, 36));
		
		for(int i = 1; i < 5; i++) {
			addSlot(new Slot(tileBonder, i, 69+((i-1)*18), 36));
		}
		
		// Player inventory.
		for(int i = 0; i < 3; i++)
        {
            for(int k = 0; k < 9; k++)
            {
                addSlot(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }
		// Player hotbar.
        for(int j = 0; j < 9; j++)
        {
            addSlot(new Slot(inventoryplayer, j, 8 + j * 18, 142));
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileBonder.isUseableByPlayer(entityplayer);
	}

	public ItemStack transferStackInSlot(int i)
    {
		int bonderInvEnd = 4;
		int playerInvStart = 5;
		int playerInvEnd = 41;
		int playerHotbarStart = 31;
		boolean tryOnce = false;
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (i >= playerInvStart && i < playerInvEnd)
            {
            	if(Util.isEmptyTube(itemstack1)) {
            		if (!mergeItemStack2(itemstack1, 0, 1, false, true))
                		return null;
                	tryOnce = true;
            	} else {
            		if (!mergeItemStack(itemstack1, 1, 5, false))
            			return null;
            	}
            }
            else if (i >= 0 && i < playerInvStart)
            {
                if (!mergeItemStack(itemstack1, playerInvStart, playerInvEnd, true))
                    return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
            
            if (itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(itemstack1);
            }
            else
            {
                return null;
            }
        }
        if(tryOnce)
        	return null;
        return itemstack;
    }
	
	

}
