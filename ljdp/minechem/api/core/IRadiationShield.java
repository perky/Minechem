package ljdp.minechem.api.core;

/**
 * To be implemented by containers and armour.
 * @author lukeperkin
 */
public interface IRadiationShield {
	
	/**
	 * Return a number from 0 to 1 to reduce radiation damage.
	 * Multiple IRadationShields will sum their reductionFactors.
	 * @return a float where n >= 0 && n <= 1
	 */
	public float getRadiationReductionFactor();
	
}
