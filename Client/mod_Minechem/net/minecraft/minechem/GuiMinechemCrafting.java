package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiContainer;

public class GuiMinechemCrafting extends GuiContainer {
	
	private TileEntityMinechemMachine tileMachine;
	
	public GuiMinechemCrafting(EntityPlayer entityplayer, TileEntity tileentity)
	{
		super(new ContainerMinechemCrafting(entityplayer.inventory, tileentity));
		tileMachine = (TileEntityMinechemMachine)tileentity;
	}
	
	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Molecular Crafting", 56, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        if(mod_Minechem.requireIC2Power) {
        	String s = "Requires " + tileMachine.IC2PowerPerTick + "EU/t";
        	fontRenderer.drawString(s, 80, (ySize - 96) + 2, 0x404040);
        }
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/minechem/crafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        
        int j1 = tileMachine.getTimer();
        if(j1 > 0)
        {
        	int k1 = (int)(76F * (1.0F - (float)j1 / 200F));
        	
        	drawTexturedModalRect(l + 57, i1 + 17, 176, 0, k1, 48);
        }
	}
}
