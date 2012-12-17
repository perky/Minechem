package ljdp.minechem.client;

import ljdp.minechem.common.ContainerMicroscope;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.TileEntityMicroscope;
import ljdp.minechem.utils.MinechemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiMicroscope extends GuiContainerTabbed {
		
	int guiWidth = 176;
	int guiHeight = 189;
	int eyepieceX = 25;
	int eyepieceY = 26;
	int inputSlotX = 44;
	int inputSlotY = 45;
	public InventoryPlayer inventoryPlayer;
	protected TileEntityMicroscope microscope;	
    
	public GuiMicroscope(InventoryPlayer inventoryPlayer, TileEntityMicroscope microscope) {
		super(new ContainerMicroscope(inventoryPlayer, microscope));
		this.inventoryPlayer = inventoryPlayer;
		this.microscope = microscope;
		this.xSize = guiWidth;
		this.ySize = guiHeight;
		this.itemRenderer = new RenderItemMicroscope(this);
		addTab(new TabHelp(this, MinechemHelper.getLocalString("help.microscope")));
	}
	
	private int getMouseX() {
		return (Mouse.getX() * this.width / this.mc.displayWidth);
	}
	
	private int getMouseY() {
		return this.height - (Mouse.getY() * this.height / this.mc.displayHeight - 1);
	}
	
	public boolean isMouseInMicroscope() {
		int mouseX = getMouseX();
		int mouseY = getMouseY();
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		x += eyepieceX; y += eyepieceY;
		int h = 54; int w = 54;
		return mouseX >= x && mouseX <= x+w && mouseY >= y && mouseY <= y+h;
	}
	
	private void drawMicroscopeOverlay() {
		zLevel = 401;
		drawTexturedModalRect(eyepieceX, eyepieceY, 176, 0, 54, 54);
	}
	
	private void drawUnshapedOverlay() {
		zLevel = 0;
		drawTexturedModalRect(97, 26, 176, 70, 54, 54);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		fontRenderer.drawString(MinechemHelper.getLocalString("gui.title.microscope"), 5, 5, 0xCCCCCC);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.MICROSCOPE_GUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		zLevel = 0;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0F);
		drawTexturedModalRect(0, 0, 0, 0, guiWidth, guiHeight);
		drawMicroscopeOverlay();
		if(!microscope.isShaped)
			drawUnshapedOverlay();
		GL11.glPopMatrix();
	}

	@Override
	protected void drawTooltips(int mouseX, int mouseY) {}

}
