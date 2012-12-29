package ljdp.minechem.common.containers;

import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSynthesis extends Container {

	private TileEntitySynthesis synthesis;
	
	public ContainerSynthesis(InventoryPlayer inventoryPlayer, TileEntitySynthesis synthesis) {
		this.synthesis = synthesis;
		addSlotToContainer(new SlotSynthesisOutput(synthesis, synthesis.kStartOutput, 134, 36));
		int slot = 0;
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 3; col++) {
				addSlotToContainer(new Slot(synthesis, synthesis.kStartInput + slot, 62 + (col * 18), 18 + (row * 18)));
				slot++;
			}
		}
		slot = 0;
		for(int row = 0; row < 2; row++) {
			for(int col = 0; col < 2; col++) {
				addSlotToContainer(new SlotOutput(synthesis, synthesis.kStartBottles + slot, 8 + (col * 18), 27 + (row * 18)));
				slot++;
			}
		}
		bindPlayerInventory(inventoryPlayer);
	}
	
	private void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                    addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                                    8 + j * 18, 84 + i * 18)
                    );
            }
		}

		for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return synthesis.isUseableByPlayer(var1);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slot) {
		if(!synthesis.hasEnoughPower())
			return null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		if(slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			ItemStack stack = stackInSlot.copy();
			if(slot == synthesis.kStartOutput) {
				return null;
			} else if(slot >= 0 && slot < synthesis.getSizeInventory()) {
				if(!mergeItemStack(stackInSlot, synthesis.getSizeInventory(), inventorySlots.size(), true))
					return null;
			} else if(!mergeItemStack(stackInSlot, synthesis.kStartInput, synthesis.kStartInput + synthesis.kSizeInput, false))
				return null;
			
			if(stackInSlot.stackSize == 0)
				slotObject.putStack(null);
			else
				slotObject.onSlotChanged();
			
			return stack;
		}
		return null;
	}
	
	@Override
	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
	}

}
