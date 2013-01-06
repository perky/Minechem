package ljdp.minechem.common;

import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntityMicroscope;
import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;


public class CommonProxy {
	
	public static final String TEXTURE_DIR		  = "/textures/minechem/";
	public static final String TEXTURE_GUI_DIR	  = TEXTURE_DIR + "gui/";
	public static final String TEXTURE_ATLAS_DIR  = TEXTURE_DIR + "atlas/";
	public static final String TEXTURE_MODEL_DIR  = TEXTURE_DIR + "model/";
	public static final String SOUNDS_DIR		  = "/sounds/minechem/";
	public static final String LANG_DIR			  = "/lang/minechem/";
	
	public static String TAB_LEFT_PNG 		= TEXTURE_GUI_DIR + "tab_left.png";
	public static String TAB_RIGHT_PNG 		= TEXTURE_GUI_DIR + "tab_right.png";
	public static String DECOMPOSER_GUI_PNG = TEXTURE_GUI_DIR + "ChemicalDecomposerGUI.png";
	public static String MICROSCOPE_GUI_PNG = TEXTURE_GUI_DIR + "MicroscopeGUI.png";
	public static String SYNTHESIS_GUI_PNG  = TEXTURE_GUI_DIR + "SynthesisGUI.png";
	public static String FUSION_GUI_PNG		= TEXTURE_GUI_DIR + "FusionChamberGUI.png";
	public static String PROJECTOR_GUI_PNG  = TEXTURE_GUI_DIR + "ProjectorGUI.png";
	public static String JOURNAL_GUI_PNG	= TEXTURE_GUI_DIR + "ChemistsJournalGUI.png";
	public static String ITEMS_PNG			= TEXTURE_ATLAS_DIR + "items.png";
	public static String BLOCKS_PNG			= TEXTURE_ATLAS_DIR + "blocktextures.png";
	public static String ICONS_PNG			= TEXTURE_ATLAS_DIR + "icons.png";
	public static String MICROSCOPE_MODEL   = TEXTURE_MODEL_DIR + "MicroscopeModel.png";
	public static String DECOMPOSER_MODEL_ON   	= TEXTURE_MODEL_DIR + "DecomposerModelOn.png";
	public static String DECOMPOSER_MODEL_OFF  	= TEXTURE_MODEL_DIR + "DecomposerModelOff.png";
	public static String SYNTHESIS_MODEL		= TEXTURE_MODEL_DIR + "SynthesiserModel.png";
	public static String PROJECTOR_MODEL_ON 	= TEXTURE_MODEL_DIR + "ProjectorModelOn.png";
	public static String PROJECTOR_MODEL_OFF 	= TEXTURE_MODEL_DIR + "ProjectorModelOff.png";
	public static String HAZMAT_PNG 			= TEXTURE_MODEL_DIR + "hazmatArmor.png";
	
	public static String PROJECTOR_SOUND	= SOUNDS_DIR + "projector.ogg";
	public static String LANG_GB			= LANG_DIR + "en_GB.properties";

	public static int CUSTOM_RENDER_ID;
	
	public void registerRenderers() {
		
	}
	
	public World getClientWorld() {
		return null;
	}

	public void registerHooks() {
	}

	public EntityPlayer findEntityPlayerByName(String name) {
		WorldServer[] servers = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;
		EntityPlayer player = null;
		for(WorldServer server : servers) {
			player = server.getPlayerEntityByName(name);
			if(player != null)
				return player;
		}
		return player;
	}

}
