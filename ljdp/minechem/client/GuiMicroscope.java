package ljdp.minechem.client;

import ljdp.minechem.common.ContainerMicroscope;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.TileEntityMicroscope;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Slot;

public class GuiMicroscope extends GuiContainer {
		
	int guiWidth = 176;
	int guiHeight = 189;
	public InventoryPlayer inventoryPlayer;
	protected TileEntityMicroscope microscope;
	
	public GuiMicroscope(InventoryPlayer inventoryPlayer, TileEntityMicroscope microscope) {
		super(new ContainerMicroscope(inventoryPlayer, microscope));
		this.inventoryPlayer = inventoryPlayer;
		this.microscope = microscope;
		this.xSize = guiWidth;
		this.ySize = guiHeight;
		this.itemRenderer = new RenderItemMicroscope(this);
	}
	
	private void setScissor(float x, float y, float w, float h) {
		ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int scale = scaledRes.getScaleFactor();
		x *= scale; y *= scale;
		w *= scale; h *= scale;
		float guiScaledWidth  = (guiWidth * scale);
		float guiScaledHeight = (guiHeight * scale);
		float guiLeft = ((mc.displayWidth / 2) - guiScaledWidth/2);
		float guiTop  = ((mc.displayHeight / 2) + guiScaledHeight/2);
		int scissorX = Math.round((guiLeft + x));
		int scissorY = Math.round(((guiTop - h) - y));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(scissorX, scissorY, (int)w, (int)h);
	}
	
	private void stopScissor() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	private int getMouseX() {
		return Mouse.getX() * this.width / this.mc.displayWidth;
	}
	
	private int getMouseY() {
		return this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
	}
	
	private void drawMagnifiedRegion() {
		ItemStack currentItem = inventoryPlayer.getItemStack();
		if(currentItem != null) {
			GL11.glPushMatrix();
			setScissor(62, 14, 52, 52);
			drawMagnifiedItem(currentItem, getMouseX(), getMouseY()-4);
			stopScissor();
			GL11.glPopMatrix();
		}
	}
	
	private void drawMagnifiedItem(ItemStack itemstack, int x, int y) {
		int itemX = x;
        int itemY = y;
        int offsetX = ((width - guiWidth) / 2) + 8;
        int offsetY = ((height - guiHeight) / 2) + 8;
        float itemZ = 300F;
        itemX -= offsetX; itemY -= offsetY;
        
        GL11.glPushMatrix();
        if(itemstack == inventoryPlayer.getItemStack()) {
        	GL11.glEnable(GL11.GL_LIGHTING);
        	itemZ = 300F;
        } else {
        	GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            itemZ = 290F;
        }
        RenderHelper.enableGUIStandardItemLighting();
        drawRect(itemX, itemY + 5);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float)itemX, (float)itemY, 0.0F);
		GL11.glScalef(3F, 3F, 1.0F);
		GL11.glTranslatef((float)(itemX * -1.0) - 5F, (float)(itemY * -1.0) - 4F, itemZ);

		this.itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, 
				itemstack, itemX, itemY);
		
		if(itemstack == inventoryPlayer.getItemStack()) {
			// do nothing
		} else {
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}
		GL11.glPopMatrix();
	}
	
	public boolean isMouseInMicroscope() {
		int mouseX = getMouseX();
		int mouseY = getMouseY();
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		x += 61; y += 14;
		int h = 54; int w = 54;
		return mouseX >= x && mouseX <= x+w && mouseY >= y && mouseY <= y+h;
	}
	
	
	private void drawRect(int x, int y) {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.MICROSCOPE_GUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		zLevel = 240;
		drawTexturedModalRect(x, y, 176, 54, 16, 16);
	}
	
	private void drawMicroscopeOverlay() {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.MICROSCOPE_GUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		x += 61; y += 14;
		zLevel = 401;
		drawTexturedModalRect(x, y, 176, 0, 54, 54);
	}
	
	private void drawMicroscopeSlot() {
		Slot inputSlot = (Slot) inventorySlots.inventorySlots.get(0);
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		zLevel = 450;
		if(inputSlot.getHasStack()) {
			drawMagnifiedItem(inputSlot.getStack(), x + x + 80 + 7, y + y + 33 + 4);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		drawMagnifiedRegion();
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
		drawTexturedModalRect(x, y, 0, 0, guiWidth, guiHeight);
		drawMicroscopeOverlay();
		drawMicroscopeSlot();
	}

}
