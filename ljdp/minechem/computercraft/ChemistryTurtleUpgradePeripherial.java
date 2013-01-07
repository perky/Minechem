package ljdp.minechem.computercraft;

import java.util.ArrayList;
import java.util.List;

import buildcraft.api.core.SafeTimeTracker;

import ljdp.minechem.api.util.Constants;
import ljdp.minechem.common.RadiationHandler;
import ljdp.minechem.computercraft.method.GetAtomicMass;
import ljdp.minechem.computercraft.method.GetChemicalName;
import ljdp.minechem.computercraft.method.GetChemicals;
import ljdp.minechem.computercraft.method.GetFormula;
import ljdp.minechem.computercraft.method.GetRadioactivity;
import ljdp.minechem.computercraft.method.GetTicksUntilDecay;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import dan200.computer.api.IComputerAccess;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;

public class ChemistryTurtleUpgradePeripherial implements IHostedPeripheral {
	
	private static String[] methodNames;
	private static ICCMethod[] methods = {
			new GetChemicalName(),
			new GetFormula(),
			new GetChemicals(),
			new GetAtomicMass(),
			new GetRadioactivity(),
			new GetTicksUntilDecay()
	};
	
	public ITurtleAccess turtle;
	public SafeTimeTracker updateTracker = new SafeTimeTracker();
	
	public ChemistryTurtleUpgradePeripherial(ITurtleAccess turtle) {
		this.turtle = turtle;
	}
	
	@Override
	public String getType() {
		return "chemistryTurtle";
	}

	@Override
	public String[] getMethodNames() {
		if(methodNames == null) {
			methodNames = new String[methods.length];
			int pos = 0;
			for(ICCMethod method : methods)
				methodNames[pos++] = method.getMethodName();
		}
		return methodNames;
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, int method, Object[] arguments) throws Exception {
		return methods[method].call(computer, turtle, arguments);
	}

	@Override
	public boolean canAttachToSide(int side) {
		return true;
	}

	@Override
	public void attach(IComputerAccess computer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void detach(IComputerAccess computer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		if(updateTracker.markTimeIfDelay(turtle.getWorld(), Constants.TICKS_PER_SECOND)) {
			List<ItemStack> inventory = getTurtleInventory();
			RadiationHandler.getInstance().updateRadiationOnItems(turtle.getWorld(), inventory);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		// TODO Auto-generated method stub

	}
	
	public List<ItemStack> getTurtleInventory() {
		List<ItemStack> inventory = new ArrayList();
		for(int slot = 0; slot < turtle.getInventorySize(); slot++) {
			ItemStack stack = turtle.getSlotContents(slot);
			if(stack != null)
				inventory.add(stack);
		}
		return inventory;
	}

}
