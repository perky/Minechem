package ljdp.minechem.common.utils;

import java.util.ArrayList;
import java.util.List;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.core.Element;
import ljdp.minechem.api.core.EnumElement;
import ljdp.minechem.api.core.Molecule;
import ljdp.minechem.common.MinechemItems;

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

public class MinechemHelper {
	
	public static float translateValue(float value, float leftMin, float leftMax, float rightMin, float rightMax) {
		float leftRange = leftMax - leftMin;
		float rightRange = rightMax - rightMin;
		float valueScaled = (value - leftMin) / leftRange;
		return rightMin + (valueScaled * rightRange);
	}
	
	public static int getSplitStringHeight(FontRenderer fontRenderer, String string, int width) {
		List stringRows = fontRenderer.listFormattedStringToWidth(string, width);
		return stringRows.size() * fontRenderer.FONT_HEIGHT;
	}
	
	public static String getLocalString(String key) {
		LanguageRegistry lr = LanguageRegistry.instance();
		String localString = lr.getStringLocalization(key);
		if(localString.equals("")) {
			localString = lr.getStringLocalization(key, "en_GB");
		}
		return localString;
	}
	
	public static NBTTagList writeItemStackArrayToTagList(ItemStack[] itemstacks) {
		NBTTagList taglist = new NBTTagList();
		for(int slot = 0; slot < itemstacks.length; slot++) {
			ItemStack itemstack = itemstacks[slot];
			if(itemstack != null) {
				NBTTagCompound itemstackCompound = new NBTTagCompound();
				itemstackCompound.setByte("slot", (byte)slot);
				itemstack.writeToNBT(itemstackCompound);
				taglist.appendTag(itemstackCompound);
			}
		}
		return taglist;
	}
	
	public static ItemStack[] readTagListToItemStackArray(NBTTagList taglist, ItemStack[] itemstacks) {
		for(int i = 0; i < taglist.tagCount(); i++) {
			NBTTagCompound itemstackCompound = (NBTTagCompound) taglist.tagAt(i);
			byte slot = itemstackCompound.getByte("slot");
			itemstacks[slot] = ItemStack.loadItemStackFromNBT(itemstackCompound);
		}
		return itemstacks;
	}
	
	public static NBTTagList writeItemStackListToTagList(ArrayList<ItemStack> list) {
		NBTTagList taglist = new NBTTagList();
		for(ItemStack itemstack : list) {
			NBTTagCompound itemstackCompound = new NBTTagCompound();
			itemstack.writeToNBT(itemstackCompound);
			taglist.appendTag(itemstackCompound);
		}
		return taglist;
	}
	
	public static ArrayList<ItemStack> readTagListToItemStackList(NBTTagList taglist) {
		ArrayList<ItemStack> itemlist = new ArrayList<ItemStack>();
		for(int i = 0; i < taglist.tagCount(); i++) {
			NBTTagCompound itemstackCompound = (NBTTagCompound) taglist.tagAt(i);
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(itemstackCompound);
			itemlist.add(itemstack);
		}
		return itemlist;
	}
	
	public static ArrayList<ItemStack> convertChemicalsIntoItemStacks(ArrayList<Chemical> chemicals) {
		ArrayList<ItemStack> stacks = new ArrayList();
		if(chemicals == null)
			return stacks;
		for(Chemical chemical : chemicals) {
			if(chemical instanceof Element) {
				stacks.add(new ItemStack(MinechemItems.element, chemical.amount, ((Element) chemical).element.ordinal()));
			} else if(chemical instanceof Molecule) {
				stacks.add(new ItemStack(MinechemItems.molecule, chemical.amount, ((Molecule) chemical).molecule.ordinal()));
			}
		}
		return stacks;
	}
	
	public static ItemStack[] convertChemicalArrayIntoItemStackArray(Chemical[] chemicals) {
		ItemStack[] stacks = new ItemStack[chemicals.length];
		for(int i = 0; i < chemicals.length; i++) {
			Chemical chemical = chemicals[i];
			if(chemical instanceof Element) {
				stacks[i] = new ItemStack(MinechemItems.element, chemical.amount, ((Element) chemical).element.ordinal());
			} else if(chemical instanceof Molecule) {
				stacks[i] = new ItemStack(MinechemItems.molecule, chemical.amount, ((Molecule) chemical).molecule.ordinal());
			}
		}
		return stacks;
	}
	
	public static boolean itemStackMatchesChemical(ItemStack itemstack, Chemical chemical) {
		if(chemical instanceof Element && itemstack.itemID == MinechemItems.element.shiftedIndex) {
			Element element = (Element)chemical;
			return (itemstack.getItemDamage() == element.element.ordinal()) && (itemstack.stackSize >= element.amount);
		}
		if (chemical instanceof Molecule && itemstack.itemID == MinechemItems.molecule.shiftedIndex) {
			Molecule molecule = (Molecule)chemical;
			return (itemstack.getItemDamage() == molecule.molecule.ordinal()) && (itemstack.stackSize >= molecule.amount);
		}
		return false;
	}
	
	public static ForgeDirection getDirectionFromFacing(int facing) {
		switch (facing) {
		case 0:
			return ForgeDirection.SOUTH;
		case 1:
			return ForgeDirection.WEST;
		case 2:
			return ForgeDirection.NORTH;
		case 3:
			return ForgeDirection.EAST;
		default:
			return null;
		}
	}
	
	/**
	 * Ensures that the given inventory is the full inventory, i.e. takes double chests into account.
	 * @param inv
	 * @return Modified inventory if double chest, unmodified otherwise.
	 * Credit to Buildcraft.
	 */
	public static IInventory getInventory(IInventory inv) {
		if (inv instanceof TileEntityChest) {
			TileEntityChest chest = (TileEntityChest) inv;
			Position pos = new Position(chest.xCoord, chest.yCoord, chest.zCoord);
			TileEntity tile;
			IInventory chest2 = null;
			tile = Utils.getTile(chest.worldObj, pos, ForgeDirection.WEST);
			if (tile instanceof TileEntityChest)
				chest2 = (IInventory) tile;
			tile = Utils.getTile(chest.worldObj, pos, ForgeDirection.EAST);
			if (tile instanceof TileEntityChest)
				chest2 = (IInventory) tile;
			tile = Utils.getTile(chest.worldObj, pos, ForgeDirection.NORTH);
			if (tile instanceof TileEntityChest)
				chest2 = (IInventory) tile;
			tile = Utils.getTile(chest.worldObj, pos, ForgeDirection.SOUTH);
			if (tile instanceof TileEntityChest)
				chest2 = (IInventory) tile;
			if (chest2 != null)
				return new InventoryLargeChest("", inv, chest2);
		}
		return inv;
	}

}
