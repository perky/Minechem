package ljdp.minechem.common.blueprint;

import java.util.HashMap;

import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.blueprint.BlueprintBlock.Type;
import ljdp.minechem.common.tileentity.TileEntityFusion;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class BlueprintFusion extends MinechemBlueprint {
	
	private static int w = wildcard;
	private static int A = 1;
	private static int C = 2;
	private static Integer[][][] structure = {
	{
		{w,w,w,w,A,A,A,A,A,w,w,w,w},
		{w,w,A,A,A,A,A,A,A,A,A,w,w},
		{w,A,A,A,C,C,C,C,C,A,A,A,w},
		{w,A,A,C,C,C,C,C,C,C,A,A,w},
		{A,A,C,C,C,C,C,C,C,C,C,A,A},
		{A,A,C,C,C,C,C,C,C,C,C,A,A},
		{A,A,C,C,C,C,C,C,C,C,C,A,A},
		{A,A,C,C,C,C,C,C,C,C,C,A,A},
		{A,A,C,C,C,C,C,C,C,C,C,A,A},
		{w,A,A,C,C,C,C,C,C,C,A,A,w},
		{w,A,A,A,C,C,C,C,C,A,A,A,w},
		{w,w,A,A,A,A,A,A,A,A,A,w,w},
		{w,w,w,w,A,A,A,A,A,w,w,w,w}
	},
	{
		{w,w,w,w,A,A,A,A,A,w,w,w,w},
		{w,w,A,A,C,C,C,C,C,A,A,w,w},
		{w,A,C,C,0,0,0,0,0,C,C,A,w},
		{w,A,C,0,0,0,0,0,0,0,C,A,w},
		{A,C,0,0,0,0,0,0,0,0,0,C,A},
		{A,C,0,0,0,0,C,0,0,0,0,C,A},
		{A,C,0,0,0,C,0,C,0,0,0,C,A},
		{A,C,0,0,0,0,C,0,0,0,0,C,A},
		{A,C,0,0,0,0,0,0,0,0,0,C,A},
		{w,A,C,0,0,0,0,0,0,0,C,A,w},
		{w,A,C,C,0,0,0,0,0,C,C,A,w},
		{w,w,A,A,C,C,C,C,C,A,A,w,w},
		{w,w,w,w,A,A,A,A,A,w,w,w,w},
	},
	{
		{w,w,w,w,A,A,A,A,A,w,w,w,w},
		{w,w,A,A,C,C,C,C,C,A,A,w,w},
		{w,A,C,C,0,0,0,0,0,C,C,A,w},
		{w,A,C,0,0,0,0,0,0,0,C,A,w},
		{A,C,0,0,0,0,0,0,0,0,0,C,A},
		{A,C,0,0,0,0,C,0,0,0,0,C,A},
		{A,C,0,0,0,C,0,C,0,0,0,C,A},
		{A,C,0,0,0,0,C,0,0,0,0,C,A},
		{A,C,0,0,0,0,0,0,0,0,0,C,A},
		{w,A,C,0,0,0,0,0,0,0,C,A,w},
		{w,A,C,C,0,0,0,0,0,C,C,A,w},
		{w,w,A,A,C,C,C,C,C,A,A,w,w},
		{w,w,w,w,A,A,A,A,A,w,w,w,w},
	},
	{
		{w,w,w,w,A,A,A,A,A,w,w,w,w},
		{w,w,A,A,C,C,C,C,C,A,A,w,w},
		{w,A,C,C,0,0,0,0,0,C,C,A,w},
		{w,A,C,0,0,0,0,0,0,0,C,A,w},
		{A,C,0,0,0,0,0,0,0,0,0,C,A},
		{A,C,0,0,0,0,C,0,0,0,0,C,A},
		{A,C,0,0,0,C,0,C,0,0,0,C,A},
		{A,C,0,0,0,0,C,0,0,0,0,C,A},
		{A,C,0,0,0,0,0,0,0,0,0,C,A},
		{w,A,C,0,0,0,0,0,0,0,C,A,w},
		{w,A,C,C,0,0,0,0,0,C,C,A,w},
		{w,w,A,A,C,C,C,C,C,A,A,w,w},
		{w,w,w,w,A,A,A,A,A,w,w,w,w},
	},
	{
		{w,w,w,w,w,w,w,w,w,w,w,w,w},
		{w,w,w,w,w,w,w,w,w,w,w,w,w},
		{w,w,w,w,C,C,C,C,C,w,w,w,w},
		{w,w,w,C,C,C,C,C,C,C,w,w,w},
		{w,w,C,C,C,C,C,C,C,C,C,w,w},
		{w,w,C,C,C,C,A,C,C,C,C,w,w},
		{w,w,C,C,C,A,A,A,C,C,C,w,w},
		{w,w,C,C,C,C,A,C,C,C,C,w,w},
		{w,w,C,C,C,C,C,C,C,C,C,w,w},
		{w,w,w,C,C,C,C,C,C,C,w,w,w},
		{w,w,w,w,C,C,C,C,C,w,w,w,w},
		{w,w,w,w,w,w,w,w,w,w,w,w,w},
		{w,w,w,w,w,w,w,w,w,w,w,w,w}
	},
	};
	
	public BlueprintFusion() {
		super(13, 5, 13);
		this.name = "blueprintFusion";
	}

	@Override
	public HashMap<Integer,BlueprintBlock> getBlockLookup() {
		HashMap<Integer,BlueprintBlock> lookup = new HashMap();
		lookup.put(A, 	new BlueprintBlock(MinechemBlocks.fusion, 0, Type.PROXY));
		lookup.put(C,	new BlueprintBlock(MinechemBlocks.fusion, 1, Type.NORMAL));
		return lookup;
	}

	@Override
	public Integer[][][] getStructure() {
		return structure;
	}

	@Override
	public Integer[][][] getResultStructure() {
		return structure;
	}

	@Override
	public int getManagerPosX() {
		return 6;
	}

	@Override
	public int getManagerPosY() {
		return 1;
	}

	@Override
	public int getManagerPosZ() {
		return 6;
	}

	@Override
	public BlueprintBlock getManagerBlock() {
		return new BlueprintBlock(MinechemBlocks.fusion, 2, Type.MANAGER);
	}
	
}
