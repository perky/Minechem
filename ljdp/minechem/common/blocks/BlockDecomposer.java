package ljdp.minechem.common.blocks;

import java.util.ArrayList;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDecomposer extends BlockMinechemContainer {
	
	IPowerProvider powerProvider;
	
	public BlockDecomposer(int id) {
		super(id, Material.iron);
		setBlockName("blockChemicalDecomposer");
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
		return new TileEntityDecomposer();
	}
	
	@Override
	public ArrayList<ItemStack> addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks) {
		TileEntityDecomposer decomposer = (TileEntityDecomposer)tileEntity;
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
		return par1 == 1 ? 7 : 2;
	}
	

}
