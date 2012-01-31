package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

public class ItemTableOfElements extends Item {

	public ItemTableOfElements(int i) {
		super(i);
		setItemName("tableOfElements");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		
		GuiTableOfElements gui = new GuiTableOfElements();
		ModLoader.OpenGUI(entityplayer, gui);
		
		return itemstack;
		
	}

}
