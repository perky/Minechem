package ljdp.minechem.client;

import ljdp.minechem.common.ContainerMicroscope;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.TileEntityMicroscope;
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
		addTab(new TabHelp(this, "Place an item under the glass to discover new recipes for the Chemical Synthesis Table. Not every item has a recipe."));
	}
	
	private int getMouseX() {
		return (Mouse.getX() * this.width / this.mc.displayWidth);
	}
	
	private int getMouseY() {
		return this.height - (Mouse.getY() * this.height / this.mc.displayHeight - 1);
	}
	
	private int getMouseX2() {
		return Mouse.getX();
	}
	
	private int getMouseY2() {
		return mc.displayHeight - Mouse.getY();
	}
	
	private void drawMagnifiedRegion() {
		ItemStack currentItem = inventoryPlayer.getItemStack();
		if(currentItem != null) {
			ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			int scale = scaledRes.getScaleFactor();
			int x = (width - guiWidth) / 2;
			int y = (height - guiHeight) / 2;
			int a = ((getMouseX2()-(x*scale))/scale) - 8;
			int b = ((getMouseY2()-(y*scale))/scale) - 8;
						
			//setScissor(eyepieceX, eyepieceY, 52, 52);
			drawMagnifiedItem(currentItem, a,b);
			//stopScissor();
		}
	}
	
	private void drawMagnifiedItem(ItemStack itemstack, int x, int y) {
		int itemX = x;
        int itemY = y;
        int offsetX = ((width - guiWidth) / 2) + 8;
        int offsetY = ((height - guiHeight) / 2) + 8;
        float itemZ = 300F;
        itemX -= 8; itemY -= 8;
        
        GL11.glPushMatrix();
        
        	GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            itemZ = 290F;
        
            RenderHelper.enableGUIStandardItemLighting();
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float)itemX, (float)itemY, 0.0F);
		GL11.glScalef(3F, 3F, 1.0F);
		GL11.glTranslatef((float)(itemX * -1.0) - 5F, (float)(itemY * -1.0) - 4F, itemZ);

		this.itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, 
				itemstack, itemX, itemY);
		//this.itemRenderer.renderItemIntoGUI(this.fontRenderer, this.mc.renderEngine, itemstack, itemX, itemY);
		
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		
		GL11.glPopMatrix();
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
	
	
	private void drawRect(int x, int y) {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.MICROSCOPE_GUI_PNG);
		mc.renderEngine.bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		zLevel = 240;
		drawTexturedModalRect(x, y, 176, 54, 16, 16);
	}
	
	private void drawMicroscopeOverlay() {
		zLevel = 401;
		drawTexturedModalRect(eyepieceX, eyepieceY, 176, 0, 54, 54);
	}
	
	private void drawUnshapedOverlay() {
		zLevel = 0;
		drawTexturedModalRect(97, 26, 176, 70, 54, 54);
	}
	
	private void drawMicroscopeSlot() {
		Slot inputSlot = (Slot) inventorySlots.inventorySlots.get(0);
		if(inputSlot.getHasStack()) {
			drawMagnifiedItem(inputSlot.getStack(), inputSlotX + 7, inputSlotY + 4);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		fontRenderer.drawString("Microscope", 5, 5, 0xCCCCCC);
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
		//drawMicroscopeSlot();
		//drawMagnifiedRegion();
		GL11.glPopMatrix();
	}



	@Override
	protected void drawTooltips(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		
	}

}
