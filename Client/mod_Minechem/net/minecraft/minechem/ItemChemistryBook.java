package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.forge.ITextureProvider;

public class ItemChemistryBook extends Item implements ITextureProvider {

	public ItemChemistryBook(int i) {
		super(i);
		setItemName("chemistrybook");
		setIconCoord(3, 0);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		
		ModLoader.OpenGUI(entityplayer, new GuiChemistryBook(entityplayer));
		
		return itemstack;
	}
	
	@Override
	public String getTextureFile() {
		return mod_Minechem.minechemItemsTexture;
	}

}
