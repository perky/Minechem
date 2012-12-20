package ljdp.minechem.common.items;

import ljdp.minechem.common.ModMinechem;
import net.minecraft.item.Item;

public class ItemAtomicManipulator extends Item {
	
	public ItemAtomicManipulator(int id) {
		super(id);
		setCreativeTab(ModMinechem.minechemTab);
		setItemName("minechem.itemAtomicManipulator");
	}

}
