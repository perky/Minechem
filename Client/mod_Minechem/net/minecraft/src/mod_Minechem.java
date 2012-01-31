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
import net.minecraft.src.forge.IOreHandler;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.MinecraftForgeClient;
import net.minecraft.src.ic2.api.*;

public class mod_Minechem extends BaseMod {

	@MLProp public static int blockIDMinechem = 236;
	@MLProp public static int itemIDTestTube = 3001;
	@MLProp public static int itemIDEmptyTestTube = 3000;
	@MLProp public static int itemIDTableOfElements = 3002;
	@MLProp public static boolean requireIC2Power = false;
	public static Item itemTesttubeEmpty;
	public static Item itemTesttube;
	public static Item leadHelmet;
	public static Item leadTorso;
	public static Item leadLeggings;
	public static Item leadBoots;
	public static Item tableOfElements;
	public static Block blockMinechem;
	public static Map<ItemStack, Molecule[]> electrolysisRecipes;
	private static File fileChemicalDictionary = new File(Minecraft.getMinecraftDir(), "/minechem/Chemical Dictionary.txt");
	private static Properties chemicalDictionary;
	private static Random random;
	
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
		leadBoots.setItemName("leadboots");
		leadLeggings.setItemName("leadleggings");
		leadTorso.setItemName("leadTorso");
		leadHelmet.setItemName("leadhelmet");
	}
	
	public mod_Minechem() {
		random = new Random();
		initItemsAndBlocks();
		MinecraftForgeClient.preloadTexture("/minechem/blocktextures.png");
		ModLoader.RegisterBlock(blockMinechem);
		
		ModLoader.AddName(itemTesttubeEmpty, "Empty Test Tube");
		ModLoader.AddName(itemTesttube, "Test Tube");
		ModLoader.AddName(tableOfElements, "Table of Elements");
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
			Character.valueOf('R'), Molecule.elementByFormula("Fr", 1).stack,
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
		
		electrolysisRecipes = new HashMap<ItemStack, Molecule[]>();
		
		addElectrolysisRecipe(new ItemStack(Item.bucketWater, 1),
				new Molecule(8, 2),
				new Molecule(1, 2)
		);
		addElectrolysisRecipe(new ItemStack(Item.potion, 1, 0),
				new Molecule(8, 2),
				new Molecule(1, 2)
		);
		addElectrolysisRecipe(new ItemStack(Item.ingotIron, 1),
				new Molecule(26, 16),
				new Molecule(26, 16)
		);
		addElectrolysisRecipe(new ItemStack(Item.ingotGold, 1),
				new Molecule(79, 32),
				new Molecule(79, 32)
		);
		addElectrolysisRecipe(new ItemStack(Block.oreIron, 1),
				Molecule.elementByFormula("Fe", 4),
				Molecule.elementByFormula("Fe", 4)
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
				new Molecule(0, 5, "KNO3")
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
				new Molecule(29, 1),
				new Molecule(47, 1)
		);
		addElectrolysisRecipe(new ItemStack(Item.clay, 1),
				new Molecule(14, 1),
				new Molecule(8, 2)
		);
		addElectrolysisRecipe(new ItemStack(Item.flint, 1),	
				new Molecule(0, 3, "SiO2"),
				new Molecule(0, 3, "SiO2")
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
				new Molecule(91, 1),
				new Molecule(6, 1)
		);
		addElectrolysisRecipe(new ItemStack(Item.appleRed, 1),	
				Molecule.moleculeByFormula("C6H8O7"),
				Molecule.moleculeByFormula("C6H8O7")
		);
		addElectrolysisRecipe(new ItemStack(Item.appleGold, 1),	
				Molecule.moleculeByFormula("C6H8O7"),
				Molecule.elementByFormula("Au", 84)
		);
		
		// Add ore dictionary for electrolysis.
		
		ItemStack fuelCan = Items.getItem("filledFuelCan");
		if(fuelCan != null) {
			addElectrolysisRecipe(fuelCan.copy(), 
					Molecule.moleculeByFormula("C3H8"), 
					Molecule.moleculeByFormula("C3H8")
			);
			
			MinechemCraftingRecipe.addRecipe(fuelCan.copy(),
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
				if(oreClass.equals("oreUranium")) {
					addElectrolysisRecipe(ore.copy(), new Molecule(92, 1), new Molecule(92, 1));
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
				if(oreClass.equals("ingotTin")) {
					MinechemCraftingRecipe.addRecipe(ore.copy(), 
							Molecule.elementByFormula("Sn", 1),
							Molecule.elementByFormula("Sn", 1)
					);
				}
				if(oreClass.equals("ingotCopper")) {
					MinechemCraftingRecipe.addRecipe(ore.copy(), 
							Molecule.elementByFormula("Cu", 1),
							Molecule.elementByFormula("Cu", 1)
					);
				}
				if(oreClass.equals("ingotSilver")) {
					MinechemCraftingRecipe.addRecipe(ore.copy(), 
							Molecule.elementByFormula("Ag", 1),
							Molecule.elementByFormula("Ag", 1)
					);
				}
				if(oreClass.equals("ingotUranium")) {
					MinechemCraftingRecipe.addRecipe(ore.copy(), 
							Molecule.elementByFormula("U", 2),
							Molecule.elementByFormula("U", 2)
					);
				}
			}
		});

		MinechemCraftingRecipe.addRecipe(new ItemStack(Item.ingotIron, 2),
				Molecule.elementByFormula("Fe", 8),
				Molecule.elementByFormula("Fe", 8)
		);
		MinechemCraftingRecipe.addRecipe(new ItemStack(Item.ingotGold, 2), 
				Molecule.elementByFormula("Au", 16),
				Molecule.elementByFormula("Au", 16)
		);
		MinechemCraftingRecipe.addRecipe(new ItemStack(Item.diamond, 1), 
				Molecule.elementByFormula("C", 32),
				Molecule.elementByFormula("C", 32)
		);
		MinechemCraftingRecipe.addRecipe(new ItemStack(Block.tnt, 2),
				Molecule.moleculeByFormula("C7H5N3O6"),
				Molecule.moleculeByFormula("C7H5N3O6")
		);
		
		ModLoader.SetInGameHook(this, true, true);
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
		electrolysisRecipes.put(input, outputs);
	}
	
	public void addElectrolysisRandomRecipe(ItemStack input, ItemStack[] outputs, int[] randoms)
	{
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
		return "0.2";
	}

	@Override
	public void load() {
	}

}
