package ljdp.minechem.common;

import java.util.ArrayList;

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
	
	private void showRecipeForItemStack(ItemStack itemstack) {
		ItemStack[] outputStacks = null;
		clearOutputStacks();
		isShaped = true;
		if(itemstack == null)
			return;
		if(itemstack.itemID == MinechemItems.molecule.shiftedIndex) {
			EnumMolecule molecule = ((ItemMolecule)MinechemItems.molecule).getMolecule(itemstack);
			ArrayList<ItemStack> moleculeComponents = molecule.components();
			outputStacks = moleculeComponents.toArray(new ItemStack[moleculeComponents.size()]);
			isShaped = false;
		} else {
			SynthesisRecipe recipe = MinechemRecipes.getInstance().getSynthesisRecipe(itemstack);
			if(recipe != null) {
				outputStacks = recipe.getShapedRecipe();
				isShaped = recipe.isShaped();
			}
		}
		if(outputStacks != null) {
			int slot = 1;
			for(ItemStack output : outputStacks) {
				microscopeInventory[slot] = output;
				slot++;
			}
		}
	}
	
	private void clearOutputStacks() {
		for(int slot = 1; slot < 10; slot++) {
			microscopeInventory[slot] = null;
		}
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
            clearOutputStacks();
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
		showRecipeForItemStack(null);
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		microscopeInventory[slot] = itemStack;
		if(slot == 0) {
			showRecipeForItemStack(itemStack);
		}
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

}
