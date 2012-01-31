package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.forge.ITextureProvider;

public class ItemTableOfElements extends Item implements ITextureProvider {

	public ItemTableOfElements(int i) {
		super(i);
		setItemName("tableOfElements");
		setIconCoord(1, 0);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		
		GuiTableOfElements gui = new GuiTableOfElements();
		ModLoader.OpenGUI(entityplayer, gui);
		
		return itemstack;
		
	}

	@Override
	public String getTextureFile() {
		return mod_Minechem.minechemItemsTexture;
	}

}
