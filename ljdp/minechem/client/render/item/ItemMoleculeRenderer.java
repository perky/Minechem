package ljdp.minechem.client.render.item;

import ljdp.minechem.api.core.EnumMolecule;
import ljdp.minechem.common.items.ItemMolecule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityItem;
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
		if(type == ItemRenderType.ENTITY)
			return true;
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		if(helper == ItemRendererHelper.EQUIPPED_BLOCK)
			return false;
		if(helper == ItemRendererHelper.ENTITY_BOBBING)
			return true;
		if(helper == ItemRendererHelper.ENTITY_ROTATION)
			return true;
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemstack, Object... data) {
		if(type == ItemRenderType.INVENTORY) {
			renderItemInInventory(type, itemstack);
		} else if(type == ItemRenderType.ENTITY) {
			EntityItem entityItem = (EntityItem)data[1];
			if(entityItem.worldObj == null) {
				float angle = (mc.getSystemTime() % 8000L) / 8000.0F * 360.0F;
				GL11.glPushMatrix();
				GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.2F, -0.5F, 0.0F);
				renderItemAsEntity(type, itemstack);
				GL11.glPopMatrix();
			} else {
				renderItemAsEntity(type, itemstack);
			}
		} else {
			renderItemInEquipped(type, itemstack);
		}
	}
	
	private void renderItemInInventory(ItemRenderType type, ItemStack itemstack) {
		EnumMolecule molecule = ((ItemMolecule)itemstack.getItem()).getMolecule(itemstack);
		GL11.glColor3f(molecule.red, molecule.green, molecule.blue);
		drawTexturedRectUV(type, 0, 0, 0, 0*16, 1*16, 16, 16);
		GL11.glColor3f(molecule.red2, molecule.green2, molecule.blue2);
		drawTexturedRectUV(type, 0, 0, 0, 0*16, 2*16, 16, 16);
		drawTestTube(type, 0);
	}
	
	private void renderItemInEquipped(ItemRenderType type, ItemStack itemstack) {
		EnumMolecule molecule = ((ItemMolecule)itemstack.getItem()).getMolecule(itemstack);
		float scale = 0.75F;
		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(1.2F, 1.1F, -0.25F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glColor3f(molecule.red, molecule.green, molecule.blue);
		drawTexturedRectUV(type, 0, 0, 0, 0*16, 1*16, 16, 16);
		GL11.glColor3f(molecule.red2, molecule.green2, molecule.blue2);
		drawTexturedRectUV(type, 0, 0, 0, 0*16, 2*16, 16, 16);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(0.0F, 0.0F, -0.001F);
		for(float i = 0.0F; i < .1F; i += .01F) {
			drawTexturedRectUV(type, 0, 0, i, 0, 0, 16, 16);
		}
		GL11.glPopMatrix();
	}
	
	private void renderItemAsEntity(ItemRenderType type, ItemStack itemstack) {
		EnumMolecule molecule = ((ItemMolecule)itemstack.getItem()).getMolecule(itemstack);

		GL11.glPushMatrix();
		GL11.glColor3f(molecule.red, molecule.green, molecule.blue);
		drawTextureIn3D(1 * 16);
		GL11.glColor3f(molecule.red2, molecule.green2, molecule.blue2);
		drawTextureIn3D(2 * 16);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		drawTextureIn3D(0);
		GL11.glPopMatrix();
	}
	
	private void drawTestTube(ItemRenderType type, float z) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		drawTexturedRectUV(type, 0, 0, z, 0*16, 3*16, 16, 16);
	}
	
	private int getMiniItemCount(ItemStack itemstack) {
		if(itemstack.stackSize >= 56)
			return 8;
		else if(itemstack.stackSize >= 48)
			return 7;
		else if(itemstack.stackSize >= 40)
			return 6;
		else if(itemstack.stackSize >= 32)
			return 5;
		else if(itemstack.stackSize >= 24)
			return 4;
		else if(itemstack.stackSize >= 16)
			return 3;
		else if(itemstack.stackSize >= 8)
			return 2;
		else
			return 1;
	}
	
	private void drawTextureIn3D(int textureID) {
		Tessellator tesselator = Tessellator.instance;
		float x = (float)(textureID % 16 * 16 + 16) / 256.0F;
		float y = (float)(textureID / 16 * 16 + 0) / 256.0F;
        float u = (float)(textureID % 16 * 16 + 0) / 256.0F;
        float v = (float)(textureID / 16 * 16 + 16) / 256.0F;
        float scale = 0.7F;
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
		ItemRenderer.renderItemIn2D(tesselator, x, y, u, v, .06F);
		GL11.glPopMatrix();
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
