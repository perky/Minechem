package ljdp.minechem.common;

import java.util.ArrayList;

import net.minecraft.src.BlockContainer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockMicroscope extends BlockMinechemContainer {

	public BlockMicroscope(int par1) {
		super(par1, Material.iron);
		setCreativeTab(ModMinechem.minechemTab);
		setBlockName("minechem.blockMicroscope");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer entityPlayer, int par6, float par7,
			float par8, float par9) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity == null || entityPlayer.isSneaking())
			return false;
		entityPlayer.openGui(ModMinechem.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityMicroscope();
	}
	
	@Override
	public void addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks) {
		ItemStack inputStack = ((TileEntityMicroscope)tileEntity).getStackInSlot(0);
		if(inputStack != null)
			itemStacks.add(inputStack);
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.BLOCKS_PNG;
	}
	
	@Override
	public int getBlockTextureFromSide(int par1) {
		return par1 == 1 ? 14 : 0;
	}

}
