package ljdp.minechem.common.blocks;

import java.util.ArrayList;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityBlueprintProjector;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockBlueprintProjector extends BlockMinechemContainer {

	public BlockBlueprintProjector(int id) {
		super(id, Material.iron);
		setBlockName("minechem.blockBlueprintProjector");
		setCreativeTab(ModMinechem.minechemTab);
		setLightValue(0.7F);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLiving entityLiving) {
		super.onBlockPlacedBy(world, x, y, z, entityLiving);
		int facing = MathHelper.floor_double((double)(entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		world.setBlockMetadata(x, y, z, facing);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBlueprintProjector();
	}
	
	@Override
	public ArrayList<ItemStack> addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks) {
		return itemStacks;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return ModMinechem.proxy.CUSTOM_RENDER_ID;
	}
	
	@Override
    public boolean isOpaqueCube()
    {
		return false;
    }
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityBlueprintProjector) {
			((TileEntityBlueprintProjector)tileEntity).destroyProjection();
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

}
