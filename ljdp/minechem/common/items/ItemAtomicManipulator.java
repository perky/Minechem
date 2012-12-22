package ljdp.minechem.common.items;

import ljdp.minechem.common.ModMinechem;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemAtomicManipulator extends Item {
	
	public ItemAtomicManipulator(int id) {
		super(id);
		setCreativeTab(ModMinechem.minechemTab);
		setItemName("minechem.itemAtomicManipulator");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int par1) {
		return 13;
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ELEMENTBOTTLES_PNG;
	}

}
