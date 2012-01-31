package net.minecraft.minechem;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.GuiScreen;

public class GuiTableOfElements extends GuiScreen {
	
	private int xSize;
	private int ySize;
	
	public GuiTableOfElements() {
		xSize = 2048 / 8;
		ySize = 1024 / 4;
	}
	
	public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        
        int k = mc.renderEngine.getTexture("/minechem/tableofelements.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glScalef(2.0F, 1.0F, 2.0F);
        mc.renderEngine.bindTexture(k);
        
        int x = (width / 4) - (1024/8);
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        super.drawScreen(i, j, f);
    }

}
