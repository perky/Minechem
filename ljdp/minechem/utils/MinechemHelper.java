package ljdp.minechem.utils;

import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public class MinechemHelper {

	public static boolean stacksAreSameKind(ItemStack is1, ItemStack is2) {
		int dmg1 = is1.getItemDamage();
		int dmg2 = is2.getItemDamage();
		return is1.itemID == is2.itemID 
				&& (dmg1 == -1 || dmg2 == -1 || (dmg1 == dmg2));
	}
	
	public static float translateValue(float value, float leftMin, float leftMax, float rightMin, float rightMax) {
		float leftRange = leftMax - leftMin;
		float rightRange = rightMax - rightMin;
		float valueScaled = (value - leftMin) / leftRange;
		return rightMin + (valueScaled * rightRange);
	}
	
	public static int getSplitStringHeight(FontRenderer fontRenderer, String string, int width) {
		List stringRows = fontRenderer.listFormattedStringToWidth(string, width);
		return stringRows.size() * fontRenderer.FONT_HEIGHT;
	}
	
	public static String getLocalString(String key) {
		LanguageRegistry lr = LanguageRegistry.instance();
		String localString = lr.getStringLocalization(key);
		if(localString.equals("")) {
			localString = lr.getStringLocalization(key, "en_GB");
		}
		return localString;
	}

}
