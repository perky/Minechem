package ljdp.minechem.api.util;

import net.minecraft.item.ItemStack;

public class Util {

	public static boolean stacksAreSameKind(ItemStack is1, ItemStack is2) {
		int dmg1 = is1.getItemDamage();
		int dmg2 = is2.getItemDamage();
		return is1.itemID == is2.itemID 
				&& (dmg1 == -1 || dmg2 == -1 || (dmg1 == dmg2));
	}

}
