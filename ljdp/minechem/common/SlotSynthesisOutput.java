package ljdp.minechem.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSynthesisOutput extends Slot {
	
	private TileEntitySynthesis synthesis;
	public SlotSynthesisOutput(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
		this.synthesis = (TileEntitySynthesis)par1iInventory;
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return false;
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer,
			ItemStack par2ItemStack) {
		super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
		synthesis.onOuputPickupFromSlot(par1EntityPlayer);
	}
}
