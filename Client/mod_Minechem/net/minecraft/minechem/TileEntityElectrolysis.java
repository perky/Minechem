package net.minecraft.minechem;

import java.util.Map;

import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.ic2.api.IEnergySink;

public class TileEntityElectrolysis extends TileEntityMinechemMachine {
	
	private boolean isProcessComplete;
	public static int timerDuration = 300;
	
	public TileEntityElectrolysis() {
		inventoryStack = new ItemStack[5];
		isProcessComplete = false;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(isPowering)
			isPowering = false;
		if(timer > 0)
		{
			timer--;
			if(timer == 0)
			{
				electrolysisComplete();
				onInventoryChanged();
				isPowering = true;
			} 
			else if(!canElectrolyse())
			{
				timer = 0;
				onInventoryChanged();
			}
		}
		else if(canElectrolyse())
		{
			timer = timerDuration;
			isProcessComplete = false;
		} else {
			takeEmptyTubeFromChest(3);
			takeEmptyTubeFromChest(4);
		}
	}
	
	private boolean canElectrolyse()
	{
		int inputExists = 0;
		int testTubeExists = 0;
		
		Map<ItemStack, Molecule[]> recipeList = mod_Minechem.electrolysisRecipes;
		for (Map.Entry<ItemStack, Molecule[]> entry : recipeList.entrySet())
		{
			inputExists = 0;
			for(int i = 0; i < 3; i++)
			{
				ItemStack stack = getStackInSlot(i);
				if(stack != null && stack.itemID == entry.getKey().itemID)
				{
					inputExists++;
				}
			}
			if(inputExists >= 3)
				break;
		}
		
		for(int i = 3; i < 5; i++)
		{
			if(isEmptyTube(inventoryStack[i]))
				testTubeExists++;
		}
		
		return (inputExists == 3 && testTubeExists > 0);
	}
	
	private void electrolysisComplete()
	{
		if(!canElectrolyse())
			return;
		
		isProcessComplete = true;
		
		Molecule[] outputs = new Molecule[2];
		Map<ItemStack, Molecule[]> recipeList = mod_Minechem.electrolysisRecipes;
		boolean quitloop = false;
		for (Map.Entry<ItemStack, Molecule[]> entry : recipeList.entrySet())
		{
			for(int i = 0; i < 3; i++)
			{
				if(inventoryStack[i] != null && inventoryStack[i].itemID == entry.getKey().itemID)
				{
					outputs = entry.getValue();
					quitloop = true;
					break;
				}
			}
			if(quitloop)
				break;
		}
		
		if(inventoryStack[3] != null && outputs[0] != null)
			inventoryStack[3] = outputs[0].stack.copy();
		if(inventoryStack[4] != null && outputs[1] != null)
			inventoryStack[4] = outputs[1].stack.copy();
		
		for(int i = 0; i < 3; i++)
		{
			ItemStack is = inventoryStack[i];
			if(is != null)
			{
				if(is.getItem().hasContainerItem())
					inventoryStack[i] = new ItemStack(is.getItem().getContainerItem(), 1, is.getItemDamage());
				else
					inventoryStack[i] = null;
			}
		}
		
		dumpSlotToChest( 3 );
		dumpSlotToChest( 4 );
	}
	
	public boolean addItem (ItemStack stack, boolean doAdd, Orientations from){
		if(from == Orientations.YPos) {
			if(isEmptyTube(stack)) {
				if(tryAddingStack(stack, 3, doAdd)) return true;
				if(tryAddingStack(stack, 4, doAdd)) return true;
			} else {
				for(int i = 0; i <= 2; i++) {
					if(tryAddingStack(stack, i, doAdd)) return true;
				}
			}
		}
		
		return false;
	}
	
	public ItemStack extractItem(boolean doRemove, Orientations from){
		if(from != Orientations.YPos) {
			for(int i = 3; i <= 4; i++) {
				if(!isEmptyTube(inventoryStack[i]) && inventoryStack[i] != null) {
					if(doRemove) {
						return decrStackSize(i, 1);
					} else {
						return inventoryStack[i];
					}
				}
			}
		}
		
		return null;
	}
	
	@Override
	public String getInvName() {
		return "Electrolysis Kit";
	}
	
	@Override
	public int getStartInventorySide(int side) {
		return side == 1 ? 0 : 3;
	}

	@Override
	public int getSizeInventorySide(int side) {
		return side == 1 ? 3 : 2;
	}

}
