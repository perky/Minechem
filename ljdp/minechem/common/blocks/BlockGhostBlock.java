package ljdp.minechem.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.ModMinechem;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGhostBlock extends Block {

	public BlockGhostBlock(int id) {
		super(id, 16, MinechemBlocks.materialGas);
		setBlockName("blockMinechemGhostBlock");
		setCreativeTab(ModMinechem.minechemTab);
		//setTickRandomly(true);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float par7,
			float par8, float par9) {
		int metadata = world.getBlockMetadata(x, y, z);
		int blockid  = metadata + 1;
		if(playerIsHoldingBlock(entityPlayer, Block.blockSteel)) {
			world.setBlockAndMetadataWithNotify(x, y, z, Block.blockSteel.blockID, 0);
			return true;
		} else
			return false;
	}
	
	private boolean playerIsHoldingBlock(EntityPlayer entityPlayer, Block block) {
		return entityPlayer.inventory.getCurrentItem().itemID == block.blockID;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		/*
		ArrayList<Vec3> airBlocks = getAdjacentAirBlocks(world, x, y, z);
		while(airBlocks.size() > 0) {
			int metadata   = world.getBlockMetadata(x, y, z);
			int randomSlot = random.nextInt(airBlocks.size());
			Vec3 expansionPos = airBlocks.get(randomSlot);
			world.setBlockAndMetadata(
					(int) expansionPos.xCoord, 
					(int) expansionPos.yCoord, 
					(int) expansionPos.zCoord, 
					this.blockID, metadata);
			airBlocks.remove(randomSlot);
			if(random.nextFloat() < 0.1F)
				break;
		}
		*/
	}
	
	private ArrayList<Vec3> getAdjacentAirBlocks(World world, int x, int y, int z) {
		ArrayList<Vec3> airBlocks = new ArrayList();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				for(int k = -1; k <= 1; k++) {
					if(world.getBlockId(x+i, y+j, z+k) == 0)
						airBlocks.add(Vec3.createVectorHelper(x+i, y+j, z+k));
				}
			}
		}
		return airBlocks;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
		return Block.blockSteel.blockIndexInTexture;
		//return Block.blocksList[metadata + 1].getBlockTextureFromSideAndMetadata(side, metadata);
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.TERRAIN_ALPHA_PNG;
	}
	
	@Override
	public int damageDropped(int par1) {
		return par1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int blockid, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++) {
			par3List.add(new ItemStack(blockid, 1, i));
		}
	}
	/**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		//Todo
	}
	
	/**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
    
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
	@Override
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
		int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
        return var6 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return true;
    }
    
    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
    	return 1;
    }
    
    @Override
    public int getRenderType() {
    	return 0;
    }
}
