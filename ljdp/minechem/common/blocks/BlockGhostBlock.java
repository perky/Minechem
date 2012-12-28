package ljdp.minechem.common.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ljdp.minechem.client.RenderBlockGhostBlock;
import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.ModMinechem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGhostBlock extends Block {
	
	static Random random = new Random();
	
	public HashMap<Integer,ItemStack> blockLookup = new HashMap();
	
	public BlockGhostBlock(int id) {
		super(id, 16, MinechemBlocks.materialGhost);
		setBlockName("block.minechemGhostBlock");
		setCreativeTab(ModMinechem.minechemTab);
		setLightValue(0.5F);
        this.setRequiresSelfNotify();
        blockLookup.put(1, new ItemStack(MinechemBlocks.fusion, 1, 0));
        blockLookup.put(2, new ItemStack(Block.blockGold, 1, 0));
        blockLookup.put(3, new ItemStack(MinechemBlocks.fusion, 1, 2));
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float par7,
			float par8, float par9) {
		int metadata = world.getBlockMetadata(x, y, z);
		ItemStack itemstack = blockLookup.get(metadata);
		if(playerIsHoldingBlock(entityPlayer, itemstack)) {
			world.setBlockAndMetadataWithNotify(x, y, z, itemstack.getItem().shiftedIndex, itemstack.getItemDamage());
			return true;
		} else
			return false;
	}
	
	private boolean playerIsHoldingBlock(EntityPlayer entityPlayer, ItemStack itemstack) {
		ItemStack helditem = entityPlayer.inventory.getCurrentItem();
		return helditem != null && helditem.itemID == itemstack.itemID
				&& helditem.getItemDamage() == itemstack.getItemDamage();
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		//System.out.println("update");
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
	
	/**
     * Returns whether this block is collideable based on the arguments passed in Args: blockMetaData, unknownFlag
     */
    public boolean canCollideCheck(int par1, boolean par2)
    {
        return true;
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
		ItemStack itemstack = blockLookup.get(metadata);
		if(itemstack != null)
			return itemstack.getIconIndex();
		else
			return 1;
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.TERRAIN_ALPHA_PNG;
	}
	
	@Override
	public int damageDropped(int par1) {
		return par1;
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
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
    }
    
    @Override
    public int idDropped(int par1, Random par2Random, int par3) {
    	return 0;
    }
}
