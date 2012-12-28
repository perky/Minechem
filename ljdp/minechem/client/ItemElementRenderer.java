package ljdp.minechem.client;

import ljdp.minechem.api.core.EnumClassification;
import ljdp.minechem.api.core.EnumElement;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.items.ItemElement;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityItem;
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
		if(helper == ItemRendererHelper.ENTITY_BOBBING)
			return true;
		if(helper == ItemRendererHelper.ENTITY_ROTATION)
			return true;
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemstack, Object... data) {
		if(type == ItemRenderType.INVENTORY) {
			renderItemInInventory(itemstack);
		} else if(type == ItemRenderType.EQUIPPED)  {
			renderItemInEquipped(itemstack);
		} else {
			EntityItem entityItem = (EntityItem)data[1];
			if(entityItem.worldObj == null) {
				float angle = (mc.getSystemTime() % 8000L) / 8000.0F * 360.0F;
				GL11.glPushMatrix();
				GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.2F, -0.5F, 0.0F);
				renderItemAsEntity(itemstack);
				GL11.glPopMatrix();
			} else {
				renderItemAsEntity(itemstack);
			}
		}
	}
	
	private void renderItemInInventory(ItemStack itemstack) {
		String shortName = ItemElement.getShortName(itemstack);
		renderContents(itemstack);
		renderTestTube(0,0,0);
		renderElementName(shortName);
	}
	
	private void renderItemInEquipped(ItemStack itemstack) {
		float scale = .06F;
		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, scale);
		GL11.glTranslatef(20.0F, 15.0F, 0.0F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		renderContents(itemstack);
		for(float i = 0.0F; i < 1.0F; i += .1F) {
			renderTestTube(0,0,i);
		}
		GL11.glPopMatrix();
	}
	
	private void renderItemAsEntity(ItemStack itemstack, Object...data) {
		EnumElement element = ItemElement.getElement(itemstack);
		int frame = getFrame();
		int textureID = 0;
		if(element.roomState() == EnumClassification.gas) {
			textureID = (2*16) + 1 + frame;
		} else if(element.roomState() == EnumClassification.liquid) {
			textureID = 1 + frame;
		} else {
			textureID = (1*16) + 1;
		}
		
		GL11.glPushMatrix();
		setColorForElement(element);
		drawTextureIn3D(textureID);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		drawTextureIn3D(0);
		GL11.glPopMatrix();
	}
	
	private void renderItemAsEntityInPipe(ItemStack itemstack) {
		
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
	
	private int getFrame() {
		float duration = 1500;
		float t = (int) (mc.getSystemTime() % duration);
		return (int) MinechemHelper.translateValue(t, 0, duration, 0, 7);
	}
	
	private void renderTestTube(float x, float y, float z) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		drawTexturedRectUV(x, y, z, 0, 0, 16, 16);
	}
	
	
	private void renderContents(ItemStack itemstack) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		int row = 0;
		int col = 0;
		EnumElement element = ItemElement.getElement(itemstack);
		int frame = getFrame();
		if(element.roomState() == EnumClassification.gas) {
			row = 1 + frame;
			col = 2;
		} else if(element.roomState() == EnumClassification.liquid) {
			row = 1 + frame;
			col = 0;
		} else {
			row = 1;
			col = 1;
		}
		setColorForElement(element);
		drawTexturedRectUV(0, 0, 0, row*16, col*16, 16, 16);
	}
	
	private void setColorForElement(EnumElement element) {
		switch(element.classification()) {
		case actinide:
			GL11.glColor3f(1.0F, 0.0F, 0.0F);
			break;
		case alkaliMetal:
			GL11.glColor3f(0.0F, 1.0F, 0.0F);
			break;
		case alkalineEarthMetal:
			GL11.glColor3f(0.0F, 0.0F, 1.0F);
			break;
		case halogen:
			GL11.glColor3f(1.0F, 1.0F, 0.0F);
			break;
		case inertGas:
			GL11.glColor3f(0.0F, 1.0F, 1.0F);
			break;
		case lanthanide:
			GL11.glColor3f(1.0F, 0.0F, 1.0F);
			break;
		case nonmetal:
			GL11.glColor3f(1.0F, 0.5F, 0.0F);
			break;
		case otherMetal:
			GL11.glColor3f(0.5F, 1.0F, 0.0F);
			break;
		case semimetallic:
			GL11.glColor3f(0.0F, 1.0F, 0.5F);
			break;
		case transitionMetal:
			GL11.glColor3f(0.0F, 0.5F, 1.0F);
			break;
		default:
			break;
		}
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
        //GL11.glTranslatef(0.0F, 0.0F, 0.0F);
		ItemRenderer.renderItemIn2D(tesselator, x, y, u, v, .05F);
		GL11.glPopMatrix();
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
	
	public void drawTexturedRectUV(float x, float y, float z, int u, int v, int w, int h) {
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
