package ljdp.minechem.common.blocks;

import java.util.ArrayList;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityFusion;
import ljdp.minechem.common.tileentity.TileEntityProxy;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFusion extends BlockMinechemContainer {

	public BlockFusion(int id) {
		super(id, Material.iron);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer entityPlayer, int side, float par7, float par8, float par9)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);;
		if(tileEntity == null || entityPlayer.isSneaking())
			return false;
		entityPlayer.openGui(ModMinechem.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public ArrayList<ItemStack> addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks) {
		return itemStacks;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		if(metadata == 1) 
			return new TileEntityFusion();
		else
			return new TileEntityProxy();
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.BLOCKS_PNG;
	}
	
	@Override
	public int getBlockTextureFromSide(int par1) {
		return par1 == 1 ? 7 : 2;
	}

}
