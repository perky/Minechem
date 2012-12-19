package ljdp.minechem.common.utils;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

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
	
	public static NBTTagList writeItemStackArrayToTagList(ItemStack[] itemstacks) {
		NBTTagList taglist = new NBTTagList();
		for(int slot = 0; slot < itemstacks.length; slot++) {
			ItemStack itemstack = itemstacks[slot];
			if(itemstack != null) {
				NBTTagCompound itemstackCompound = new NBTTagCompound();
				itemstackCompound.setByte("slot", (byte)slot);
				itemstack.writeToNBT(itemstackCompound);
				taglist.appendTag(itemstackCompound);
			}
		}
		return taglist;
	}
	
	public static ItemStack[] readTagListToItemStackArray(NBTTagList taglist, ItemStack[] itemstacks) {
		for(int i = 0; i < taglist.tagCount(); i++) {
			NBTTagCompound itemstackCompound = (NBTTagCompound) taglist.tagAt(i);
			byte slot = itemstackCompound.getByte("slot");
			itemstacks[slot] = ItemStack.loadItemStackFromNBT(itemstackCompound);
		}
		return itemstacks;
	}
	
	public static NBTTagList writeItemStackListToTagList(ArrayList<ItemStack> list) {
		NBTTagList taglist = new NBTTagList();
		for(ItemStack itemstack : list) {
			NBTTagCompound itemstackCompound = new NBTTagCompound();
			itemstack.writeToNBT(itemstackCompound);
			taglist.appendTag(itemstackCompound);
		}
		return taglist;
	}
	
	public static ArrayList<ItemStack> readTagListToItemStackList(NBTTagList taglist) {
		ArrayList<ItemStack> itemlist = new ArrayList<ItemStack>();
		for(int i = 0; i < taglist.tagCount(); i++) {
			NBTTagCompound itemstackCompound = (NBTTagCompound) taglist.tagAt(i);
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(itemstackCompound);
			itemlist.add(itemstack);
		}
		return itemlist;
	}

}
