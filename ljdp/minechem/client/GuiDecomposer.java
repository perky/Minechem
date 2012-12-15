package ljdp.minechem.client;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import ljdp.minechem.common.ContainerDecomposer;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.TileEntityDecomposer;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ScaledResolution;

public class GuiDecomposer extends GuiContainer {
	
	TileEntityDecomposer decomposer;
	InventoryPlayer inventoryPlayer;
	int mouseX = 0;
	int mouseY = 0;
	int guiWidth = 176;
	int guiHeight = 166;
	
	public GuiDecomposer(InventoryPlayer inventoryPlayer, TileEntityDecomposer decomposer) {
		super(new ContainerDecomposer(inventoryPlayer, decomposer));
		this.decomposer = decomposer;
		this.inventoryPlayer = inventoryPlayer;
	}
	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		fontRenderer.drawString("state: " + decomposer.state, 8, 6, 0xCCCCCC);
		ItemStack currentHeldItem = inventoryPlayer.getItemStack();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.DECOMPOSER_GUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		drawTexturedModalRect(x, y, 0, 0, guiWidth, guiHeight);
	}

}
