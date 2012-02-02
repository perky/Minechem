package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerFission extends ContainerMinechem {

	private int brewTime;
	private TileEntityFission tileFission;
	
	public ContainerFission(InventoryPlayer inventoryplayer, TileEntityFission tileentity) {
		tileFission = tileentity;
		
		addSlot(new SlotMinechemElementTube(this, inventoryplayer.player, tileFission, 0, 63, 36));
		addSlot(new SlotMinechemEmptyTube(this, inventoryplayer.player, tileFission, 1, 98, 21));
		addSlot(new SlotMinechemEmptyTube(this, inventoryplayer.player, tileFission, 2, 98, 51));
		
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
		return tileFission.isUseableByPlayer(entityplayer);
	}
	
	public ItemStack transferStackInSlot(int i)
    {
		int machineSize = 3;
		int machineStart = 0;
		int playerInvStart = machineSize;
		int playerInvEnd = (9*4) + playerInvStart;
		
		boolean tryOnce = false;

        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if(i >= playerInvStart && i < playerInvEnd+1) 
            {
            	if(Util.isEmptyTube(itemstack1)) {
            		if(!mergeItemStack2(itemstack1, 1, 3, false, true))
            			return null;
            	} else {
            		if(!mergeItemStack(itemstack1, 0, 1, false))
            			return null;
            	}
            }
            else if(i >= 0 && i < machineSize+1) 
            {
            	if(!mergeItemStack(itemstack1, playerInvStart, playerInvEnd, false))
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
        else
        	return itemstack;
    }

}
