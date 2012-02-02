package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerElectrolysis extends ContainerMinechem {

	private int brewTime;
	private TileEntityElectrolysis tileElectrolysis;
	
	public ContainerElectrolysis(InventoryPlayer inventoryplayer, TileEntityElectrolysis tileentityelectrolysis) {
		brewTime = 0;
		tileElectrolysis = tileentityelectrolysis;
		
		addSlot(new SlotElectrolysisInput(this, inventoryplayer.player, tileElectrolysis, 0, 62, 53));
		addSlot(new SlotElectrolysisInput(this, inventoryplayer.player, tileElectrolysis, 1, 80, 53));
		addSlot(new SlotElectrolysisInput(this, inventoryplayer.player, tileElectrolysis, 2, 98, 53));
		
		addSlot(new SlotElectrolysisOutput(this, inventoryplayer.player, tileElectrolysis, 3, 68, 22));
		addSlot(new SlotElectrolysisOutput(this, inventoryplayer.player, tileElectrolysis, 4, 92, 22));
		
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
		return tileElectrolysis.isUseableByPlayer(entityplayer);
	}
	
	
	@Override
	public ItemStack slotClick(int i, int j, boolean flag,
			EntityPlayer entityplayer) {
		
		return super.slotClick(i, j, flag, entityplayer);
	}
	
	public ItemStack transferStackInSlot(int i)
    {
		int machineInvEnd = 4;
		int playerInvStart = 5;
		int playerInvEnd = 41;
		int playerHotbarStart = 27;
		
		boolean tryOnce = false;

        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (i >= 0 && i <= machineInvEnd)
            {
                if (!mergeItemStack(itemstack1, playerInvStart, playerInvEnd, true))
                    return null;
            }
            else if (i >= playerInvStart && i < playerInvEnd)
            {
            	if(Util.isEmptyTube(itemstack1)) {
            		if (!mergeItemStack2(itemstack1, 3, 5, false, true))
                        return null;
            	} else {
            		if (!mergeItemStack2(itemstack1, 0, 3, false, true))
                        return null;
            	}
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
