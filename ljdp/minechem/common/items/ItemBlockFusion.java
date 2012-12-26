package ljdp.minechem.common.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFusion extends ItemBlock {
	
	private static final String[] names = {
		"Fusion Wall", "Fusion Something", "Fusion Core", "Fusion Something"
	};
	
	public ItemBlockFusion(int par1) {
		super(par1);
		setHasSubtypes(true);
		setItemName("minechem.itemBlockFusion");
	}
	
	@Override
	public int getMetadata(int damageValue) {
		return super.getMetadata(damageValue);
	}
	
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return super.getItemNameIS(itemstack) + names[itemstack.getItemDamage()];
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		return names[itemstack.getItemDamage()];
	}

}
