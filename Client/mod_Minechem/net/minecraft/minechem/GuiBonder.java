package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;

import org.lwjgl.opengl.GL11;

public class GuiBonder extends GuiMinechemMachine {

	public GuiBonder(EntityPlayer entityplayer, TileEntity tileentityfusion)
	{
		super(new ContainerBonder(entityplayer.inventory, (TileEntityBonder)tileentityfusion));
		tileMachine = (TileEntityMinechemMachine)tileentityfusion;
	}
	
	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Bonder", 56, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        fontRenderer.drawString(((TileEntityBonder)tileMachine).currentBondFormula, 20, 20, 0x404040);
        drawIC2Information();
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/minechem/bonder.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        
        int time = tileMachine.timer;
        if(time > 0)
        {
        	int k1 = time % 2;
        	if(k1 == 0) {
        		drawTexturedModalRect(l+54, i1+39, 176, 0, 13, 10);
        	}
        }
	}

}
