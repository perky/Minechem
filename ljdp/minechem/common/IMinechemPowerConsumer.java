package ljdp.minechem.common;

import buildcraft.api.power.IPowerReceptor;

public interface IMinechemPowerConsumer extends IPowerReceptor {
	/* Return the amount of power the machine is currently consuming */
	public int getCurrentPowerUsage();
}
