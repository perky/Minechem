package ljdp.minechem.common;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSynthesis extends BlockMinechemContainer {

	public BlockSynthesis(int par1) {
		super(par1, Material.iron);
		setBlockName("minechem.blockSynthesis");
		setCreativeTab(ModMinechem.minechemTab);
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
		return new TileEntitySynthesis();
	}
	
	@Override
	public ArrayList<ItemStack> addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks) {
		TileEntitySynthesis decomposer = (TileEntitySynthesis)tileEntity;
		for(int slot = 0; slot < decomposer.getSizeInventory(); slot++) {
			ItemStack itemstack = decomposer.getStackInSlot(slot);
			if(itemstack != null) {
				itemStacks.add(itemstack);
			}
		}
		return itemStacks;
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.BLOCKS_PNG;
	}
	
	@Override
	public int getBlockTextureFromSide(int par1) {
		return par1 == 1 ? 6 : 1;
	}

}
