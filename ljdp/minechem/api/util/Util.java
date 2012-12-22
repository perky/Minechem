package ljdp.minechem.api.util;

import java.util.ArrayList;
import java.util.List;

import buildcraft.api.core.Position;
import buildcraft.core.utils.Utils;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.common.ForgeDirection;

public class Util {

	public static boolean stacksAreSameKind(ItemStack is1, ItemStack is2) {
		int dmg1 = is1.getItemDamage();
		int dmg2 = is2.getItemDamage();
		return is1.itemID == is2.itemID 
				&& (dmg1 == -1 || dmg2 == -1 || (dmg1 == dmg2));
	}

}
