package ljdp.minechem.common.items;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import ljdp.minechem.common.ModMinechem;
import net.minecraft.item.Item;

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
