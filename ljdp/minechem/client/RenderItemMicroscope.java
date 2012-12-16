package ljdp.minechem.client;

import org.lwjgl.opengl.GL11;

import ljdp.minechem.common.ContainerMicroscope;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderItem;
import net.minecraft.src.ScaledResolution;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class RenderItemMicroscope extends RenderItem {
	public ContainerMicroscope microscopeContainer;
	public InventoryPlayer inventoryPlayer;
	public GuiMicroscope guiMicroscope;
	
	public RenderItemMicroscope(GuiMicroscope guiMicroscope, Minecraft mc) {
		super();
		this.guiMicroscope = guiMicroscope;
		microscopeContainer = (ContainerMicroscope) guiMicroscope.inventorySlots;
		inventoryPlayer = guiMicroscope.inventoryPlayer;
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
			RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4,
			int par5) {
		// TODO Auto-generated method stub
		super.renderItemAndEffectIntoGUI(par1FontRenderer, par2RenderEngine,
				par3ItemStack, par4, par5);
	}
	
	@Override
	public void renderItemIntoGUI(FontRenderer par1FontRenderer,
			RenderEngine par2RenderEngine, ItemStack itemstack, int x,
			int y) {
		
		if(itemstack == microscopeContainer.getSlot(0).getStack() 
				) {
			//dont render the item if it's in the slot.
		} else {
			//Render the item and then clear
			super.renderItemIntoGUI(par1FontRenderer, par2RenderEngine, itemstack, x, y);
			GL11.glPushMatrix();
			setScissor(guiMicroscope.eyepieceX, guiMicroscope.eyepieceY, 52, 52);
			GL11.glClearDepth(100);
			stopScissor();
			GL11.glPopMatrix();
		}
		
		if(itemstack == microscopeContainer.getSlot(0).getStack() || itemstack == inventoryPlayer.getItemStack()) {
			GL11.glPushMatrix();
			setScissor(guiMicroscope.eyepieceX, guiMicroscope.eyepieceY, 52, 52);
			GL11.glTranslatef(x, y, 0.0F);
			GL11.glScalef(3.0F, 3.0F, 1.0F);
			GL11.glTranslatef(-x-5.3F, -y-5.3F, 0.0F);
			super.renderItemIntoGUI(par1FontRenderer, par2RenderEngine, itemstack, x, y);
			stopScissor();
			GL11.glPopMatrix();
		}
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
