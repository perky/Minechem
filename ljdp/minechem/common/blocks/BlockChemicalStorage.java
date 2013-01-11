package ljdp.minechem.common.blocks;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityChemicalStorage;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChemicalStorage extends BlockChest {

	public BlockChemicalStorage(int id) {
		super(id);
		setBlockName("minechem.blockChemicalStorage");
		setCreativeTab(ModMinechem.minechemTab);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer entityPlayer, int side, float par7,
			float par8, float par9)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(world.isRemote)
			return true;
		if(tileEntity instanceof TileEntityChemicalStorage) {
			entityPlayer.openGui(ModMinechem.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4) {
		par1World.setBlockTileEntity(par2, par3, par4, 
				this.createTileEntity(par1World, par1World.getBlockMetadata(par2, par3, par4))
		);
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityChemicalStorage();
	}
	
	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World par1World) {
		return new TileEntityChemicalStorage();
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityChemicalStorage();
	}

}
