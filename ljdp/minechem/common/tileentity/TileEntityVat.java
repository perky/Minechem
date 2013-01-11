package ljdp.minechem.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.inventory.BoundedInventory;
import ljdp.minechem.common.inventory.Transactor;
import ljdp.minechem.common.utils.MinechemHelper;
import ljdp.minechem.computercraft.IMinechemMachinePeripheral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import buildcraft.api.inventory.ISpecialInventory;

public class TileEntityVat extends MinechemTileEntity implements
		ISpecialInventory, ISidedInventory, IMinechemMachinePeripheral {
	
	public static final int MAX_CHEMICAL_AMOUNT = 64 * 256;
	public static final int kStartInput 		= 0;
	public static final int kStartTestTubeIn	= 1;
	public static final int kStartTestTubeOut	= 2;
	public static final int kStartOutput 		= 3;
	
	public Chemical chemical;
	public int amountOfChemical = 0;
	public int amountOfTestTube = 0;
	private int maxChemicalAmount = MAX_CHEMICAL_AMOUNT;
	public boolean isSpoiled = false;
	
	private final BoundedInventory inputInventory = new BoundedInventory(this, kStartInput, kStartInput + 1);
	private final BoundedInventory tubeInInventory  = new BoundedInventory(this, kStartTestTubeIn, kStartTestTubeIn + 1);
	private final BoundedInventory tubeOutInventory = new BoundedInventory(this, kStartTestTubeOut, kStartTestTubeOut + 1);
	private final BoundedInventory outputInventory = new BoundedInventory(this, kStartOutput, kStartOutput + 1);
	private Transactor outputTransactor = new Transactor(outputInventory);
	private Transactor inputTransactor  = new Transactor(inputInventory);
	private Transactor tubeOutTransactor = new Transactor(tubeOutInventory);
	private boolean workNeeded = false;
	
	public TileEntityVat() {
		this.inventory = new ItemStack[getSizeInventory()];
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		//if(workNeeded) {
			boolean did1 = moveInputToStorage();
			boolean did2 = moveStorageToTestTubeIn();
			boolean did3 = moveStorageToOutput();
			if(!did1 && !did2 && !did3)
				workNeeded = false;
		//}
	}
	
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
		workNeeded = true;
	}

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public String getInvName() {
		return "container.chemicalVat";
	}

	@Override
	public int getStartInventorySide(ForgeDirection side) {
		if(side == side.UP) {
			return kStartInput;
		} else {
			return kStartOutput;
		}
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}

	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from) {
		int stackSize = stack.stackSize;
		ItemStack remainingStack = inputTransactor.add(stack, doAdd);
		int remainingStackSize = remainingStack == null ? 0 : remainingStack.stackSize;
		return stackSize - remainingStackSize;
	}

	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from, int maxItemCount) {
		if(doRemove) {
			List<ItemStack> outputs = new ArrayList();
			for(int i = 0; i < maxItemCount; i++) {
				ItemStack output = takeOutput();
				if(output != null)
					outputs.add(output);
				else
					break;
			}
			return outputs.toArray(new ItemStack[outputs.size()]);
		} else {
			return outputTransactor.remove(maxItemCount, false);
		}
		
	}

	@Override
	void sendUpdatePacket() {
		// TODO Auto-generated method stub
	}

	@Override
	public ItemStack takeEmptyTestTube() {
		if(amountOfTestTube > 0) {
			amountOfTestTube--;
			return new ItemStack(MinechemItems.testTube, 1);
		}
		return null;
	}

	@Override
	public ItemStack putEmptyTestTube(ItemStack testTube) {
		if(amountOfTestTube < maxChemicalAmount) {
			int amountToAdd = Math.min(maxChemicalAmount - amountOfTestTube, testTube.stackSize);
			amountOfTestTube++;
			testTube.stackSize -= amountToAdd;
			if(testTube.stackSize == 0)
				return null;
			else
				return testTube;
		}
		return testTube;
	}
	
	private boolean moveInputToStorage() {
		ItemStack inputStack = decrStackSize(kStartInput, 1);
		if(inputStack == null || !Util.isStackAChemical(inputStack))
			return false;
		if(amountOfChemical < maxChemicalAmount && amountOfTestTube < maxChemicalAmount) {
			Chemical inputChemical = MinechemHelper.itemStackToChemical(inputStack);
			if(chemical == null)
				chemical = inputChemical;
			else if(!chemical.sameAs(inputChemical))
				isSpoiled = true;
			amountOfChemical++;
			amountOfTestTube++;
			return true;
		}
		return false;
	}
	
	private boolean moveStorageToTestTubeIn() {
		if(amountOfTestTube == 0)
			return false;
		ItemStack testTube = getStackInSlot(kStartTestTubeIn);
		if(testTube == null) {
			setInventorySlotContents(kStartTestTubeIn, new ItemStack(MinechemItems.testTube));
			amountOfTestTube--;
			return true;
		} else if(testTube.stackSize < 64) {
			testTube.stackSize++;
			amountOfTestTube--;
			return true;
		}
		return false;
	}
	
	private boolean moveStorageToOutput() {
		ItemStack testTube = tubeOutTransactor.removeItem(true);
		if(testTube != null && chemical != null && amountOfChemical > 0 && amountOfTestTube > 0 && !isSpoiled) {
			ItemStack outputStack = MinechemHelper.chemicalToItemStack(chemical, 1);
			ItemStack remainingStack = putOutput(outputStack);
			if(remainingStack == null) {
				amountOfChemical--;
				return true;
			} else {
				tubeOutTransactor.add(testTube, true);
			}
		}
		return false;
	}

	@Override
	public ItemStack takeOutput() {
		ItemStack outputStack = getStackInSlot(kStartOutput);
		if(outputStack != null || (outputStack == null && moveStorageToOutput())) {
			return decrStackSize(kStartOutput, 1);
		}
		return null;
	}

	@Override
	public ItemStack putOutput(ItemStack output) {
		return outputTransactor.add(output, true);
	}

	@Override
	public ItemStack takeInput() {
		return inputTransactor.removeItem(true);
	}

	@Override
	public ItemStack putInput(ItemStack input) {
		return inputTransactor.add(input, true);
	}

	private int getChemicalSpaceAvailable() {
		return this.maxChemicalAmount - this.amountOfChemical;
	}
	
	private int getTestTubeSpaceAvailable() {
		return this.maxChemicalAmount - this.amountOfTestTube;
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
	public String getMachineState() {
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("amountOfChemical", amountOfChemical);
		nbt.setInteger("amountOfTestTube", amountOfTestTube);
		NBTTagList inventoryTagList = MinechemHelper.writeItemStackArrayToTagList(inventory);
		nbt.setTag("inventory", inventoryTagList);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.amountOfChemical = nbt.getInteger("amountOfChemical");
		this.amountOfTestTube = nbt.getInteger("amountOfTestTube");
		NBTTagList inventoryTagList = nbt.getTagList("inventory");
		this.inventory = MinechemHelper.readTagListToItemStackArray(inventoryTagList, 
				new ItemStack[getSizeInventory()]);
	}

}
