package ljdp.minechem.computercraft.method;

import ljdp.minechem.api.util.Util;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.computercraft.ICCMethod;
import ljdp.minechem.computercraft.IMinechemPeripheral;
import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.turtle.api.ITurtleAccess;

public class GetChemicalName implements ICCMethod {

	@Override
	public String getMethodName() {
		return "getChemicalName";
	}

	@Override
	public Object[] call(IComputerAccess computer, ITurtleAccess turtle, IMinechemPeripheral minechemPeripheral, Object[] arguments) throws Exception {
		if(arguments.length != 0)
			throw new Exception("Invalid number of arguments");
		Object result = null;
		int selectedSlot = turtle.getSelectedSlot();
		ItemStack selectedStack = turtle.getSlotContents(selectedSlot);
		if(selectedStack != null) {
			if(Util.isStackAnElement(selectedStack)) {
				result = MinechemItems.element.getLongName(selectedStack);
			} else if(Util.isStackAMolecule(selectedStack)) {
				result = MinechemItems.molecule.getMolecule(selectedStack).descriptiveName();
			}
		}
		return new Object[] {result};
	}

}
