package ljdp.minechem.common.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;

import ljdp.minechem.api.recipe.DecomposerRecipe;
import ljdp.minechem.api.util.Constants;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.client.ModelDecomposer;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.gates.IMinechemTriggerProvider;
import ljdp.minechem.common.gates.MinechemTriggers;
import ljdp.minechem.common.inventory.BoundedInventory;
import ljdp.minechem.common.inventory.Transactor;
import ljdp.minechem.common.items.ItemMolecule;
import ljdp.minechem.common.network.PacketDecomposerUpdate;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.recipe.DecomposerRecipeHandler;
import ljdp.minechem.common.utils.MinechemHelper;
import ljdp.minechem.computercraft.IMinechemMachinePeripheral;
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
import buildcraft.api.core.SafeTimeTracker;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerProvider;
import buildcraft.api.inventory.ISpecialInventory;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.transport.IPipe;
import java.util.List;

public class TileEntityDecomposer extends MinechemTileEntity implements ISidedInventory, 
IPowerReceptor, ITriggerProvider, IMinechemTriggerProvider, ISpecialInventory, IMinechemMachinePeripheral
{
	
	private static final int MAX_POWER_STORAGE = 100;
	private static final float MIN_WORK_PER_SECOND = 1.0F;
	private static final float MAX_WORK_PER_SECOND = 10.0F;
	
	private ArrayList<ItemStack> outputBuffer;
	public final int kInputSlot = 0;
	public final int kOutputSlotStart    = 1;
	public final int kOutputSlotEnd		= 9;
	public final int kEmptyTestTubeSlotStart = 10;
	public final int kEmptyTestTubeSlotEnd   = 13;
	public final int kEmptyBottleSlotsSize = 4;
	public final int kOutputSlotsSize		= 9;
	private MinechemPowerProvider powerProvider;
	private SafeTimeTracker updateTracker = new SafeTimeTracker();
	public State state = State.kProcessIdle;
	private ItemStack activeStack;
	private float workToDo = 0;
	public ModelDecomposer model;
	private boolean hasFullEnergy;
	
	private final BoundedInventory testTubeInventory;
	private final BoundedInventory outputInventory;
	private final BoundedInventory inputInventory;
	private Transactor testTubeTransactor;
	private Transactor outputTransactor;
	private Transactor inputTransactor;
	
	public enum State {
		kProcessIdle, kProcessActive, kProcessFinished, kProcessJammed, kProcessNoBottles
	}
	
	public TileEntityDecomposer() {
		testTubeInventory  = new BoundedInventory(this, kEmptyTestTubeSlotStart, kEmptyTestTubeSlotEnd + 1);
		outputInventory    = new BoundedInventory(this, kOutputSlotStart, kOutputSlotEnd + 1);
		inputInventory	   = new BoundedInventory(this, kInputSlot, kInputSlot + 1);
		testTubeTransactor = new Transactor(testTubeInventory);
		outputTransactor   = new Transactor(outputInventory);
		inputTransactor	   = new Transactor(inputInventory);
		inventory = new ItemStack[getSizeInventory()];
		outputBuffer = new ArrayList<ItemStack>();
		powerProvider = new MinechemPowerProvider(2, 20, 0, 10000);
		powerProvider.configurePowerPerdition(1, Constants.TICKS_PER_SECOND * 2);
		model = new ModelDecomposer();
		ActionManager.registerTriggerProvider(this);
	}
	
	@Override
	public void updateEntity() {
		powerProvider.update(this);
		if(!worldObj.isRemote && (powerProvider.didEnergyStoredChange() || powerProvider.didEnergyUsageChange()))
			sendUpdatePacket();
		
		float energyStored = powerProvider.getEnergyStored();
		if(energyStored >= powerProvider.getMaxEnergyStored())
			hasFullEnergy = true;
		if(hasFullEnergy && energyStored < powerProvider.getMaxEnergyStored() / 2)
			hasFullEnergy = false;
		
		if((state == State.kProcessIdle  || state == State.kProcessFinished) && canDecomposeInput()) {
			activeStack = null;
			decomposeActiveStack();
			state = State.kProcessActive;
			this.onInventoryChanged();
		} else if(!canTakeEmptyTestTube()) {
			state = State.kProcessNoBottles;
		} else if(state == State.kProcessFinished) {
			activeStack = null;
			state = State.kProcessIdle;
		} else if(state == State.kProcessJammed && canUnjam()) {
			state = State.kProcessActive;
		} else if(state == State.kProcessNoBottles && canTakeEmptyTestTube()) {
			state = State.kProcessActive;
		}
	}
		
	@Override
	public void sendUpdatePacket() {
		if(worldObj.isRemote) return;
		PacketDecomposerUpdate packetDecomposerUpdate = new PacketDecomposerUpdate(this);
		int dimensionID = worldObj.getWorldInfo().getDimension();
		PacketHandler.getInstance().decomposerUpdateHandler.sendToAllPlayersInDimension(packetDecomposerUpdate, dimensionID);
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
		DecomposerRecipe recipe = DecomposerRecipeHandler.instance.getRecipe(inputStack);
		return (recipe != null) && canTakeEmptyTestTube();
	}
	
	private void decomposeActiveStack() {
		ItemStack inputStack = getActiveStack();
		DecomposerRecipe recipe = DecomposerRecipeHandler.instance.getRecipe(inputStack);
		if(recipe != null && recipe.getOutput() != null) {
			ArrayList<ItemStack> stacks = MinechemHelper.convertChemicalsIntoItemStacks(recipe.getOutput());
			placeStacksInBuffer(stacks);
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
			if(!canTakeEmptyTestTube())
				return State.kProcessNoBottles;
			else if(addStackToOutputSlots(outputStack.copy().splitStack(1))) {
				outputStack.splitStack(1);
				if(outputStack.stackSize == 0)
					outputBuffer.remove(outputStack);
				takeEmptyTestTube();
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
	
	private boolean canTakeEmptyTestTube() {
		ItemStack testTube = testTubeTransactor.removeItem(false);
		return testTube != null;
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		switch(side) {
		case NORTH:
		case SOUTH:
			return kInputSlot;
		case EAST:
		case WEST:
		case UNKNOWN:
		default:
			return kOutputSlotStart;
		case UP:
		case DOWN:
			return kEmptyTestTubeSlotStart;
		}
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		switch(side) {
		case NORTH:
		case SOUTH:
			return 1;
		case EAST:
		case WEST:
		case UNKNOWN:
		default:
			return kOutputSlotsSize;
		case UP:
		case DOWN:
			return kEmptyBottleSlotsSize;
		}
	}

	@Override
	public int addItem(ItemStack incoming, boolean doAdd, ForgeDirection from) {
		
		if (incoming != null){
			 if (incoming.itemID == MinechemItems.testTube.itemID) {
		        	return testTubeTransactor.add(incoming, doAdd).stackSize;
		        } else {
		        	ItemStack tmp = inputTransactor.add(incoming, doAdd); return (tmp == null) ? 0 : tmp.stackSize;
		        }	
			
		}else{
       
	}
		return kEmptyBottleSlotsSize;
		}

	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from, int maxItemCount) {
		return outputTransactor.remove(maxItemCount, doRemove);
	}

	@Override
	public int getSizeInventory() {
		return 14;
	}

	@Override
	public String getInvName() {
		return "container.decomposer";
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagList inventoryTagList = MinechemHelper.writeItemStackArrayToTagList(inventory);
		NBTTagList buffer	 = MinechemHelper.writeItemStackListToTagList(outputBuffer);
		nbtTagCompound.setTag("inventory", inventoryTagList);
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
		NBTTagList inventoryTagList = nbtTagCompound.getTagList("inventory");
		NBTTagList buffer	 = nbtTagCompound.getTagList("buffer");
		outputBuffer		 = MinechemHelper.readTagListToItemStackList(buffer);
		inventory = MinechemHelper.readTagListToItemStackArray(inventoryTagList, new ItemStack[getSizeInventory()]);
		
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

	@Override
	public LinkedList<ITrigger> getPipeTriggers(IPipe pipe) {
		return null;
	}

	@Override
	public LinkedList<ITrigger> getNeighborTriggers(Block block, TileEntity tile) {
		if(tile instanceof TileEntityDecomposer) {
			LinkedList<ITrigger> triggers = new LinkedList();
			triggers.add(MinechemTriggers.fullEnergy);
			triggers.add(MinechemTriggers.noTestTubes);
			triggers.add(MinechemTriggers.outputJammed);
			return triggers;
		}
		return null;
	}

	@Override
	public boolean hasFullEnergy() {
		return this.hasFullEnergy;
	}
	
	@Override
	public boolean hasNoTestTubes() {
		return this.state == State.kProcessNoBottles;
	}

	@Override
	public boolean isJammed() {
		return this.state == State.kProcessJammed;
	}

	@Override
	public ItemStack takeEmptyTestTube() {
		return testTubeTransactor.removeItem(true);
	}

	@Override
	public ItemStack putEmptyTestTube(ItemStack testTube) {
		return testTubeTransactor.add(testTube, true);
	}

	@Override
	public ItemStack takeOutput() {
		return outputTransactor.removeItem(true);
	}

	@Override
	public ItemStack putInput(ItemStack input) {
		return inputTransactor.add(input, true);
	}

	@Override
	public ItemStack takeFusionStar() {
		return null;
	}

	@Override
	public ItemStack putFusionStar(ItemStack fusionStar) {
		return null;
	}

	@Override
	public ItemStack takeJournal() {
		return null;
	}

	@Override
	public ItemStack putJournal(ItemStack journal) {
		return null;
	}

	@Override
	public ItemStack putOutput(ItemStack output) {
		return outputTransactor.add(output, true);
	}

	@Override
	public ItemStack takeInput() {
		return outputTransactor.removeItem(true);
	}

	@Override
	public String getMachineState() {
		if(this.state == State.kProcessJammed) {
			return "outputjammed";
		} else if(this.state == State.kProcessNoBottles) {
			return "needtesttubes";
		} else if(this.state == State.kProcessActive) {
			return "decomposing";
		} else if(this.powerProvider.getEnergyStored() > this.powerProvider.getMinEnergyReceived()) {
			return "powered";
		} else {
			return "unpowered";
		}
	}

}
