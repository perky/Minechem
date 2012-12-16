package ljdp.minechem.client;

import ljdp.minechem.common.ItemElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemElementRenderer implements IItemRenderer {
	
	private Minecraft mc;
	
	public ItemElementRenderer() {
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
		String shortName = ItemElement.getShortName(item);
		renderTestTube(item);
		renderElementName(shortName);
	}
	
	private void renderTestTube(ItemStack item) {
		int iconIndex = item.getItem().getIconFromDamage(item.getItemDamage());
		int row = iconIndex % 16;
		int column = (int) Math.floor(iconIndex / 16);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		drawTexturedRectUV(0, 0, 0, row*16, column*16, 16, 16);
	}
	
	private void renderElementName(String name) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		FontRenderer fontRenderer = mc.fontRenderer;
		fontRenderer.drawString(name, 1, 2, 0x000000);
		fontRenderer.drawString(name, 1, 1, 0xEEEEEE);
	}
	
	public void drawTextureRect(int x, int y, int z, int w, int h) {
		float scale = 0.00390625F;
		Tessellator tesselator = Tessellator.instance;
		tesselator.startDrawingQuads();
		tesselator.addVertex(x, y + h, z);
		tesselator.addVertex(x + w, y + h, z);
		tesselator.addVertex(x + w, y, z);
		tesselator.addVertex(x, y, z);
		tesselator.draw();
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
