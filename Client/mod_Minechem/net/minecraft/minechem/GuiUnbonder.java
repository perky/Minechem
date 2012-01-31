package net.minecraft.minechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_Minechem;

import org.lwjgl.opengl.GL11;

public class GuiUnbonder extends GuiContainer {

	private TileEntityMinechemMachine tileMinechem;
	
	public GuiUnbonder(EntityPlayer entityplayer, TileEntity tileentity)
	{
		super(new ContainerUnbonder(entityplayer.inventory, (TileEntityUnbonder)tileentity));
		tileMinechem = (TileEntityMinechemMachine)tileentity;
	}
	
	protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Un-Bonder", 56, 6, 0x404040);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
        if(mod_Minechem.requireIC2Power) {
        	String s = "Requires " + tileMinechem.IC2PowerPerTick + "EU/t";
        	fontRenderer.drawString(s, 80, (ySize - 96) + 2, 0x404040);
        }
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int k = mc.renderEngine.getTexture("/minechem/unbonder.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        
        int time = tileMinechem.timer;
        if(time > 0)
        {
        	int k1 = time % 2;
        	if(k1 == 0) {
        		drawTexturedModalRect(l+54, i1+39, 176, 0, 13, 10);
        	}
        }
	}

}
