package ljdp.minechem.computercraft.method;

import net.minecraft.item.ItemStack;
import dan200.computer.api.IComputerAccess;
import dan200.turtle.api.ITurtleAccess;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.computercraft.ICCMethod;
import ljdp.minechem.computercraft.IMinechemPeripheral;

public class StoreSynthesisRecipe implements ICCMethod {

	@Override
	public String getMethodName() {
		return "storeSynthesisRecipe";
	}

	@Override
	public Object[] call(IComputerAccess computer, ITurtleAccess turtle,
			IMinechemPeripheral minechemPeripheral, Object[] arguments)
			throws Exception 
	{
		int selectedSlot = turtle.getSelectedSlot();
		ItemStack selectedStack = turtle.getSlotContents(selectedSlot);
		SynthesisRecipe recipe = null;
		if(selectedStack != null) {
			recipe = SynthesisRecipeHandler.instance.getRecipeFromOutput(selectedStack);
			minechemPeripheral.setSynthesisRecipe(recipe);
		}
		return new Object[]{ recipe != null };
	}

}
