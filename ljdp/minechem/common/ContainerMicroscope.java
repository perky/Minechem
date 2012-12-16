package ljdp.minechem.common;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerMicroscope extends Container {
	
	TileEntityMicroscope microscope;
	
	public ContainerMicroscope(InventoryPlayer inventoryPlayer, TileEntityMicroscope microscope) {
		this.microscope = microscope;
		addSlotToContainer(new Slot(microscope, 0, 44, 45));
		
		int slot = 0;
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				addSlotToContainer(new SlotMicroscopeOutput(microscope, 1 + slot, 98 + (col * 18), 27 + (row * 18)));
				slot++;
			}
		}
		bindPlayerInventory(inventoryPlayer);
	}
	
	private void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		int inventoryY = 107;
		int hotBarY = 165;
		for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                    addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                                    8 + j * 18, inventoryY + i * 18)
                    );
            }
		}

		for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, hotBarY));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return microscope.isUseableByPlayer(var1);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slot) {
		Slot slotObject = (Slot) inventorySlots.get(slot);
		if(slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			ItemStack stack = stackInSlot.copy();
			if(slot == 0) {
				if(!mergeItemStack(stackInSlot, 1, inventorySlots.size(), true))
					return null;
			} else if(slot >= 1) {
				if(!mergeItemStack(stackInSlot, 0, 1, false))
					return null;
			} else if(!mergeItemStack(stackInSlot, 1, inventorySlots.size(), true))
				return null;
			
			if(stackInSlot.stackSize == 0)
				slotObject.putStack(null);
			else
				slotObject.onSlotChanged();
			
			return stack;
		}
		return null;
	}
	

}
