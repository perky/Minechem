package ljdp.minechem.client;

import java.util.Random;

import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.ModMinechem;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;

public class TileEntityGhostBlockRenderer extends TileEntitySpecialRenderer {
	
	private final RenderBlocks renderBlocks = new RenderBlocks();
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float scale) {
		//RenderBlocks renderBlocks = FMLClientHandler.instance().getClient().renderGlobal.globalRenderBlocks;
		//renderBlocks.renderStandardBlock(Block.stone, (int)x, (int)y, (int)z);
		renderBlocks.blockAccess = tileEntity.worldObj;
		//renderBlocks.renderBlockAllFaces(Block.blockGold, (int)x, (int)y, (int)z);
		ForgeHooksClient.bindTexture(ModMinechem.proxy.BLOCKS_PNG, 0);
		Random random = new Random();
		float alpha = random.nextFloat();
		
		int metadata = this.tileEntityRenderer.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
		Block block;
		if(metadata == 0) {
			block = MinechemBlocks.fusion;
		} else if(metadata == 2) {
			ForgeHooksClient.bindTexture("/terrain.png", 0);
			block = Block.blockGold;
		} else {
			block = MinechemBlocks.fusion;
		}
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_COLOR);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        renderBlocks.renderBlockAsItem(block, metadata, alpha);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

}
