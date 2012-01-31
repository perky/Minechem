package net.minecraft.minechem;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.World;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.ic2.api.EnergyNet;

public class BlockMinechem extends BlockContainer implements ITextureProvider {

	public BlockMinechem(int i) {
		super(i, Material.iron);
	}
	
	@Override
	public boolean blockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer) {
		if(world.multiplayerWorld)
			return true;
		
		int md = world.getBlockMetadata(i, j, k);
		TileEntity tileentity = world.getBlockTileEntity(i, j, k);
		if(tileentity != null)
		{
			if(md == 0)
				ModLoader.OpenGUI(entityplayer, new GuiElectrolysis(entityplayer, tileentity));
			if(md == 1)
				ModLoader.OpenGUI(entityplayer, new GuiFusion(entityplayer, tileentity));
			if(md == 2)
				ModLoader.OpenGUI(entityplayer, new GuiBonder(entityplayer, tileentity));
			if(md == 3)
				ModLoader.OpenGUI(entityplayer, new GuiUnbonder(entityplayer, tileentity));
			if(md == 4)
				ModLoader.OpenGUI(entityplayer, new GuiFission(entityplayer, tileentity));
			if(md == 5)
				ModLoader.OpenGUI(entityplayer, new GuiMinechemCrafting(entityplayer, tileentity));
		}
		
		return true;
	}
	
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
        switch (j) {
        case ItemMinechem.electrolysis:
        	return i == 1 ? 7 : 0;
        case ItemMinechem.fusion:
        	return i == 1 ? 7 : 4;
        case ItemMinechem.bonder:
            return i == 1 ? 7 : 1;
        case ItemMinechem.unbonder:
        	return i == 1 ? 7 : 2;
        case ItemMinechem.fission:
        	return i == 1 ? 7 : 3;
        case ItemMinechem.crafting:
        	return i == 1 ? 6 : 5;
        default:
        	return i == 1 ? 7 : 0;
        }
	}
	
	public TileEntityChest findAdjacentChest(World world, int i, int j, int k) {
		TileEntityChest chest = null;
		if(chest == null) 
			chest = getAdjacentChest(world, i+1, j, k);
		if(chest == null) 
			chest = getAdjacentChest(world, i-1, j, k);
		if(chest == null) 
			chest = getAdjacentChest(world, i, j, k+1);
		if(chest == null) 
			chest = getAdjacentChest(world, i, j, k-1);
		if(chest == null) 
			chest = getAdjacentChest(world, i, j+1, k);
		return chest;
	}
	
	public TileEntityChest getAdjacentChest(World world, int i, int j, int k) {
		if(world.getBlockId(i, j, k) == Block.chest.blockID) {
			TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
			if(tileEntity != null)
				return (TileEntityChest)tileEntity;
		}
		
		return null;
	}
	
	@Override
	public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		TileEntityMinechemMachine minechemMachine = (TileEntityMinechemMachine)iblockaccess.getBlockTileEntity(i, j, k);
		return minechemMachine.isPowering;
	}


	@Override
	public TileEntity getBlockEntity() {
		return null;
	}

	@Override
	public TileEntity getBlockEntity(int md) {
		World world = ModLoader.getMinecraftInstance().theWorld;
		if(md == 0)
			return new TileEntityElectrolysis();
		if(md == 1) {
			TileEntity tileEntity = new TileEntityFusion();
			if(mod_Minechem.requireIC2Power)
				EnergyNet.getForWorld(world).addTileEntity(tileEntity);
			return tileEntity;
		}
		if(md == 2) {
			TileEntity tileEntity = new TileEntityBonder();
			if(mod_Minechem.requireIC2Power)
				EnergyNet.getForWorld(world).addTileEntity(tileEntity);
			return tileEntity;
		}
		if(md == 3) {
			TileEntity tileEntity = new TileEntityUnbonder();
			if(mod_Minechem.requireIC2Power)
				EnergyNet.getForWorld(world).addTileEntity(tileEntity);
			return tileEntity;
		}
		if(md == 4) {
			TileEntity tileEntity = new TileEntityFission();
			if(mod_Minechem.requireIC2Power)
				EnergyNet.getForWorld(world).addTileEntity(tileEntity);
			return tileEntity;
		}
		if(md == 5) {
			TileEntity tileEntity = new TileEntityMinechemCrafting();
			if(mod_Minechem.requireIC2Power)
				EnergyNet.getForWorld(world).addTileEntity(tileEntity);
			return tileEntity;
		}
		
		return null;
	}

	@Override
	public String getTextureFile() {
		return "/minechem/blocktextures.png";
	}

}
