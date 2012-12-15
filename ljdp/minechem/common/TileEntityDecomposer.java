package ljdp.minechem.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ljdp.minechem.common.network.PacketDecomposerUpdate;
import ljdp.minechem.common.network.PacketHandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class TileEntityDecomposer extends TileEntity implements IInventory, ISidedInventory {
	
	private ItemStack[] decomposerItemStacks;
	private ArrayList<ItemStack> outputBuffer;
	protected final int kInputSlot = 0;
	protected final int kOutputSlotStart    = 1;
	protected final int kOutputSlotEnd		= 9;
	protected final int kEmptyBottleSlotStart = 10;
	protected final int kEmptyBottleSlotEnd   = 13;
	protected final int kEmptyBottleSlotsSize = 4;
	protected final int kOutputSlotsSize		= 9;

	protected enum State {
		kProcessIdle, kProcessActive, kProcessFinished, kProcessJammed, kProcessNoBottles
	}
	
	public float processSpeed = 0.5F;
	private int tickTimer = 0;
	public State state = State.kProcessIdle;
	private ItemStack activeStack;
	
	public TileEntityDecomposer() {
		decomposerItemStacks = new ItemStack[getSizeInventory()];
		outputBuffer = new ArrayList<ItemStack>();
	}
	
	@Override
	public void updateEntity() {
		if(state == State.kProcessIdle && canDecomposeInput()) {
			decomposeActiveStack();
			state = State.kProcessActive;
			this.onInventoryChanged();
		} else if(state == State.kProcessActive && !worldObj.isRemote) {
			doProcess();
		} else if(state == State.kProcessFinished) {
			activeStack = null;
			state = State.kProcessIdle;
		} else if(state == State.kProcessJammed && canUnjam()) {
			state = State.kProcessActive;
		} else if(state == State.kProcessNoBottles && canTakeEmptyBottle()) {
			state = State.kProcessActive;
		}
	}
	
	private void doProcess() {
		tickTimer++;
		float processTickRate = 1 / processSpeed;
		if(tickTimer > processTickRate) {
			tickTimer = 0;
			State oldState = state;
			int maxItems = (int) Math.max(1, processSpeed);
			for(int i = 0; i < maxItems; i++) {
				state = moveBufferItemToOutputSlot();
				if(state != State.kProcessActive)
					break;
			}
			this.onInventoryChanged();
			if(!state.equals(oldState))
				sendUpdatePacket();
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
			UnbondingRecipe recipe = MinechemRecipes.getInstance().getRecipe(inputStack);
			return (recipe != null) && canTakeEmptyBottle();
		}
	}
	
	private void decomposeActiveStack() {
		ItemStack inputStack = getActiveStack();
		if(inputStack.itemID == MinechemItems.molecule.shiftedIndex) {
			ArrayList<ItemStack> outputStacks = ((ItemMolecule)MinechemItems.molecule).getElements(inputStack);
			placeStacksInBuffer(outputStacks);
		} else {
			UnbondingRecipe recipe = MinechemRecipes.getInstance().getRecipe(inputStack);
			if(recipe != null) {
				placeStacksInBuffer(recipe.getOutput());
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
		for(int outputSlot = kOutputSlotStart; outputSlot <= kOutputSlotEnd; outputSlot++) {
			ItemStack stackInSlot = getStackInSlot(outputSlot);
			if(stackInSlot == null) {
				setInventorySlotContents(outputSlot, itemstack);
				return true;
			} else if(stackInSlot.isItemEqual(itemstack) 
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
	
	private NBTTagList writeItemStackArrayToTagList(ItemStack[] itemstacks) {
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
	
	private ItemStack[] readTagListToItemStackArray(NBTTagList taglist, ItemStack[] itemstacks) {
		for(int i = 0; i < taglist.tagCount(); i++) {
			NBTTagCompound itemstackCompound = (NBTTagCompound) taglist.tagAt(i);
			byte slot = itemstackCompound.getByte("slot");
			itemstacks[slot] = ItemStack.loadItemStackFromNBT(itemstackCompound);
		}
		return itemstacks;
	}
	
	private NBTTagList writeItemStackListToTagList(ArrayList<ItemStack> list) {
		NBTTagList taglist = new NBTTagList();
		for(ItemStack itemstack : list) {
			NBTTagCompound itemstackCompound = new NBTTagCompound();
			itemstack.writeToNBT(itemstackCompound);
			taglist.appendTag(itemstackCompound);
		}
		return taglist;
	}
	
	private ArrayList<ItemStack> readTagListToItemStackList(NBTTagList taglist) {
		ArrayList<ItemStack> itemlist = new ArrayList<ItemStack>();
		for(int i = 0; i < taglist.tagCount(); i++) {
			NBTTagCompound itemstackCompound = (NBTTagCompound) taglist.tagAt(i);
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(itemstackCompound);
			itemlist.add(itemstack);
		}
		return itemlist;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagList inventory = writeItemStackArrayToTagList(decomposerItemStacks);
		NBTTagList buffer	 = writeItemStackListToTagList(outputBuffer);
		nbtTagCompound.setTag("inventory", inventory);
		nbtTagCompound.setTag("buffer", buffer);
		if(activeStack != null) {
			NBTTagCompound activeStackCompound = new NBTTagCompound();
			activeStack.writeToNBT(activeStackCompound);
			nbtTagCompound.setTag("activeStack", activeStackCompound);
		}
		nbtTagCompound.setByte("state", (byte)state.ordinal());
		nbtTagCompound.setShort("tickTimer", (short) tickTimer);
		nbtTagCompound.setFloat("processSpeed", (float) processSpeed);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		NBTTagList inventory = nbtTagCompound.getTagList("inventory");
		NBTTagList buffer	 = nbtTagCompound.getTagList("buffer");
		decomposerItemStacks = new ItemStack[getSizeInventory()];
		outputBuffer		 = readTagListToItemStackList(buffer);
		readTagListToItemStackArray(inventory, decomposerItemStacks);
		
		if(nbtTagCompound.getTag("activeStack") != null) {
			NBTTagCompound activeStackCompound = (NBTTagCompound)nbtTagCompound.getTag("activeStack");
			activeStack = ItemStack.loadItemStackFromNBT(activeStackCompound);
		}
		state = State.values()[nbtTagCompound.getByte("state")];
		tickTimer = nbtTagCompound.getShort("tickTimer");
		processSpeed = nbtTagCompound.getFloat("processSpeed");
	}

	public int getState() {
		return state.ordinal();
	}

	public void setState(int state) {
		this.state = State.values()[state];
	}

}
