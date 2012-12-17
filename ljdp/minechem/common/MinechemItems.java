package ljdp.minechem.common;

import net.minecraft.item.Item;
import ljdp.minechem.common.ModMinechem;
public class MinechemItems {
	public static Item element;
	public static Item molecule;
	
	public static void registerItems() {
		element  = new ItemElement(ModMinechem.elementID);
		molecule = new ItemMolecule(ModMinechem.moleculeID);
	}
}
