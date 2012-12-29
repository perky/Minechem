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
import buildcraft.core.IMachine;
import buildcraft.core.inventory.TransactorRoundRobin;
import java.util.List;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.client.ModelSynthesizer;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.gates.IMinechemTriggerProvider;
import ljdp.minechem.common.gates.MinechemTriggers;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.network.PacketSynthesisUpdate;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.common.utils.BoundedInventory;
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

public class TileEntitySynthesis extends TileEntity implements IInventory, ISidedInventory, 
IPowerReceptor, ITriggerProvider, IMinechemTriggerProvider, IMachine, ISpecialInventory
{
	
	private ItemStack[] synthesisInventory;
	public static final int kSizeOutput = 1;
	public static final int kSizeInput  = 9;
	public static final int kSizeBottles = 4;
	public static final int kStartOutput = 0;
	public static final int kStartInput = 1;
	public static final int kStartBottles = 10;
	private SynthesisRecipe currentRecipe;
	MinechemPowerProvider powerProvider;
	public ModelSynthesizer model;
    private final IInventory craftingInventory = new BoundedInventory(this, kStartInput, kStartInput + kSizeInput);
	
	int minEnergyPerTick = 30;
	int maxEnergyPerTick = 200;
	int activationEnergy = 100;
	int energyStorage = 900000;
	private boolean hasFullEnergy;
	
	private class ItemStackPointer {
		IInventory inventory;
		ItemStack itemstack;
		int slot;
		public int stackSize;
	}
	
	public TileEntitySynthesis() {
		synthesisInventory = new ItemStack[getSizeInventory()];
		if (PowerFramework.currentFramework != null) {
			powerProvider = new MinechemPowerProvider(minEnergyPerTick, maxEnergyPerTick, activationEnergy, energyStorage);
			powerProvider.configurePowerPerdition(1, 10);
		}
		model = new ModelSynthesizer();
		ActionManager.registerTriggerProvider(this);
	}

    @Override
    public void validate() {
        super.validate();
        getRecipeResult();
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
		
		if(currentRecipe != null
				&& synthesisInventory[kStartOutput] == null
				&& canAffordRecipe(currentRecipe) 
				&& canAddEmptyBottles(currentRecipe.getIngredientCount()))
		{
			synthesisInventory[kStartOutput] = currentRecipe.getOutput().copy();
		}
	}
	
	private void sendUpdatePacket() {
		PacketSynthesisUpdate packetSynthesisUpdate = new PacketSynthesisUpdate(this);
		PacketHandler.sendPacket(packetSynthesisUpdate);
	}
	
	private boolean getRecipeResult() {
		ItemStack[] craftingItems = getCraftingItems();
		SynthesisRecipe recipe = SynthesisRecipeHandler.instance.getRecipeFromInput(craftingItems);
		if(recipe != null) {
			if(canAffordRecipe(recipe) && canAddEmptyBottles(recipe.getIngredientCount()))
				synthesisInventory[kStartOutput] = recipe.getOutput().copy();
			currentRecipe = recipe;
			return true;
		} else {
			synthesisInventory[kStartOutput] = null;
			currentRecipe = null;
			return false;
		}
	}
	
	private boolean canAffordRecipe(SynthesisRecipe recipe) {
		int energyCost = recipe.energyCost();
		return powerProvider.getEnergyStored() >= energyCost;
	}
	
	public ItemStack[] getCraftingItems() {
		ItemStack[] currentRecipe = new ItemStack[9];
		int i = 0;
		for(int slot = kStartInput; slot < kStartInput + kSizeInput; slot++) {
			currentRecipe[i] = synthesisInventory[slot];
			i++;
		}
		return currentRecipe;
	}
	
	private boolean hasCraftingItems() {
		for(int slot = kStartInput; slot < kStartInput + kSizeInput; slot++) {
			if(synthesisInventory[slot] != null)
				return true;
		}
		return false;
	}
	
	private void takeCraftingItems() {
		ArrayList<ItemStack> stacks = MinechemHelper.convertChemicalsIntoItemStacks(currentRecipe.getShapelessRecipe());
		for(ItemStack ingredient : stacks) {
			takeCraftingItem(ingredient);
		}
	}
	
	private void takeCraftingItem(ItemStack itemstack) {
		ItemStack[] craftingItems = getCraftingItems();
		for(int i = 0; i < craftingItems.length; i++) {
			ItemStack craftingItem = craftingItems[i];
			if(craftingItem != null && Util.stacksAreSameKind(itemstack, craftingItem)
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
			} else if(synthesisInventory[slot].itemID == MinechemItems.testTube.shiftedIndex) {
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
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		double dist = entityPlayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D);
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false :  dist <= 64.0D;
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
	
	public void onOuputPickupFromSlot(EntityPlayer entityPlayer) {
		takeEnergy(currentRecipe);
		takeCraftingItems();
	}
	
	private void takeEnergy(SynthesisRecipe recipe) {
		int energyCost = recipe.energyCost();
		powerProvider.useEnergy(energyCost, energyCost, true);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagList inventory = MinechemHelper.writeItemStackArrayToTagList(synthesisInventory);
		nbtTagCompound.setTag("inventory", inventory);
		powerProvider.writeToNBT(nbtTagCompound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		synthesisInventory = new ItemStack[getSizeInventory()];
		NBTTagList inventory = nbtTagCompound.getTagList("inventory");
		MinechemHelper.readTagListToItemStackArray(inventory, synthesisInventory);
		powerProvider.readFromNBT(nbtTagCompound);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
        this.writeToNBT(tagCompound);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tagCompound);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		this.readFromNBT(pkt.customParam1);
	}

	@Override
	public void setPowerProvider(IPowerProvider provider) {
		this.powerProvider = (MinechemPowerProvider) provider;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return this.powerProvider;
	}

	@Override
	public void doWork() {
	}

	@Override
	public int powerRequest() {
		if(powerProvider.getEnergyStored() < powerProvider.getMaxEnergyStored())
			return powerProvider.getMaxEnergyReceived();
		else
			return 0;
	}

	public boolean hasEnoughPower() {
		return this.powerProvider.getEnergyStored() >= this.powerProvider.getActivationEnergy();
	}

	public int getFacing() {
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		if(side == side.WEST || side == side.NORTH || side == side.EAST || side == side.SOUTH)
			return kStartOutput;
		else
			return kStartBottles;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		if(side == side.WEST || side == side.NORTH || side == side.EAST || side == side.SOUTH)
			return kSizeOutput;
		else
			return kSizeBottles;
	}

	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection direction) {
        // add items in round robin fashion to input slots (as the autobench does)
        return new TransactorRoundRobin(craftingInventory).add(stack, direction, doAdd).stackSize;
	}

	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection direction, int maxItemCount) {
		switch (direction) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
            case UNKNOWN:
                // extract crafted item from all sides (and unkonwn, which logistics pipes specifies)
                if(currentRecipe == null) break;
                ItemStack outputStack = currentRecipe.getOutput().copy();
                if(outputStack.itemID == MinechemItems.element.shiftedIndex)
                    MinechemItems.element.initiateRadioactivity(outputStack, worldObj);
                ItemStack[] output = new ItemStack[] { outputStack };
                if(!doRemove) return output;
                if(!canAddEmptyBottles(currentRecipe.getIngredientCount())) break;
                if(!takeStacks(currentRecipe)) break;
                if(!canAffordRecipe(currentRecipe)) break;
				takeEnergy(currentRecipe);
                addEmptyBottles(currentRecipe.getIngredientCount());
                return output;
            case UP:
            case DOWN:
                // extract bottles from top/bottom
                List<ItemStack> stacks = new LinkedList<ItemStack>();
                for (int slot = kStartBottles; slot < kStartBottles + kSizeBottles; slot++) {
                    ItemStack stack = doRemove
                        ? decrStackSize(slot, maxItemCount)
                        : getStackInSlot(slot);
                    if (stack != null && stack.stackSize > 0) {
                        maxItemCount -= stack.stackSize;
                        stacks.add(stack.copy());
                        if (maxItemCount == 0) break;
                    }
                }
                return stacks.toArray(new ItemStack[0]);
        }
        return new ItemStack[0];
	}

    private boolean takeStacks(SynthesisRecipe recipe) {
        System.out.println("takeStacks");
        return SynthesisRecipeHandler.takeFromCraftingInventory(recipe, craftingInventory)
            || takeStacksFromAdjacentChests(recipe);
    }
	
	private boolean takeStacksFromAdjacentChests(SynthesisRecipe recipe) {
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
	
	private boolean itemStackPointersHasSlot(ArrayList<ItemStackPointer> allPointers, int slot, IInventory inventory) {
		for(ItemStackPointer itemstackPointer : allPointers) {
			if(itemstackPointer.inventory == inventory && itemstackPointer.slot == slot)
				return true;
		}
		return false;
	}

	@Override
	public boolean isActive() {
		return currentRecipe != null;
	}

	@Override
	public boolean manageLiquids() {
		return false;
	}

	@Override
	public boolean manageSolids() {
		return true;
	}

	@Override
	public boolean allowActions() {
		return false;
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
	public LinkedList<ITrigger> getPipeTriggers(IPipe pipe) {
		return null;
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

	public SynthesisRecipe getCurrentRecipe() {
		return currentRecipe;
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

}
