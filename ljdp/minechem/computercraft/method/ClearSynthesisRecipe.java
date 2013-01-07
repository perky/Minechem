package ljdp.minechem.computercraft.method;

import dan200.computer.api.IComputerAccess;
import dan200.turtle.api.ITurtleAccess;
import ljdp.minechem.computercraft.ICCMethod;
import ljdp.minechem.computercraft.IMinechemPeripheral;

public class ClearSynthesisRecipe implements ICCMethod {

	@Override
	public String getMethodName() {
		return "clearSynthesisRecipe";
	}

	@Override
	public Object[] call(IComputerAccess computer, ITurtleAccess turtle,
			IMinechemPeripheral minechemPeripheral, Object[] arguments)
			throws Exception 
	{
		minechemPeripheral.setSynthesisRecipe(null);
		return null;
	}

}
