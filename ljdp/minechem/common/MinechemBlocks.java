package ljdp.minechem.common;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ljdp.minechem.common.ModMineChem;
public class MinechemBlocks {
	public static Block decomposer;
	public static Block microscope;
	public static Block synthesis;
	
	public static void registerBlocks() {
		decomposer = new BlockDecomposer(decomposerID);
		microscope = new BlockMicroscope(microscopeID);
		synthesis  = new BlockSynthesis(synthesisID);
		
		GameRegistry.registerBlock(decomposer);
		GameRegistry.registerTileEntity(TileEntityDecomposer.class, "MinechemTileEntityDecomposer");
		LanguageRegistry.addName(decomposer, "Chemical Decomposer");
		
		GameRegistry.registerBlock(microscope);
		GameRegistry.registerTileEntity(TileEntityMicroscope.class, "minechem.tileEntityMicroscope");
		LanguageRegistry.addName(microscope, "Microscope");
		
		GameRegistry.registerBlock(synthesis);
		GameRegistry.registerTileEntity(TileEntitySynthesis.class, "minechem.tileEntitySynthesis");
		LanguageRegistry.addName(synthesis, "Chemical Synthesis Table");
	}

}
