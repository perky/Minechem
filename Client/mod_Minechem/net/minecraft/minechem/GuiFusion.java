package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;

import org.lwjgl.opengl.GL11;

public class GuiFusion extends GuiContainer {
	
	private TileEntityMinechemMachine tileFusion;
	
	public GuiFusion(EntityPlayer entityplayer, TileEntity tileentityfusion)
	{
		super(new ContainerFusion(entityplayer.inventory, (TileEntityFusion)tileentityfusion));
		tileFusion = (TileEntityMinechemMachine)tileentityfusion;
	}
	
	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Fusion Reactor", 56, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        if(mod_Minechem.requireIC2Power) {
        	String s = "Requires " + tileFusion.IC2PowerPerTick + "EU/t";
        	fontRenderer.drawString(s, 80, (ySize - 96) + 2, 0x404040);
        }
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/minechem/fusion.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        
        int j1 = tileFusion.getTimer();
        if(j1 > 0)
        {
        	int j2 = 0;
        	int k1 = (int)(54F * (1.0F - (float)j1 / (float)tileFusion.timerDuration));
        	if(k1 < 7)
        		j2 = 0;
        	else
        		j2 = k1-7;
        	
        	drawTexturedModalRect(l + 61 + k1, i1 + 28, 176+k1, 0, 7, 31);
        }
	}

}
