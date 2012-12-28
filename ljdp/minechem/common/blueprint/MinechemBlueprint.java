package ljdp.minechem.common.blueprint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class MinechemBlueprint {
	
	public static int air   = 0;
	public static HashMap<Integer,MinechemBlueprint> blueprints = new HashMap();
	public static MinechemBlueprint fusion;
	
	public int xSize;
	public int ySize;
	public int zSize;
	private int totalSize;
	private int horizontalSize;
	private int verticalSize;
	public String name;
	public int id;
	
	public static void registerBlueprint(int id, MinechemBlueprint blueprint) {
		blueprint.id = id;
		blueprints.put(id, blueprint);
	}
	
	public static void registerBlueprints() {
		fusion = new BlueprintFusion();
		registerBlueprint(0, fusion);
	}
	
	public MinechemBlueprint(int xSize, int ySize, int zSize) 
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.horizontalSize = xSize * zSize;
		this.verticalSize = ySize * zSize;
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
	
	public Integer[][] getVerticalSlice(int x) {
		Integer[][][] structure = getStructure();
		Integer[][] slice = new Integer[ySize][zSize];
		for(int y = 0; y < ySize; y++) {
			for(int z = 0; z < zSize; z++){
				slice[y][z] = structure[y][x][z];
			}
		}
		return slice;
	}
	
	public int getHorizontalSliceSize() {
		return this.horizontalSize;
	}
	
	public int getVerticalSliceSize() {
		return this.verticalSize;
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
