package ljdp.minechem.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.blueprint.BlueprintFusion;
import ljdp.minechem.common.blueprint.MinechemBlueprint;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBlueprint extends Item {

	public static final String[] names = {
		"item.name.blueprintFusion", 
		"item.name.blueprintFisson",
		"item.name.blueprintVat"
	};
	
	public static final MinechemBlueprint[] blueprints = {
		new BlueprintFusion(),
		null,
		null
	};
	
	public ItemBlueprint(int id) {
		super(id);
		setItemName("minechem.itemBlueprint");
		setCreativeTab(ModMinechem.minechemTab);
		setHasSubtypes(true);
	}
	
	public MinechemBlueprint getBlueprint(ItemStack itemstack) {
		int metadata = itemstack.getItemDamage();
		return blueprints[metadata];
	}
	
	@Override
	public String getItemNameIS(ItemStack itemstack) {
		return getItemName() + "." + names[itemstack.getItemDamage()];
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		int metadata = itemstack.getItemDamage();
		return MinechemHelper.getLocalString(names[metadata]);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativeTabs, List list) {
		for(int i = 0; i < names.length; i++) {
			list.add(new ItemStack(id, 1, i));
		}
	}

}
