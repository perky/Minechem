package ljdp.minechem.client;

import ljdp.minechem.common.EnumMolecule;
import ljdp.minechem.common.ItemMolecule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemMoleculeRenderer implements IItemRenderer {
	
	private Minecraft mc;
	public ItemMoleculeRenderer() {
		mc = Minecraft.getMinecraft();
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type == ItemRenderType.INVENTORY) {
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		if (helper == ItemRendererHelper.INVENTORY_BLOCK) {
			return false;
		}
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		int iconIndex = item.getItem().getIconFromDamage(item.getItemDamage());
		int row = iconIndex % 16;
		int column = (int) Math.floor(iconIndex / 16);
		EnumMolecule molecule = ((ItemMolecule)item.getItem()).getMolecule(item);
		GL11.glColor3f(molecule.red, molecule.green, molecule.blue);
		drawTexturedRectUV(0, 0, 0, row*16, column*16, 16, 10);
		
		GL11.glColor3f(molecule.red2, molecule.green2, molecule.blue2);
		drawTexturedRectUV(0, 10, 0, row*16, (column*16)+10, 16, 6);
		
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		drawTexturedRectUV(0, 0, 0, 0, 0, 16, 16);
	}
	
	public void drawTexturedRectUV(int x, int y, int z, int u, int v, int w, int h) {
		float scale = 0.00390625F;
		Tessellator tesselator = Tessellator.instance;
		tesselator.startDrawingQuads();
		tesselator.addVertexWithUV(x, y + h, z, u * scale, (v + h) * scale);
		tesselator.addVertexWithUV(x + w, y + h, z, (u + w) * scale, (v + h) * scale);
		tesselator.addVertexWithUV(x + w, y, z, (u + w) * scale, v * scale);
		tesselator.addVertexWithUV(x, y, z, u * scale, v * scale);
		tesselator.draw();
	}

}
