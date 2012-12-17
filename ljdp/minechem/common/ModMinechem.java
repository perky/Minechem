package ljdp.minechem.common;

import ljdp.minechem.client.GuiDecomposer;
import ljdp.minechem.client.GuiMicroscope;
import ljdp.minechem.client.GuiSynthesis;
import ljdp.minechem.common.network.PacketHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="minechem", name="MineChem", version="2.0.0a")
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
	public static CreativeTabs minechemTab = new CreativeTabMinechem("mineChem");
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(MinechemRecipes.getInstance());
		blLog.setParent(FMLLog.getLogger());

	 	blLog.info(Loading Config);
	 	loadConfig(event);
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		blLog.info("Adding Items");
		MinechemItems.registerItems();
		blLog.info("Adding Blocks");
		MinechemBlocks.registerBlocks();
		blLog.info("Adding Recipes");
		MinechemRecipes.getInstance().RegisterRecipes();
		
		NetworkRegistry.instance().registerGuiHandler(this, this);
		proxy.registerRenderers();
		LanguageRegistry.instance().addStringLocalization("itemGroup.mineChem", "en_US", "MineChem");
	}
	public void loadConfig(FMLPreInitializationEvent event){
	 	Configuration config = new Configuration(event.getSuggestedConfigurationFile());

	 	//Items go below here(I put in an example)
	 	//obbySwordID = config.getItem(config.CATEGORY_ITEM,"Obsidian Sword", 509).getInt(509);
	 	//Blocks go below here(Another Example)
	 	//oreObbyID = config.get(config.CATEGORY_BLOCK,"Obsidian Ore", 201).getInt(201);
	 	elementID = config.getItem(config.CATEGORY_ITEM,"Element", 4736).getInt(4736);
	 	moleculeID = config.getItem(config.CATEGORY_ITEM,"MoleCule", 4737).getInt(4737);
	 	microscopeID = config.get(config.CATEGORY_BLOCK,"Microscope", 4012).getInt(4012);
	 	decomposerID = config.get(config.CATEGORY_BLOCK,"Decomposer", 4011).getInt(4011);
	 	synthesisID = config.get(config.CATEGORY_BLOCK,"Synthesis", 4013).getInt(4013);
	 	config.save();
	 }
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		
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
		return null;
	}
	
}
