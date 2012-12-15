package ljdp.minechem.common;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.src.Block;

public class MinechemBlocks {
	public static Block decomposer;
	public static Block microscope;
	
	public static void registerBlocks() {
		decomposer = new BlockDecomposer(4011);
		microscope = new BlockMicroscope(4012);
		GameRegistry.registerBlock(decomposer);
		GameRegistry.registerTileEntity(TileEntityDecomposer.class, "MinechemTileEntityDecomposer");
		LanguageRegistry.addName(decomposer, "Chemical Decomposer");
		
		GameRegistry.registerBlock(microscope);
		GameRegistry.registerTileEntity(TileEntityMicroscope.class, "minechem.tileEntityMicroscope");
		LanguageRegistry.addName(microscope, "Microscope");
	}

}
