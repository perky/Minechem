package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerFusion extends Container {
	
	private int brewTime;
	private TileEntityFusion tileFusion;
	
	public ContainerFusion(InventoryPlayer inventoryplayer, TileEntityFusion tileentityfusion) {
		tileFusion = tileentityfusion;
		
		addSlot(new SlotMinechemElementTube(this, inventoryplayer.player, tileFusion, 0, 44, 22));
		addSlot(new SlotMinechemElementTube(this, inventoryplayer.player, tileFusion, 1, 44, 49));
		
		addSlot(new SlotMinechemEmptyTube(this, inventoryplayer.player, tileFusion, 2, 116, 36));
		
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
		return tileFusion.isUseableByPlayer(entityplayer);
	}
	
	@Override
	public ItemStack slotClick(int i, int j, boolean flag,
			EntityPlayer entityplayer) {
		
		if(flag)
			return null;
		
		return super.slotClick(i, j, flag, entityplayer);
	}

}
