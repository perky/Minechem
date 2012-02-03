package net.minecraft.src;

import java.util.LinkedList;

import net.minecraft.minechem.Molecule;
import net.minecraft.minechem.Util;
import net.minecraft.src.buildcraft.api.EntityPassiveItem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.transport.IPipeTransportItemsHook;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogic;
import net.minecraft.src.buildcraft.transport.PipeLogicStone;
import net.minecraft.src.buildcraft.transport.PipeTransport;
import net.minecraft.src.buildcraft.transport.PipeTransportItems;

public class TileMinechemPipe extends Pipe implements IPipeTransportItemsHook {
	
	int nextTexture = 1 * 16 + 5;
	
	public TileMinechemPipe(int itemID) {
		super(new PipeTransportItems(), new PipeLogicMinechem(), itemID);
	}
	
	@Override
	public int getMainBlockTexture() {
		return nextTexture;
	}
	
	@Override
	public void prepareTextureFor(Orientations connection) {
		if (connection == Orientations.Unknown) {
			nextTexture = 1 * 16 + 5;
		} else {
			nextTexture = BuildCraftTransport.diamondTextures[connection.ordinal()];
		}
	}

	@Override
	public LinkedList<Orientations> filterPossibleMovements(
			LinkedList<Orientations> possibleOrientations, Position pos,
			EntityPassiveItem item) {
		
		LinkedList<Orientations> defaultOrientations = (LinkedList<Orientations>)possibleOrientations.clone();
		
		ItemStack itemstack = item.item;
		if(Util.isTube(itemstack)) {
			Molecule m = Molecule.moleculeByItemStack(itemstack);
			if( m.name != null ) {
				possibleOrientations = ((PipeLogicMinechem)logic).filterPossibleMovementsWithFormula( m.name, possibleOrientations );
			}
		}
		
		if(possibleOrientations.size() == 0) {
			return defaultOrientations;
		}

		return possibleOrientations;
	}

	@Override
	public void entityEntered(EntityPassiveItem item, Orientations orientation) {
		
	}

	@Override
	public void readjustSpeed(EntityPassiveItem item) {
		
		
	}
	
	

}
