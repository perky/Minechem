package ljdp.minechem.common;

import java.util.ArrayList;
import java.util.List;

import ljdp.minechem.api.core.EnumRadioactivity;
import ljdp.minechem.api.core.IRadiationShield;
import ljdp.minechem.common.containers.ContainerChemicalStorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class RadiationHandler {
	
	private static RadiationHandler instance = new RadiationHandler();
	public static RadiationHandler getInstance() {
		if(instance == null)
			instance = new RadiationHandler();
		return instance;
	}
	
	private RadiationHandler() {
		
	}
	
	public void update(EntityPlayer player) {
		Container openContainer = player.openContainer;
		if(openContainer != null && openContainer instanceof ContainerChemicalStorage)
			updateChemicalStorageContainer(player, openContainer);
		else if(openContainer != null)
			updateContainer(player, openContainer);
		else
			updateContainer(player, player.inventoryContainer);
	}
	
	private void updateChemicalStorageContainer(EntityPlayer player, Container openContainer) {
		ContainerChemicalStorage chemicalStorage = (ContainerChemicalStorage) openContainer;
		List<ItemStack> itemstacks = chemicalStorage.getStorageInventory();
		for(ItemStack itemstack : itemstacks) {
			if(itemstack != null && itemstack.itemID == MinechemItems.element.shiftedIndex) {
				RadiationInfo radiationInfo = MinechemItems.element.getRadiationInfo(itemstack, player.worldObj);
				radiationInfo.lastRadiationUpdate = player.worldObj.getTotalWorldTime();
				MinechemItems.element.setRadiationInfo(radiationInfo, itemstack);
			}
		}
		List<ItemStack> playerStacks = chemicalStorage.getPlayerInventory();
		updateRadiationOnItems(player, openContainer, playerStacks);
	}

	private void updateContainer(EntityPlayer player, Container container) {
		List<ItemStack> itemstacks = container.getInventory();
		updateRadiationOnItems(player, container, itemstacks);
	}
	
	private void updateRadiationOnItems(EntityPlayer player, Container container, List<ItemStack> itemstacks) {
		for(ItemStack itemstack : itemstacks) {
			if(itemstack != null && itemstack.itemID == MinechemItems.element.shiftedIndex) {
				ItemStack elementBeforeDecay = itemstack.copy();
				int radiationDamage = updateRadiation(player, itemstack);
				ItemStack elementAfterDecay  = itemstack.copy();
				if(radiationDamage > 0) {
					applyRadiationDamage(player, container, radiationDamage);
					printRadiationDamageToChat(player, elementBeforeDecay, elementAfterDecay);
				}
			}
		}
	}
	
	private void applyRadiationDamage(EntityPlayer player, Container container, int damage) {
		List<Float> reductions = new ArrayList();
		if(container instanceof IRadiationShield) {
			float reduction = ((IRadiationShield)container).getRadiationReductionFactor(damage, null, player);
			reductions.add(reduction);
		}
		for(ItemStack armour : player.inventory.armorInventory) {
			if(armour != null && armour.getItem() instanceof IRadiationShield) {
				float reduction = ((IRadiationShield)armour.getItem()).getRadiationReductionFactor(damage, armour, player);
				reductions.add(reduction);
			}
		}
		float totalReductionFactor = 1;
		for(float reduction : reductions)
			totalReductionFactor -= reduction;
		if(totalReductionFactor < 0)
			totalReductionFactor = 0;
		damage = Math.round((float)damage * totalReductionFactor);
		player.attackEntityFrom(DamageSource.generic, damage);
	}
	
	private void printRadiationDamageToChat(EntityPlayer player, ItemStack elementBeforeDecay, ItemStack elementAfterDecay) {
		String nameBeforeDecay = MinechemItems.element.getLongName(elementBeforeDecay);
		String nameAfterDecay  = MinechemItems.element.getLongName(elementAfterDecay);
		String message = String.format("Radiation Warning: Element %s decayed into %s.", nameBeforeDecay, nameAfterDecay);
		player.addChatMessage(message);
	}
	
	private int updateRadiation(EntityPlayer player, ItemStack element) {
		World world = player.worldObj;
		RadiationInfo radiationInfo = MinechemItems.element.getRadiationInfo(element, world);
		int dimensionID = world.getWorldInfo().getDimension();
		if(dimensionID != radiationInfo.dimensionID && radiationInfo.isRadioactive()) {
			radiationInfo.dimensionID = dimensionID;
			MinechemItems.element.setRadiationInfo(radiationInfo, element);
			return 0;
		} else {
			long currentTime = world.getTotalWorldTime();
			return decayElement(element, radiationInfo, currentTime, world);
		}
	}
	
	private int decayElement(ItemStack element, RadiationInfo radiationInfo, long currentTime, World world) {
		long timeDifference = currentTime - radiationInfo.lastRadiationUpdate;
		int maxDamage = 0;
		while(timeDifference > 0 && radiationInfo.isRadioactive()) {
			long lifeToRemove	 = Math.min(timeDifference, radiationInfo.radiationLife);
			timeDifference		-= lifeToRemove;
			radiationInfo.radiationLife -= lifeToRemove;
			radiationInfo.lastRadiationUpdate = currentTime;
			if(radiationInfo.radiationLife <= 0) {
				int defaultDamage 	= radiationInfo.radioactivity.getDamage();
				maxDamage = Math.max(maxDamage, defaultDamage);
				radiationInfo = MinechemItems.element.decay(element, world);
			}
		}
		MinechemItems.element.setRadiationInfo(radiationInfo, element);
		return maxDamage;
	}
	
}
