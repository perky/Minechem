package ljdp.minechem.api.examples;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ljdp.minechem.api.core.Element;
import ljdp.minechem.api.core.Molecule;
import ljdp.minechem.api.recipe.DecomposerRecipe;
import ljdp.minechem.api.recipe.DecomposerRecipeChance;
import ljdp.minechem.api.recipe.DecomposerRecipeSelect;
import ljdp.minechem.api.recipe.SynthesisRecipe;

//Import the EnumElement and EnumMolecule like this so you have
//all the words in your scope.
import static ljdp.minechem.api.core.EnumElement.*;
import static ljdp.minechem.api.core.EnumMolecule.*;

public class RecipeExample {
	
	public RecipeExample() {
		// This is how to add a recipe for the chemical decomposer
		// It will turn 1 cake into Uranium and a Carbon Dioxide molecule.
		ItemStack cake = new ItemStack(Item.cake);
		DecomposerRecipe.add(new DecomposerRecipe(cake, new Element(U,1), new Molecule(carbonDioxide,1)));
		
		// If you want a recipe to be the same for all sub types of an item pass -1
		// into the itemDamage field.
		ItemStack anyWood = new ItemStack(Block.wood, 1, -1);
		
		// You can have decomposer recipes based on chance.
		// 1.0 = 100%
		// 0.0 = 0%
		// The following will only giver you aluminium oxide 10% of the time.
		ItemStack dirt = new ItemStack(Block.dirt);
		DecomposerRecipe.add(new DecomposerRecipeChance(dirt, 0.1F, new Molecule(aluminiumOxide,1)));
		
		// As well as having a chance you can have a decomposer recipe select from
		// a list of possible outputs.
		// The following will decompose dirt into something 10% of the time.
		// When it does decompose it will select one of the three possible outcomes.
		DecomposerRecipe.add(new DecomposerRecipeSelect(dirt, 0.1F, 
				new DecomposerRecipe(new Molecule(water,1), new Element(C,2)),
				new DecomposerRecipe(new Element(Fe,1), new Element(K,3)),
				new DecomposerRecipe(new Molecule(aluminiumOxide,1))
		));
		
		// A synthesis takes the ItemStack it will output,
		// A boolean indicated if it's a shaped recipe
		// And then up to 9 chemicals to represent the 9 slots
		// in the crafting matrix.
		// The following will create a recipe for cake out of
		// uranium stacks in cross shape.
		Element uranium64 = new Element(U,64);
		SynthesisRecipe.add(new SynthesisRecipe(cake, true,
				uranium64,		null,		uranium64,
				null,			uranium64,	null,
				uranium64,		null,		uranium64
		));
		
		
	}
}
