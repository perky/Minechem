package ljdp.minechem.common.blueprint;

import net.minecraft.item.ItemStack;

public class MinechemBlueprint {
	
	public static int air   = 0;
	
	public int xSize;
	public int ySize;
	public int zSize;
	public int totalSize;
	public int horizontalSize;
	public Integer[][][] structure;
	
	public MinechemBlueprint(int xSize, int ySize, int zSize, Integer[][][] structure) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.horizontalSize = xSize * zSize;
		this.totalSize = this.horizontalSize * ySize;
		this.structure = structure;
	}
	
	public Integer[][] getHorizontalSlice(int y) {
		Integer[][] slice = new Integer[xSize][zSize];
		for(int x = 0; x < xSize; x++) {
			for(int z = 0; z < zSize; z++){
				slice[x][z] = structure[y][x][z];
			}
		}
		return slice;
	}
}
