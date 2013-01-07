package ljdp.minechem.computercraft.method;

import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.turtle.api.ITurtleAccess;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.computercraft.ICCMethod;
import ljdp.minechem.computercraft.IMinechemMachinePeripheral;

public class PutJournal extends InteractMachine implements ICCMethod {

	@Override
	public String getMethodName() {
		return "putJournal";
	}

	@Override
	public Object[] call(IComputerAccess computer, ITurtleAccess turtle,
			Object[] arguments) throws Exception
	{
		IMinechemMachinePeripheral machine = getMachineInFront(turtle);
		ItemStack selectedStack = turtle.getSlotContents(turtle.getSelectedSlot());
		boolean didPut = false;
		if(machine != null && selectedStack != null && selectedStack.itemID == MinechemItems.journal.shiftedIndex) {
			ItemStack before = selectedStack.copy();
			ItemStack after	 = machine.putJournal(selectedStack);
			didPut	= tryPut(before, after, turtle);
		}
		return new Object[]{ didPut };
	}

}
