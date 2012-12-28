package ljdp.minechem.common.blocks;

import java.util.ArrayList;

import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.blueprint.MinechemBlueprint;
import ljdp.minechem.common.tileentity.TileEntityBlueprintProjector;
import ljdp.minechem.common.utils.MinechemHelper;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
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
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer entityPlayer, int side, float par7,
			float par8, float par9)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityBlueprintProjector) {
			TileEntityBlueprintProjector projector = (TileEntityBlueprintProjector) tileEntity;
			if(projector.hasBlueprint()) {
				ItemStack blueprintItem = takeBlueprintFromProjector(projector);
				ejectItem(blueprintItem, world, x, y, z);
			} else if(playerIsHoldingBlueprint(entityPlayer)) {
				ItemStack blueprintItem = getBlueprintFromPlayer(entityPlayer);
				putBlueprintInsideProjector(blueprintItem, (TileEntityBlueprintProjector)tileEntity);
			}
		}
		return true;
	}

	private ItemStack takeBlueprintFromProjector(TileEntityBlueprintProjector projector) {
		MinechemBlueprint blueprint = projector.takeBlueprint();
		ItemStack blueprintItem = MinechemItems.blueprint.createItemStackFromBlueprint(blueprint);
		return blueprintItem;
	}

	private void ejectItem(ItemStack itemstack, World world, int x, int y, int z) {
		if(!world.isRemote)
			MinechemHelper.ejectItemStackIntoWorld(itemstack, world, x, y, z);
	}

	private void putBlueprintInsideProjector(ItemStack blueprintItem, TileEntityBlueprintProjector projector) {
		MinechemBlueprint blueprint = MinechemItems.blueprint.getBlueprint(blueprintItem);
		projector.setBlueprint(blueprint);
	}

	private ItemStack getBlueprintFromPlayer(EntityPlayer entityPlayer) {
		return entityPlayer.inventory.getCurrentItem();
	}

	private boolean playerIsHoldingBlueprint(EntityPlayer entityPlayer) {
		ItemStack currentItem = entityPlayer.inventory.getCurrentItem();
		return currentItem != null && currentItem.itemID == MinechemItems.blueprint.shiftedIndex; 
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityBlueprintProjector();
	}
	
	@Override
	public ArrayList<ItemStack> addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks) {
		if(tileEntity instanceof TileEntityBlueprintProjector) {
			TileEntityBlueprintProjector projector = (TileEntityBlueprintProjector) tileEntity;
			if(projector.hasBlueprint())
				itemStacks.add(takeBlueprintFromProjector(projector));
		}
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
