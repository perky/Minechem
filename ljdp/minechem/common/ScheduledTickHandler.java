package ljdp.minechem.common;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ScheduledTickHandler implements IScheduledTickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer entityPlayer = (EntityPlayer)tickData[0];
		World world = entityPlayer.worldObj;
		updateElements(entityPlayer, world);
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "minechem.ScheduledTickHandler";
	}

	@Override
	public int nextTickSpacing() {
		return 20;
	}
	
	private void updateElements(EntityPlayer entityPlayer, World world) {
		Container openContainer = entityPlayer.openContainer;
		if(openContainer != null) {
			List<ItemStack> itemstacks = openContainer.getInventory();
			for(ItemStack itemstack : itemstacks) {
				if(itemstack != null && itemstack.itemID == MinechemItems.element.shiftedIndex)
					updateElement(itemstack, entityPlayer, world);
			}
		}
	}

	private void updateElement(ItemStack itemstack, EntityPlayer entityPlayer, World world) {
		MinechemItems.element.onUpdate(itemstack, world, entityPlayer, 0, false);
	}

}
