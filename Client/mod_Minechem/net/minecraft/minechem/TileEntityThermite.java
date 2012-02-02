package net.minecraft.minechem;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;

public class TileEntityThermite extends TileEntity {
	
	private int nextUpdate;
	private int tickCounter;
	private int dropCount;
	private int initialY;
	private boolean isActive;
	Random random;
	
	public static int[] blacklist = new int[]{
		Block.bedrock.blockID,
		Block.obsidian.blockID,
		Block.oreDiamond.blockID,
	};
	
	public TileEntityThermite() {
		random = new Random();
		nextUpdate = -1;
		isActive = false;
		dropCount = 0;
	}
	
	@Override
	public void validate() {
		super.validate();
		initialY = yCoord;
	}

	public void setActive( boolean state ) {
		isActive = state;
	}
	
	public void setDropCount( int count ) {
		dropCount = count;
		if(dropCount > random.nextInt(40) + 30) {
			worldObj.setBlock(xCoord, yCoord, zCoord, 0);
		}
	}
	
	public void setInitialY(int y) {
		initialY = y;
	}

	@Override
	public void updateEntity() {
		if(nextUpdate == -1) {
			if(worldObj.getBlockId(xCoord, yCoord-1, zCoord) == 0)
				nextUpdate = 1;
			else
				nextUpdate = random.nextInt(20*3);
			tickCounter = 0;
		} else if(isActive) {
			tickCounter++;
			if(tickCounter >= nextUpdate) {
				doThermiteAction();
				nextUpdate = -1;
			}
		}
	}
	
	private void doThermiteAction() {
		if(yCoord > 1) {
			int newYCoord = yCoord - 1;
			int blockIDBelow = worldObj.getBlockId(xCoord, newYCoord, zCoord);
			
			boolean isBlacklisted = false;
			for(int blacklistID : blacklist) {
				if(blockIDBelow == blacklistID) {
					isBlacklisted = true;
					break;
				}
			}
			
			if(!isBlacklisted) {
				int dropAdd = 1;
				if(blockIDBelow != 0) {
					Block.blocksList[blockIDBelow].dropBlockAsItemWithChance(worldObj, xCoord, initialY, zCoord, 
						worldObj.getBlockMetadata(xCoord, newYCoord, zCoord), 0.6F, 0);
				} else {
					dropAdd = 0;
				}
				worldObj.setBlockAndMetadataWithNotify(xCoord, newYCoord, zCoord, 
						mod_Minechem.blockIDMinechem, ItemMinechem.thermite);
				worldObj.setBlock(xCoord, yCoord, zCoord, Block.fire.blockID);
				TileEntity tileEntity = worldObj.getBlockTileEntity(xCoord, newYCoord, zCoord);
				if(tileEntity instanceof TileEntityThermite) {
					TileEntityThermite thermite = (TileEntityThermite)tileEntity;
					thermite.setActive(true);
					thermite.setDropCount(dropCount+dropAdd);
					thermite.setInitialY(initialY);
				}
			} else {
				worldObj.setBlock(xCoord, yCoord, zCoord, 0);
			}
			
		} else {
			worldObj.setBlock(xCoord, yCoord, zCoord, 0);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		isActive = nbttagcompound.getBoolean("isActive");
		dropCount = nbttagcompound.getInteger("dropCount");
		initialY = nbttagcompound.getInteger("initialY");
		nextUpdate = -1;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setBoolean("isActive", isActive);
		nbttagcompound.setInteger("dropCount", dropCount);
		nbttagcompound.setInteger("initialY", initialY);
	}
	
	

}
