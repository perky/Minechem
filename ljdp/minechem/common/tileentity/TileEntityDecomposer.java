package ljdp.minechem.common.tileentity;

import java.util.ArrayList;

import ljdp.minechem.api.recipe.DecomposerRecipe;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.client.ModelDecomposer;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.items.ItemMolecule;
import ljdp.minechem.common.network.PacketDecomposerUpdate;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.recipe.DecomposerRecipeHandler;
import ljdp.minechem.common.utils.MinechemHelper;
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
import buildcraft.api.core.SafeTimeTracker;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

public class TileEntityDecomposer extends TileEntity implements IInventory, ISidedInventory, IPowerReceptor {
	
	private static final int MAX_POWER_STORAGE = 100;
	private static final float MIN_WORK_PER_SECOND = 1.0F;
	private static final float MAX_WORK_PER_SECOND = 10.0F;
	
	private ItemStack[] decomposerItemStacks;
	private ArrayList<ItemStack> outputBuffer;
	public final int kInputSlot = 0;
	public final int kOutputSlotStart    = 1;
	public final int kOutputSlotEnd		= 9;
	public final int kEmptyBottleSlotStart = 10;
	public final int kEmptyBottleSlotEnd   = 13;
	public final int kEmptyBottleSlotsSize = 4;
	public final int kOutputSlotsSize		= 9;
	private MinechemPowerProvider powerProvider;
	private SafeTimeTracker updateTracker = new SafeTimeTracker();
	public State state = State.kProcessIdle;
	private ItemStack activeStack;
	private float workToDo = 0;
	public ModelDecomposer model;
	
	public enum State {
		kProcessIdle, kProcessActive, kProcessFinished, kProcessJammed, kProcessNoBottles
	}
	
	public TileEntityDecomposer() {
		decomposerItemStacks = new ItemStack[getSizeInventory()];
		outputBuffer = new ArrayList<ItemStack>();
		if (PowerFramework.currentFramework != null) {
			powerProvider = new MinechemPowerProvider(2, 20, 0, 10000);
			powerProvider.configurePowerPerdition(1, 10);
		}
		model = new ModelDecomposer();
	}
	
	@Override
	public void updateEntity() {
		powerProvider.update(this);
		if(!worldObj.isRemote && (powerProvider.didEnergyStoredChange() || powerProvider.didEnergyUsageChange()))
			sendUpdatePacket();
		
		if((state == State.kProcessIdle  || state == State.kProcessFinished) && canDecomposeInput()) {
			activeStack = null;
			decomposeActiveStack();
			state = State.kProcessActive;
			this.onInventoryChanged();
		} else if(!canTakeEmptyBottle()) {
			state = State.kProcessNoBottles;
		} else if(state == State.kProcessFinished) {
			activeStack = null;
			state = State.kProcessIdle;
		} else if(state == State.kProcessJammed && canUnjam()) {
			state = State.kProcessActive;
		} else if(state == State.kProcessNoBottles && canTakeEmptyBottle()) {
			state = State.kProcessActive;
		}
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
		
	private void sendUpdatePacket() {
		if(worldObj.isRemote)
			return;
		PacketDecomposerUpdate packetDecomposerUpdate = new PacketDecomposerUpdate(this);
		PacketHandler.sendPacket(packetDecomposerUpdate);
	}
	
	private ItemStack getActiveStack() {
		if(activeStack == null){
			if(getStackInSlot(kInputSlot) != null) {
				activeStack = decrStackSize(kInputSlot, 1);
			} else {
				return null;
			}
		}
		return activeStack;
	}

	private boolean canDecomposeInput() {
		ItemStack inputStack = getStackInSlot(kInputSlot);
		if(inputStack == null)
			return false;
		if(inputStack.itemID == MinechemItems.molecule.shiftedIndex) {
			return canTakeEmptyBottle();
		} else {
			DecomposerRecipe recipe = DecomposerRecipeHandler.instance.getRecipe(inputStack);
			return (recipe != null) && canTakeEmptyBottle();
		}
	}
	
	private void decomposeActiveStack() {
		ItemStack inputStack = getActiveStack();
		if(inputStack.itemID == MinechemItems.molecule.shiftedIndex) {
			ArrayList<ItemStack> outputStacks = ((ItemMolecule)MinechemItems.molecule).getElements(inputStack);
			placeStacksInBuffer(outputStacks);
		} else {
			DecomposerRecipe recipe = DecomposerRecipeHandler.instance.getRecipe(inputStack);
			if(recipe != null && recipe.getOutput() != null) {
				ArrayList<ItemStack> stacks = MinechemHelper.convertChemicalsIntoItemStacks(recipe.getOutput());
				placeStacksInBuffer(stacks);
			}
		}
	}
	
	private void placeStacksInBuffer(ArrayList<ItemStack> outputStacks) {
		if(outputStacks != null) {
			outputBuffer = outputStacks;
		} else {
			state = State.kProcessFinished;
		}
	}
	
	private boolean canUnjam() {
		for(int slot = kOutputSlotStart; slot <= kOutputSlotEnd; slot++) {
			if(getStackInSlot(slot) == null)
				return true;
		}
		return false;
	}
	
	private State moveBufferItemToOutputSlot() {
		for(ItemStack outputStack : outputBuffer) {
			if(!canTakeEmptyBottle())
				return State.kProcessNoBottles;
			else if(addStackToOutputSlots(outputStack.copy().splitStack(1))) {
				outputStack.splitStack(1);
				if(outputStack.stackSize == 0)
					outputBuffer.remove(outputStack);
				takeEmptyBottle();
				return State.kProcessActive;
			} else {
				return State.kProcessJammed;
			}
		}
		return State.kProcessFinished;
	}
	
	private boolean addStackToOutputSlots(ItemStack itemstack) {
		itemstack.getItem().onCreated(itemstack, this.worldObj, null);
		for(int outputSlot = kOutputSlotStart; outputSlot <= kOutputSlotEnd; outputSlot++) {
			ItemStack stackInSlot = getStackInSlot(outputSlot);
			if(stackInSlot == null) {
				setInventorySlotContents(outputSlot, itemstack);
				return true;
			} else if(Util.stacksAreSameKind(stackInSlot, itemstack)
					&& (stackInSlot.stackSize + itemstack.stackSize) <= getInventoryStackLimit()) {
				stackInSlot.stackSize += itemstack.stackSize;
				return true;
			}
		}
		return false;
	}
	
	private boolean canTakeEmptyBottle() {
		for(int i = kEmptyBottleSlotStart; i <= kEmptyBottleSlotEnd; i++) {
			ItemStack itemstack = getStackInSlot(i);
			if(itemstack == null)
				continue;
			if(itemstack.itemID == Item.glassBottle.shiftedIndex) {
				return true;
			}
		}
		return false;
	}
	
	private boolean takeEmptyBottle() {
		for(int i = kEmptyBottleSlotStart; i <= kEmptyBottleSlotEnd; i++) {
			ItemStack itemstack = getStackInSlot(i);
			if(itemstack == null)
				continue;
			if(itemstack.itemID == Item.glassBottle.shiftedIndex) {
				decrStackSize(i, 1);
				return true;
			}
		}
		return false;
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		if(side == ForgeDirection.UP) return kInputSlot;
		if(side == ForgeDirection.DOWN) return kOutputSlotStart;
		if(side == ForgeDirection.NORTH) return kEmptyBottleSlotStart;
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		if(side == ForgeDirection.UP) return 1;
		if(side == ForgeDirection.DOWN) return kOutputSlotsSize;
		if(side == ForgeDirection.NORTH) return kEmptyBottleSlotsSize;
		return 0;
	}

	@Override
	public int getSizeInventory() {
		return 14;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return this.decomposerItemStacks[var1];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(this.decomposerItemStacks[slot] != null) {
			ItemStack itemstack;
			if(this.decomposerItemStacks[slot].stackSize <= amount) {
				itemstack = this.decomposerItemStacks[slot];
				this.decomposerItemStacks[slot] = null;
				return itemstack;
			} else {
				itemstack = this.decomposerItemStacks[slot].splitStack(amount);
				if(this.decomposerItemStacks[slot].stackSize == 0)
					this.decomposerItemStacks[slot] = null;
				return itemstack;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (this.decomposerItemStacks[slot] != null)
        {
            ItemStack itemstack = this.decomposerItemStacks[slot];
            this.decomposerItemStacks[slot] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		this.decomposerItemStacks[slot] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
			itemstack.stackSize = this.getInventoryStackLimit();
	}

	@Override
	public String getInvName() {
		return "container.decomposer";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : entityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}
	
	
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagList inventory = MinechemHelper.writeItemStackArrayToTagList(decomposerItemStacks);
		NBTTagList buffer	 = MinechemHelper.writeItemStackListToTagList(outputBuffer);
		nbtTagCompound.setTag("inventory", inventory);
		nbtTagCompound.setTag("buffer", buffer);
		if(activeStack != null) {
			NBTTagCompound activeStackCompound = new NBTTagCompound();
			activeStack.writeToNBT(activeStackCompound);
			nbtTagCompound.setTag("activeStack", activeStackCompound);
		}
		nbtTagCompound.setByte("state", (byte)state.ordinal());
		powerProvider.writeToNBT(nbtTagCompound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		NBTTagList inventory = nbtTagCompound.getTagList("inventory");
		NBTTagList buffer	 = nbtTagCompound.getTagList("buffer");
		decomposerItemStacks = new ItemStack[getSizeInventory()];
		outputBuffer		 = MinechemHelper.readTagListToItemStackList(buffer);
		MinechemHelper.readTagListToItemStackArray(inventory, decomposerItemStacks);
		
		if(nbtTagCompound.getTag("activeStack") != null) {
			NBTTagCompound activeStackCompound = (NBTTagCompound)nbtTagCompound.getTag("activeStack");
			activeStack = ItemStack.loadItemStackFromNBT(activeStackCompound);
		}
		state = State.values()[nbtTagCompound.getByte("state")];
		powerProvider.readFromNBT(nbtTagCompound);
	}

	public State getState() {
		return state;
	}

	public void setState(int state) {
		this.state = State.values()[state];
	}

	@Override
	public void setPowerProvider(IPowerProvider provider) {
		this.powerProvider = (MinechemPowerProvider) provider;
	}

	@Override
	public IPowerProvider getPowerProvider() {
		return powerProvider;
	}

	@Override
	public void doWork() {
		if(state != State.kProcessActive)
			return;
		
		State oldState = state;
		float minEnergy = powerProvider.getMinEnergyReceived();
		float maxEnergy = powerProvider.getMaxEnergyReceived();
		float energyUsed = powerProvider.useEnergy(minEnergy, maxEnergy, true);
		workToDo += MinechemHelper.translateValue(energyUsed, minEnergy, maxEnergy, 
				MIN_WORK_PER_SECOND / 20, MAX_WORK_PER_SECOND / 20);
		if(!worldObj.isRemote) {
			while(workToDo >= 1) {
				workToDo--;
				state = moveBufferItemToOutputSlot();
				if(state != State.kProcessActive)
					break;
			}
			this.onInventoryChanged();
			if(!state.equals(oldState)) {
				sendUpdatePacket();
			}
		}
	}

	@Override
	public int powerRequest() {
		if(powerProvider.getEnergyStored() < powerProvider.getMaxEnergyStored()) {
			return powerProvider.getMaxEnergyReceived();
		} else {
			return 0;
		}
	}

	public boolean isPowered() {
		return (state != State.kProcessJammed && state != State.kProcessNoBottles && (powerProvider.getEnergyStored() > powerProvider.getMinEnergyReceived()));
	}

}
