package ljdp.minechem.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemOreExtraction extends Item {

	public static int MAX_USES = 100;
	
	public ItemOreExtraction(int id) {
		super(id);
		setMaxDamage(MAX_USES);
		setMaxStackSize(1);
		setContainerItem(this);
	}
	
	@Override
	public boolean hasContainerItem() {
		return true;
	}
	
	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack) {
		itemStack.setItemDamage(itemStack.getItemDamage() + 1);
		if(itemStack.getItemDamage() > getMaxDamage())
			return null;
		else
			return itemStack;
	}

}
