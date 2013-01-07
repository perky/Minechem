package ljdp.minechem.client.gui;

import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

public class GuiChemicalStorage extends GuiChest {

	private IInventory playerInventory;

	public GuiChemicalStorage(IInventory playerInventory, IInventory blockInventory) {
		super(playerInventory, blockInventory);
        this.playerInventory = playerInventory;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String chestName = MinechemHelper.getLocalString("block.name.chemicalStorage");
		String playerInventoryTitle = StatCollector.translateToLocal(this.playerInventory.getInvName());
		this.fontRenderer.drawString(chestName, 8, 6, 4210752);
		this.fontRenderer.drawString(playerInventoryTitle, 8, this.ySize - 96 + 2, 4210752);
	}

}
