package ljdp.minechem.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFusionStar extends Item {

	public ItemFusionStar(int id) {
		super(id);
		this.maxStackSize = 1;
		this.setMaxDamage(1000);
		this.setNoRepair();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int par1) {
		return Item.netherStar.getIconFromDamage(par1);
	}
	
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return false;
	}

}
