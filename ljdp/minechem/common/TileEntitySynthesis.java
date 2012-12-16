package ljdp.minechem.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySynthesis extends TileEntity implements IInventory {
	
	private ItemStack[] synthesisInventory;
	public static final int kSizeOutput = 1;
	public static final int kSizeInput  = 9;
	public static final int kSizeBottles = 4;
	public static final int kStartOutput = 0;
	public static final int kStartInput = 1;
	public static final int kStartBottles = 10;
	private SynthesisRecipe currentRecipe;
	
	public TileEntitySynthesis() {
		synthesisInventory = new ItemStack[getSizeInventory()];
	}
	
	public void onOuputPickupFromSlot(EntityPlayer entityPlayer) {
		takeCraftingItems();
	}
	
	private boolean getRecipeResult() {
		ItemStack[] craftingItems = getCraftingItems();
		SynthesisRecipe recipe = MinechemRecipes.getInstance().getSynthesisRecipe(craftingItems);
		if(recipe != null && canAddEmptyBottles(recipe.getIngredientCount())) {
			synthesisInventory[kStartOutput] = recipe.getOutputStack().copy();
			currentRecipe = recipe;
			return true;
		} else {
			synthesisInventory[kStartOutput] = null;
			currentRecipe = null;
			return false;
		}
	}
	
	private ItemStack[] getCraftingItems() {
		ItemStack[] currentRecipe = new ItemStack[9];
		int i = 0;
		for(int slot = kStartInput; slot < kStartInput + kSizeInput; slot++) {
			currentRecipe[i] = synthesisInventory[slot];
			i++;
		}
		return currentRecipe;
	}
	
	private void takeCraftingItems() {
		for(ItemStack ingredient : currentRecipe.getUnshapedRecipe()) {
			takeCraftingItem(ingredient);
		}
	}
	
	private void takeCraftingItem(ItemStack itemstack) {
		ItemStack[] craftingItems = getCraftingItems();
		for(int i = 0; i < craftingItems.length; i++) {
			ItemStack craftingItem = craftingItems[i];
			if(craftingItem != null && MinechemHelper.stacksAreSameKind(itemstack, craftingItem)
					&& craftingItem.stackSize >= itemstack.stackSize) {
				decrStackSize(kStartInput + i, itemstack.stackSize);
				addEmptyBottles(itemstack.stackSize);
			}
		}
	}
	
	private boolean canAddEmptyBottles(int amount) {
		for(int slot = kStartBottles; slot < kStartBottles + kSizeBottles; slot++) {
			if(synthesisInventory[slot] == null) {
				amount -= 64;
			} else if(synthesisInventory[slot].itemID == Item.glassBottle.shiftedIndex) {
				amount -= getInventoryStackLimit() - synthesisInventory[slot].stackSize;
			}
			if(amount <= 0)
				return true;
		}
		return false;
	}
	
	private void addEmptyBottles(int amount) {
		for(int slot = kStartBottles; slot < kStartBottles + kSizeBottles; slot++) {
			if(synthesisInventory[slot] == null) {
				int stackSize = Math.min(amount, getInventoryStackLimit());
				setInventorySlotContents(slot, new ItemStack(Item.glassBottle, stackSize));
				amount -= stackSize;
			} else if(synthesisInventory[slot].itemID == Item.glassBottle.shiftedIndex) {
				int stackAddition = getInventoryStackLimit() - synthesisInventory[slot].stackSize;
				stackAddition = Math.min(amount, stackAddition);
				synthesisInventory[slot].stackSize += stackAddition;
				amount -= stackAddition;
			}
			if(amount <= 0)
				break;
		}
		if(amount > 0) {
			//Drop the bottles?
		}
			
	}

	@Override
	public int getSizeInventory() {
		return kSizeOutput + kSizeInput + kSizeBottles;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return synthesisInventory[var1];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(this.synthesisInventory[slot] != null) {
			ItemStack itemstack;
			if(this.synthesisInventory[slot].stackSize <= amount) {
				itemstack = this.synthesisInventory[slot];
				this.synthesisInventory[slot] = null;
				return itemstack;
			} else {
				itemstack = this.synthesisInventory[slot].splitStack(amount);
				if(this.synthesisInventory[slot].stackSize == 0)
					this.synthesisInventory[slot] = null;
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		synthesisInventory[slot] = itemstack;
	}

	@Override
	public String getInvName() {
		return "container.synthesis";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}
	
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		getRecipeResult();
	}

	public int craftAll() {
		int count = 0;
		while(getRecipeResult()) {
			takeCraftingItems();
			count++;
		}
		return count;
	}

}
