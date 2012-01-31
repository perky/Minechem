package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.TileEntity;

public class ContainerMinechemCrafting extends Container {
	
	private TileEntityMinechemMachine tileEntity;
	
	public ContainerMinechemCrafting(InventoryPlayer inventoryplayer, TileEntity tileentity){
		this.tileEntity = (TileEntityMinechemMachine)tileentity;
		
		addSlot(new SlotMinechemElementTube(this, inventoryplayer.player, tileEntity, 0, 42, 34));
		addSlot(new SlotMinechemElementTube(this, inventoryplayer.player, tileEntity, 1, 60, 34));
		addSlot(new Slot(tileEntity, 2, 118, 34));
		
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
		return tileEntity.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack slotClick(int i, int j, boolean flag,
			EntityPlayer entityplayer) {
		
		if(flag)
			return null;
		
		return super.slotClick(i, j, flag, entityplayer);
	}

}
