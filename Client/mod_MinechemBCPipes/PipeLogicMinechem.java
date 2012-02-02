package net.minecraft.src;

import net.minecraft.src.buildcraft.transport.BlockGenericPipe;
import net.minecraft.src.buildcraft.transport.PipeLogic;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;
import net.minecraft.src.buildcraft.transport.TransportProxy;

public class PipeLogicMinechem extends PipeLogicStone {
	
	String[] formulas;
	
	public PipeLogicMinechem() {
		formulas = new String[6];
		for(int i = 0; i < 6; i++) {
			formulas[i] = "";
		}
	}
	
	public int findFormulaMatch(String formula) {
		for(int i = 0; i < 6; i++) {
			String formula1 = formulas[i];
			if(formula1.equals(formula))
				return i;
		}
		
		return -1;
	}
	
	public void onFormulasChanged(String[] formulas) {
		this.formulas = formulas;
	}
	
	@Override
	public boolean blockActivated(EntityPlayer entityplayer) {
		if (entityplayer.getCurrentEquippedItem() != null
				&& entityplayer.getCurrentEquippedItem().itemID < Block.blocksList.length) {
			
			if (Block.blocksList[entityplayer.getCurrentEquippedItem().itemID] instanceof BlockGenericPipe) {
				return false;
			}
		}
		
		GuiMinechemPipe gui = new GuiMinechemPipe( this, formulas );
		ModLoader.OpenGUI(entityplayer, gui);
		return true;	
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		//super.writeToNBT(nbttagcompound);
		NBTTagList tagList = new NBTTagList();
		for(int i = 0; i < 6; i++) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setString("formula", formulas[i]);
			tagList.setTag(nbttagcompound1);
		}
		nbttagcompound.setTag("formulas", tagList);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		//super.readFromNBT(nbttagcompound);
		NBTTagList tagList = nbttagcompound.getTagList("formulas");
		for(int i = 0; i < 6; i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)tagList.tagAt(i);
			formulas[i] = nbttagcompound1.getString("formula");
		}
	}

	
}
