package net.minecraft.minechem;

import java.io.IOException;

import net.minecraft.src.ItemStack;
import net.minecraft.src.mod_Minechem;

public class Util {
	
	public static String convertNumbersToSuperscript(String formula) {
		formula = formula.replaceAll("0", "\u2080");
		formula = formula.replaceAll("1", "\u2081");
		formula = formula.replaceAll("2", "\u2082");
		formula = formula.replaceAll("3", "\u2083");
		formula = formula.replaceAll("4", "\u2084");
		formula = formula.replaceAll("5", "\u2085");
		formula = formula.replaceAll("6", "\u2086");
		formula = formula.replaceAll("7", "\u2087");
		formula = formula.replaceAll("8", "\u2088");
		formula = formula.replaceAll("9", "\u2089");
		return formula;
	}
	
	public static String getFullChemicalName(String formula) {
		try {
			return mod_Minechem.findChemicalName(formula);
		} catch(IOException e) {
			return "Unknown";
		}
	}
	
	public static boolean isElementTube(ItemStack itemstack) {
		return itemstack != null && itemstack.itemID == mod_Minechem.itemTesttube.shiftedIndex && itemstack.getItemDamage() > 0;
	}
	
	public static boolean isTube(ItemStack itemstack) {
		return itemstack != null && itemstack.itemID == mod_Minechem.itemTesttube.shiftedIndex;
	}

	public static boolean isEmptyTube(ItemStack itemstack) {
		return itemstack != null && itemstack.itemID == mod_Minechem.itemTesttubeEmpty.shiftedIndex;
	}
	
	public static boolean itemsEqualWithMeta(ItemStack stack1, ItemStack stack2) {
		if(stack1 == null || stack2 == null)
			return false;
		if(stack1.getItemDamage() == -1 || stack2.getItemDamage() == -1) {
			if(stack1.itemID == stack2.itemID)
				return true;
			else
				return false;
		} else {
			return stack1.isItemEqual(stack2);
		}
	}
}
