package ljdp.minechem.computercraft;

import dan200.turtle.api.TurtleAPI;
import net.minecraftforge.common.Configuration;

public class CCMain {
	
	private static CCMain instance;
	public static CCMain getInstance() {
		if(instance == null)
			instance = new CCMain();
		return instance;
	}
	
	private CCMain() {
	}
	
	public void loadConfig(Configuration config) {
		CCItems.loadConfig(config);
	}
	
	public void init() {
		CCItems.registerItems();
		TurtleAPI.registerUpgrade(new ChemistryUpgrade());
	}
	
}
