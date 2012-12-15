package ljdp.minechem.common;

import net.minecraft.src.Item;

public class MinechemItems {
	public static Item element;
	public static Item molecule;
	
	public static void registerItems() {
		element = new ItemElement(4736);
		molecule = new ItemMolecule(4737);
	}
}
