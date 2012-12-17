package ljdp.minechem.common;

import ljdp.minechem.utils.MinechemHelper;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class MinechemBlocks {
	public static Block decomposer;
	public static Block microscope;
	public static Block synthesis;
	
	public static void registerBlocks() {
		decomposer = new BlockDecomposer(4011);
		microscope = new BlockMicroscope(4012);
		synthesis  = new BlockSynthesis(4013);
		
		GameRegistry.registerBlock(decomposer);
		GameRegistry.registerTileEntity(TileEntityDecomposer.class, "MinechemTileEntityDecomposer");
		LanguageRegistry.addName(decomposer, MinechemHelper.getLocalString("block.name.decomposer"));
		
		GameRegistry.registerBlock(microscope);
		GameRegistry.registerTileEntity(TileEntityMicroscope.class, "minechem.tileEntityMicroscope");
		LanguageRegistry.addName(microscope, MinechemHelper.getLocalString("block.name.microscope"));
		
		GameRegistry.registerBlock(synthesis);
		GameRegistry.registerTileEntity(TileEntitySynthesis.class, "minechem.tileEntitySynthesis");
		LanguageRegistry.addName(synthesis, MinechemHelper.getLocalString("block.name.synthesis"));
	}

}
