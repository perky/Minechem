package net.minecraft.minechem;

import java.util.Random;

import javax.jws.Oneway;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockElectrolysis extends BlockContainer {

	public BlockElectrolysis(int i) {
		super(i, Material.iron);
	}
	
	@Override
	public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) 
	{
		if(world.multiplayerWorld)
			return true;
		
		TileEntityElectrolysis tileentity = (TileEntityElectrolysis)world.getBlockTileEntity(i, j, k);
		if(tileentity != null)
		{
			GuiScreen guiscreen = new GuiElectrolysis(entityplayer, tileentity);
			ModLoader.OpenGUI(entityplayer, guiscreen);
		}
		
		return true;
	}
	
	@Override
	public TileEntity getBlockEntity() {
		return null;
	}

	@Override
	public TileEntity getBlockEntity(int md) {
		if(md == 0)
			return new TileEntityElectrolysis();
		if(md == 1)
			return null;
		return new TileEntityElectrolysis();
	}

}
