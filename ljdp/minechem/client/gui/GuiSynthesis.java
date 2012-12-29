package ljdp.minechem.client.gui;

import ljdp.minechem.client.gui.tabs.TabEnergy;
import ljdp.minechem.client.gui.tabs.TabEnergySynthesis;
import ljdp.minechem.client.gui.tabs.TabHelp;
import ljdp.minechem.client.gui.tabs.TabStateControlSynthesis;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.containers.ContainerSynthesis;
import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

public class GuiSynthesis extends GuiContainerTabbed {
	
	int guiWidth = 176;
	int guiHeight = 166;
	
	public GuiSynthesis(InventoryPlayer inventoryPlayer, TileEntitySynthesis synthesis) {
		super(new ContainerSynthesis(inventoryPlayer, synthesis));
		addTab(new TabStateControlSynthesis(this, synthesis));
		addTab(new TabEnergySynthesis(this, synthesis));
		addTab(new TabHelp(this, MinechemHelper.getLocalString("help.synthesis")));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		String info = MinechemHelper.getLocalString("gui.title.synthesis");
		int infoWidth = fontRenderer.getStringWidth(info);
		fontRenderer.drawString(info, (guiWidth - infoWidth) / 2, 5, 0xCCCCCC);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.SYNTHESIS_GUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		drawTexturedModalRect(x, y, 0, 0, guiWidth, guiHeight);
	}

	@Override
	protected void drawTooltips(int mouseX, int mouseY) {
		
	}

}
