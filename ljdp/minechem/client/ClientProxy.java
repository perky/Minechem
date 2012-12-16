package ljdp.minechem.client;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import ljdp.minechem.common.CommonProxy;
import ljdp.minechem.common.MinechemItems;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers() {
		MinecraftForgeClient.preloadTexture(ELEMENTBOTTLES_PNG);
		MinecraftForgeClient.preloadTexture(DECOMPOSER_GUI_PNG);
		MinecraftForgeClient.preloadTexture(MICROSCOPE_GUI_PNG);
		MinecraftForgeClient.preloadTexture(SYNTHESIS_GUI_PNG);
		MinecraftForgeClient.preloadTexture(BLOCKS_PNG);
		MinecraftForgeClient.registerItemRenderer(MinechemItems.element.shiftedIndex, new ItemElementRenderer());
		MinecraftForgeClient.registerItemRenderer(MinechemItems.molecule.shiftedIndex, new ItemMoleculeRenderer());
	}
	
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}

}
