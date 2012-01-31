package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

public class ContainerUnbonder extends Container {

	private int brewTime;
	private TileEntityUnbonder tileUnbonder;
	
	public ContainerUnbonder(InventoryPlayer inventoryplayer, TileEntityUnbonder tileentity) {
		tileUnbonder = tileentity;
		
		//addSlot(new SlotMinechemElementTube(this, inventoryplayer.player, tileUnbonder, 0, 36, 36));
		addSlot(new Slot(tileUnbonder, 0, 36, 36));
		
		for(int i = 1; i < 5; i++) {
			addSlot(new SlotMinechemEmptyTube(this, inventoryplayer.player, tileUnbonder, i, 69+((i-1)*18), 36));
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
		return tileUnbonder.isUseableByPlayer(entityplayer);
	}

}
