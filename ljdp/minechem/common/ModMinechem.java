package ljdp.minechem.common;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import buildcraft.api.power.PowerFramework;

import ljdp.minechem.client.gui.GuiDecomposer;
import ljdp.minechem.client.gui.GuiFusion;
import ljdp.minechem.client.gui.GuiMicroscope;
import ljdp.minechem.client.gui.GuiProjector;
import ljdp.minechem.client.gui.GuiSynthesis;
import ljdp.minechem.client.sound.MinechemSoundEvent;
import ljdp.minechem.common.blueprint.MinechemBlueprint;
import ljdp.minechem.common.containers.ContainerDecomposer;
import ljdp.minechem.common.containers.ContainerFusion;
import ljdp.minechem.common.containers.ContainerMicroscope;
import ljdp.minechem.common.containers.ContainerProjector;
import ljdp.minechem.common.containers.ContainerSynthesis;
import ljdp.minechem.common.gates.MinechemTriggers;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.recipe.MinechemRecipes;
import ljdp.minechem.common.tileentity.TileEntityBlueprintProjector;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntityFusion;
import ljdp.minechem.common.tileentity.TileEntityMicroscope;
import ljdp.minechem.common.tileentity.TileEntityProxy;
import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import ljdp.minechem.computercraft.ICCMain;
import net.minecraft.command.CommandHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.src.TradeEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.modloader.ModLoaderHelper;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="minechem", name="MineChem", version="@VERSION@")
@NetworkMod(
		clientSideRequired=true, 
		serverSideRequired=false, 
		channels={PacketHandler.MINECHEM_PACKET_CHANNEL}, 
		packetHandler=PacketHandler.class
)

public class ModMinechem {
	@Instance("minechem")
	public static ModMinechem instance;
	
	@SidedProxy(clientSide="ljdp.minechem.client.ClientProxy", serverSide="ljdp.minechem.common.CommonProxy")
	public static CommonProxy proxy;
	public static int elementID;
	public static int moleculeID;
	public static int decomposerID;
	public static int microscopeID;
	public static int synthesisID;
	public static Logger blLog = Logger.getLogger("MineChem");
	public static CreativeTabs minechemTab = new CreativeTabMinechem("MineChem");
	private Configuration config;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		blLog.setParent(FMLLog.getLogger());
		blLog.info("Loading localization");
		LanguageRegistry.instance().loadLocalization("/lang/minechem/en_GB.properties", "en_GB", false);
	 	blLog.info("Loading Config");
	 	loadConfig(event);
	 	blLog.info("Adding Items");
		MinechemItems.registerItems();
		blLog.info("Adding Blocks");
		MinechemBlocks.registerBlocks();
		blLog.info("Adding Recipes");
		MinechemRecipes.getInstance().RegisterRecipes();
		blLog.info("Registering Ore Dictionary items");
		MinechemItems.registerToOreDictionary();
		blLog.info("Registering Forge Events");
		proxy.registerHooks();
		blLog.info("Adding Ore Dictionary Recipes");
		MinecraftForge.EVENT_BUS.register(MinechemRecipes.getInstance());
		blLog.info("Registering Blueprints");
		MinechemBlueprint.registerBlueprints();
		blLog.info("Registering Villager Trades");
		for(int i = 0; i < 5; i++)
			VillagerRegistry.instance().registerVillageTradeHandler(i, new VillageTradeHandler());
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		TickRegistry.registerScheduledTickHandler(new ScheduledTickHandler(), Side.SERVER);
		proxy.registerRenderers();
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) throws IOException {
		checkForBuildcraft();
		MinechemTriggers.registerTriggers();
		initComputerCraftAddon(event);
	}
	
	@ServerStarting
    public void serverStarting(FMLServerStartingEvent event)
    {
		CommandHandler commandManager = (CommandHandler) event.getServer().getCommandManager();
		commandManager.registerCommand(new CommandPrintOreDict());
    }
	
	private void checkForBuildcraft() throws IOException {
		if (PowerFramework.currentFramework == null) {
			throw new IOException("MineChem needs BuildCraft installed");
		}
	}
	
	private void initComputerCraftAddon(FMLPostInitializationEvent event) {
		Object ccMain = event.buildSoftDependProxy("CCTurtle", "ljdp.minechem.computercraft.CCMain");
		if(ccMain != null) {
			ICCMain iCCMain = (ICCMain) ccMain;
			iCCMain.loadConfig(config);
			iCCMain.init();
		}
	}
	
	private void loadConfig(FMLPreInitializationEvent event){
	 	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
	 	MinechemBlocks.loadConfig(config);
	 	MinechemItems.loadConfig(config);
	 	config.save();
	 	this.config = config;
	 }
}
