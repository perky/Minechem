package ljdp.minechem.common.tileentity;

import ljdp.minechem.api.recipe.DecomposerRecipe;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.inventory.BoundedInventory;
import ljdp.minechem.common.inventory.Transactor;
import ljdp.minechem.common.recipe.DecomposerRecipeHandler;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.computercraft.IMinechemMachinePeripheral;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityMicroscope extends MinechemTileEntity implements IInventory, IMinechemMachinePeripheral {
	
	public boolean isShaped = true;
	
	private final BoundedInventory inputInvetory = new BoundedInventory(this, 0, 1);
	private final BoundedInventory journalInventory = new BoundedInventory(this, 1, 2);
	private Transactor inputTransactor = new Transactor(inputInvetory, 1);
	private Transactor journalTransactor = new Transactor(journalInventory, 1);
	
	public TileEntityMicroscope() {
		inventory = new ItemStack[getSizeInventory()];
	}
	
	public void onInspectItemStack(ItemStack itemstack) {
		SynthesisRecipe synthesisRecipe = SynthesisRecipeHandler.instance.getRecipeFromOutput(itemstack);
		DecomposerRecipe decomposerRecipe = DecomposerRecipeHandler.instance.getRecipe(itemstack);
		if(inventory[1] != null && (synthesisRecipe != null || decomposerRecipe != null))
		{
			MinechemItems.journal.addItemStackToJournal(itemstack, inventory[1], worldObj);
		}
	}

	@Override
	public int getSizeInventory() {
		return 11;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		ItemStack itemstack = inventory[slot];
		return itemstack;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot >= 0 && slot < inventory.length)
        {
            ItemStack itemstack = inventory[slot];
            inventory[slot] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {
		inventory[slot] = itemStack;
		if(slot == 0 && itemStack != null && !worldObj.isRemote)
			onInspectItemStack(itemStack);
		if(slot == 1 && itemStack != null && inventory[0] != null && !worldObj.isRemote)
			onInspectItemStack(inventory[0]);
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

	@Override
	void sendUpdatePacket() {

	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		ItemStack inpectingStack = inventory[0];
		if(inpectingStack != null) {
			NBTTagCompound inspectingStackTag = inpectingStack.writeToNBT(new NBTTagCompound());
			nbtTagCompound.setTag("inspectingStack", inspectingStackTag);
		}
		ItemStack journal = inventory[1];
		if(journal != null) {
			NBTTagCompound journalTag = journal.writeToNBT(new NBTTagCompound());
			nbtTagCompound.setTag("journal", journalTag);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		NBTTagCompound inspectingStackTag = nbtTagCompound.getCompoundTag("inspectingStack");
		NBTTagCompound journalTag = nbtTagCompound.getCompoundTag("journal");
		ItemStack inspectingStack = ItemStack.loadItemStackFromNBT(inspectingStackTag);
		ItemStack journalStack = ItemStack.loadItemStackFromNBT(journalTag);
		inventory[0] = inspectingStack;
		inventory[1] = journalStack;
	}

	@Override
	public ItemStack takeEmptyTestTube() {
		return null;
	}

	@Override
	public int putEmptyTestTube(ItemStack testTube) {
		return 0;
	}

	@Override
	public ItemStack takeOutput() {
		return null;
	}

	@Override
	public int putOutput(ItemStack output) {
		return 0;
	}

	@Override
	public ItemStack takeInput() {
		return inputTransactor.removeItem(true);
	}

	@Override
	public int putInput(ItemStack input) {
		return inputTransactor.add(input, true);
	}

	@Override
	public ItemStack takeFusionStar() {
		return null;
	}

	@Override
	public int putFusionStar(ItemStack fusionStar) {
		return 0;
	}

	@Override
	public ItemStack takeJournal() {
		return journalTransactor.removeItem(true);
	}

	@Override
	public int putJournal(ItemStack journal) {
		return journalTransactor.add(journal, true);
	}

	@Override
	public String getMachineState() {
		return null;
	}
}
