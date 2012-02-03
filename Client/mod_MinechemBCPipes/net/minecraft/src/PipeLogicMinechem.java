package net.minecraft.src;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.minechem.Molecule;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.transport.BlockGenericPipe;
import net.minecraft.src.buildcraft.transport.PipeLogic;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;
import net.minecraft.src.buildcraft.transport.TransportProxy;

public class PipeLogicMinechem extends PipeLogicStone {
	
	String[] formulas;
	private static final String greaterThanPattern = "([A-z]*)>([0-9]*)";
	private static final String lesserThanPattern = "([A-z]*)<([0-9]*)";
	private static final String singleElementPattern = "^([A-z]*)([0-9]*)$";
	
	public PipeLogicMinechem() {
		formulas = new String[6];
		for(int i = 0; i < 6; i++) {
			formulas[i] = "";
		}
	}
	
	public LinkedList<Orientations> filterPossibleMovementsWithFormula(
			String suppliedFormula, 
			LinkedList<Orientations> possibleOrientations) 
	{
		for(int i = 0; i < 6; i++) {
			String requestedFormula = formulas[i];
			
			if(!requestedFormula.equals("")){
				if(requestedFormula.substring(0, 1).equals("!")) {
					requestedFormula = requestedFormula.replaceFirst("!", "");
				} else {
					requestedFormula = requestedFormula.replaceAll("\\*", "[0-9]*");
				}
			}
			
			if(requestedFormula.equals("")) {
				//Do nothing.
				continue;
			}
			else if(requestedFormula.matches(greaterThanPattern) && suppliedFormula.matches(singleElementPattern)) {
				Matcher match1 = Pattern.compile(greaterThanPattern).matcher(requestedFormula);
				Matcher match2 = Pattern.compile(singleElementPattern).matcher(suppliedFormula);
				if(match1.find() && match2.find()) {
					String requestedElement = match1.group(1);
					int requestedAtoms = Integer.valueOf(match1.group(2));
					
					String suppliedElement = match2.group(1);
					int suppliedAtoms = Integer.valueOf(match2.group(2));
					
					if( suppliedElement.equals(requestedElement) && suppliedAtoms > requestedAtoms) {
						possibleOrientations.clear();
						possibleOrientations.add( Orientations.dirs()[i] );
					} else {
						possibleOrientations.remove( Orientations.dirs()[i] );
					}
				}
			}
			else if(requestedFormula.matches(lesserThanPattern) && suppliedFormula.matches(singleElementPattern)) {
				Matcher match1 = Pattern.compile(lesserThanPattern).matcher(requestedFormula);
				Matcher match2 = Pattern.compile(singleElementPattern).matcher(suppliedFormula);
				if(match1.find() && match2.find()) {
					String requestedElement = match1.group(1);
					int requestedAtoms = Integer.valueOf(match1.group(2));
					
					String suppliedElement = match2.group(1);
					int suppliedAtoms = Integer.valueOf(match2.group(2));
					
					if( suppliedElement.equals(requestedElement) && suppliedAtoms < requestedAtoms) {
						possibleOrientations.clear();
						possibleOrientations.add( Orientations.dirs()[i] );
					} else {
						possibleOrientations.remove( Orientations.dirs()[i] );
					}
				}
			} 
			else if(suppliedFormula.matches(requestedFormula)){
				possibleOrientations.clear();
				possibleOrientations.add( Orientations.dirs()[i] );
			}
		}
		
		
		return possibleOrientations;
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
