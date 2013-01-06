package ljdp.minechem.api.core;

import net.minecraft.item.ItemStack;

/**
 * To be implemented by containers and armour.
 * @author lukeperkin
 */
public interface IRadiationShield {
	
	/**
	 * Return a number from 0 to 1 to reduce radiation damage.
	 * Multiple IRadationShields will sum their reductionFactors.
	 * @param itemstack if armour implements this it's the armour stack.
	 * @return a float where n >= 0 && n <= 1
	 */
	public float getRadiationReductionFactor(ItemStack itemstack);
	
}
