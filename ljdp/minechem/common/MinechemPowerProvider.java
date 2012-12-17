package ljdp.minechem.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import net.minecraftforge.classloading.FMLForgePlugin;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerProvider;

public class MinechemPowerProvider extends PowerProvider {
	
	float lastEnergyStored = 0.0F;
	float currentEnergyUsage = 0.0F;
	float currentEnergyInput = 0.0F;
	boolean didEnergyStoredChange;
	
	public MinechemPowerProvider(int minEnergyReceived, int maxEnergyReceived, int maxStoredEnergy) {
		this.configure(0, minEnergyReceived, maxEnergyReceived, 0, maxStoredEnergy * 1200);
	}
	
	public boolean didEnergyStoredChange() {
		return this.didEnergyStoredChange;
	}
	
	public void setEnergyStored(float amount) {
		this.energyStored = Math.min(amount, maxEnergyStored);
	}
	
	@Override
	public float useEnergy(float min, float max, boolean doUse) {
		float energyUsage = super.useEnergy(min, max, doUse);
		currentEnergyUsage += energyUsage;
		return energyUsage;
	}
	
	public void resetCurrentEnergyUsage() {
		currentEnergyUsage = 0;
	}
	
	public void setCurrentEnergyUsage(float amount) {
		currentEnergyUsage = amount;
	}

	public float getCurrentEnergyUsage() {
		return currentEnergyUsage;
	}
	
	public void setCurrentEnergyInput(float amount) {
		currentEnergyInput = amount;
	}
	
	public float getCurrentEnergyInput() {
		return currentEnergyInput;
	}
	
	@Override
	public boolean update(IPowerReceptor receptor) {
		currentEnergyInput = energyStored - lastEnergyStored;
		this.didEnergyStoredChange = (energyStored != lastEnergyStored);
		lastEnergyStored = energyStored;
		return super.update(receptor);
	}
}
