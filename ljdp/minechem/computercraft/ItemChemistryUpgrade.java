package ljdp.minechem.computercraft;

import ljdp.minechem.common.ModMinechem;
import dan200.computer.api.ComputerCraftAPI;
import net.minecraft.item.Item;

public class ItemChemistryUpgrade extends Item {

	public ItemChemistryUpgrade(int id) {
		super(id);
		setItemName("minechem.itemChemistryTurtleUpgrade");
		setCreativeTab(ModMinechem.minechemTab);
	}

}
