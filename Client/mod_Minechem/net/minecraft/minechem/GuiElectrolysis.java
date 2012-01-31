package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.TileEntity;

import org.lwjgl.opengl.GL11;

public class GuiElectrolysis extends GuiContainer {
	
	private TileEntityElectrolysis tileElectrolysis;
	
	public GuiElectrolysis(EntityPlayer entityplayer, TileEntity tileentityelectrolysis)
	{
		super(new ContainerElectrolysis(entityplayer.inventory, (TileEntityElectrolysis)tileentityelectrolysis));
		tileElectrolysis = (TileEntityElectrolysis)tileentityelectrolysis;
	}
	
	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Electrolysis", 56, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int k = mc.renderEngine.getTexture("/minechem/electrolysis.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        int j1 = tileElectrolysis.getTimer();
        if(j1 > 0)
        {
            int k1 = (int)(19F * (1.0F - (float)j1 / (float)tileElectrolysis.timerDuration));
            if(k1 > 0)
            {
                drawTexturedModalRect(l + 111, i1 + 31 + 19 - k1, 197, 19-k1, 7, k1);
            }
            int l1 = (j1 / 2) % 7;
            switch(l1)
            {
            case 6: // '\006'
                k1 = 0;
                break;

            case 5: // '\005'
                k1 = 6;
                break;

            case 4: // '\004'
                k1 = 11;
                break;

            case 3: // '\003'
                k1 = 16;
                break;

            case 2: // '\002'
                k1 = 20;
                break;

            case 1: // '\001'
                k1 = 24;
                break;

            case 0: // '\0'
                k1 = 29;
                break;
            }
            if(k1 > 0)
            {
                drawTexturedModalRect(l + 53, (i1 + 21 + 29) - k1, 185, 29 - k1, 12, k1);
            }
        }
    }

}
