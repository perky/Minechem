package ljdp.minechem.client;

import ljdp.minechem.api.core.EnumMolecule;
import ljdp.minechem.common.items.ItemMolecule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemMoleculeRenderer implements IItemRenderer {
	
	private Minecraft mc;
	public ItemMoleculeRenderer() {
		mc = Minecraft.getMinecraft();
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type == ItemRenderType.INVENTORY)
			return true;
		if(type == ItemRenderType.EQUIPPED)
			return true;
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		if(helper == ItemRendererHelper.EQUIPPED_BLOCK)
			return false;
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		int iconIndex = item.getItem().getIconFromDamage(item.getItemDamage());
		int row = iconIndex % 16;
		int column = (int) Math.floor(iconIndex / 16);
		EnumMolecule molecule = ((ItemMolecule)item.getItem()).getMolecule(item);
		

		if(type == ItemRenderType.INVENTORY) {
			GL11.glColor3f(molecule.red, molecule.green, molecule.blue);
			drawTexturedRectUV(type, 0, 0, 0, row*16, column*16, 16, 10);
			
			GL11.glColor3f(molecule.red2, molecule.green2, molecule.blue2);
			drawTexturedRectUV(type, 0, 10, 0, row*16, (column*16)+10, 16, 6);
			
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			drawTexturedRectUV(type, 0, 0, 0, 0, 0, 16, 16);
		} else {
			GL11.glPushMatrix();

			GL11.glTranslatef(1.2F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			
			GL11.glColor3f(molecule.red, molecule.green, molecule.blue);
			drawTexturedRectUV(type, 0, 0, 0, row*16, column*16, 16, 10);
			
			GL11.glColor3f(molecule.red2, molecule.green2, molecule.blue2);
			drawTexturedRectUV(type, 0, 0.625F, 0, row*16, (column*16)+10, 16, 6);
			
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(0.0F, 0.0F, -0.001F);
			drawTexturedRectUV(type, 0, 0, 0, 0, 0, 16, 16);
			GL11.glPopMatrix();
		}
		
	}
	
	public void drawTexturedRectUV(ItemRenderType type, float x, float y, float z, float u, float v, float w, float h) {
		float scale = 0.00390625F;
		float w2;
		float h2;
		if(type == ItemRenderType.EQUIPPED) {
			w2 = (1.0F/16.0F)*w;
			h2 = (1.0F/16.0F)*h;
		} else {
			w2 = w;
			h2 = h;
		}
		Tessellator tesselator = Tessellator.instance;
		tesselator.startDrawingQuads();
		tesselator.addVertexWithUV(x, y + h2, z, u * scale, (v + h) * scale);
		tesselator.addVertexWithUV(x + w2, y + h2, z, (u + w) * scale, (v + h) * scale);
		tesselator.addVertexWithUV(x + w2, y, z, (u + w) * scale, v * scale);
		tesselator.addVertexWithUV(x, y, z, u * scale, v * scale);
		tesselator.draw();
	}

}
