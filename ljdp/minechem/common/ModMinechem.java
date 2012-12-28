package ljdp.minechem.common;

import java.util.logging.Logger;

import ljdp.minechem.client.gui.GuiDecomposer;
import ljdp.minechem.client.gui.GuiFusion;
import ljdp.minechem.client.gui.GuiMicroscope;
import ljdp.minechem.client.gui.GuiProjector;
import ljdp.minechem.client.gui.GuiSynthesis;
import ljdp.minechem.common.containers.ContainerDecomposer;
import ljdp.minechem.common.containers.ContainerFusion;
import ljdp.minechem.common.containers.ContainerMicroscope;
import ljdp.minechem.common.containers.ContainerProjector;
import ljdp.minechem.common.containers.ContainerSynthesis;
import ljdp.minechem.common.gates.MinechemTriggers;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.sound.MinechemSoundEvent;
import ljdp.minechem.common.tileentity.TileEntityBlueprintProjector;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntityFusion;
import ljdp.minechem.common.tileentity.TileEntityMicroscope;
import ljdp.minechem.common.tileentity.TileEntityProxy;
import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.src.TradeEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.modloader.ModLoaderHelper;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;

@Mod(modid="minechem", name="MineChem", version="2.0.0pr1")
@NetworkMod(
		clientSideRequired=true, 
		serverSideRequired=false, 
		channels={PacketHandler.MINECHEM_PACKET_CHANNEL}, 
		packetHandler=PacketHandler.class
)
public class ModMinechem implements IGuiHandler {
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
		blLog.info("Adding Ore Dictionary Recipes");
		MinecraftForge.EVENT_BUS.register(MinechemRecipes.getInstance());
		blLog.info("Adding Sound Events");
		MinecraftForge.EVENT_BUS.register(new MinechemSoundEvent());
		blLog.info("Registering Villager Trades");
		for(int i = 0; i < 5; i++)
			VillagerRegistry.instance().registerVillageTradeHandler(i, new VillageTradeHandler());
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.instance().registerGuiHandler(this, this);
		proxy.registerRenderers();
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		MinechemTriggers.registerTriggers();
		//EntityVillager.
		//VillagerRegistry.addEmeraldBuyRecipe(villager, list, random, item, chance, min, max)
	}
	
	private void loadConfig(FMLPreInitializationEvent event){
	 	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
	 	MinechemBlocks.loadConfig(config);
	 	MinechemItems.loadConfig(config);
	 	config.save();
	 }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityDecomposer)
			return new ContainerDecomposer(player.inventory, (TileEntityDecomposer)tileEntity);
		if(tileEntity instanceof TileEntityMicroscope)
			return new ContainerMicroscope(player.inventory, (TileEntityMicroscope)tileEntity);
		if(tileEntity instanceof TileEntitySynthesis)
			return new ContainerSynthesis(player.inventory, (TileEntitySynthesis)tileEntity);
		if(tileEntity instanceof TileEntityFusion)
			return new ContainerFusion(player.inventory, (TileEntityFusion)tileEntity);
		if(tileEntity instanceof TileEntityProxy)
			return getServerGuiElementFromProxy((TileEntityProxy)tileEntity, player);
		if(tileEntity instanceof TileEntityBlueprintProjector)
			return new ContainerProjector(player.inventory, (TileEntityBlueprintProjector)tileEntity);
		return null;
	}

	public Object getServerGuiElementFromProxy(TileEntityProxy proxy, EntityPlayer player) {
		TileEntity tileEntity = proxy.getManager();
		if(tileEntity instanceof TileEntityFusion)
			return new ContainerFusion(player.inventory, (TileEntityFusion)tileEntity);
		return null;
	}
	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(tileEntity instanceof TileEntityDecomposer)
			return new GuiDecomposer(player.inventory, (TileEntityDecomposer)tileEntity);
		if(tileEntity instanceof TileEntityMicroscope)
			return new GuiMicroscope(player.inventory, (TileEntityMicroscope)tileEntity);
		if(tileEntity instanceof TileEntitySynthesis)
			return new GuiSynthesis(player.inventory, (TileEntitySynthesis)tileEntity);
		if(tileEntity instanceof TileEntityFusion)
			return new GuiFusion(player.inventory, (TileEntityFusion)tileEntity);
		if(tileEntity instanceof TileEntityProxy)
			return getClientGuiElementFromProxy((TileEntityProxy)tileEntity, player);
		if(tileEntity instanceof TileEntityBlueprintProjector)
			return new GuiProjector(player.inventory, (TileEntityBlueprintProjector)tileEntity);
		return null;
	}
	
	public Object getClientGuiElementFromProxy(TileEntityProxy proxy, EntityPlayer player) {
		TileEntity tileEntity = proxy.getManager();
		if(tileEntity instanceof TileEntityFusion)
			return new GuiFusion(player.inventory, (TileEntityFusion)tileEntity);
		return null;
	}
	
}
