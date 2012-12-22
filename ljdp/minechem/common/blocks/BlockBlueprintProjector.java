package ljdp.minechem.common.blocks;

import java.util.ArrayList;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityBlueprintProjector;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBlueprintProjector extends BlockMinechemContainer {

	public BlockBlueprintProjector(int id) {
		super(id, Material.iron);
		setBlockName("minechem.blockBlueprintProjector");
		setCreativeTab(ModMinechem.minechemTab);
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

}
