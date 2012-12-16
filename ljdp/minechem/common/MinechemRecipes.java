package ljdp.minechem.common;

import static ljdp.minechem.common.EnumElement.*;
import static ljdp.minechem.common.EnumMolecule.*;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MinechemRecipes {
	
	/*	The static instance of this class */
	private static final MinechemRecipes instance = new MinechemRecipes();
	
	/* The list of decomposer recipes */
	public HashMap<String, UnbondingRecipe> unbondingRecipes;
	public ArrayList<SynthesisRecipe> synthesisRecipes;
	
	public static MinechemRecipes getInstance() {
		return instance;
	}
	
	private MinechemRecipes() {
		unbondingRecipes = new HashMap<String, UnbondingRecipe>();
		synthesisRecipes = new ArrayList<SynthesisRecipe>();
		addRandomDecomposerRecipe(new ItemStack(Block.stone), 0.3F, 
				itemStacks(element(Si), element(O,2)),
				itemStacks(element(Al,2), element(O,3)),
				itemStacks(element(K,2), element(O,2)),
				itemStacks(element(Na,2), element(O)),
				itemStacks(element(Ca), element(O)),
				itemStacks(element(Fe), element(O)),
				itemStacks(element(Fe,2), element(O,3)),
				itemStacks(element(Mg), element(O)),
				itemStacks(element(Ti), element(O,2)),
				itemStacks(element(P,2), element(O,5)),
				itemStacks(element(Mn), element(O))
				);
		
		addRandomDecomposerRecipe(new ItemStack(Block.cobblestone), 0.1F,
				itemStacks(element(Si), element(O,2)),
				itemStacks(element(Al,2), element(O,3)),
				itemStacks(element(K,2), element(O,2)),
				itemStacks(element(Na,2), element(O)),
				itemStacks(element(Ca), element(O)),
				itemStacks(element(Fe), element(O)),
				itemStacks(element(Fe,2), element(O,3)),
				itemStacks(element(Mg), element(O)),
				itemStacks(element(Ti), element(O,2)),
				itemStacks(element(P,2), element(O,5)),
				itemStacks(element(Mn), element(O))
				);
		
		addRandomDecomposerRecipe(new ItemStack(Block.dirt), 0.01F,
				element(H,2), element(O,6), element(Si), element(Ca), element(C)
				);
		
		addDecomposerRecipe(new ItemStack(Block.oreIron), element(Fe,2));
		addDecomposerRecipe(new ItemStack(Item.ingotIron), element(Fe,1));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.ingotIron));
		addDecomposerRecipe(new ItemStack(Item.doorSteel), molecule(cellulose,6));
		addDecomposerRecipe(new ItemStack(Item.swordSteel), element(Fe,2), molecule(cellulose,1));
		addDecomposerRecipe(new ItemStack(Item.axeSteel), element(Fe,6), molecule(cellulose,2));
		addDecomposerRecipe(new ItemStack(Item.hoeSteel), element(Fe,1), molecule(cellulose,1));
		addDecomposerRecipe(new ItemStack(Item.pickaxeSteel), element(Fe,6), molecule(cellulose,2));
		
		addDecomposerRecipe(new ItemStack(Block.oreGold), element(Au,2));
		addDecomposerRecipe(new ItemStack(Item.ingotGold), element(Au,1));
		addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.ingotGold));
		addDecomposerRecipe(new ItemStack(Item.swordGold), element(Au,2), molecule(cellulose,1));
		addDecomposerRecipe(new ItemStack(Item.axeGold),   element(Au,6), molecule(cellulose,2));
		addDecomposerRecipe(new ItemStack(Item.hoeGold),  element(Au,1), molecule(cellulose,1));
		addDecomposerRecipe(new ItemStack(Item.pickaxeGold), element(Au,6), molecule(cellulose,2));
		
		
		addDecomposerRecipe(new ItemStack(Block.oreDiamond), element(C,64), element(C,64));
		addDecomposerRecipe(new ItemStack(Item.diamond), element(C,64));
		addShapedSynthesisRecipe(new ItemStack(Item.diamond), 
				null, 			element(C,64), 		null,
				element(C,64),	null,				element(C,64),
				null,			element(C,64), 		null);
		addDecomposerRecipe(new ItemStack(Item.swordDiamond), element(C,64), element(C,64), molecule(cellulose,1));
		addDecomposerRecipe(new ItemStack(Item.axeDiamond), element(C,64), element(C,64), element(C,64), molecule(cellulose,2));
		addDecomposerRecipe(new ItemStack(Item.hoeDiamond), element(C,64), molecule(cellulose,1));
		addDecomposerRecipe(new ItemStack(Item.pickaxeDiamond), element(C,64), element(C,64), element(C,64), molecule(cellulose,2));
		
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
		
		addDecomposerRecipe(new ItemStack(Block.planks, 1, -1), molecule(cellulose,4));
		addDecomposerRecipe(new ItemStack(Item.stick), molecule(cellulose,1));
		addDecomposerRecipe(new ItemStack(Item.doorWood), molecule(cellulose,24));
		addDecomposerRecipe(new ItemStack(Item.swordWood), molecule(cellulose,9));
		addDecomposerRecipe(new ItemStack(Item.axeWood), molecule(cellulose,14));
		addDecomposerRecipe(new ItemStack(Item.hoeWood), molecule(cellulose,6));
		addDecomposerRecipe(new ItemStack(Item.pickaxeWood), molecule(cellulose,14));
		
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
		
		// OreDictionary Recipes
		addOreDictDecomposerRecipe("oreSilver", element(Ag,2));
		addOreDictDecomposerAndSynthesisRecipe("ingotSilver", element(Ag));
		addOreDictDecomposerRecipe("oreLead", element(Pb,2));
		addOreDictDecomposerAndSynthesisRecipe("ingotLead", element(Pb));
		addOreDictDecomposerRecipe("oreCopper", element(Cu,2));
		addOreDictDecomposerAndSynthesisRecipe("ingotCopper", element(Cu));
		addOreDictDecomposerRecipe("oreTin", element(Sn,2));
		addOreDictDecomposerAndSynthesisRecipe("ingotTin", element(Sn));
		addOreDictDecomposerRecipe("oreUranium", element(U,2));
		addOreDictDecomposerAndSynthesisRecipe("itemDropUranium", element(U));
		
		addOreDictDecomposerAndSynthesisRecipe("ingotBronze", element(Sn), element(Cu,9));
		addOreDictDecomposerAndSynthesisRecipe("ingotNickel", element(Ni));
		addOreDictDecomposerAndSynthesisRecipe("ingotAluminium", element(Al));
		
		addSynthesisRecipesFromMolecules();
	}
	
	private void addSynthesisRecipesFromMolecules() {
		for(EnumMolecule molecule : EnumMolecule.molecules) {
			ArrayList<ItemStack> components = molecule.components();
			ItemStack[] ingredients = components.toArray(new ItemStack[components.size()]);
			synthesisRecipes.add(new SynthesisRecipe(molecule(molecule), ingredients, false));
		}
	}
	
	private boolean addOreDictDecomposerRecipe(String ore, Object...objects) {
		if(oreNamesHasOre(ore)) {
			int oreID = OreDictionary.getOreID(ore);
			addDecomposerRecipe(new ItemStack(oreID, 1, 0), objects);
			return true;
		}
		return false;
	}
	
	private boolean addOreDictDecomposerAndSynthesisRecipe(String ore, Object...objects) {
		if(addOreDictDecomposerRecipe(ore, objects)) {
			int oreID = OreDictionary.getOreID(ore);
			addUnshapedSynthesisRecipeFromDecomposerRecipe(new ItemStack(oreID, 1, 0));
			return true;
		}
		return false;
	}
	
	private boolean oreNamesHasOre(String ore) {
		String[] oreNames = OreDictionary.getOreNames();
		for(int i = 0; i < oreNames.length; i++) {
			if(oreNames[i].equals(ore))
				return true;
		}
		return false;
	}
	
	private void addDecomposerRecipe(ItemStack input, Object...objects) {
		ArrayList outputStacks = itemStacks(objects);
		UnbondingRecipe unbondingRecipe = new UnbondingRecipe(input, outputStacks);
		unbondingRecipes.put(getKeyFromItemStack(input), unbondingRecipe);
	}
	
	private void addRandomDecomposerRecipe(ItemStack input, float chance, Object...objects) {
		ArrayList outcomes = new ArrayList<ArrayList<ItemStack>>();
		for(int i = 0; i < objects.length; i++) {
			outcomes.add(objects[i]);
		}
		UnbondingRecipeRandom unbondingRecipe = new UnbondingRecipeRandom(input, chance, outcomes);
		unbondingRecipes.put(getKeyFromItemStack(input), unbondingRecipe);
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
		return unbondingRecipes.get(getKeyFromItemStack(itemstack));
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
