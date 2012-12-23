package ljdp.minechem.common.blueprint;

import java.util.HashMap;

import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.blueprint.BlueprintBlock.Type;
import ljdp.minechem.common.tileentity.TileEntityFusion;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class BlueprintFusion extends MinechemBlueprint {
	
	public static int wall    = 1;
	public static int manager = 2;
	public static int proxy   = 3;
	private static Integer[][][] structure = {
	{
		{air, 	air, 	wall, 	wall, 	wall, 	air, 	air},
		{air, 	wall, 	air,  	air,  	air, 	wall, 	air},
		{wall, 	air, 	air,  	air,  	air, 	air, 	wall},
		{wall, 	air, 	air,  	wall,  	air, 	air, 	wall},
		{wall, 	air, 	air,  	air,  	air, 	air, 	wall},
		{air,	wall,	air,	air,	air,	wall,	air},
		{air,	air,	wall,	wall,	wall,	air,	air},
	},
	{
		{air, 	air, 	wall, 	wall, 	wall, 	air, 	air},
		{air, 	wall, 	air,  	air,  	air, 	wall, 	air},
		{wall, 	air, 	air,  	air,  	air, 	air, 	wall},
		{wall, 	air, 	air,  	wall,  	air, 	air, 	wall},
		{wall, 	air, 	air,  	air,  	air, 	air, 	wall},
		{air,	wall,	air,	air,	air,	wall,	air},
		{air,	air,	wall,	wall,	wall,	air,	air},
	},
	{
		{air, 	air, 	air, 	air, 	air, 	air, 	air},
		{air, 	air, 	wall,  	wall,  	wall, 	air, 	air},
		{air, 	wall, 	wall,  	wall,  	wall, 	wall, 	air},
		{air, 	wall, 	wall,  	wall,  	wall, 	wall, 	air},
		{air, 	wall, 	wall,  	wall,  	wall, 	wall, 	air},
		{air,	air,	wall,	wall,	wall,	air,	air},
		{air,	air,	air,	air,	air,	air,	air},
	}
	};
	private static Integer[][][] resultStructure = {
		{
			{air, 	air, 	proxy, 	proxy, 	proxy, 	air, 	air},
			{air, 	proxy, 	air,  	air,  	air, 	proxy, 	air},
			{proxy, air, 	air,  	air,  	air, 	air, 	proxy},
			{proxy, air, 	air,  manager,  air, 	air, 	proxy},
			{proxy, air, 	air,  	air,  	air, 	air, 	proxy},
			{air,	proxy,	air,	air,	air,	proxy,	air},
			{air,	air,	proxy,	proxy,	proxy,	air,	air},
		},
		{
			{air, 	air, 	proxy, 	proxy, 	proxy, 	air, 	air},
			{air, 	proxy, 	air,  	air,  	air, 	proxy, 	air},
			{proxy, air, 	air,  	air,  	air, 	air, 	proxy},
			{proxy, air, 	air,  	wall,  	air, 	air, 	proxy},
			{proxy, air, 	air,  	air,  	air, 	air, 	proxy},
			{air,	proxy,	air,	air,	air,	proxy,	air},
			{air,	air,	proxy,	proxy,	proxy,	air,	air},
		},
		{
			{air, 	air, 	air, 	air, 	air, 	air, 	air},
			{air, 	air, 	proxy,  proxy,  proxy, 	air, 	air},
			{air, 	proxy, 	proxy,  proxy,  proxy, 	proxy, 	air},
			{air, 	proxy, 	proxy,  proxy,  proxy, 	proxy, 	air},
			{air, 	proxy, 	proxy,  proxy,  proxy, 	proxy, 	air},
			{air,	air,	proxy,	proxy,	proxy,	air,	air},
			{air,	air,	air,	air,	air,	air,	air},
		}
		};
	
	public BlueprintFusion() {
		super(7, 3, 7);
	}

	@Override
	public HashMap<Integer,BlueprintBlock> getBlockLookup() {
		HashMap<Integer,BlueprintBlock> lookup = new HashMap();
		lookup.put(wall, 	new BlueprintBlock(Block.blockSteel, 		0, Type.NORMAL));
		lookup.put(manager, new BlueprintBlock(MinechemBlocks.fusion, 	1, Type.MANAGER));
		lookup.put(proxy, 	new BlueprintBlock(MinechemBlocks.fusion, 	0, Type.PROXY));
		return lookup;
	}

	@Override
	public Integer[][][] getStructure() {
		return structure;
	}

	@Override
	public Integer[][][] getResultStructure() {
		return resultStructure;
	}

	@Override
	public int getManagerPosX() {
		return 3;
	}

	@Override
	public int getManagerPosY() {
		return 0;
	}

	@Override
	public int getManagerPosZ() {
		return 3;
	}

	@Override
	public BlueprintBlock getManagerBlock() {
		return new BlueprintBlock(MinechemBlocks.fusion, 1, Type.MANAGER);
	}
	
}
