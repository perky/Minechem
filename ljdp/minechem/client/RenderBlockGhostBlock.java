package ljdp.minechem.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderBlockGhostBlock implements ISimpleBlockRenderingHandler {
	
	public static int renderID;
	private boolean enableAO;
	private float lightValueOwn;
	private Object aoLightValueXNeg;
	private IBlockAccess blockAccess;
	private float aoLightValueYPos;
	private float aoLightValueYNeg;
	private float aoLightValueZNeg;
	private float aoLightValueXPos;
	private float aoLightValueZPos;
	private Object colorRedTopLeft;
	private Object colorRedBottomLeft;
	private Object colorRedBottomRight;
	private float colorRedTopRight;
	private Object colorGreenBottomRight;
	
	public RenderBlockGhostBlock() {
		renderID = RenderingRegistry.getNextAvailableRenderId();
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
		renderer.renderStandardBlock(block, x, y, z);
		//int colorMultiplier = 0xFF0000;
        //float r = (float)(colorMultiplier >> 16 & 255) / 255.0F;
        //float g = (float)(colorMultiplier >> 8 & 255) / 255.0F;
        //float b = (float)(colorMultiplier & 255) / 255.0F;
        //renderer.renderStandardBlockWithAmbientOcclusion(block, x, y, z, r, g, b);
        //renderer.renderStandardBlockWithColorMultiplier(block, x, z, z, r, g, b);
		//renderer.renderStandardBlock(block, x, y, z);
		//GL11.glDisable(GL11.GL_BLEND);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}

}
