package ljdp.minechem.common.containers;

import ljdp.minechem.common.tileentity.TileEntityFusion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerFusion extends Container {

	TileEntityFusion fusion;
	InventoryPlayer inventoryPlayer;

	public ContainerFusion(InventoryPlayer inventoryPlayer, TileEntityFusion fusion) {
		this.inventoryPlayer = inventoryPlayer;
		this.fusion = fusion;
		
		addSlotToContainer(new SlotFusionStar(fusion, fusion.kStartFusionStar, 80, 18));
		addSlotToContainer(new SlotElement(fusion, fusion.kStartInput1, 22, 62));
		addSlotToContainer(new SlotElement(fusion, fusion.kStartInput2, 138, 62));
		addSlotToContainer(new SlotOutput(fusion, fusion.kStartOutput, 80, 62));
		
		bindPlayerInventory(inventoryPlayer);
	}
	
	private void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                    addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                                    8 + j * 18, 105 + i * 18)
                    );
            }
		}

		for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 163));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return fusion.isUseableByPlayer(var1);
	}


}
