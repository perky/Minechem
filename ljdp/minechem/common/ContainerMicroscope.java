package ljdp.minechem.common;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

public class ContainerMicroscope extends Container {
	
	public ContainerMicroscope(InventoryPlayer inventoryPlayer, TileEntityMicroscope microscope) {
		addSlotToContainer(new Slot(microscope, 0, 80, 33));
		for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotMicroscopeOutput(microscope, 1+i, 8 + i * 18, 75));
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
		return true;
	}
	

}
