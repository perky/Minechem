package ljdp.minechem.common;

import net.minecraft.block.Block;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class MinechemBlocks {
	public static Block decomposer;
	public static Block microscope;
	public static Block synthesis;
	private static int microscopeID;
	private static int decomposerID;
	private static int synthesisID;
	
	public static void loadConfig(Configuration config) {
		microscopeID = config.get(config.CATEGORY_BLOCK, "Microscope", 4012).getInt(4012);
	 	decomposerID = config.get(config.CATEGORY_BLOCK, "Decomposer", 4011).getInt(4011);
	 	synthesisID  = config.get(config.CATEGORY_BLOCK, "Synthesis",  4013).getInt(4013);
	}
	
	public static void registerBlocks() {
		decomposer = new BlockDecomposer(decomposerID);
		microscope = new BlockMicroscope(microscopeID);
		synthesis  = new BlockSynthesis(synthesisID);
		
		GameRegistry.registerBlock(decomposer);
		GameRegistry.registerTileEntity(TileEntityDecomposer.class, "minechem.tileEntityDecomposer");
		LanguageRegistry.addName(decomposer, "Chemical Decomposer");
		
		GameRegistry.registerBlock(microscope);
		GameRegistry.registerTileEntity(TileEntityMicroscope.class, "minechem.tileEntityMicroscope");
		LanguageRegistry.addName(microscope, "Microscope");
		
		GameRegistry.registerBlock(synthesis);
		GameRegistry.registerTileEntity(TileEntitySynthesis.class, "minechem.tileEntitySynthesis");
		LanguageRegistry.addName(synthesis, "Chemical Synthesis Table");
	}

}
