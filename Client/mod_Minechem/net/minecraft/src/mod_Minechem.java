package net.minecraft.src;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.Properties;

import net.minecraft.client.Minecraft;
import net.minecraft.minechem.*;
import net.minecraft.src.forge.ForgeHooks;
import net.minecraft.src.forge.ForgeHooksClient;
import net.minecraft.src.forge.ICustomItemRenderer;
import net.minecraft.src.forge.IOreHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.ic2.api.*;

public class mod_Minechem extends BaseMod {
	
	private static boolean debug = false;
	
	@MLProp public static int blockIDMinechem = 253;
	@MLProp public static int itemIDTestTube = 3001;
	@MLProp public static int itemIDEmptyTestTube = 3000;
	@MLProp public static int itemIDTableOfElements = 3002;
	@MLProp public static int itemIDHangableTableOfElements = 3003;
	@MLProp public static int itemIDChemistryBook = 3004;
	@MLProp public static boolean requireIC2Power = false;
	@MLProp public static boolean autoUpdateChemicalDictionary = true;
	public static Item itemTesttubeEmpty;
	public static Item itemTesttube;
	public static Item leadHelmet;
	public static Item leadTorso;
	public static Item leadLeggings;
	public static Item leadBoots;
	public static Item tableOfElements;
	public static Item hangableTableOfElements;
	public static Item chemistryBook;
	public static Block blockMinechem;
	public static Map<ItemStack, ElectrolysisRecipe> electrolysisRecipes;
	
	public static File dirMinechem = new File(Minecraft.getMinecraftDir(), "/minechem/");
	public static File fileChemicalDictionary = new File(Minecraft.getMinecraftDir(), "/minechem/Chemical Dictionary.txt");
	private static Properties chemicalDictionary;
	private static Random random;
	@MLProp public static String minechemBlocksTexture = "/minechem/blocktextures.png";
	public static String minechemItemsTexture = "/minechem/items.png";
	
	private int tickCount;
	private int updateDelay;
	
	public static void initItemsAndBlocks() {
		itemTesttubeEmpty = new ItemEmptyTestTube(itemIDEmptyTestTube).setItemName("blah");
		itemTesttube = new ItemTestTube(itemIDTestTube).setItemName("minechemTesttube");
		leadHelmet = new ItemArmor(2161, EnumArmorMaterial.DIAMOND, ModLoader.AddArmor("leadhelmet"), 0);
		leadTorso = new ItemArmor(2162, EnumArmorMaterial.DIAMOND, ModLoader.AddArmor("leadtorso"), 1);
		leadLeggings = new ItemArmor(2163, EnumArmorMaterial.DIAMOND, ModLoader.AddArmor("leadleggings"), 2);
		leadBoots = new ItemArmor(2164, EnumArmorMaterial.DIAMOND, ModLoader.AddArmor("leadboots"), 3);
		blockMinechem = new BlockMinechem(blockIDMinechem).setBlockName("blockminechem");
		itemTesttubeEmpty.iconIndex = ModLoader.addOverride("/gui/items.png", "/minechem/testtube_empty.png");
		tableOfElements = new ItemTableOfElements(itemIDTableOfElements);
		hangableTableOfElements = new ItemHangableTableOfElements(itemIDHangableTableOfElements);
		chemistryBook = new ItemChemistryBook(itemIDChemistryBook);
		leadBoots.setItemName("leadboots");
		leadLeggings.setItemName("leadleggings");
		leadTorso.setItemName("leadTorso");
		leadHelmet.setItemName("leadhelmet");
	}
	
	public mod_Minechem() {
		random = new Random();
		initItemsAndBlocks();
		MinecraftForgeClient.preloadTexture(minechemBlocksTexture);
		MinecraftForgeClient.preloadTexture(minechemItemsTexture);
		
		ModLoader.RegisterBlock(blockMinechem);
		ModLoader.RegisterEntityID(EntityTableOfElements.class, "entityTableOfElements", ModLoader.getUniqueEntityId());
		
		ModLoader.AddName(itemTesttubeEmpty, "Empty Test Tube");
		ModLoader.AddName(itemTesttube, "Test Tube");
		ModLoader.AddName(tableOfElements, "Table of Elements");
		ModLoader.AddName(hangableTableOfElements, "Hangable Table of Elements");
		ModLoader.AddName(chemistryBook, "Chemistry Book");
		ModLoader.AddName(leadBoots, "Lead Boots");
		ModLoader.AddName(leadLeggings, "Lead Leggings");
		ModLoader.AddName(leadTorso, "Lead Chestplate");
		ModLoader.AddName(leadHelmet, "Lead Helmet");
		
		ModLoader.RegisterTileEntity(net.minecraft.minechem.TileEntityElectrolysis.class, "minechem_tileelectrolysis");
		ModLoader.RegisterTileEntity(net.minecraft.minechem.TileEntityFusion.class, "minechem_tilefusion");
		ModLoader.RegisterTileEntity(net.minecraft.minechem.TileEntityBonder.class, "minechem_tilebonder");
		ModLoader.RegisterTileEntity(net.minecraft.minechem.TileEntityUnbonder.class, "minechem_tileunbonder");
		ModLoader.RegisterTileEntity(net.minecraft.minechem.TileEntityFission.class, "minechem_tilefission");
		ModLoader.RegisterTileEntity(net.minecraft.minechem.TileEntityMinechemCrafting.class, "minechem_tilecrafting");
		ModLoader.RegisterTileEntity(net.minecraft.minechem.TileEntityThermite.class, "minechem_tilethermite");
		
		if( !mod_Minechem.dirMinechem.exists() ) {
			dirMinechem.mkdir();
		}
		
		if(autoUpdateChemicalDictionary) {
			URLReader urlReader = new URLReader();
			urlReader.getChemicalDictionary();
		}
		
		try{
			loadChemicalDictionary();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		Item.itemsList[blockIDMinechem] = new ItemMinechem(blockIDMinechem-256, blockMinechem).setItemName("itemminechem");
		
		ModLoader.AddRecipe(new ItemStack(blockMinechem, 1, ItemMinechem.electrolysis), new Object[]{
			"RRR",
			"#-#",
			"#-#",
			Character.valueOf('R'), Item.redstone,
			Character.valueOf('#'), Item.ingotIron
		});
		ModLoader.AddRecipe(new ItemStack(blockMinechem, 1, ItemMinechem.fusion), new Object[]{
			"#D#",
			"#R#",
			"#D#",
			Character.valueOf('D'), Item.diamond,
			Character.valueOf('R'), Molecule.elementByFormula("Th", 1).stack,
			Character.valueOf('#'), Item.ingotIron
		});
		ModLoader.AddRecipe(new ItemStack(blockMinechem, 1, ItemMinechem.bonder), new Object[]{
			"#S#",
			"#S#",
			"#S#",
			Character.valueOf('S'), Item.slimeBall,
			Character.valueOf('#'), Item.ingotIron
		});
		ModLoader.AddRecipe(new ItemStack(blockMinechem, 1, ItemMinechem.unbonder), new Object[]{
			"#S#",
			"#S#",
			"#S#",
			Character.valueOf('S'), Block.tnt,
			Character.valueOf('#'), Item.ingotIron
		});
		ModLoader.AddRecipe(new ItemStack(blockMinechem, 1, ItemMinechem.fission), new Object[]{
			"#D#",
			"#U#",
			"#D#",
			Character.valueOf('D'), Item.diamond,
			Character.valueOf('U'), Molecule.elementByFormula("U", 1).stack,
			Character.valueOf('#'), Item.ingotIron
		});
		ModLoader.AddRecipe(new ItemStack(blockMinechem, 1, ItemMinechem.crafting), new Object[]{
			"#C#",
			"#H#",
			"###",
			Character.valueOf('H'), Molecule.elementByFormula("H", 1).stack,
			Character.valueOf('#'), Item.ingotIron,
			Character.valueOf('C'), Block.workbench
		});
		ModLoader.AddRecipe(new ItemStack(itemTesttubeEmpty, 1), new Object[]{
			"#--",
			"#--",
			"#--",
			Character.valueOf('#'), Block.glass
		});
		ModLoader.AddRecipe(new ItemStack(leadBoots, 1), new Object[]{
			"   ",
			"# #",
			"# #",
			Character.valueOf('#'), Molecule.elementByFormula("Pb", 1).stack
		});
		ModLoader.AddRecipe(new ItemStack(leadLeggings, 1), new Object[]{
			"###",
			"# #",
			"# #",
			Character.valueOf('#'), Molecule.elementByFormula("Pb", 1).stack
		});
		ModLoader.AddRecipe(new ItemStack(leadTorso, 1), new Object[]{
			"# #",
			"###",
			"###",
			Character.valueOf('#'), Molecule.elementByFormula("Pb", 1).stack
		});
		ModLoader.AddRecipe(new ItemStack(leadHelmet, 1), new Object[]{
			"###",
			"# #",
			"   ",
			Character.valueOf('#'), Molecule.elementByFormula("Pb", 1).stack
		});
		ModLoader.AddRecipe(new ItemStack(tableOfElements, 1), new Object[]{
			"PPP",
			"III",
			"PPP",
			Character.valueOf('P'), Item.paper,
			Character.valueOf('I'), itemTesttubeEmpty
		});
		ModLoader.AddRecipe(new ItemStack(hangableTableOfElements, 1), new Object[]{
			"S",
			"T",
			Character.valueOf('S'), Item.stick,
			Character.valueOf('T'), tableOfElements
		});
		ModLoader.AddRecipe(new ItemStack(chemistryBook, 1), new Object[]{
			"STS",
			"SBS",
			"SSS",
			Character.valueOf('S'), Item.silk,
			Character.valueOf('T'), tableOfElements,
			Character.valueOf('B'), Item.book
		});
		
		electrolysisRecipes = new HashMap<ItemStack, ElectrolysisRecipe>();
		
		
		addElectrolysisRecipe(new ItemStack(Item.potion, 1, 0),
				new Molecule(8, 2),
				new Molecule(1, 2)
		);
		addElectrolysisRecipe(new ItemStack(Item.bucketWater, 1),
				new Molecule(8, 2),
				new Molecule(1, 2)
		);
		addElectrolysisRecipe(new ItemStack(Item.ingotIron, 1),
				new Molecule(26, 5),
				new Molecule(26, 5)
		);
		addElectrolysisRecipe(new ItemStack(Item.ingotGold, 1),
				new Molecule(79, 9),
				new Molecule(79, 9)
		);
		addElectrolysisRecipe(new ItemStack(Block.oreIron, 1),
				Molecule.elementByFormula("Fe", 4),
				Molecule.elementByFormula("Fe", 4)
		);
		addElectrolysisRecipe(new ItemStack(Block.oreGold, 1),
				Molecule.elementByFormula("Au", 8),
				Molecule.elementByFormula("Au", 8)
		);
		addElectrolysisRecipe(new ItemStack(Item.diamond, 1),
				new Molecule(6, 64),
				new Molecule(6, 64)
		);
		addElectrolysisRecipe(new ItemStack(Item.coal, 1),
				new Molecule(6, 1),
				new Molecule(1, 1)
		);
		addElectrolysisRecipe(new ItemStack(Block.sand, 1),
				new Molecule(14, 1),
				new Molecule(8, 2)
		);
		addElectrolysisRecipe(new ItemStack(Item.gunpowder, 1),
				new Molecule(16, 1),
				Molecule.moleculeByFormula("KNO3")
		);
		addElectrolysisRecipe(new ItemStack(Item.sugar, 1),
				new Molecule(0, 21, "C2H8O11"),
				new Molecule(0, 45, "C12H22O11")
		);
		addElectrolysisRecipe(new ItemStack(Item.lightStoneDust, 1),
				new Molecule(15, 1),
				new Molecule(15, 1)
		);
		addElectrolysisRecipe(new ItemStack(Item.redstone, 1),
				Molecule.moleculeByFormula("Cu2O"),
				Molecule.moleculeByFormula("Cu2O")
		);
		addElectrolysisRecipe(new ItemStack(Item.clay, 1),
				new Molecule(14, 1),
				new Molecule(8, 2)
		);
		addElectrolysisRecipe(new ItemStack(Item.flint, 1),
				Molecule.moleculeByFormula("SiO2"),
				Molecule.moleculeByFormula("SiO2")
		);
		addElectrolysisRecipe(new ItemStack(Item.silk, 1),	
				new Molecule(6, 1),
				new Molecule(8, 1)
		);
		addElectrolysisRecipe(new ItemStack(Item.leather, 1),	
				new Molecule(6, 1),
				new Molecule(7, 1)
		);
		addElectrolysisRecipe(new ItemStack(Item.blazeRod, 1),	
				new Molecule(23, 2),
				new Molecule(23, 2)
		);
		addElectrolysisRecipe(new ItemStack(Block.tnt, 1),
				Molecule.moleculeByFormula("C7H5N3O6"),
				Molecule.moleculeByFormula("C7H5N3O6")
		);
		addElectrolysisRecipe(new ItemStack(Item.slimeBall, 1),	
				new Molecule(92, 2),
				new Molecule(6, 16)
		);
		addElectrolysisRecipe(new ItemStack(Item.appleRed, 1),	
				Molecule.moleculeByFormula("C6H8O7"),
				Molecule.moleculeByFormula("C6H8O7")
		);
		addElectrolysisRecipe(new ItemStack(Item.appleGold, 1),	
				Molecule.moleculeByFormula("C6H8O7"),
				Molecule.elementByFormula("Au", 42)
		);
		addElectrolysisRecipe(new ItemStack(blockMinechem, 1, ItemMinechem.thermite),
				Molecule.moleculeByFormula("Fe2O3"),
				Molecule.moleculeByFormula("Fe2O3")
		);
		addElectrolysisRecipe(new ItemStack(Item.bone, 1),	
				Molecule.elementByFormula("Ca", 16),
				Molecule.elementByFormula("Fe", 2)
		);
		addElectrolysisRecipe(new ItemStack(Block.wood, 1, -1),	
				Molecule.moleculeByFormula("C6H10O5"),
				Molecule.moleculeByFormula("C5H10O5")
		);
		addElectrolysisRecipe(new ItemStack(Block.wood, 1, -1),	
				Molecule.moleculeByFormula("C6H10O5"),
				Molecule.moleculeByFormula("C5H10O5")
		);
		addElectrolysisRecipe(new ItemStack(Block.cactus, 1),
				Molecule.moleculeByFormula("C11H17NO3"),
				Molecule.moleculeByFormula("H2O")
		);
		addElectrolysisRecipe(new ItemStack(Item.record13),
				Molecule.moleculeByFormula("C2H3"),
				Molecule.moleculeByFormula("C2H3Cl")
		);
		addElectrolysisRecipe(new ItemStack(Item.record11),
				Molecule.moleculeByFormula("C2H3"),
				Molecule.moleculeByFormula("C5H10O2S")
		);
		addElectrolysisRecipe(new ItemStack(Block.sponge),
				Molecule.moleculeByFormula("C5H10O2S"),
				Molecule.moleculeByFormula("C5H10O2S")
		);
		
		addElectrolysisRandomRecipe(new ItemStack(Item.bucketLava), 
				new Molecule[]{
					Molecule.moleculeByFormula("SiO2"),
					Molecule.moleculeByFormula("TiO2"),
					Molecule.moleculeByFormula("Al2O3"),
					Molecule.moleculeByFormula("Fe2O3"),
					Molecule.moleculeByFormula("FeO"),
					Molecule.moleculeByFormula("MnO"),
					Molecule.moleculeByFormula("MgO"),
					Molecule.moleculeByFormula("CaO"),
					Molecule.moleculeByFormula("Na2O"),
					Molecule.moleculeByFormula("K2O"),
					Molecule.moleculeByFormula("P2O5"),
				},
				new double[]{ 49.20, 1.84, 15.47, 3.97, 7.13, 0.2, 6.73, 9.47, 2.91, 1.1, 0.35}
		);
		
		//Lapis
		addElectrolysisRandomRecipe(new ItemStack(Item.dyePowder, 1, 4), 
				new Molecule[]{
					Molecule.elementByFormula("Na", 3),
					Molecule.elementByFormula("Ca", 6),
					Molecule.elementByFormula("Al", 3),
					Molecule.elementByFormula("O", 12),
					Molecule.elementByFormula("S", 1),
					Molecule.moleculeByFormula("Na3CaAl3Si3O12S"),
					Molecule.moleculeByFormula("Na2O"),
					Molecule.moleculeByFormula("Al2O3"),
					Molecule.moleculeByFormula("SiO2"),
				}, 
				new double[]{13.84, 8.04, 16.24, 38.53, 6.43, 0.5, 1, 1, 1}
		);
		MinechemCraftingRecipe.addRecipe(new ItemStack(Item.dyePowder, 1, 4),	
				Molecule.moleculeByFormula("Na3CaAl3Si3O12S"),
				Molecule.moleculeByFormula("Na3CaAl3Si3O12S")
		);
		

		addElectrolysisRandomRecipe(new ItemStack(Block.obsidian, 1), 
				new Molecule[]{
					Molecule.moleculeByFormula("SiO2"),
					Molecule.moleculeByFormula("MgO"),
					Molecule.moleculeByFormula("Fe3O4")
				}, 
				new double[]{75, 10, 5}
		);
		MinechemCraftingRecipe.addRecipe(new ItemStack(Block.obsidian),
				Molecule.moleculeByFormula("SiO2"),
				Molecule.moleculeByFormula("Fe3O4")
		);
		
		// Add ore dictionary for electrolysis.
		
		ItemStack fuelCan = Items.getItem("filledFuelCan");
		if(fuelCan != null) {
			addElectrolysisRecipe(fuelCan.copy(), 
					Molecule.moleculeByFormula("C3H8"), 
					Molecule.moleculeByFormula("C3H8")
			);
		}
		
		MinecraftForge.registerOreHandler(new IOreHandler() {
			
			@Override
			public void registerOre(String oreClass, ItemStack ore) {
				if(oreClass.equals("oreTin")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(50, 1), new Molecule(50, 1));
				}
				if(oreClass.equals("oreCopper")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(29, 1), new Molecule(29, 1));
				}
				if(oreClass.equals("oreSilver")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(47, 1), new Molecule(47, 1));
				}
				if(oreClass.equals("oreTungsten")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(74, 1), new Molecule(74, 1));
				}
				if(oreClass.equals("gemRuby")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(24, 1), new Molecule(13, 2));
				}
				if(oreClass.equals("gemSapphire")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(22, 1), new Molecule(13, 2));
				}
				if(oreClass.equals("gemEmerald")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(4, 1), new Molecule(13, 2));
				}
				if(oreClass.equals("itemDropUranium")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(92, 4), new Molecule(92, 4));
				}
				if(oreClass.equals("itemRubber")) {
					addElectrolysisRecipe(ore.copy(), 
							Molecule.elementByFormula("C", 2),
							Molecule.moleculeByFormula("ZnO")
					);
				}
			}
		});
		
		MinecraftForge.addDungeonLoot(Molecule.elementByFormula("C", 64).stack, 0.2F);
		MinecraftForge.addDungeonLoot(Molecule.moleculeByFormula("C64C64").stack, 0.007F);
		MinecraftForge.addDungeonLoot(Molecule.elementByFormula("Pb", 12).stack, 0.4F);
		MinecraftForge.addDungeonLoot(Molecule.elementByFormula("Au", 128).stack, 0.4F);
		MinecraftForge.addDungeonLoot(Molecule.elementByFormula("Zr", 1).stack, 0.05F);
		MinecraftForge.addDungeonLoot(Molecule.moleculeByFormula("C21H30O2").stack, 0.01F);
		MinecraftForge.addDungeonLoot(new ItemStack(tableOfElements), 0.6F);
		MinecraftForge.addDungeonLoot(new ItemStack(blockMinechem, 8, ItemMinechem.thermite), 0.2F);
		MinecraftForge.addDungeonLoot(new ItemStack(itemTesttubeEmpty), 1, 12, 64);
		
		tickCount = 0;
		updateDelay = 4;
		ModLoader.SetInGameHook(this, true, true);
		
		//MinecraftForgeClient.registerCustomItemRenderer(itemTesttube.shiftedIndex, new RenderTestTube());
	}
	
	@Override
	public void ModsLoaded() {
		super.ModsLoaded();
	}

	@Override
	public void AddRenderer(Map map) {
		map.put(EntityTableOfElements.class, new RenderTableOfElements());
	}
	
	/**
	 * Add a recipe for the Electrolysis Kit.
	 * @param input - three of these itemstacks will be needed to generate output.
	 * @param output1 - the molecule put in output 1.
	 * @param output2 - the molecule put in output 2.
	 */
	public void addElectrolysisRecipe(ItemStack input, Molecule output1, Molecule output2)
	{
		if(input == null)
			return;
		
		Molecule outputs[] = new Molecule[2];
		outputs[0] = output1;
		outputs[1] = output2;
		ElectrolysisRecipe recipe = new ElectrolysisRecipe(false, outputs, null);
		electrolysisRecipes.put(input, recipe);
		
		// Also create reversable recipe.
		ItemStack out = input.copy();
		out.stackSize = 3;
		MinechemCraftingRecipe.addRecipe(out, output1, output2);
	}
	
	public void addElectrolysisRandomRecipe(ItemStack input, Molecule[] possibleOutputs, double[] weights)
	{
		ElectrolysisRecipe recipe = new ElectrolysisRecipe(true, possibleOutputs, weights);
		electrolysisRecipes.put(input, recipe);
 	}
	
	public void loadChemicalDictionary() throws FileNotFoundException, IOException
	{
		chemicalDictionary = new Properties();
		chemicalDictionary.load(new FileInputStream( fileChemicalDictionary ));
	}
	
	private static String cachedFormula;
	private static String cachedChemicalName;
	public static String findChemicalName(String formula) throws IOException
	{
		if(chemicalDictionary == null || chemicalDictionary.isEmpty())
			return "???";
		else
			return chemicalDictionary.getProperty(formula, "???");
	}
	
	

	@Override
	public boolean OnTickInGame(float f, Minecraft minecraft) {
		tickCount++;
		if(tickCount >= updateDelay) {
			tickCount = 0;
		} else {
			return true;
		}
		
		World world = minecraft.theWorld;
		EntityPlayer player = minecraft.thePlayer;
		
		int currentTries = 0;
		int currentBlock = 0;
		int tries = 50;
		int blocklimit = 10;
		int radius = 8;
		int posx = MathHelper.floor_double(player.posX);
		int posy = MathHelper.floor_double(player.posY);
		int posz = MathHelper.floor_double(player.posZ);
		
		while(currentTries < tries) {
			currentTries++;
			int x = posx + (random.nextInt(radius * 2) - radius);
			int y = posy + (random.nextInt(radius * 2) - radius);
			int z = posz + (random.nextInt(radius * 2) - radius);
			
			if(y > 255)
				y = 255;
			if(y < 1)
				y = 1;
			
			if(world.getBlockId(x, y, z) == Block.chest.blockID) {
				TileEntityChest chest = (TileEntityChest)world.getBlockTileEntity(x, y, z);
				for(int i = 0; i < chest.getSizeInventory(); i++) {
					ItemStack stack = chest.getStackInSlot(i);
					if(stack != null && stack.itemID == itemTesttube.shiftedIndex) {
						stack.getItem().onUpdate(stack, world, player, 0, false);
					}
				}
			}
		}
		return true;
	}

	@Override
	public String getVersion() {
		return "1.4.1";
	}

	@Override
	public void load() {
		
	}

}
