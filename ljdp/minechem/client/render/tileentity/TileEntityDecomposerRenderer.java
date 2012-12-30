package ljdp.minechem.client.render.tileentity;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntityMicroscope;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;

public class TileEntityDecomposerRenderer extends TileEntitySpecialRenderer {
	
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float var8) {
		if(tileEntity instanceof TileEntityDecomposer) {
			TileEntityDecomposer decomposer = (TileEntityDecomposer)tileEntity;
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
			GL11.glRotatef(180f, 0f, 0f, 1f);
			GL11.glEnable(GL11.GL_LIGHTING);
			if(decomposer.isPowered()) {
				ForgeHooksClient.bindTexture(ModMinechem.proxy.DECOMPOSER_MODEL_ON, 0);
				decomposer.model.updateWindillRotation(decomposer);
			} else {
				ForgeHooksClient.bindTexture(ModMinechem.proxy.DECOMPOSER_MODEL_OFF, 0);
			}
			decomposer.model.render(0.0625F);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}

}
