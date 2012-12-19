package ljdp.minechem.common;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class MinechemItems {
	public static Item element;
	public static Item molecule;
	public static Item copper;
	public static void registerItems() {
		element  = new ItemElement(4736);
		molecule = new ItemMolecule(4737);
		copper   = new ItemCopper(4738);
		OreDictionary.registerOre("oreCopper", copper);
	}
}
