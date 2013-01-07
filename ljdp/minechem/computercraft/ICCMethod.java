package ljdp.minechem.computercraft;

import dan200.computer.api.IComputerAccess;
import dan200.turtle.api.ITurtleAccess;
import ljdp.minechem.computercraft.IMinechemPeripheral;

public interface ICCMethod {
	
	public String getMethodName();
	public Object[] call(IComputerAccess computer, ITurtleAccess turtle, IMinechemPeripheral minechemPeripheral, Object[] arguments) throws Exception;
	
}
