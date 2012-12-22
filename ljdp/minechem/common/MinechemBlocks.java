package ljdp.minechem.common;

import ljdp.minechem.common.blocks.BlockBlueprintProjector;
import ljdp.minechem.common.blocks.BlockDecomposer;
import ljdp.minechem.common.blocks.BlockGhostBlock;
import ljdp.minechem.common.blocks.BlockMicroscope;
import ljdp.minechem.common.blocks.BlockSynthesis;
import ljdp.minechem.common.blocks.MaterialGas;
import ljdp.minechem.common.items.ItemGhostBlock;
import ljdp.minechem.common.tileentity.TileEntityBlueprintProjector;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntityMicroscope;
import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class MinechemBlocks {
	public static Block decomposer;
	public static Block microscope;
	public static Block synthesis;
	public static Block ghostBlock;
	public static Block blueprintProjector;
	public static Material materialGas = new MaterialGas();
	private static int microscopeID;
	private static int decomposerID;
	private static int synthesisID;
	private static int ghostBlockID;
	private static int blueprintProjectorID;
	
	public static void loadConfig(Configuration config) {
		microscopeID = config.get(config.CATEGORY_BLOCK, "Microscope", 4012).getInt(4012);
	 	decomposerID = config.get(config.CATEGORY_BLOCK, "Decomposer", 4011).getInt(4011);
	 	synthesisID  = config.get(config.CATEGORY_BLOCK, "Synthesis",  4013).getInt(4013);
	 	blueprintProjectorID = config.get(config.CATEGORY_BLOCK, "BlueprintProjector", 4014).getInt();
	 	ghostBlockID = config.get(config.CATEGORY_BLOCK, "GhostBlock", 4015).getInt(4015);
	}
	
	public static void registerBlocks() {
		decomposer = new BlockDecomposer(decomposerID);
		microscope = new BlockMicroscope(microscopeID);
		synthesis  = new BlockSynthesis(synthesisID);
		ghostBlock = new BlockGhostBlock(ghostBlockID);
		blueprintProjector = new BlockBlueprintProjector(blueprintProjectorID);
		
		GameRegistry.registerBlock(decomposer, "minechem.blockDecomposer");
		LanguageRegistry.addName(decomposer, "Chemical Decomposer");
		
		GameRegistry.registerBlock(microscope, "minechem.blockMicroscope");
		LanguageRegistry.addName(microscope, MinechemHelper.getLocalString("block.name.microscope"));
		
		GameRegistry.registerBlock(synthesis, "minechem.blockSynthesis");
		LanguageRegistry.addName(synthesis, MinechemHelper.getLocalString("block.name.synthesis"));
		
		GameRegistry.registerBlock(ghostBlock, ItemGhostBlock.class);
		LanguageRegistry.addName(ghostBlock, "ghost block");
		
		GameRegistry.registerBlock(blueprintProjector, "minechem.blockBlueprintProjector");
		LanguageRegistry.addName(blueprintProjector, "Blueprint Projector");
		
		GameRegistry.registerTileEntity(TileEntityMicroscope.class, "minechem.tileEntityMicroscope");
		GameRegistry.registerTileEntity(TileEntitySynthesis.class,  "minechem.tileEntitySynthesis");
		GameRegistry.registerTileEntity(TileEntityDecomposer.class, "minechem.tileEntityDecomposer");
		GameRegistry.registerTileEntity(TileEntityBlueprintProjector.class, "minechem.tileEntityBlueprintProjector");
	}

}
