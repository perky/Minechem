package net.minecraft.minechem;

import java.nio.ByteBuffer;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.src.Entity;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;
import net.minecraft.src.mod_Minechem;
import net.minecraft.src.forge.ForgeHooksClient;
import net.minecraft.src.forge.ICustomItemRenderer;
import net.minecraft.src.forge.MinecraftForgeClient;

public class RenderTestTube implements ICustomItemRenderer {
	
	private int zLevel;
	
	public RenderTestTube() {
		super();
		zLevel = 0;
	}

	@Override
	public void renderInventory(RenderBlocks renderblocks, int itemID, int meta) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		//GL11.glMatrixMode (GL11.GL_PROJECTION);
		//GL11.glLoadIdentity ();
		//GLU.gluOrtho2D(0, 100, 0, 100);
		//GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		//GL11.glRotatef(-90F, 0.0F, -1.0F, 0.0F);
        //GL11.glRotatef(45F, 0.0F, -1.0F, 0.0F);
        //GL11.glRotatef(210F, -1.0F, 0.0F, 0.0F);
        //GL11.glScalef(0.1F, 0.1F, 0.1F);
		
		FontRenderer fontrenderer = RenderManager.instance.getFontRenderer();
		if(fontrenderer != null)
			fontrenderer.drawString("H", 5, 5, -1);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
	}
	
	public void renderTexturedQuad(int iconIndex)
    {
		int l = (iconIndex / 16) * 16;
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, 16, zLevel, (float)(iconIndex + 0) * f, (float)(l + 16) * f1);
        tessellator.addVertexWithUV(16, 16, zLevel, (float)(iconIndex + 16) * f, (float)(l + 16) * f1);
        tessellator.addVertexWithUV(16, 0, zLevel, (float)(iconIndex + 16) * f, (float)(l + 0) * f1);
        tessellator.addVertexWithUV(0, 0, zLevel, (float)(iconIndex+ 0) * f, (float)(l + 0) * f1);
        tessellator.draw();
    }

}
