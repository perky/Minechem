package ljdp.minechem.client;

import ljdp.minechem.client.render.RenderBlockGhostBlock;
import ljdp.minechem.client.render.item.ItemBlueprintProjectorRenderer;
import ljdp.minechem.client.render.item.ItemDecomposerRenderer;
import ljdp.minechem.client.render.item.ItemElementRenderer;
import ljdp.minechem.client.render.item.ItemMicroscopeRenderer;
import ljdp.minechem.client.render.item.ItemMoleculeRenderer;
import ljdp.minechem.client.render.item.ItemSynthesisRenderer;
import ljdp.minechem.client.render.tileentity.TileEntityBlueprintProjectorRenderer;
import ljdp.minechem.client.render.tileentity.TileEntityDecomposerRenderer;
import ljdp.minechem.client.render.tileentity.TileEntityGhostBlockRenderer;
import ljdp.minechem.client.render.tileentity.TileEntityMicroscopeRenderer;
import ljdp.minechem.client.render.tileentity.TileEntitySynthesisRenderer;
import ljdp.minechem.client.sound.MinechemSoundEvent;
import ljdp.minechem.common.CommonProxy;
import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityBlueprintProjector;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntityGhostBlock;
import ljdp.minechem.common.tileentity.TileEntityMicroscope;
import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import net.minecraft.client.renderer.entity.RenderPainting;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers() {
		MinecraftForgeClient.preloadTexture(ITEMS_PNG);
		MinecraftForgeClient.preloadTexture(DECOMPOSER_GUI_PNG);
		MinecraftForgeClient.preloadTexture(MICROSCOPE_GUI_PNG);
		MinecraftForgeClient.preloadTexture(SYNTHESIS_GUI_PNG);
		MinecraftForgeClient.preloadTexture(BLOCKS_PNG);
		MinecraftForgeClient.preloadTexture(TERRAIN_ALPHA_PNG);
		MinecraftForgeClient.preloadTexture(MICROSCOPE_MODEL);
		MinecraftForgeClient.preloadTexture(DECOMPOSER_MODEL_ON);
		MinecraftForgeClient.preloadTexture(DECOMPOSER_MODEL_OFF);
		MinecraftForgeClient.preloadTexture(SYNTHESIS_MODEL);
		MinecraftForgeClient.preloadTexture(PROJECTOR_MODEL_OFF);
		MinecraftForgeClient.preloadTexture(PROJECTOR_MODEL_ON);
		MinecraftForgeClient.preloadTexture(FUSION_GUI_PNG);
		MinecraftForgeClient.preloadTexture(PROJECTOR_GUI_PNG);
		
		CUSTOM_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		
		MinecraftForgeClient.registerItemRenderer(MinechemItems.element.shiftedIndex, new ItemElementRenderer());
		MinecraftForgeClient.registerItemRenderer(MinechemItems.molecule.shiftedIndex, new ItemMoleculeRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocks.microscope.blockID].shiftedIndex, new ItemMicroscopeRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocks.decomposer.blockID].shiftedIndex, new ItemDecomposerRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocks.synthesis.blockID].shiftedIndex, new ItemSynthesisRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.itemsList[MinechemBlocks.blueprintProjector.blockID].shiftedIndex, new ItemBlueprintProjectorRenderer());
		RenderingRegistry.registerBlockHandler(new RenderBlockGhostBlock());
		TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMicroscope.class, new TileEntityMicroscopeRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDecomposer.class, new TileEntityDecomposerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySynthesis.class, new TileEntitySynthesisRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlueprintProjector.class, new  TileEntityBlueprintProjectorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGhostBlock.class, new TileEntityGhostBlockRenderer());
	}
	
	@Override
	public void registerHooks() {
		ModMinechem.instance.blLog.info("Adding Sound Events");
		MinecraftForge.EVENT_BUS.register(new MinechemSoundEvent());
	}
	
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

}
