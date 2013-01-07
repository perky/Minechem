package ljdp.minechem.computercraft.method;

import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.turtle.api.ITurtleAccess;
import ljdp.minechem.computercraft.ICCMethod;
import ljdp.minechem.computercraft.IMinechemMachinePeripheral;

public class TakeJournal extends InteractMachine implements ICCMethod {

	@Override
	public String getMethodName() {
		return "takeJournal";
	}

	@Override
	public Object[] call(IComputerAccess computer, ITurtleAccess turtle,
			Object[] arguments) throws Exception 
	{
		IMinechemMachinePeripheral machine = getMachineInFront(turtle);
		ItemStack takenStack = machine.takeJournal();
		boolean didTake = false;
		if(takenStack != null){
			if(turtle.storeItemStack(takenStack)) {
				didTake = true;
			} else {
				machine.putJournal(takenStack);
			}
		}
		return new Object[]{ didTake };
	}

}
