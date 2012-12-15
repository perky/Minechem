package ljdp.minechem.client;

import org.lwjgl.opengl.GL11;

import ljdp.minechem.common.ContainerMicroscope;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderItem;

public class RenderItemMicroscope extends RenderItem {
	public ContainerMicroscope microscopeContainer;
	public InventoryPlayer inventoryPlayer;
	public GuiMicroscope guiMicroscope;
	
	public RenderItemMicroscope(GuiMicroscope guiMicroscope) {
		super();
		this.guiMicroscope = guiMicroscope;
		microscopeContainer = (ContainerMicroscope) guiMicroscope.inventorySlots;
		inventoryPlayer = guiMicroscope.inventoryPlayer;
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
