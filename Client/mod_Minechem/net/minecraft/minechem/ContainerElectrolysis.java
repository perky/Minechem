package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class ContainerElectrolysis extends Container {

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
		
		if(flag)
			return null;
		
		return super.slotClick(i, j, flag, entityplayer);
	}

}
