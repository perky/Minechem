package codechicken.nei.api;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import codechicken.nei.VisiblityData;

/**
 * If this is implemented on a gui, it will be automatically registered
 */
public interface INEIGuiHandler
{
	public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility);
	
	/**
	 * NEI will give the specified item to the slot returned if the player's inventory is full.
	 * return -1 for no slot.
	 */
	public int getItemSpawnSlot(GuiContainer gui, ItemStack item);
	
	/**
	 * @return A list of TaggedInventoryAreas that will be used with the savestates.
	 */
	public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui);
}
