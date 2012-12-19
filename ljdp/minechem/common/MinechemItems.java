package ljdp.minechem.common;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import ljdp.minechem.common.ModMinechem;
public class MinechemItems {
	public static Item element;
	public static Item molecule;
	private static int elementID;
	private static int moleculeID;
	
	public static void loadConfig(Configuration config) {
		elementID = config.getItem(config.CATEGORY_ITEM,"Element", 4736).getInt(4736);
	 	moleculeID = config.getItem(config.CATEGORY_ITEM,"MoleCule", 4737).getInt(4737);
	}
	
	public static void registerItems() {
		element  = new ItemElement(elementID);
		molecule = new ItemMolecule(moleculeID);
	}
}
