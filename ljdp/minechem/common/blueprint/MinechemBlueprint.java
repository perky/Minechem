package ljdp.minechem.common.blueprint;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class MinechemBlueprint {
	
	public static int air   = 0;
	
	public int xSize;
	public int ySize;
	public int zSize;
	private int totalSize;
	private int horizontalSize;
	
	public MinechemBlueprint(int xSize, int ySize, int zSize) 
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.horizontalSize = xSize * zSize;
		this.totalSize = this.horizontalSize * ySize;
	}
	
	public Integer[][] getHorizontalSlice(int y) {
		Integer[][][] structure = getStructure();
		Integer[][] slice = new Integer[xSize][zSize];
		for(int x = 0; x < xSize; x++) {
			for(int z = 0; z < zSize; z++){
				slice[x][z] = structure[y][x][z];
			}
		}
		return slice;
	}
	
	public int getHorizontalSliceSize() {
		return this.horizontalSize;
	}
	
	public int getTotalSize() {
		return this.totalSize;
	}
	
	public abstract HashMap<Integer,BlueprintBlock> getBlockLookup();
	public abstract Integer[][][] getStructure();
	public abstract Integer[][][] getResultStructure();
	public abstract int getManagerPosX();
	public abstract int getManagerPosY();
	public abstract int getManagerPosZ();
	public abstract BlueprintBlock getManagerBlock();
}
