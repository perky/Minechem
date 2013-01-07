package ljdp.minechem.common.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;

import buildcraft.api.core.Position;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerProvider;
import buildcraft.api.inventory.ISpecialInventory;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.transport.IPipe;

import java.util.List;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.api.util.Constants;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.client.ModelSynthesizer;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.gates.IMinechemTriggerProvider;
import ljdp.minechem.common.gates.MinechemTriggers;
import ljdp.minechem.common.inventory.BoundedInventory;
import ljdp.minechem.common.inventory.Transactor;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.network.PacketSynthesisUpdate;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntitySynthesis extends MinechemTileEntity implements IInventory, ISidedInventory, 
IPowerReceptor, ITriggerProvider, IMinechemTriggerProvider, ISpecialInventory
{
	
	private class ItemStackPointer {
		IInventory inventory;
		ItemStack itemstack;
		int slot;
		public int stackSize;
	}
	private ItemStack[] synthesisInventory;
	public static final int kSizeOutput = 1;
	public static final int kSizeRecipe  = 9;
	public static final int kSizeBottles = 4;
	public static final int kSizeStorage = 9;
	public static final int kSizeJournal = 1;
	public static final int kStartOutput = 0;
	public static final int kStartRecipe = 1;
	public static final int kStartBottles = 10;
	public static final int kStartStorage = 14;
	public static final int kStartJournal = 23;
	
	//public static final int
	private SynthesisRecipe currentRecipe;
	MinechemPowerProvider powerProvider;
    public ModelSynthesizer model;
    private final BoundedInventory recipeMatrix = 
    		new BoundedInventory(this, kStartRecipe, kStartRecipe + kSizeRecipe);
	private final BoundedInventory storageInventory = 
			new BoundedInventory(this, kStartStorage, kStartStorage + kSizeStorage);
	private final BoundedInventory tubeInventory = 
			new BoundedInventory(this, kStartBottles, kStartBottles + kSizeBottles);
	
	int minEnergyPerTick = 30;
	int maxEnergyPerTick = 200;
	int activationEnergy = 100;
	int energyStorage = 900000;
	
	private boolean hasFullEnergy;
	
	public TileEntitySynthesis() {
		synthesisInventory = new ItemStack[getSizeInventory()];
		if (PowerFramework.currentFramework != null) {
			powerProvider = new MinechemPowerProvider(minEnergyPerTick, maxEnergyPerTick, activationEnergy, energyStorage);
			powerProvider.configurePowerPerdition(1, Constants.TICKS_PER_SECOND * 2);
		}
		model = new ModelSynthesizer();
		ActionManager.registerTriggerProvider(this);
	}

    @Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection direction) {
        return new Transactor(storageInventory).add(stack, doAdd).stackSize;
	}
	
	public boolean canTakeOutputStack() {
		return synthesisInventory[kStartOutput] != null && hasEnoughPowerForCurrentRecipe() && takeStacksFromStorage(false);
	}
	
	public void clearRecipeMatrix() {
		for(int slot = kStartRecipe; slot < kStartRecipe + kSizeRecipe; slot++) {
			synthesisInventory[slot] = null;
		}
	}
	
	@Override
	public void closeChest() {}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(slot == kStartJournal)
			clearRecipeMatrix();
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
	public void doWork() {
	}
	
	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection direction, int maxItemCount) {
		switch (direction) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
            case UNKNOWN:
                return extractOutput(doRemove, maxItemCount);
            case UP:
            case DOWN:
                return extractTestTubes(doRemove, maxItemCount);
            	
        }
        return new ItemStack[0];
	}
	
	public ItemStack[] extractOutput(boolean doRemove, int maxItemCount) {
    	if(currentRecipe == null || !takeInputStacks() || !canAffordRecipe(currentRecipe))
        	return null;
        ItemStack outputStack = currentRecipe.getOutput().copy();
        ItemStack[] output = new ItemStack[] { outputStack };
        if(doRemove) {
        	takeEnergy(currentRecipe);
        	addEmptyBottles(currentRecipe.getIngredientCount());
        }
        return output;
	}
	
	public ItemStack[] extractTestTubes(boolean doRemove, int maxItemCount) {
		return new Transactor(tubeInventory).remove(maxItemCount, doRemove);
	}
	
	public SynthesisRecipe getCurrentRecipe() {
		return currentRecipe;
	}
	
	public int getFacing() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public String getInvName() {
		return "container.synthesis";
	}

	@Override
	public LinkedList<ITrigger> getNeighborTriggers(Block block, TileEntity tile) {
		if(tile instanceof TileEntitySynthesis) {
			LinkedList<ITrigger> triggers = new LinkedList();
			triggers.add(MinechemTriggers.fullEnergy);
			triggers.add(MinechemTriggers.noTestTubes);
			triggers.add(MinechemTriggers.outputJammed);
			return triggers;
		}
		return null;
	}

	@Override
	public LinkedList<ITrigger> getPipeTriggers(IPipe pipe) {
		return null;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return this.powerProvider;
	}

	public ItemStack[] getRecipeMatrixItems() {
		return recipeMatrix.copyInventoryToArray();
	}

	@Override
	public int getSizeInventory() {
		return kSizeOutput + kSizeRecipe + kSizeStorage + kSizeBottles + kSizeJournal;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		switch(side) {
		case NORTH:
		case SOUTH:
		case UNKNOWN:
			return kSizeStorage;
		case EAST:
		case WEST:
			return 1;
		case UP:
		case DOWN:
		default:
			return kSizeBottles;
		}
	}
	
	@Override
	public int getStartInventorySide(ForgeDirection side) {
		switch(side) {
		case NORTH:
		case SOUTH:
		case UNKNOWN:
			return kStartStorage;
		case EAST:
		case WEST:
			return kStartOutput;
		case UP:
		case DOWN:
		default:
			return kStartBottles;
		}
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return synthesisInventory[var1];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return null;
	}

	public boolean hasEnoughPowerForCurrentRecipe() {
		return currentRecipe != null && powerProvider.getEnergyStored() >= currentRecipe.energyCost();
	}
	
	@Override
	public boolean hasFullEnergy() {
		return hasFullEnergy;
	}
	
	@Override
	public boolean hasNoTestTubes() {
		boolean hasNoTestTubes = true;
		for(int slot = kStartBottles; slot < kStartBottles + kSizeBottles; slot++) {
			if(this.synthesisInventory[slot] != null) {
				hasNoTestTubes = false;
				break;
			}
		}
		return hasNoTestTubes;
	}

	@Override
	public boolean isJammed() {
		int count = 0;
		for(int slot = kStartBottles; slot < kStartBottles + kSizeBottles; slot++) {
			if(synthesisInventory[slot] != null) {
				count += synthesisInventory[slot].stackSize;
			}
		}
		return count == (64*4);
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		double dist = entityPlayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D);
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false :  dist <= 64.0D;
	}

	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		getRecipeResult();
	}

	public void onOuputPickupFromSlot(EntityPlayer entityPlayer) {
		if(takeStacksFromStorage(true))
			takeEnergy(currentRecipe);
	}

	@Override
	public void openChest() {}

	@Override
	public int powerRequest() {
		if(powerProvider.getEnergyStored() < powerProvider.getMaxEnergyStored())
			return powerProvider.getMaxEnergyReceived();
		else
			return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		synthesisInventory = new ItemStack[getSizeInventory()];
		NBTTagList inventory = nbtTagCompound.getTagList("inventory");
		MinechemHelper.readTagListToItemStackArray(inventory, synthesisInventory);
		powerProvider.readFromNBT(nbtTagCompound);
	}

	public void sendUpdatePacket() {
		PacketSynthesisUpdate packet = new PacketSynthesisUpdate(this);
		int dimensionID = worldObj.getWorldInfo().getDimension();
		PacketHandler.getInstance().synthesisUpdateHandler.sendToAllPlayersInDimension(packet, dimensionID);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		synthesisInventory[slot] = itemstack;
		if(slot == kStartJournal && itemstack != null)
			onPutJournal(itemstack);
	}

	@Override
	public void setPowerProvider(IPowerProvider provider) {
		this.powerProvider = (MinechemPowerProvider) provider;
	}

    public boolean takeStacksFromStorage(boolean doTake) {
		List<ItemStack> ingredients = MinechemHelper.convertChemicalsIntoItemStacks(currentRecipe.getShapelessRecipe());
		ItemStack[] storage = storageInventory.copyInventoryToArray();
		for(ItemStack ingredient : ingredients) {
			if(!takeStackFromStorage(ingredient, storage))
				return false;
		}
		if(doTake) {
			addEmptyBottles(currentRecipe.getIngredientCount());
			storageInventory.setInventoryStacks(storage);
		}
		return true;
	}
	
	@Override
	public void updateEntity() {
		powerProvider.update(this);
		if(!worldObj.isRemote && powerProvider.didEnergyStoredChange()) {
			sendUpdatePacket();
		}
		if(powerProvider.getEnergyStored() >= powerProvider.getMaxEnergyStored())
			hasFullEnergy = true;
		if(hasFullEnergy && powerProvider.getEnergyStored() < powerProvider.getMaxEnergyStored()/2)
			hasFullEnergy = false;
		
		if(currentRecipe != null && synthesisInventory[kStartOutput] == null)
		{
			synthesisInventory[kStartOutput] = currentRecipe.getOutput().copy();
		}
	}
	
	@Override
    public void validate() {
        super.validate();
        getRecipeResult();
    }
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagList inventory = MinechemHelper.writeItemStackArrayToTagList(synthesisInventory);
		nbtTagCompound.setTag("inventory", inventory);
		powerProvider.writeToNBT(nbtTagCompound);
	}
	
	
	private void addEmptyBottles(int amount) {
		for(int slot = kStartBottles; slot < kStartBottles + kSizeBottles; slot++) {
			if(synthesisInventory[slot] == null) {
				int stackSize = Math.min(amount, getInventoryStackLimit());
				setInventorySlotContents(slot, new ItemStack(MinechemItems.testTube, stackSize));
				amount -= stackSize;
			} else if(synthesisInventory[slot].itemID == MinechemItems.testTube.shiftedIndex) {
				int stackAddition = getInventoryStackLimit() - synthesisInventory[slot].stackSize;
				stackAddition = Math.min(amount, stackAddition);
				synthesisInventory[slot].stackSize += stackAddition;
				amount -= stackAddition;
			}
			if(amount <= 0)
				break;
		}
		// Drop the test tubes.
		if(amount > 0) {
			ItemStack tubes = new ItemStack(MinechemItems.testTube, amount);
			MinechemHelper.ejectItemStackIntoWorld(tubes, worldObj, xCoord, yCoord, zCoord);
		}
	}
	
	private boolean canAffordRecipe(SynthesisRecipe recipe) {
		int energyCost = recipe.energyCost();
		return powerProvider.getEnergyStored() >= energyCost;
	}

	private boolean getRecipeResult() {
		ItemStack[] recipeMatrixItems = getRecipeMatrixItems();
		SynthesisRecipe recipe = SynthesisRecipeHandler.instance.getRecipeFromInput(recipeMatrixItems);
		if(recipe != null) {
			synthesisInventory[kStartOutput] = recipe.getOutput().copy();
			currentRecipe = recipe;
			return true;
		} else {
			synthesisInventory[kStartOutput] = null;
			currentRecipe = null;
			return false;
		}
	}

	private ArrayList<ItemStackPointer> getStackFromAdjacentChest(int itemId, int itemDamage, int stackSize, ForgeDirection direction,
			ArrayList<ItemStackPointer>allPointers)
	{
		Position position = new Position(xCoord, yCoord, zCoord, direction);
		position.moveForwards(1.0);
		TileEntity tileEntity = worldObj.getBlockTileEntity((int)position.x, (int)position.y, (int)position.z);
		ArrayList<ItemStackPointer> itemStackPointers = new ArrayList();
		
		if(tileEntity instanceof IInventory) {
			IInventory inventory = MinechemHelper.getInventory((IInventory)tileEntity);
			for(int slot = 0; slot < inventory.getSizeInventory(); slot++) {
				ItemStack itemstack = inventory.getStackInSlot(slot);
				if(itemstack != null && itemstack.itemID == itemId && itemstack.getItemDamage() == itemDamage
						&& !itemStackPointersHasSlot(allPointers, slot, inventory))
				{
					int amount = Math.min(stackSize, itemstack.stackSize);
					stackSize -= amount;
					
					ItemStackPointer itemStackPointer = new ItemStackPointer();
					itemStackPointer.inventory = inventory;
					itemStackPointer.itemstack = itemstack;
					itemStackPointer.slot 	   = slot;
					itemStackPointer.stackSize = amount;
					itemStackPointers.add(itemStackPointer);
					
					if(stackSize <= 0) {
						return itemStackPointers;
					}
				}
			}
		}
		return null;
	}

	private ArrayList<ItemStackPointer> getStackFromAdjacentChests(int itemId, int itemDamage, int stackSize,
			ArrayList<ItemStackPointer>allPointers)
	{
		ArrayList<ItemStackPointer> itemStackPointers = null;
		itemStackPointers = getStackFromAdjacentChest(itemId, itemDamage, stackSize, ForgeDirection.NORTH, allPointers);
		if(itemStackPointers == null)
			itemStackPointers = getStackFromAdjacentChest(itemId, itemDamage, stackSize, ForgeDirection.EAST, allPointers);
		if(itemStackPointers == null)
			itemStackPointers = getStackFromAdjacentChest(itemId, itemDamage, stackSize, ForgeDirection.SOUTH, allPointers);
		if(itemStackPointers == null)
			itemStackPointers = getStackFromAdjacentChest(itemId, itemDamage, stackSize, ForgeDirection.WEST, allPointers);
		if(itemStackPointers == null)
			itemStackPointers = getStackFromAdjacentChest(itemId, itemDamage, stackSize, ForgeDirection.UP, allPointers);
		return itemStackPointers;
	}

	private ArrayList<ItemStackPointer> getStacksFromAdjacentChests(ArrayList<ItemStack> stacks) {
		ArrayList<ItemStackPointer> allPointers = new ArrayList();
		for(ItemStack itemstack : stacks) {
			ArrayList<ItemStackPointer> itemStackPointers = getStackFromAdjacentChests(
					itemstack.itemID, 
					itemstack.getItemDamage(), 
					itemstack.stackSize,
					allPointers
			);
			if(itemStackPointers != null) {
				allPointers.addAll(itemStackPointers);
			} else {
				return null;
			}
		}
		return allPointers;
	}

	private boolean hasItemsInRecipeMatrix() {
		ItemStack[] recipeMatrixItems = getRecipeMatrixItems();
		for(ItemStack itemstack : recipeMatrixItems) {
			if(itemstack != null)
				return true;
		}
		return false;
	}

	private boolean itemStackPointersHasSlot(ArrayList<ItemStackPointer> allPointers, int slot, IInventory inventory) {
		for(ItemStackPointer itemstackPointer : allPointers) {
			if(itemstackPointer.inventory == inventory && itemstackPointer.slot == slot)
				return true;
		}
		return false;
	}

	private void onPutJournal(ItemStack itemstack) {
		ItemStack activeItem = MinechemItems.journal.getActiveStack(itemstack);
		if(activeItem != null) {
			SynthesisRecipe recipe = SynthesisRecipeHandler.instance.getRecipeFromOutput(activeItem);
			setRecipe(recipe);
		}
	}

	private void takeEnergy(SynthesisRecipe recipe) {
		int energyCost = recipe.energyCost();
		powerProvider.useEnergy(energyCost, energyCost, true);
	}

	private boolean takeStackFromStorage(ItemStack ingredient, ItemStack[] storage) {
		int ingredientAmountLeft = ingredient.stackSize;
		for(int slot = 0; slot < storage.length; slot++) {
			ItemStack storageItem = storage[slot];
			if(storageItem != null && Util.stacksAreSameKind(storageItem, ingredient)) {
				int amountToTake = Math.min(storageItem.stackSize, ingredientAmountLeft);
				ingredientAmountLeft  -= amountToTake;
				storageItem.stackSize -= amountToTake;
				if(storageItem.stackSize <= 0)
					storage[slot] = null;
				if(ingredientAmountLeft <= 0)
					break;
			}
		}
		return ingredientAmountLeft == 0;
	}

	private boolean takeInputStacks() {
    	if(takeStacksFromStorage(false)) {
    		takeStacksFromStorage(true);
    		return true;
    	}
        return false;
    }

	private boolean takeStacksFromAdjacentChests(SynthesisRecipe recipe, boolean doTake) {
		ArrayList<ItemStack> ingredients = MinechemHelper.convertChemicalsIntoItemStacks(recipe.getShapelessRecipe());
		ArrayList<ItemStackPointer> itemStackPointers = getStacksFromAdjacentChests(ingredients);
		if(itemStackPointers != null) {
			for(ItemStackPointer pointer : itemStackPointers) {
				pointer.inventory.decrStackSize(pointer.slot, pointer.stackSize);
			}
			return true;
		}
		return false;
	}

	public List<ItemStack> getMaximumOutput() {
		return getOutput(0, true);
	}
	
	public ItemStack getOutputTemplate() {
		ItemStack template = null;
		ItemStack outputStack = synthesisInventory[kStartOutput];
		if(outputStack != null) {
			template = outputStack.copy();
			if(template.stackSize == 0)
				template.stackSize = 1;
		}
		return template;
	}
	
	public List<ItemStack> getOutput(int amount, boolean all) {
		if(currentRecipe == null)
			return null;
		ItemStack template = getOutputTemplate();
		List<ItemStack> outputs = new ArrayList();
		ItemStack initialStack = template.copy();
		initialStack.stackSize = 0;
		outputs.add(initialStack);
		while(canTakeOutputStack() && (amount > 0 || all) && takeInputStacks()) {
			takeEnergy(currentRecipe);
			ItemStack output  = outputs.get(outputs.size()-1);
			if(output.stackSize + template.stackSize > output.getMaxStackSize()) {
				int leftOverStackSize = template.stackSize - (output.getMaxStackSize() - output.stackSize);
				output.stackSize = output.getMaxStackSize();
				if(leftOverStackSize > 0) {
					ItemStack newOutput = template.copy();
					newOutput.stackSize = leftOverStackSize;
					outputs.add(newOutput);
				}
			} else {
				output.stackSize += template.stackSize;
			}
			onInventoryChanged();
			amount--;
		}
		return outputs;
	}

	public void setRecipe(SynthesisRecipe recipe) {
		clearRecipeMatrix();
		if(recipe != null) {
			ItemStack[] ingredients = MinechemHelper.convertChemicalArrayIntoItemStackArray(recipe.getShapedRecipe());
			for(int i = 0; i < Math.min(kSizeRecipe, ingredients.length); i++) {
				synthesisInventory[kStartRecipe + i] = ingredients[i];
			}
			onInventoryChanged();
		}
	}

}
