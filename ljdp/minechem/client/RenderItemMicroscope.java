package ljdp.minechem.client;


import ljdp.minechem.common.ContainerMicroscope;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class RenderItemMicroscope extends RenderItem {
	public ContainerMicroscope microscopeContainer;
	public InventoryPlayer inventoryPlayer;
	public GuiMicroscope guiMicroscope;
	private Minecraft mc;
	
	int colorTextureID;
	int framebufferID;
	int depthRenderBufferID;
	boolean isFBOSupported;
	
	public RenderItemMicroscope(GuiMicroscope guiMicroscope, Minecraft mc) {
		super();
		this.guiMicroscope = guiMicroscope;
		microscopeContainer = (ContainerMicroscope) guiMicroscope.inventorySlots;
		inventoryPlayer = guiMicroscope.inventoryPlayer;
		this.mc = Minecraft.getMinecraft();
	}
	
	private void setScissor(float x, float y, float w, float h) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int scale = scaledRes.getScaleFactor();
		x *= scale; y *= scale;
		w *= scale; h *= scale;
		float guiScaledWidth  = (guiMicroscope.guiWidth * scale);
		float guiScaledHeight = (guiMicroscope.guiHeight * scale);
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
	
	@Override
	public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer,
			RenderEngine par2RenderEngine, ItemStack itemstack, int x,
			int y) {
		
		if(itemstack == microscopeContainer.getSlot(0).getStack() || itemstack == inventoryPlayer.getItemStack()) {
			GL11.glPushMatrix();
			//GL11.glDisable(GL11.GL_DEPTH_TEST);
			setScissor(guiMicroscope.eyepieceX, guiMicroscope.eyepieceY, 52, 52);
			GL11.glTranslatef(x, y, 0.0F);
			GL11.glScalef(3.0F, 3.0F, 1.0F);
			GL11.glTranslatef(-x-5.3F, -y-5.3F, 2.0F);
			super.renderItemAndEffectIntoGUI(par1FontRenderer, par2RenderEngine, itemstack, x, y);
			stopScissor();
			//GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
		}
		
		if(itemstack != microscopeContainer.getSlot(0).getStack()) {
			float oldZlevel = this.zLevel;
			if(itemstack == inventoryPlayer.getItemStack())
				this.zLevel = 40.0F;
			else
				this.zLevel = 50F;
			
			super.renderItemAndEffectIntoGUI(par1FontRenderer, par2RenderEngine, itemstack, x, y);
		}
		
		if(inventoryPlayer.getItemStack() != null && itemstack == inventoryPlayer.getItemStack()
				) {
			GL11.glPushMatrix();
			par2RenderEngine.bindTexture(0);
			drawRect(guiMicroscope.eyepieceX, guiMicroscope.eyepieceY, 54, 54);
			GL11.glPopMatrix();
		}
	}
	
	private void drawRect(int x, int y, int width, int height) {
		double z = 50D;
		GL11.glDisable(GL11.GL_LIGHTING);
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
        t.setColorOpaque_I(0x121212);
        //t.setColorRGBA_I(0x121212, 0xAA);
		//t.setColorRGBA_F(1.0F, 0.0F, 0.0F, 0.5F);
        t.addVertex((double)(x + 0), (double)(y + 0), z);
        t.addVertex((double)(x + 0), (double)(y + height), z);
        t.addVertex((double)(x + width), (double)(y + height), z);
        t.addVertex((double)(x + width), (double)(y + 0), z);
        t.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	@Override
	public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer,
			RenderEngine par2RenderEngine, ItemStack itemstack, int par4,
			int par5) {
		if(itemstack == null)
			return;
		if(itemstack == microscopeContainer.getSlot(0).getStack() || 
				(itemstack == inventoryPlayer.getItemStack() && guiMicroscope.isMouseInMicroscope())) {
			if (itemstack.stackSize > 1)
            {
				/*
                String stackSize = "" + itemstack.stackSize;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                par1FontRenderer.drawStringWithShadow(stackSize, par4 + 29 - 2 - par1FontRenderer.getStringWidth(stackSize), par5 + 16 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                */
            }
		} else {
			super.renderItemOverlayIntoGUI(par1FontRenderer, par2RenderEngine, itemstack, par4, par5);
		}
	}
}
