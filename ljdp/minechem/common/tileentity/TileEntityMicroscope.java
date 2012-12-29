package ljdp.minechem.common.tileentity;

import java.util.ArrayList;

import ljdp.minechem.api.core.EnumMolecule;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.MinechemRecipes;
import ljdp.minechem.common.items.ItemMolecule;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.common.utils.MinechemHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMicroscope extends TileEntity implements IInventory {
	
	private ItemStack[] microscopeInventory;
	public boolean isShaped = true;
	
	public TileEntityMicroscope() {
		microscopeInventory = new ItemStack[getSizeInventory()];
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		ItemStack itemstack = microscopeInventory[slot];
		return itemstack;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot >= 0 && slot < microscopeInventory.length)
        {
            ItemStack itemstack = microscopeInventory[slot];
            microscopeInventory[slot] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack itemstack = microscopeInventory[slot];
		microscopeInventory[slot] = null;
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		microscopeInventory[slot] = itemStack;
	}

	@Override
	public String getInvName() {
		return "container.microscope";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		double dist = entityPlayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D);
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false :  dist <= 64.0D;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}
	
	public int getFacing() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}
}
