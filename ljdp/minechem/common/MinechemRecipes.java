package ljdp.minechem.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.lang.model.element.Element;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import static ljdp.minechem.common.EnumElement.*;
import static ljdp.minechem.common.EnumMolecule.*;

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
		
		addRandomDecomposerRecipe(new ItemStack(Block.stone), 0.5F, 
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
		addSynthesisRecipe(new ItemStack(Block.stone), element(Si), element(O,2));
		
		addRandomDecomposerRecipe(new ItemStack(Block.cobblestone), 0.4F,
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
		addSynthesisRecipe(new ItemStack(Block.cobblestone), element(Si), element(O,2), element(Ca));
		
		addDecomposerRecipe(new ItemStack(Block.dirt),
				element(H,2), element(O,6), element(Si), element(Ca), element(C)
				);
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Block.dirt));
		
		addDecomposerRecipe(new ItemStack(Block.oreIron), element(Fe,2));
		addDecomposerRecipe(new ItemStack(Item.ingotIron), element(Fe,1));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.ingotIron));
		
		addDecomposerRecipe(new ItemStack(Block.oreGold), element(Au,2));
		addDecomposerRecipe(new ItemStack(Item.ingotGold), element(Au,1));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.ingotGold));
		
		addDecomposerRecipe(new ItemStack(Block.oreDiamond), element(C,64), element(C,64));
		addDecomposerRecipe(new ItemStack(Item.diamond), element(C,64));
		addSynthesisRecipe(new ItemStack(Item.diamond), element(C,64), element(C,64));
		
		addDecomposerRecipe(new ItemStack(Block.oreCoal), element(C,2));
		addDecomposerRecipe(new ItemStack(Item.coal), element(C,1));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.coal));
		
		addDecomposerRecipe(new ItemStack(Item.gunpowder), molecule(potassiumNitrate), element(S,2), element(C,1));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.gunpowder));
		
		addDecomposerRecipe(new ItemStack(Block.sand), 		molecule(siliconDioxide, 8));
		addDecomposerRecipe(new ItemStack(Block.glass), 	molecule(siliconDioxide, 16));
		addDecomposerRecipe(new ItemStack(Block.thinGlass), molecule(siliconDioxide,1));
		addDecomposerRecipe(new ItemStack(Block.sandStone), molecule(siliconDioxide,12));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Block.sand));
		
		addDecomposerRecipe(new ItemStack(Block.wood), molecule(cellulose,4));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Block.wood));
		
		addDecomposerRecipe(new ItemStack(Block.oreLapis),
				molecule(lazurite, 4),
				molecule(sodalite, 4),
				molecule(noselite, 4),
				molecule(calcite, 4),
				molecule(pyrite, 4)
				);
		addDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 4), molecule(lazurite));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.dyePowder, 1, 4));
		
		addDecomposerRecipe(new ItemStack(Block.glowStone), element(P,4));
		addDecomposerRecipe(new ItemStack(Item.lightStoneDust), element(P));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Item.lightStoneDust));
		
		addDecomposerRecipe(new ItemStack(Block.tnt), molecule(tnt, 4));
		addSynthesisRecipeFromDecomposerRecipe(new ItemStack(Block.tnt));
		
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
			addSynthesisRecipeFromDecomposerRecipe(new ItemStack(oreID, 1, 0));
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
	
	private void addSynthesisRecipe(ItemStack output, Object...objects) {
		ArrayList inputStacks = itemStacks(objects);
		SynthesisRecipe synthesisRecipe = new SynthesisRecipe(output, inputStacks);
		synthesisRecipes.add(synthesisRecipe);
	}
	
	private void addSynthesisRecipeFromDecomposerRecipe(ItemStack output) {
		ArrayList inputStacks = getRecipe(output).getOutput();
		SynthesisRecipe synthesisRecipe = new SynthesisRecipe(output, inputStacks);
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
	
	public ItemStack getSynthesisOutput(ArrayList<ItemStack> inputStacks) {
		for(SynthesisRecipe recipe : synthesisRecipes) {
			if(recipe.matches(inputStacks)) {
				return recipe.getOutputStack();
			}
		}
		return null;
	}
	
	public ArrayList<ItemStack> getSynthesisRecipe(ItemStack outputStack) {
		for(SynthesisRecipe recipe : synthesisRecipes) {
			if(recipe.hasOutputStack(outputStack)) {
				return recipe.getInputStacks();
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
