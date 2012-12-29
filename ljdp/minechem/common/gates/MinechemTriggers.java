package ljdp.minechem.common.gates;

import buildcraft.api.gates.ITrigger;

public class MinechemTriggers {
	
	public static ITrigger fullEnergy;
	public static ITrigger noTestTubes;
	public static ITrigger outputJammed;
	
	public static void registerTriggers() {
		fullEnergy = new TriggerFullEnergy(191);
		noTestTubes = new TriggerNoTestTubes(192);
		outputJammed = new TriggerOutputJammed(193);
	}
	
}
