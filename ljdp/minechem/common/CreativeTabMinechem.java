package ljdp.minechem.common;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class CreativeTabMinechem extends CreativeTabs {

	public CreativeTabMinechem(String label) {
		super(label);
	}

	public CreativeTabMinechem(int par1, String par2Str) {
		super(par1, par2Str);
	}
	
	@Override
	public ItemStack getIconItemStack() {
		return new ItemStack(MinechemItems.element, 1, EnumElement.Mc.ordinal());
	}

}
