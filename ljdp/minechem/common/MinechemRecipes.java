package ljdp.minechem.common;

import static ljdp.minechem.api.core.EnumElement.*;
import static ljdp.minechem.api.core.EnumMolecule.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import cpw.mods.fml.common.registry.GameRegistry;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.core.Element;
import ljdp.minechem.api.core.EnumElement;
import ljdp.minechem.api.core.EnumMolecule;
import ljdp.minechem.api.core.Molecule;
import ljdp.minechem.api.recipe.DecomposerRecipe;
import ljdp.minechem.api.recipe.DecomposerRecipeChance;
import ljdp.minechem.api.recipe.DecomposerRecipeSelect;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.api.util.Util;
import ljdp.minechem.common.utils.MinechemHelper;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class MinechemRecipes {
	
	/*	The static instance of this class */
	private static final MinechemRecipes instance = new MinechemRecipes();
	
	/* The list of decomposer recipes */
	public ArrayList<UnbondingRecipe> unbondingRecipes;
	public ArrayList<SynthesisRecipe> synthesisRecipes;
	
	public static MinechemRecipes getInstance() {
		return instance;
	}
	
	private MinechemRecipes() {
		unbondingRecipes = new ArrayList<UnbondingRecipe>();
		synthesisRecipes = new ArrayList<SynthesisRecipe>();
	}
	
	public void RegisterRecipes() {
		ItemStack stone = new ItemStack(Block.stone);
		ItemStack cobble = new ItemStack(Block.cobblestone);
		ItemStack dirt = new ItemStack(Block.dirt);
		ItemStack sand = new ItemStack(Block.sand);
		ItemStack gravel = new ItemStack(Block.gravel);
		ItemStack glass = new ItemStack(Block.glass);
		ItemStack glasspane = new ItemStack(Block.thinGlass);
		ItemStack ironOre = new ItemStack(Block.oreIron);
		ItemStack goldOre = new ItemStack(Block.oreGold);
		ItemStack diamondOre = new ItemStack(Block.oreDiamond);
		ItemStack emeraldOre = new ItemStack(Block.oreEmerald);
		ItemStack coalOre = new ItemStack(Block.oreCoal);
		ItemStack redstoneOre = new ItemStack(Block.oreRedstone);
		ItemStack lapisOre = new ItemStack(Block.oreLapis);
		ItemStack ironIngot = new ItemStack(Item.ingotIron);
		ItemStack ironBlock = new ItemStack(Block.blockSteel);
		ItemStack atomicManipulator = new ItemStack(MinechemItems.atomicManipulator);
		ItemStack redstone = new ItemStack(Item.redstone);
		
		GameRegistry.addRecipe(new ItemStack(Item.glassBottle, 32), 
				"p#p", 
				"#p#", 
				'p', glasspane
		);
		GameRegistry.addRecipe(MinechemItems.concaveLens, 
				"G G",
				"GGG",
				"G G",
				'G', glass
		);
		GameRegistry.addRecipe(MinechemItems.convexLens, 
				" G ",
				"GGG",
				" G ",
				'G', glass
		);
		GameRegistry.addRecipe(MinechemItems.microscopeLens, 
				"A",
				"B",
				"A",
				'A', MinechemItems.convexLens,
				'B', MinechemItems.concaveLens
		);
		GameRegistry.addRecipe(new ItemStack(MinechemBlocks.microscope), 
				" LI",
				" PI",
				"III",
				'L', MinechemItems.microscopeLens,
				'P', glasspane,
				'I', ironIngot
		);
		GameRegistry.addRecipe(new ItemStack(MinechemItems.atomicManipulator), 
				"PPP",
				"PIP",
				"PPP",
				'P', new ItemStack(Block.pistonBase),
				'I', ironBlock
		);
		GameRegistry.addRecipe(new ItemStack(MinechemBlocks.decomposer), 
				"III",
				"IAI",
				"IRI",
				'A', atomicManipulator,
				'I', ironIngot,
				'R', redstone
		);
		GameRegistry.addRecipe(new ItemStack(MinechemBlocks.synthesis), 
				"IRI",
				"IAI",
				"IDI",
				'A', atomicManipulator,
				'I', ironIngot,
				'R', redstone,
				'D', new ItemStack(Item.diamond)
		);
		
		
		Element carbonStack = element(C,64);
		
		//ORES
		DecomposerRecipe.add(new DecomposerRecipe(ironOre, element(Fe,4)));
		DecomposerRecipe.add(new DecomposerRecipe(goldOre, element(Au,4)));
		DecomposerRecipe.add(new DecomposerRecipe(diamondOre, molecule(fullrene,6)));
		DecomposerRecipe.add(new DecomposerRecipe(coalOre, element(C,32)));
		DecomposerRecipe.add(new DecomposerRecipe(emeraldOre, molecule(beryl,4), element(Cr,4), element(V,4)));
		DecomposerRecipe.add(new DecomposerRecipe(lapisOre, molecule(lazurite,4), molecule(sodalite), molecule(noselite), molecule(calcite), molecule(pyrite)));
		
		//INGOTS DIAMOND EMERALD COAL
		ItemStack goldIngot = new ItemStack(Item.ingotGold);
		ItemStack diamond	= new ItemStack(Item.diamond);
		ItemStack emerald	= new ItemStack(Item.emerald);
		ItemStack coal		= new ItemStack(Item.coal);
		DecomposerRecipe.add(new DecomposerRecipe(ironIngot, element(Fe,2)));
		DecomposerRecipe.add(new DecomposerRecipe(goldIngot, element(Au,2)));
		DecomposerRecipe.add(new DecomposerRecipe(diamond,   molecule(fullrene,4)));
		DecomposerRecipe.add(new DecomposerRecipe(emerald,   molecule(beryl,2), element(Cr,2), element(V,2)));
		DecomposerRecipe.add(new DecomposerRecipe(coal, 	 element(C,8)));
		
		SynthesisRecipe.add(new SynthesisRecipe(ironIngot, false, 1000, element(Fe,2)));
		SynthesisRecipe.add(new SynthesisRecipe(goldIngot, false, 1000, element(Au,2)));
		SynthesisRecipe.add(new SynthesisRecipe(diamond, true, 60000,
				null,				molecule(fullrene),	null,
				molecule(fullrene),	null,				molecule(fullrene),
				null,				molecule(fullrene),	null
				));
		SynthesisRecipe.add(new SynthesisRecipe(emerald, true, 80000,
				null,			element(Cr), 		null,
				element(V),		molecule(beryl,2),	element(V),
				null,			element(Cr),		null
				));
		
		//SAND GLASS FLINT GRAVEL
		ItemStack sandstone = new ItemStack(Block.sandStone);
		ItemStack flint     = new ItemStack(Item.flint);
		DecomposerRecipe.add(new DecomposerRecipe(sandstone, 	molecule(siliconDioxide,16)));
		DecomposerRecipe.add(new DecomposerRecipe(sand, 	 	molecule(siliconDioxide,16)));
		DecomposerRecipe.add(new DecomposerRecipe(glass, 		molecule(siliconDioxide,16)));
		DecomposerRecipe.add(new DecomposerRecipe(glasspane, 	molecule(siliconDioxide,1)));
		DecomposerRecipe.add(new DecomposerRecipeChance(gravel, .35F, molecule(siliconDioxide)));
		DecomposerRecipe.add(new DecomposerRecipeChance(flint,  .5F,  molecule(siliconDioxide)));
		
		Molecule siliconDioxide4 = molecule(siliconDioxide, 4);
		Molecule siliconDioxide1 = molecule(siliconDioxide, 4);
		SynthesisRecipe.add(new SynthesisRecipe(glass, true, 500,
				siliconDioxide4, 	null, 	siliconDioxide4, 
				null,				null,	null,
				siliconDioxide4, 	null,	siliconDioxide4
				));
		SynthesisRecipe.add(new SynthesisRecipe(sand, true, 200,
				siliconDioxide4, siliconDioxide4,
				siliconDioxide4, siliconDioxide4
				));
		SynthesisRecipe.add(new SynthesisRecipe(flint, true, 100,
				null,				siliconDioxide1,	null,
				siliconDioxide1, 	siliconDioxide1, 	siliconDioxide1,
				null,				null,				null
		));
		SynthesisRecipe.add(new SynthesisRecipe(glasspane, true, 50,
				null, 						null, null,
				molecule(siliconDioxide), 	null, null,
				null, 						null, null
		));
		SynthesisRecipe.add(new SynthesisRecipe(gravel, true, 30,
				null, 	null, null,
				null, 	null, null,
				null, 	null, molecule(siliconDioxide)
		));
		
		// FEATHER
		ItemStack feather = new ItemStack(Item.feather);
		DecomposerRecipe.add(new DecomposerRecipe(feather, molecule(water,8), element(N,6)));
		
		SynthesisRecipe.add(new SynthesisRecipe(feather, true, 800,
				element(N), molecule(water,2), element(N),
				element(N), molecule(water,1), element(N),
				element(N), molecule(water,5), element(N)
		));
		
		//BREWING ITEMS
		ItemStack slimeBall = new ItemStack(Item.slimeBall);
		ItemStack blazeRod  = new ItemStack(Item.blazeRod);
		ItemStack blazePowder = new ItemStack(Item.blazePowder);
		ItemStack magmaCream  = new ItemStack(Item.magmaCream);
		ItemStack ghastTear   = new ItemStack(Item.ghastTear);
		ItemStack netherStar  = new ItemStack(Item.netherStar);
		ItemStack spiderEye	  = new ItemStack(Item.spiderEye);
		ItemStack fermentedSpiderEye = new ItemStack(Item.fermentedSpiderEye);
		ItemStack netherWart  = new ItemStack(Item.netherStalkSeeds);
		ItemStack glowstoneBlock = new ItemStack(Block.glowStone);
		ItemStack glowstoneDust = new ItemStack(Item.lightStoneDust);
		ItemStack waterBottle   = new ItemStack(Item.potion, 1, 0);
		ItemStack waterBucket   = new ItemStack(Item.bucketWater);
		
		DecomposerRecipe.add(new DecomposerRecipeSelect(slimeBall, 0.9F,
				new DecomposerRecipe(molecule(polycyanoacrylate)),
				new DecomposerRecipe(element(Nd), element(Hg)),
				new DecomposerRecipe(molecule(water,10))
		));
		DecomposerRecipe.add(new DecomposerRecipe(blazeRod, element(Pu,3)));
		DecomposerRecipe.add(new DecomposerRecipe(blazePowder, element(Pu)));
		DecomposerRecipe.add(new DecomposerRecipe(magmaCream, element(Hg), element(Pu), molecule(polycyanoacrylate,3)));
		DecomposerRecipe.add(new DecomposerRecipe(ghastTear, element(Yb,4), element(No,4)));
		Element hydrogenStack = element(H,64);
		Element heliumStack	  = element(He,64);
		DecomposerRecipe.add(new DecomposerRecipe(netherStar, element(Cn,16), hydrogenStack, hydrogenStack, hydrogenStack, heliumStack, heliumStack, heliumStack, carbonStack, carbonStack));
		DecomposerRecipe.add(new DecomposerRecipe(spiderEye, element(C,2), element(Po)));
		DecomposerRecipe.add(new DecomposerRecipe(fermentedSpiderEye, element(Po), molecule(ethanol)));
		DecomposerRecipe.add(new DecomposerRecipeChance(netherWart, .5F, molecule(amphetamine)));
		DecomposerRecipe.add(new DecomposerRecipe(glowstoneBlock, element(P,4)));
		DecomposerRecipe.add(new DecomposerRecipe(glowstoneDust, element(P)));
		DecomposerRecipe.add(new DecomposerRecipe(waterBottle, molecule(water, 8)));
		DecomposerRecipe.add(new DecomposerRecipe(waterBucket, molecule(water, 16)));
		
		SynthesisRecipe.add(new SynthesisRecipe(blazeRod, true, 15000,
				element(Pu),	null,	null,
				element(Pu),	null,	null,
				element(Pu),	null,	null
		));
		SynthesisRecipe.add(new SynthesisRecipe(magmaCream, true, 5000,
				null,							element(Pu),					null,
				molecule(polycyanoacrylate),	element(Hg),					molecule(polycyanoacrylate),
				null,							molecule(polycyanoacrylate),	null
		));
		SynthesisRecipe.add(new SynthesisRecipe(ghastTear, true, 15000,
				element(Yb), 	element(Yb), 	element(No),
				null, 			element(Yb,2), 	element(No,2),
				null, 			element(No),	null
		));
		SynthesisRecipe.add(new SynthesisRecipe(netherStar, true, 500000,
				heliumStack, 	heliumStack, 	heliumStack,
				carbonStack, 	element(Cn,16), heliumStack,
				hydrogenStack, 	hydrogenStack, 	hydrogenStack
		));
		SynthesisRecipe.add(new SynthesisRecipe(spiderEye, true, 2000,
				element(C),	null,			null,
				null,		element(Po), 	null,
				null, 		null, 			element(C)
		));
		SynthesisRecipe.add(new SynthesisRecipe(glowstoneBlock, true, 500,
				element(P),	null,	element(P),
				element(P),	null, 	element(P),
				null, 		null, 	null
		));
		
		//FOODS
		ItemStack sugar		  = new ItemStack(Item.sugar);
		ItemStack reeds		  = new ItemStack(Item.reed);
		ItemStack pumpkin	  = new ItemStack(Block.pumpkin);
		ItemStack watermelon  = new ItemStack(Block.melon);
		ItemStack glisteningMelon = new ItemStack(Item.speckledMelon);
		ItemStack melon		  = new ItemStack(Item.melon);
		ItemStack carrot	  = new ItemStack(Item.carrot);
		ItemStack goldenCarrot = new ItemStack(Item.goldenCarrot);
		ItemStack cocoaBean	  = new ItemStack(Item.dyePowder, 1, 3);
		ItemStack potato	  = new ItemStack(Item.potato);
		ItemStack bread		  = new ItemStack(Item.bread);
		ItemStack appleRed	  = new ItemStack(Item.appleRed);
		ItemStack appleGold   = new ItemStack(Item.appleGold, 1, 0);
		ItemStack appleGoldEnchanted   = new ItemStack(Item.appleGold, 1, 1);
		DecomposerRecipe.add(new DecomposerRecipeChance(sugar, .75F, molecule(sucrose)));
		DecomposerRecipe.add(new DecomposerRecipeChance(reeds, .65F, molecule(sucrose), element(H,2), element(O)));
		DecomposerRecipe.add(new DecomposerRecipe(cocoaBean, molecule(theobromine)));
		DecomposerRecipe.add(new DecomposerRecipe(pumpkin, molecule(cucurbitacin)));
		DecomposerRecipe.add(new DecomposerRecipe(watermelon, molecule(cucurbitacin), molecule(asparticAcid), molecule(water,16)));
		DecomposerRecipe.add(new DecomposerRecipe(glisteningMelon, molecule(water,4), element(Cr)));
		DecomposerRecipe.add(new DecomposerRecipe(melon, molecule(water)));
		Element carbon40 = element(C,40);
		DecomposerRecipe.add(new DecomposerRecipeSelect(carrot, .05F,
				new DecomposerRecipe(carbon40, element(H,2)),
				new DecomposerRecipe(carbon40, element(H,4)),
				new DecomposerRecipe(carbon40, element(H,8)),
				new DecomposerRecipe(carbon40, element(H,16)),
				new DecomposerRecipe(carbon40, element(H,32)),
				new DecomposerRecipe(carbon40, element(H,64))
		));
		DecomposerRecipe.add(new DecomposerRecipeSelect(goldenCarrot, .2F,
				new DecomposerRecipe(carbon40, element(H,2), element(Au)),
				new DecomposerRecipe(carbon40, element(H,4), element(Au)),
				new DecomposerRecipe(carbon40, element(H,8), element(Au)),
				new DecomposerRecipe(carbon40, element(H,16), element(Au)),
				new DecomposerRecipe(carbon40, element(H,32), element(Au)),
				new DecomposerRecipe(carbon40, element(H,64), element(Au))
		));
		DecomposerRecipe.add(new DecomposerRecipeChance(potato, .4F, molecule(water,8), element(K,2), molecule(cellulose)));
		DecomposerRecipe.add(new DecomposerRecipeChance(bread, .1F, molecule(starch), molecule(sucrose)));
		DecomposerRecipe.add(new DecomposerRecipe(appleRed, molecule(malicAcid)));
		DecomposerRecipe.add(new DecomposerRecipe(appleGold, molecule(malicAcid), element(Au,8)));
		DecomposerRecipe.add(new DecomposerRecipe(appleGold, molecule(malicAcid), element(Au,64), element(Np)));
		
		SynthesisRecipe.add(new SynthesisRecipe(sugar, false, 400, molecule(sucrose)));
		SynthesisRecipe.add(new SynthesisRecipe(appleRed, false, 400, molecule(malicAcid), molecule(water,2)));
		SynthesisRecipe.add(new SynthesisRecipe(cocoaBean, false, 400, molecule(theobromine)));
		SynthesisRecipe.add(new SynthesisRecipe(pumpkin, false, 400, molecule(cucurbitacin)));
		
		// EXPLOSIVES
		ItemStack gunpowder = new ItemStack(Item.gunpowder);
		ItemStack tntBlock  = new ItemStack(Block.tnt);
		DecomposerRecipe.add(new DecomposerRecipe(gunpowder, molecule(potassiumNitrate), element(S,2), element(C)));
		DecomposerRecipe.add(new DecomposerRecipe(tntBlock, molecule(tnt)));
		
		SynthesisRecipe.add(new SynthesisRecipe(tntBlock, false, 1000, molecule(tnt)));
		SynthesisRecipe.add(new SynthesisRecipe(gunpowder, true, 600,
				molecule(potassiumNitrate), element(C), null,
				element(S,2), 				null, 		null,
				null, 						null, 		null
		));
		
		// WOOD
		ItemStack anyLog 	 = new ItemStack(Block.wood, 1, -1);
		ItemStack anyPlank   = new ItemStack(Block.planks, 1, -1);
		ItemStack stick		 = new ItemStack(Item.stick);
		ItemStack log1		 = new ItemStack(Block.wood, 1, 0);
		ItemStack log2		 = new ItemStack(Block.wood, 1, 1);
		ItemStack log3		 = new ItemStack(Block.wood, 1, 2);
		ItemStack log4		 = new ItemStack(Block.wood, 1, 3);
		ItemStack woodDoor   = new ItemStack(Item.doorWood);
		ItemStack woodPlate  = new ItemStack(Block.pressurePlatePlanks, 1, -1);
		ItemStack woodStairs = new ItemStack(Block.stairCompactPlanks, 1, -1);
		
		DecomposerRecipe.add(new DecomposerRecipeChance(anyLog, .5F, molecule(cellulose,8)));
		DecomposerRecipe.add(new DecomposerRecipeChance(anyPlank, .4F, molecule(cellulose,2)));
		DecomposerRecipe.add(new DecomposerRecipeChance(stick, .3F, molecule(cellulose)));
		DecomposerRecipe.add(new DecomposerRecipeChance(woodDoor, .4F, molecule(cellulose, 12)));
		DecomposerRecipe.add(new DecomposerRecipeChance(woodPlate, .4F, molecule(cellulose, 4)));
		DecomposerRecipe.add(new DecomposerRecipeChance(woodStairs, .4F, molecule(cellulose, 12)));
		
		Molecule cellulose1 = molecule(cellulose,1);
		SynthesisRecipe.add(new SynthesisRecipe(log1, true, 100,
				cellulose1,	cellulose1,	cellulose1,
				null,		cellulose1,	null,
				null,		null,		null
		));
		SynthesisRecipe.add(new SynthesisRecipe(log2, true, 100,
				null,		null,		null,
				null,		cellulose1,	null,
				cellulose1,	cellulose1,	cellulose1
		));
		SynthesisRecipe.add(new SynthesisRecipe(log3, true, 100,
				cellulose1,	null,		cellulose1,
				null,		null,		null,
				cellulose1,	null,		cellulose1
		));
		SynthesisRecipe.add(new SynthesisRecipe(log4, true, 100,
				cellulose1,	null,		null,
				cellulose1,	cellulose1,	null,
				cellulose1,	null,		null
		));
		
		// DYES
		ItemStack blackDye = new ItemStack(Item.dyePowder, 1, 0);
		ItemStack redDye = new ItemStack(Item.dyePowder, 1, 1);
		ItemStack greenDye = new ItemStack(Item.dyePowder, 1, 2);
		ItemStack lapis  = new ItemStack(Item.dyePowder, 1, 4);
		ItemStack purpleDye = new ItemStack(Item.dyePowder, 1, 5);
		ItemStack cyanDye = new ItemStack(Item.dyePowder, 1, 6);
		ItemStack lightGrayDye = new ItemStack(Item.dyePowder, 1, 7);
		ItemStack grayDye = new ItemStack(Item.dyePowder, 1, 8);
		ItemStack pinkDye = new ItemStack(Item.dyePowder, 1, 9);
		ItemStack limeDye = new ItemStack(Item.dyePowder, 1, 10);
		ItemStack yellowDye = new ItemStack(Item.dyePowder, 1, 11);
		ItemStack lightBlueDye = new ItemStack(Item.dyePowder, 1, 12);
		ItemStack magentaDye = new ItemStack(Item.dyePowder, 1, 13);
		ItemStack orangeDye = new ItemStack(Item.dyePowder, 1, 14);
		ItemStack whiteDye = new ItemStack(Item.dyePowder, 1, 15);
		
		DecomposerRecipe.add(new DecomposerRecipe(blackDye, 	molecule(blackPigment)));
		DecomposerRecipe.add(new DecomposerRecipe(redDye, 		molecule(redPigment)));
		DecomposerRecipe.add(new DecomposerRecipe(greenDye, 	molecule(greenPigment)));
		DecomposerRecipe.add(new DecomposerRecipe(lapis,  		molecule(lazurite)));
		DecomposerRecipe.add(new DecomposerRecipe(purpleDye, 	molecule(purplePigment)));
		DecomposerRecipe.add(new DecomposerRecipe(cyanDye,		molecule(lightbluePigment), molecule(whitePigment)));
		DecomposerRecipe.add(new DecomposerRecipe(lightGrayDye,	molecule(whitePigment), molecule(blackPigment)));
		DecomposerRecipe.add(new DecomposerRecipe(grayDye, 		molecule(whitePigment), molecule(blackPigment, 2)));
		DecomposerRecipe.add(new DecomposerRecipe(pinkDye, 		molecule(redPigment), molecule(whitePigment)));
		DecomposerRecipe.add(new DecomposerRecipe(limeDye, 		molecule(limePigment)));
		DecomposerRecipe.add(new DecomposerRecipe(yellowDye, 	molecule(yellowPigment)));
		DecomposerRecipe.add(new DecomposerRecipe(lightBlueDye, molecule(lightbluePigment)));
		DecomposerRecipe.add(new DecomposerRecipe(magentaDye, 	molecule(lightbluePigment), molecule(redPigment)));
		DecomposerRecipe.add(new DecomposerRecipe(orangeDye, 	molecule(orangePigment)));
		DecomposerRecipe.add(new DecomposerRecipe(whiteDye, 	molecule(whitePigment)));
		
		SynthesisRecipe.add(new SynthesisRecipe(blackDye, 		false, 50, molecule(blackPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(redDye, 		false, 50, molecule(redPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(greenDye, 		false, 50, molecule(greenPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(lapis,  		false, 50, molecule(lazurite)));
		SynthesisRecipe.add(new SynthesisRecipe(purpleDye, 		false, 50, molecule(purplePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(cyanDye,		false, 50, molecule(lightbluePigment), molecule(whitePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(lightGrayDye,	false, 50, molecule(whitePigment), molecule(blackPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(grayDye, 		false, 50, molecule(whitePigment), molecule(blackPigment, 2)));
		SynthesisRecipe.add(new SynthesisRecipe(pinkDye, 		false, 50, molecule(redPigment), molecule(whitePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(limeDye, 		false, 50, molecule(limePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(yellowDye, 		false, 50, molecule(yellowPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(lightBlueDye, 	false, 50, molecule(lightbluePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(magentaDye, 	false, 50, molecule(lightbluePigment), molecule(redPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(orangeDye, 		false, 50, molecule(orangePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(whiteDye, 		false, 50, molecule(whitePigment)));
		
		ItemStack whitewool = new ItemStack(Block.cloth, 1, 0);
		ItemStack orangewool = new ItemStack(Block.cloth, 1, 1);
		ItemStack magentawool = new ItemStack(Block.cloth, 1, 2);
		ItemStack lightbluewool = new ItemStack(Block.cloth, 1, 3);
		ItemStack yellowwool = new ItemStack(Block.cloth, 1, 4);
		ItemStack limewool = new ItemStack(Block.cloth, 1, 5);
		ItemStack pinkwool = new ItemStack(Block.cloth, 1, 6);
		ItemStack graywool = new ItemStack(Block.cloth, 1, 7);
		ItemStack lightgraywool = new ItemStack(Block.cloth, 1, 8);
		ItemStack cyanwool = new ItemStack(Block.cloth, 1, 9);
		ItemStack purplewool = new ItemStack(Block.cloth, 1, 10);
		ItemStack bluewool = new ItemStack(Block.cloth, 1, 11);
		ItemStack brownwool = new ItemStack(Block.cloth, 1, 12);
		ItemStack greenwool = new ItemStack(Block.cloth, 1, 13);
		ItemStack redwool = new ItemStack(Block.cloth, 1, 14);
		ItemStack blackwool = new ItemStack(Block.cloth, 1, 15);
		
		DecomposerRecipe.add(new DecomposerRecipeChance(redwool, 		.6F, molecule(glycine), molecule(redPigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(greenwool, 		.6F, molecule(glycine), molecule(greenPigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(bluewool,  		.6F, molecule(glycine), molecule(lazurite)));
		DecomposerRecipe.add(new DecomposerRecipeChance(purplewool, 	.6F, molecule(glycine), molecule(purplePigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(cyanwool,		.6F, molecule(glycine), molecule(lightbluePigment), molecule(whitePigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(lightgraywool,	.6F, molecule(glycine), molecule(whitePigment), molecule(blackPigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(graywool, 		.6F, molecule(glycine), molecule(whitePigment), molecule(blackPigment, 2)));
		DecomposerRecipe.add(new DecomposerRecipeChance(pinkwool, 		.6F, molecule(glycine), molecule(redPigment), molecule(whitePigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(limewool, 		.6F, molecule(glycine), molecule(limePigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(yellowwool, 	.6F, molecule(glycine), molecule(yellowPigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(lightbluewool, 	.6F, molecule(glycine), molecule(lightbluePigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(magentawool, 	.6F, molecule(glycine), molecule(lightbluePigment), molecule(redPigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(orangewool, 	.6F, molecule(glycine), molecule(orangePigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(whitewool, 		.6F, molecule(glycine), molecule(whitePigment)));
		DecomposerRecipe.add(new DecomposerRecipeChance(blackwool, 		.6F, molecule(glycine), molecule(blackPigment)));
		
		SynthesisRecipe.add(new SynthesisRecipe(redwool, 		false, 50, molecule(glycine), molecule(redPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(greenwool, 		false, 50, molecule(glycine), molecule(greenPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(bluewool,  		false, 50, molecule(glycine), molecule(lazurite)));
		SynthesisRecipe.add(new SynthesisRecipe(purplewool, 	false, 50, molecule(glycine), molecule(purplePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(cyanwool,		false, 50, molecule(glycine), molecule(lightbluePigment), molecule(whitePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(lightgraywool,	false, 50, molecule(glycine), molecule(whitePigment), molecule(blackPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(graywool, 		false, 50, molecule(glycine), molecule(whitePigment), molecule(blackPigment, 2)));
		SynthesisRecipe.add(new SynthesisRecipe(pinkwool, 		false, 50, molecule(glycine), molecule(redPigment), molecule(whitePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(limewool, 		false, 50, molecule(glycine), molecule(limePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(yellowwool, 	false, 50, molecule(glycine), molecule(yellowPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(lightbluewool, 	false, 50, molecule(glycine), molecule(lightbluePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(magentawool, 	false, 50, molecule(glycine), molecule(lightbluePigment), molecule(redPigment)));
		SynthesisRecipe.add(new SynthesisRecipe(orangewool, 	false, 50, molecule(glycine), molecule(orangePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(whitewool, 		false, 50, molecule(glycine), molecule(whitePigment)));
		SynthesisRecipe.add(new SynthesisRecipe(blackwool, 		false, 50, molecule(glycine), molecule(blackPigment)));
		
		
		// RECORDS
		Molecule pvc = molecule(polyvinylChloride);
		ItemStack record1 = new ItemStack(Item.record13);
		ItemStack record2 = new ItemStack(Item.recordCat);
		ItemStack record3 = new ItemStack(Item.recordFar);
		ItemStack record4 = new ItemStack(Item.recordMall);
		ItemStack record5 = new ItemStack(Item.recordMellohi);
		ItemStack record6 = new ItemStack(Item.recordStal);
		ItemStack record7 = new ItemStack(Item.recordStrad);
		ItemStack record8 = new ItemStack(Item.recordWard);
		ItemStack record9 = new ItemStack(Item.recordChirp);
		
		DecomposerRecipe.add(new DecomposerRecipe(record1, pvc));
		DecomposerRecipe.add(new DecomposerRecipe(record2, pvc));
		DecomposerRecipe.add(new DecomposerRecipe(record3, pvc));
		DecomposerRecipe.add(new DecomposerRecipe(record4, pvc));
		DecomposerRecipe.add(new DecomposerRecipe(record5, pvc));
		DecomposerRecipe.add(new DecomposerRecipe(record6, pvc));
		DecomposerRecipe.add(new DecomposerRecipe(record7, pvc));
		DecomposerRecipe.add(new DecomposerRecipe(record8, pvc));
		DecomposerRecipe.add(new DecomposerRecipe(record9, pvc));
		
		SynthesisRecipe.add(new SynthesisRecipe(record1, false, 1000, pvc));
		SynthesisRecipe.add(new SynthesisRecipe(record2, false, 1000, null, pvc));
		SynthesisRecipe.add(new SynthesisRecipe(record3, false, 1000, null, null, pvc));
		SynthesisRecipe.add(new SynthesisRecipe(record4, false, 1000, null, null, null, pvc));
		SynthesisRecipe.add(new SynthesisRecipe(record5, false, 1000, null, null, null, null, pvc));
		SynthesisRecipe.add(new SynthesisRecipe(record6, false, 1000, null, null, null, null, null, pvc));
		SynthesisRecipe.add(new SynthesisRecipe(record7, false, 1000, null, null, null, null, null, null, pvc));
		SynthesisRecipe.add(new SynthesisRecipe(record8, false, 1000, null, null, null, null, null, null, null, pvc));
		SynthesisRecipe.add(new SynthesisRecipe(record9, false, 1000, null, null, null, null, null, null, null, null, pvc));
		
		
		// PLANTS
		ItemStack brownMushroom = new ItemStack(Block.mushroomBrown);
		ItemStack redMushroom   = new ItemStack(Block.mushroomRed);
		ItemStack cactus        = new ItemStack(Block.cactus);
		DecomposerRecipe.add(new DecomposerRecipe(brownMushroom,  molecule(psilocybin), molecule(water,2)));
		DecomposerRecipe.add(new DecomposerRecipe(redMushroom,    molecule(muscarine),  molecule(water,2)));
		DecomposerRecipe.add(new DecomposerRecipe(cactus, molecule(mescaline), molecule(water,20)));
		
		SynthesisRecipe.add(new SynthesisRecipe(cactus, true, 200,
				molecule(water,5),		null,					molecule(water,5),
				null,					molecule(mescaline),	null,
				molecule(water,5),		null,					molecule(water,5)
		));
		
		// REDSTONE
		DecomposerRecipe.add(new DecomposerRecipeChance(redstoneOre, .8F, molecule(iron3oxide,6), molecule(strontiumNitrate,6)));
		DecomposerRecipe.add(new DecomposerRecipeChance(redstone, .42F, molecule(iron3oxide), molecule(strontiumNitrate)));
		
		SynthesisRecipe.add(new SynthesisRecipe(redstone, true, 100,
				null,	null,						molecule(iron3oxide),
				null,	molecule(strontiumNitrate), null,
				null,	null,						null
		));
		
		
		ItemStack enderPearl = new ItemStack(Item.enderPearl);
		DecomposerRecipe.add(new DecomposerRecipe(enderPearl, element(Es), molecule(calciumCarbonate,8)));
		SynthesisRecipe.add(new SynthesisRecipe(enderPearl, true, 5000,
				molecule(calciumCarbonate), molecule(calciumCarbonate), molecule(calciumCarbonate),
				molecule(calciumCarbonate), element(Es),				molecule(calciumCarbonate),
				molecule(calciumCarbonate), molecule(calciumCarbonate), molecule(calciumCarbonate)
		));
		
		ItemStack obsidian = new ItemStack(Block.obsidian);
		DecomposerRecipe.add(new DecomposerRecipe(obsidian, molecule(siliconDioxide,16), molecule(magnetite,8), molecule(magnesiumOxide,8)));
		SynthesisRecipe.add(new SynthesisRecipe(obsidian, true, 1000,
				molecule(siliconDioxide,4), molecule(siliconDioxide,4), molecule(siliconDioxide,4),
				molecule(siliconDioxide,4), molecule(magnetite, 8),		molecule(magnesiumOxide,2),
				molecule(magnesiumOxide,2), molecule(magnesiumOxide,2), molecule(magnesiumOxide,2)
		));
		
		
		// MOB DROPS
		ItemStack bone = new ItemStack(Item.bone);
		ItemStack silk = new ItemStack(Item.silk);
		ItemStack anyWool = new ItemStack(Block.cloth, 1, -1);
		ItemStack whiteWool = new ItemStack(Block.cloth, 1, 0);
		DecomposerRecipe.add(new DecomposerRecipe(bone, molecule(hydroxylapatite)));
		DecomposerRecipe.add(new DecomposerRecipeChance(silk, .45F, molecule(serine), molecule(glycine), molecule(alinine)));
		
		SynthesisRecipe.add(new SynthesisRecipe(bone, false, 100, molecule(hydroxylapatite)));
		SynthesisRecipe.add(new SynthesisRecipe(silk, true, 150, molecule(serine), molecule(glycine), molecule(alinine)));
		
		// STONE EARTH
		ItemStack cobbleStone = new ItemStack(Block.cobblestone);
		DecomposerRecipe.add(new DecomposerRecipeSelect(stone, .2F,
				new DecomposerRecipe(element(Si), element(O)),
				new DecomposerRecipe(element(Fe), element(O)),
				new DecomposerRecipe(element(Mg), element(O)),
				new DecomposerRecipe(element(Ti), element(O)),
				new DecomposerRecipe(element(Pb), element(O)),
				new DecomposerRecipe(element(Zn), element(O))
		));
		DecomposerRecipe.add(new DecomposerRecipeSelect(cobbleStone, .1F,
				new DecomposerRecipe(element(Si), element(O)),
				new DecomposerRecipe(element(Fe), element(O)),
				new DecomposerRecipe(element(Mg), element(O)),
				new DecomposerRecipe(element(Ti), element(O)),
				new DecomposerRecipe(element(Pb), element(O)),
				new DecomposerRecipe(element(Zn), element(O))
		));
		DecomposerRecipe.add(new DecomposerRecipeSelect(dirt, .07F,
				new DecomposerRecipe(element(Si), element(O)),
				new DecomposerRecipe(element(Fe), element(O)),
				new DecomposerRecipe(element(Mg), element(O)),
				new DecomposerRecipe(element(Ti), element(O)),
				new DecomposerRecipe(element(Pb), element(O)),
				new DecomposerRecipe(element(Zn), element(O))
		));

		addSynthesisRecipesFromMolecules();
		addUnusedSynthesisRecipes();
	}
	
	private void addSynthesisRecipesFromMolecules() {
		for(EnumMolecule aMolecule : EnumMolecule.molecules) {
			ArrayList<Chemical> components = aMolecule.components();
			ItemStack output = new ItemStack(MinechemItems.molecule, 1, aMolecule.id());
			SynthesisRecipe.add(new SynthesisRecipe(output, false, 50, components));
		}
	}
	
	private void addUnusedSynthesisRecipes() {
		for(DecomposerRecipe decomposerRecipe : DecomposerRecipe.recipes) {
			boolean hasRecipe = false;
			for(SynthesisRecipe synthesisRecipe : SynthesisRecipe.recipes) {
				if(Util.stacksAreSameKind(synthesisRecipe.getOutput(), decomposerRecipe.getInput())) {
					hasRecipe = true;
					break;
				}
			}
			if(!hasRecipe) {
				ArrayList<Chemical> chemicals = decomposerRecipe.getOutputRaw();
				if(chemicals != null)
					SynthesisRecipe.add(new SynthesisRecipe(decomposerRecipe.getInput(), false, 100, chemicals));
			}
		}
	}
	
	@ForgeSubscribe
	public void oreEvent(OreRegisterEvent event) {
		if(event.Name.contains("oreCopper")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Cu,4)));
		}
		else if(event.Name.contains("ingotCopper")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Cu,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Cu,2)));
		}
		else if(event.Name.contains("oreSilver")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Ag,4)));
		}
		else if(event.Name.contains("ingotSilver")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Ag,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Ag,2)));
		}
		else if(event.Name.contains("oreTin")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Sn,4)));
		}
		else if(event.Name.contains("ingotTin")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Sn,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Sn,2)));
		}
		else if(event.Name.contains("oreLead")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Pb,4)));
		}
		else if(event.Name.contains("ingotLead")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Pb,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Pb,2)));
		}
		else if(event.Name.contains("ingotBronze")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Sn), element(Cu,9)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Sn), element(Cu,9)));
		}
		else if(event.Name.contains("oreUranium")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(U,4)));
		}
		else if(event.Name.contains("ingotUranium")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(U,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 5000, element(U,2)));
		}
		else if(event.Name.contains("itemDropUranium")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(U,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 5000, element(U,2)));
		}
		else if(event.Name.contains("ingotBrass")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Cu,3), element(Zn,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Cu,3), element(Zn,2)));
		}
		else if(event.Name.contains("ingotSteel")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Fe,4), element(C)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Fe,4), element(C)));
		}
		else if(event.Name.contains("ingotTitanium")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Ti,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Ti,2)));
		}
		else if(event.Name.contains("gemApatite")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Ca,5), molecule(phosphate,4), element(F)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Ca,5), molecule(phosphate,4), element(F)));
		}
		else if(event.Name.contains("ingotRefinedIron")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Fe,3)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Fe,3)));
		}
		else if(event.Name.contains("ingotChrome")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Cr,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Cr,2)));
		}
		else if(event.Name.contains("ingotAluminium")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Al,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Al,2)));
		}
		else if(event.Name.contains("ingotIridium")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, element(Ir,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, element(Ir,2)));
		}
		else if(event.Name.contains("gemRuby")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, molecule(aluminiumOxide), element(Cr)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, molecule(aluminiumOxide), element(Cr)));
		}
		else if(event.Name.contains("gemSapphire")) {
			DecomposerRecipe.add(new DecomposerRecipe(event.Ore, molecule(aluminiumOxide,2)));
			SynthesisRecipe.add(new SynthesisRecipe(event.Ore, false, 1000, molecule(aluminiumOxide,2)));
		}
		
	}
	
	private Element element(EnumElement element, int amount) {
		return new Element(element, amount);
	}
	
	private Element element(EnumElement element) {
		return new Element(element, 1);
	}
	
	private Molecule molecule(EnumMolecule molecule, int amount) {
		return new Molecule(molecule, amount);
	}
	
	private Molecule molecule(EnumMolecule molecule) {
		return new Molecule(molecule, 1);
	}
}
