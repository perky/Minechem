package ljdp.minechem.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import ljdp.minechem.common.CommonProxy;
import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.tileentity.TileEntityMicroscope;

public class ClientProxy extends CommonProxy {
	
	
	
	@Override
	public void registerRenderers() {
		MinecraftForgeClient.preloadTexture(ELEMENTBOTTLES_PNG);
		MinecraftForgeClient.preloadTexture(DECOMPOSER_GUI_PNG);
		MinecraftForgeClient.preloadTexture(MICROSCOPE_GUI_PNG);
		MinecraftForgeClient.preloadTexture(SYNTHESIS_GUI_PNG);
		MinecraftForgeClient.preloadTexture(BLOCKS_PNG);
		MinecraftForgeClient.preloadTexture(TERRAIN_ALPHA_PNG);
		MinecraftForgeClient.preloadTexture(MICROSCOPE_MODEL);
		
		RENDER_MICROSCOPE = RenderingRegistry.getNextAvailableRenderId();
		
		MinecraftForgeClient.registerItemRenderer(MinechemItems.element.shiftedIndex, new ItemElementRenderer());
		MinecraftForgeClient.registerItemRenderer(MinechemItems.molecule.shiftedIndex, new ItemMoleculeRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocks.microscope.blockID].shiftedIndex, new ItemMicroscopeRenderer());
		
		RenderingRegistry.registerBlockHandler(new RenderBlockGhostBlock());
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMicroscope.class, new RenderTileEntityMicroscope());
	}
	
	@Override
	public void registerTileEntities() {
		ClientRegistry.registerTileEntity(TileEntityMicroscope.class, "minechem.tileEntityMicroscope", new RenderTileEntityMicroscope());
	}
	
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

}
