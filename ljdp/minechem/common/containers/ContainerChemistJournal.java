package ljdp.minechem.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerChemistJournal extends Container {
	
	public ContainerChemistJournal(InventoryPlayer inventoryPlayer) {
		
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityPlayer) {
		return true;
	}

}
