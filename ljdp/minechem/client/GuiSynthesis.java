package ljdp.minechem.client;

import ljdp.minechem.common.ContainerSynthesis;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.TileEntitySynthesis;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;

public class GuiSynthesis extends GuiContainer {
	
	int guiWidth = 176;
	int guiHeight = 166;
	
	public GuiSynthesis(InventoryPlayer inventoryPlayer, TileEntitySynthesis synthesis) {
		super(new ContainerSynthesis(inventoryPlayer, synthesis));
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

}
