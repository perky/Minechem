package ljdp.minechem.common;

import static ljdp.minechem.common.EnumElement.*;
import static ljdp.minechem.common.EnumMolecule.*;

import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.registry.GameRegistry;

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
		ItemStack glassPane = new ItemStack(Block.thinGlass);
		ItemStack ironIngot = new ItemStack(Item.ingotIron);
		ItemStack ironBlock = new ItemStack(Block.blockSteel);
		ItemStack atomicManipulator = new ItemStack(MinechemItems.atomicManipulator);
		ItemStack glass = new ItemStack(Block.glass);
		ItemStack redstone = new ItemStack(Item.redstone);
		GameRegistry.addRecipe(new ItemStack(Item.glassBottle, 32), 
				"p#p", 
				"#p#", 
				'p', glassPane
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
				'P', glassPane,
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
	
		
		addRandomDecomposerRecipe(new ItemStack(Block.stone), 0.05F, 
				itemStacks(element(Si), element(O)),
				itemStacks(element(K,2), element(O)),
				itemStacks(element(Na,2), element(O)),
				itemStacks(element(Ca), element(O)),
				itemStacks(element(Mg), element(O)),
				itemStacks(element(Ti), element(O)),
				itemStacks(element(Mn), element(O))
				);
		
		addRandomDecomposerRecipe(new ItemStack(Block.cobblestone), 0.01F,
				itemStacks(element(Si), element(O)),
				itemStacks(element(K,2), element(O)),
				itemStacks(element(Na,2), element(O)),
				itemStacks(element(Ca), element(O)),
				itemStacks(element(Mg), element(O)),
				itemStacks(element(Ti), element(O)),
				itemStacks(element(Mn), element(O))
				);
		
		addRandomDecomposerRecipe(new ItemStack(Block.dirt), 0.005F,
				itemStacks(element(H,2), element(O,6), element(Si), element(Ca), element(C))
		);
		
		addDecomposerRecipe(new ItemStack(Block.oreIron), element(Fe,2));
		addDecomposerRecipe(new ItemStack(Item.ingotIron), element(Fe,1));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.ingotIron));
		addDecomposerRecipe(new ItemStack(Item.doorSteel), molecule(cellulose,6));
		
		addDecomposerRecipe(new ItemStack(Block.oreGold), element(Au,2));
		addDecomposerRecipe(new ItemStack(Item.ingotGold), element(Au,1));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.ingotGold));
		
		addDecomposerRecipe(new ItemStack(Block.oreDiamond), element(C,64), element(C,64));
		addDecomposerRecipe(new ItemStack(Item.diamond), element(C,64));
		addShapedSynthesisRecipe(new ItemStack(Item.diamond), 
				null, 			element(C,64), 		null,
				element(C,64),	null,				element(C,64),
				null,			element(C,64), 		null);

		addDecomposerRecipe(new ItemStack(Block.oreCoal), element(C,2));
		addDecomposerRecipe(new ItemStack(Item.coal), element(C,1));
		
		addDecomposerRecipe(new ItemStack(Item.gunpowder), molecule(potassiumNitrate), element(S,2), element(C,1));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.gunpowder));
		
		addDecomposerRecipe(new ItemStack(Block.sand), 		molecule(siliconDioxide, 8));
		addDecomposerRecipe(new ItemStack(Block.glass), 	molecule(siliconDioxide, 16));
		addDecomposerRecipe(new ItemStack(Block.thinGlass), molecule(siliconDioxide, 1));
		addDecomposerRecipe(new ItemStack(Block.sandStone), molecule(siliconDioxide, 12));
		addShapedSynthesisRecipe(new ItemStack(Block.glass), 
				molecule(siliconDioxide,2), molecule(siliconDioxide,2), molecule(siliconDioxide,2),
				molecule(siliconDioxide,2), null, 						molecule(siliconDioxide,2),
				molecule(siliconDioxide,2), molecule(siliconDioxide,2), molecule(siliconDioxide,2));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Block.sand));
		
		addDecomposerRecipe(new ItemStack(Block.wood, 1, -1), molecule(cellulose,8));
		ItemStack cellulose2 = molecule(cellulose,2);
		addShapedSynthesisRecipe(new ItemStack(Block.wood, 1, 0), 
				cellulose2,	cellulose2,	cellulose2,
				null,		cellulose2,	null,
				null,		null,		null);
		addShapedSynthesisRecipe(new ItemStack(Block.wood, 1, 1), 
				null,		null,		null,
				null,		cellulose2,	null,
				cellulose2,	cellulose2,	cellulose2);
		addShapedSynthesisRecipe(new ItemStack(Block.wood, 1, 2), 
				cellulose2,	null,		cellulose2,
				null,		null,		null,
				cellulose2,	null,		cellulose2);
		addShapedSynthesisRecipe(new ItemStack(Block.wood, 1, 3), 
				cellulose2,	null,		null,
				cellulose2,	cellulose2,	null,
				cellulose2,	null,		null);
		addDecomposerRecipe(new ItemStack(Block.planks, 1, -1), molecule(cellulose, 4));
		
		addDecomposerRecipe(new ItemStack(Item.stick), molecule(cellulose,1));
		addDecomposerRecipe(new ItemStack(Item.doorWood), molecule(cellulose,24));
		
		addDecomposerRecipe(new ItemStack(Item.flint), molecule(siliconDioxide,16));
		addDecomposerRecipe(new ItemStack(Block.gravel), molecule(siliconDioxide,4));
		
		addDecomposerRecipe(new ItemStack(Item.appleRed), molecule(malicAcid));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.appleRed));
		
		addDecomposerRecipe(new ItemStack(Item.appleGold, 1, 0), molecule(malicAcid), element(Au));
		addDecomposerRecipe(new ItemStack(Item.appleGold, 1, 1), molecule(malicAcid), element(Au,36), element(Au,36), element(Mc));
		
		addDecomposerRecipe(new ItemStack(Block.oreLapis),
				molecule(lazurite, 4),
				molecule(sodalite, 4),
				molecule(noselite, 4),
				molecule(calcite, 4),
				molecule(pyrite, 4)
				);
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 4), molecule(lazurite));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 4));
		
		addDecomposerRecipe(new ItemStack(Block.glowStone), element(P,4));
		addDecomposerRecipe(new ItemStack(Item.lightStoneDust), element(P));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.lightStoneDust));
		
		addDecomposerRecipe(new ItemStack(Block.tnt), molecule(tnt, 4));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Block.tnt));
		
		addDecomposerRecipe(new ItemStack(Item.potion, 1, 0), element(H,2), element(O), molecule(siliconDioxide, 16*3));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.potion, 1, 0));
		addDecomposerRecipe(new ItemStack(Item.bucketWater), element(H,2), element(O), element(Fe,3));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.bucketWater));
		
		addDecomposerRecipe(new ItemStack(Block.mushroomBrown), molecule(psilocybin));
		
		addDecomposerRecipe(new ItemStack(Block.oreRedstone), molecule(iron3oxide, 6), molecule(strontiumNitrate, 6));
		addDecomposerRecipe(new ItemStack(Item.redstone), molecule(iron3oxide), molecule(strontiumNitrate));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.redstone));
		
		addDecomposerRecipe(new ItemStack(Item.enderPearl), element(Es), molecule(calciumCarbonate,8));
		addShapedSynthesisRecipe(new ItemStack(Item.enderPearl), 
				molecule(calciumCarbonate), molecule(calciumCarbonate), molecule(calciumCarbonate),
				molecule(calciumCarbonate), element(Es),				molecule(calciumCarbonate),
				molecule(calciumCarbonate), molecule(calciumCarbonate), molecule(calciumCarbonate)
		);
		
		addDecomposerRecipe(new ItemStack(Block.obsidian), molecule(siliconDioxide, 16), molecule(magnetite, 8), molecule(magnesiumOxide, 8));
		addShapedSynthesisRecipe(new ItemStack(Block.obsidian),
			molecule(siliconDioxide,4), molecule(siliconDioxide,4), molecule(siliconDioxide,4),
			molecule(siliconDioxide,4), molecule(magnetite, 8),		molecule(magnesiumOxide,2),
			molecule(magnesiumOxide,2), molecule(magnesiumOxide,2), molecule(magnesiumOxide,2)
		);
		
		
		addRandomDecomposerRecipe(new ItemStack(Block.reed), 0.1F, molecule(sucrose), element(H,2), element(O), molecule(asparticAcid));
		addRandomDecomposerRecipe(new ItemStack(Item.sugar), 0.2F, molecule(sucrose));
		
		addShapedSynthesisRecipe(new ItemStack(Block.reed, 32),
				molecule(sucrose), 	molecule(sucrose), 	null,
				null,				null,				null,
				null,				null,				null);
		addShapedSynthesisRecipe(new ItemStack(Item.sugar, 40),
				molecule(sucrose), 	null, 				null,
				null,				molecule(sucrose),	null,
				null,				null,				null);
		
		addDecomposerRecipe(new ItemStack(Block.pumpkin), molecule(cucurbitacin));
		addDecomposerRecipe(new ItemStack(Block.melon), molecule(cucurbitacin), molecule(asparticAcid));
		
		addDecomposerRecipe(new ItemStack(Item.bone), molecule(hydroxylapatite, 3));
		// BONEMEAL
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 15), molecule(hydroxylapatite));
		
		addShapedSynthesisRecipe(new ItemStack(Item.bone),
				molecule(hydroxylapatite),	null,						null,
				null,						molecule(hydroxylapatite),	null,
				null,						null,						molecule(hydroxylapatite)
		);
		
		addRandomDecomposerRecipe(new ItemStack(Item.silk), 0.1F, molecule(serine), molecule(glycine), molecule(alinine));
		addShapedSynthesisRecipe(new ItemStack(Item.silk, 64),
				molecule(serine), molecule(glycine), molecule(alinine),
				null,			  null,				 null,
				null,			  null,				 null
		);
		
		addRandomDecomposerRecipe(new ItemStack(Block.cactus), 0.1F, molecule(mescaline));
		addShapedSynthesisRecipe(new ItemStack(Block.cactus, 16),
				molecule(mescaline),	null,					molecule(mescaline),
				null,					molecule(mescaline),	null,
				molecule(mescaline),	null,					molecule(mescaline)
		);
		
		addRandomDecomposerRecipe(new ItemStack(Item.slimeBall), 0.8F, 
				itemStacks(molecule(polycyanoacrylate)),
				itemStacks(element(Nd), element(Hg)),
				itemStacks(element(H,2), element(O))
		);
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.slimeBall));
		
		addDecomposerRecipe(new ItemStack(Item.blazeRod), element(Pu,3));
		addShapedSynthesisRecipe(new ItemStack(Item.blazeRod), 
				null, 	element(Pu),	null,
				null,	element(Pu),	null,
				null,	element(Pu),	null
		);
		
		addDecomposerRecipe(new ItemStack(Item.magmaCream), element(Hg), element(Pu), molecule(polycyanoacrylate,3));
		addShapedSynthesisRecipe(new ItemStack(Item.magmaCream), 
				null, 							element(Hg),					null,
				molecule(polycyanoacrylate),	molecule(polycyanoacrylate),	molecule(polycyanoacrylate),
				null,							element(Pu),					null
		);
		
		addDecomposerRecipe(new ItemStack(Item.ghastTear), element(Yb,4), element(No,4));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.ghastTear));
		
		
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 0), molecule(blackPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 1), molecule(redPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 2), molecule(greenPigment), molecule(blackPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 3), molecule(redPigment), molecule(blackPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 5), molecule(purplePigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 6), molecule(lightbluePigment), molecule(blackPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 7), molecule(whitePigment), molecule(blackPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 8), molecule(whitePigment), molecule(blackPigment, 2));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 9), molecule(redPigment), molecule(whitePigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1,10), molecule(greenPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1,11), molecule(yellowPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1,12), molecule(lightbluePigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1,13), molecule(lightbluePigment), molecule(redPigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1,14), molecule(orangePigment));
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1,15), molecule(whitePigment));
		
		for(int i = 0; i < 16; i++) {
			addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.dyePowder, 1, i));
		}
		
		addDecomposerRecipe(new ItemStack(Item.record13), molecule(polyvinylChloride));
		addDecomposerRecipe(new ItemStack(Item.recordCat), molecule(polyvinylChloride));
		addDecomposerRecipe(new ItemStack(Item.recordFar), molecule(polyvinylChloride));
		addDecomposerRecipe(new ItemStack(Item.recordMall), molecule(polyvinylChloride));
		addDecomposerRecipe(new ItemStack(Item.recordMellohi), molecule(polyvinylChloride));
		addDecomposerRecipe(new ItemStack(Item.recordStal), molecule(polyvinylChloride));
		addDecomposerRecipe(new ItemStack(Item.recordStrad), molecule(polyvinylChloride));
		addDecomposerRecipe(new ItemStack(Item.recordWard), molecule(polyvinylChloride));
		addDecomposerRecipe(new ItemStack(Item.record11), molecule(polyvinylChloride));
		
		addShapedSynthesisRecipe(new ItemStack(Item.record13), molecule(polyvinylChloride));
		addShapedSynthesisRecipe(new ItemStack(Item.recordCat), null, molecule(polyvinylChloride));
		addShapedSynthesisRecipe(new ItemStack(Item.recordFar), null, null, molecule(polyvinylChloride));
		addShapedSynthesisRecipe(new ItemStack(Item.recordMall), null, null, null, molecule(polyvinylChloride));
		addShapedSynthesisRecipe(new ItemStack(Item.recordMellohi), null, null, null, null, molecule(polyvinylChloride));
		addShapedSynthesisRecipe(new ItemStack(Item.recordStal), null, null, null, null, null, molecule(polyvinylChloride));
		addShapedSynthesisRecipe(new ItemStack(Item.recordStrad), null, null, null, null, null, null, molecule(polyvinylChloride));
		addShapedSynthesisRecipe(new ItemStack(Item.recordWard), null, null, null, null, null, null, null, molecule(polyvinylChloride));
		addShapedSynthesisRecipe(new ItemStack(Item.record11), null, null, null, null, null, null, null, null, molecule(polyvinylChloride));
		
		//TEMP
		//addDecomposerRecipe(new ItemStack(Item.netherStalkSeeds), );
		
		addSynthesisRecipesFromMolecules();
	}
	
	@ForgeSubscribe
	public void oreEvent(OreRegisterEvent event) {
		if(event.Name.contains("oreCopper")) {
			addDecomposerRecipe(event.Ore, element(Cu,2));
			return;
		}
		if(event.Name.contains("ingotCopper")) {
			addDecomposerRecipe(event.Ore, element(Cu));
			addUnshapedSynthesisRecipeFromDecomposerRecipe(event.Ore);
		}
		if(event.Name.contains("oreSilver")) {
			addDecomposerRecipe(event.Ore, element(Ag,2));
			return;
		}
		if(event.Name.contains("ingotSilver")) {
			addDecomposerRecipe(event.Ore, element(Ag));
			addUnshapedSynthesisRecipeFromDecomposerRecipe(event.Ore);
		}
		if(event.Name.contains("oreTin")) {
			addDecomposerRecipe(event.Ore, element(Sn,2));
			return;
		}
		if(event.Name.contains("ingotTin")) {
			addDecomposerRecipe(event.Ore, element(Sn));
			addUnshapedSynthesisRecipeFromDecomposerRecipe(event.Ore);
		}
		if(event.Name.contains("oreLead")) {
			addDecomposerRecipe(event.Ore, element(Pb,2));
			return;
		}
		if(event.Name.contains("ingotLead")) {
			addDecomposerRecipe(event.Ore, element(Pb));
			addUnshapedSynthesisRecipeFromDecomposerRecipe(event.Ore);
		}
		if(event.Name.contains("oreCopper")) {
			addDecomposerRecipe(event.Ore, element(Cu,2));
			return;
		}
		if(event.Name.contains("ingotCopper")) {
			addDecomposerRecipe(event.Ore, element(Cu));
			addUnshapedSynthesisRecipeFromDecomposerRecipe(event.Ore);
		}
		if(event.Name.contains("ingotBronze")) {
			addDecomposerRecipe(event.Ore, element(Sn), element(Cu,9));
			addUnshapedSynthesisRecipeFromDecomposerRecipe(event.Ore);
		}
		if(event.Name.contains("oreUranium")) {
			addDecomposerRecipe(event.Ore, element(U,2));
		}
		if(event.Name.contains("itemDropUranium")) {
			addDecomposerRecipe(event.Ore, element(U));
			addUnshapedSynthesisRecipeFromDecomposerRecipe(event.Ore);
		}
		if(event.Name.contains("ingotNickel")) {
			addDecomposerRecipe(event.Ore, element(Ni));
			addUnshapedSynthesisRecipeFromDecomposerRecipe(event.Ore);
		}
	}
	
	private void addSynthesisRecipesFromMolecules() {
		for(EnumMolecule molecule : EnumMolecule.molecules) {
			ArrayList<ItemStack> components = molecule.components();
			ItemStack[] ingredients = components.toArray(new ItemStack[components.size()]);
			synthesisRecipes.add(new SynthesisRecipe(molecule(molecule), ingredients, false));
		}
	}
	
	private void addDecomposerRecipe(ItemStack input, Object...objects) {
		ArrayList outputStacks = itemStacks(objects);
		UnbondingRecipe unbondingRecipe = new UnbondingRecipe(input, outputStacks);
		unbondingRecipes.add(unbondingRecipe);
	}
	
	private void addRandomDecomposerRecipe(ItemStack input, float chance, Object...objects) {
		ArrayList outcomes = new ArrayList<ArrayList<ItemStack>>();
		for(int i = 0; i < objects.length; i++) {
			outcomes.add(objects[i]);
		}
		UnbondingRecipeRandom unbondingRecipe = new UnbondingRecipeRandom(input, chance, outcomes);
		unbondingRecipes.add(unbondingRecipe);
	}
	
	private SynthesisRecipe newSynthesisRecipe(ItemStack output, boolean isShaped, Object...objects) {
		ArrayList inputStacks = itemStacks(objects);
		ItemStack[] recipe = new ItemStack[objects.length];
		for(int i = 0; i < objects.length; i++) {
			recipe[i] = (ItemStack) objects[i];
		}
		return new SynthesisRecipe(output, recipe, isShaped);
	}
	
	private void addShapedSynthesisRecipe(ItemStack output, Object...objects) {
		synthesisRecipes.add(newSynthesisRecipe(output, true, objects));
	}
	
	private void addUnshapedSynthesisRecipe(ItemStack output, Object...objects) {
		synthesisRecipes.add(newSynthesisRecipe(output, false, objects));
	}
	
	private void addUnshapedSynthesisRecipeFromDecomposerRecipe(ItemStack output) {
		ArrayList<ItemStack> inputStacks = getRecipe(output).getOutput();
		ItemStack[] unshapedRecipe = inputStacks.toArray(new ItemStack[inputStacks.size()]);
		SynthesisRecipe synthesisRecipe = new SynthesisRecipe(output, unshapedRecipe, false);
		synthesisRecipes.add(synthesisRecipe);
	}
	
	private ArrayList<ItemStack> itemStacks(Object...objects) {
		ArrayList stacks = new ArrayList<ItemStack>();
		for(int i = 0; i < objects.length; i++) {
			stacks.add((ItemStack)objects[i]);
		}
		return stacks;
	}
	
	private static String getKeyFromItemStack(ItemStack itemstack) {
		return "@"+itemstack.itemID+":"+itemstack.getItemDamage();
	}
	
	public UnbondingRecipe getRecipe(ItemStack itemstack) {
		for(UnbondingRecipe unbondingRecipe : unbondingRecipes) {
			if(MinechemHelper.stacksAreSameKind(unbondingRecipe.inputStack, itemstack))
				return unbondingRecipe;
		}
		return null;
	}
	
	public SynthesisRecipe getSynthesisRecipe(ItemStack[] inputRecipe) {
		for(SynthesisRecipe recipe : synthesisRecipes) {
			if(recipe.matches(inputRecipe)) {
				return recipe;
			}
		}
		return null;
	}
	
	public ItemStack getSynthesisOutput(ItemStack[] inputRecipe) {
		SynthesisRecipe recipe = getSynthesisRecipe(inputRecipe);
		if(recipe != null)
			return recipe.getOutputStack();
		return null;
	}
	
	public SynthesisRecipe getSynthesisRecipe(ItemStack outputStack) {
		for(SynthesisRecipe recipe : synthesisRecipes) {
			if(recipe.hasOutputStack(outputStack)) {
				return recipe;
			}
		}
		return null;
	}
	
	public ArrayList<ItemStack> getOutputStacksForItem(ItemStack itemstack) {
		UnbondingRecipe recipe = getRecipe(itemstack);
		if(recipe != null)
			return recipe.getOutput();
		return null;
	}
	
	private static ItemStack element(EnumElement enumElement, int amount) {
		return new ItemStack(MinechemItems.element, amount, enumElement.ordinal());
	}
	
	private static ItemStack element(EnumElement enumElement) {
		return new ItemStack(MinechemItems.element, 1, enumElement.ordinal());
	}
	
	private static ItemStack molecule(EnumMolecule molecule, int amount) {
		return new ItemStack(MinechemItems.molecule, amount, molecule.id());
	}
	
	private static ItemStack molecule(EnumMolecule molecule) {
		return new ItemStack(MinechemItems.molecule, 1, molecule.id());
	}
	
}
