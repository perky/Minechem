package ljdp.minechem.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ljdp.minechem.common.ModMinechem;
import net.minecraft.item.Item;

public class ItemTestTube extends Item {

	public ItemTestTube(int id) {
		super(id);
		setItemName("minechem.itemTestTube");
		setCreativeTab(ModMinechem.minechemTab);
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ITEMS_PNG;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int par1) {
		return 0;
	}

}
