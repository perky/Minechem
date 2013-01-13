package ljdp.minechem.common.items;

import java.util.List;

import ljdp.minechem.common.utils.MinechemHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemFusionStar extends Item {

        public static int MAX_USES = 2000;

	public ItemFusionStar(int id) {
		super(id);
		this.maxStackSize = 1;
		this.setMaxDamage(MAX_USES);
		this.setNoRepair();
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemStack) {
		return MinechemHelper.getLocalString("item.name.fusionStar");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		int damage = itemStack.getItemDamage();
		int usesLeft = itemStack.getMaxDamage() - damage;
		list.add(usesLeft + " Exajoules");
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
