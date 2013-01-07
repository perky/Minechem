package ljdp.minechem.computercraft;

import ljdp.minechem.common.ModMinechem;
import net.minecraft.item.ItemStack;
import dan200.computer.api.IHostedPeripheral;
import dan200.turtle.api.ITurtleAccess;
import dan200.turtle.api.ITurtleUpgrade;
import dan200.turtle.api.TurtleSide;
import dan200.turtle.api.TurtleUpgradeType;
import dan200.turtle.api.TurtleVerb;

public class ChemistryUpgrade implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return 200;
	}

	@Override
	public String getAdjective() {
		return "Chemistry";
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Peripheral;
	}

	@Override
	public ItemStack getCraftingItem() {
		return new ItemStack(CCItems.chemistryUpgrade);
	}

	@Override
	public boolean isSecret() {
		return false;
	}

	@Override
	public String getIconTexture(ITurtleAccess turtle, TurtleSide side) {
		return ModMinechem.proxy.ITEMS_PNG;
	}

	@Override
	public int getIconIndex(ITurtleAccess turtle, TurtleSide side) {
		return 14;
	}

	@Override
	public IHostedPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return new ChemistryTurtleUpgradePeripherial(turtle);
	}

	@Override
	public boolean useTool(ITurtleAccess turtle, TurtleSide side, TurtleVerb verb, int direction) {
		return false;
	}

}
