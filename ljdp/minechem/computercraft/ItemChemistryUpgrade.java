package ljdp.minechem.computercraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ljdp.minechem.common.ModMinechem;
import dan200.computer.api.ComputerCraftAPI;
import net.minecraft.item.Item;

public class ItemChemistryUpgrade extends Item {

	public ItemChemistryUpgrade(int id) {
		super(id);
		setItemName("minechem.itemChemistryTurtleUpgrade");
		setCreativeTab(ModMinechem.minechemTab);
		setIconIndex(14);
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ITEMS_PNG;
	}

}
