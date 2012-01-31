package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

public class ContainerFission extends Container {

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

}
