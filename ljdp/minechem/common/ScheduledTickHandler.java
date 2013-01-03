package ljdp.minechem.common;

import java.util.EnumSet;
import java.util.List;

import ljdp.minechem.api.util.Constants;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
		EntityPlayer entityPlayer = (EntityPlayer)tickData[0];
		World world = entityPlayer.worldObj;
		checkForPoison(entityPlayer);
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
	
	private void checkForPoison(EntityPlayer entityPlayer) {
		ItemStack itemInUse = entityPlayer.getItemInUse();
		if(itemInUse != null && itemInUse.getTagCompound() != null) {
			NBTTagCompound stackTag = itemInUse.getTagCompound();
			boolean isPoisoned = stackTag.getBoolean("minechem.isPoisoned");
			if(isPoisoned && !entityPlayer.isPotionActive(Potion.wither)) {
				entityPlayer.addPotionEffect(new PotionEffect(Potion.wither.getId(), Constants.TICKS_PER_MINUTE, 1));
			}
		}
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
