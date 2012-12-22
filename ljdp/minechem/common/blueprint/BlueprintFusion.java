package ljdp.minechem.common.blueprint;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class BlueprintFusion extends MinechemBlueprint {
	
	public static int wall = Block.stone.blockID;
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
	
	public BlueprintFusion() {
		this(7, 3, 7, structure);
	}
	
	private BlueprintFusion(int xSize, int ySize, int zSize, Integer[][][] structure) {
		super(xSize, ySize, zSize, structure);
	}
}
