package ljdp.minechem.computercraft.method;

import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.turtle.api.ITurtleAccess;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.computercraft.ICCMethod;
import ljdp.minechem.computercraft.IMinechemMachinePeripheral;

public class PutEmptyTestTube extends InteractMachine implements ICCMethod {

	@Override
	public String getMethodName() {
		return "putEmptyTestTube";
	}

	@Override
	public Object[] call(IComputerAccess computer, ITurtleAccess turtle, Object[] arguments) throws Exception {
		boolean didPut = false;
		IMinechemMachinePeripheral machine = getMachineInFront(turtle);
		int selectedSlot = turtle.getSelectedSlot();
		ItemStack selectedStack = turtle.getSlotContents(selectedSlot);
		if(machine != null && selectedStack != null && Util.isStackAnEmptyTestTube(selectedStack)) {
			ItemStack before    = selectedStack.copy();
			ItemStack after 	= machine.putEmptyTestTube(selectedStack);
			before.stackSize   -= after.stackSize;
			if(before.stackSize <= 0)
				turtle.setSlotContents(selectedSlot, null);
			else
				turtle.setSlotContents(selectedSlot, before);
			System.out.println(after.stackSize);
			if(after.stackSize > 0)
				didPut = true;
		}
		return new Object[]{ didPut };
	}

}
