package ljdp.minechem.common.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockMinechemContainer extends BlockContainer {
	
	private Random random = new Random();
	
	protected BlockMinechemContainer(int par1, Material par2Material) {
		super(par1, par2Material);
		setHardness(10F);
		setResistance(1000F);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}
	
	public abstract ArrayList<ItemStack> addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks);
	
	@Override
	public void breakBlock(World world, int x, int y, int z,
			int par5, int par6) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity != null) {
			ArrayList<ItemStack> droppedStacks = new ArrayList();
			droppedStacks = addStacksDroppedOnBlockBreak(tileEntity, droppedStacks);
			for(ItemStack itemstack : droppedStacks) {
				float randomX = this.random.nextFloat() * 0.8F + 0.1F;
                float randomY = this.random.nextFloat() * 0.8F + 0.1F;
                float randomZ = this.random.nextFloat() * 0.8F + 0.1F;
                while(itemstack.stackSize > 0) {
                	int randomN = this.random.nextInt(21) + 10;
                	if(randomN > itemstack.stackSize)
                		randomN = itemstack.stackSize;
                	itemstack.stackSize -= randomN;
                	EntityItem droppedEntity = new EntityItem(world, 
                			(double)((float)x + randomX), 
                			(double)((float)y + randomY), 
                			(double)((float)z + randomZ), 
                			new ItemStack(itemstack.itemID, randomN, itemstack.getItemDamage())
                	);
                	if(itemstack.hasTagCompound())
                		droppedEntity.item.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                	float amplitude = 0.05F;
                	float yAmplitude = amplitude + 0.2F;
                	droppedEntity.motionX = (double)((float)this.random.nextGaussian() * amplitude);
                	droppedEntity.motionY = (double)((float)this.random.nextGaussian() * yAmplitude);
                	droppedEntity.motionZ = (double)((float)this.random.nextGaussian() * amplitude);
                    world.spawnEntityInWorld(droppedEntity);
                }
			}
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

}
