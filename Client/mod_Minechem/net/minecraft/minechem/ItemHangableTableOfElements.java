package net.minecraft.minechem;

import java.util.List;

import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.forge.ITextureProvider;

public class ItemHangableTableOfElements extends Item implements ITextureProvider {

	public ItemHangableTableOfElements(int i) {
		super(i);
		setItemName("hangableTableOfElements");
		setIconCoord(2, 0);
	}
	
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if (l == 0)
        {
            return false;
        }
        if (l == 1)
        {
            return false;
        }
        byte byte0 = 0;
        if (l == 4)
        {
            byte0 = 1;
        }
        if (l == 3)
        {
            byte0 = 2;
        }
        if (l == 5)
        {
            byte0 = 3;
        }
        if (!entityplayer.canPlayerEdit(i, j, k))
        {
            return false;
        }
        EntityTableOfElements entityTable = new EntityTableOfElements(world, i, j, k, byte0);
        if (entityTable.canStay())
        {
            if (!world.multiplayerWorld)
            {
                world.spawnEntityInWorld(entityTable);
            }
            itemstack.stackSize--;
        }
        return true;
    }

	@Override
	public void addInformation(ItemStack itemstack, List list) {
		list.add("5 x 9");
	}

	@Override
	public String getTextureFile() {
		return mod_Minechem.minechemItemsTexture;
	}
	
	

}
