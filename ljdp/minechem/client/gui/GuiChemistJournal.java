package ljdp.minechem.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.client.gui.tabs.TabHelp;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.containers.ContainerChemistJournal;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GuiChemistJournal extends GuiContainerTabbed implements IVerticalScrollContainer {
	
	GuiVerticalScrollBar scrollBar;
	List<GuiFakeSlot> slots = new ArrayList();
	GuiFakeSlot[] synthesisSlots = new GuiFakeSlot[9];
	EntityPlayer player;
	int listHeight;
	
	public GuiChemistJournal(EntityPlayer entityPlayer) {
		super(new ContainerChemistJournal(entityPlayer.inventory));
		this.player = entityPlayer;
		this.xSize = 302;
		this.ySize = 191;
		
		int i = 0;
		int j = 0;
		for(Item item : Item.itemsList) {
			if(item == null)
				continue;
			int xpos = (i * 18) + 18;
			int ypos = (j * 18) + 10;
			GuiFakeSlot slot = new GuiFakeSlot(this, entityPlayer);
			slot.setXPos(xpos);
			slot.setYPos(ypos);
			slot.setItemStack(new ItemStack(item, 1, 0));
			slots.add(slot);
			if(++i == 6) {
				i = 0;
				j++;
			}
		}
		listHeight = j * 18;
	
		scrollBar = new GuiVerticalScrollBar(this, 125, 12, 164, this.xSize, this.ySize);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mouseButton) {
		super.mouseClicked(x, y, mouseButton);
		GuiFakeSlot clickedSlot = null;
		for(GuiFakeSlot slot : slots) {
			if(slot.getMouseIsOver()) {
				clickedSlot = slot;
				break;
			}
		}
		if(clickedSlot != null) {
			onSlotClick(clickedSlot);
		}
	}
	
	public void onSlotClick(GuiFakeSlot slot) {
		ItemStack itemstack = slot.getItemStack();
		SynthesisRecipe recipe = SynthesisRecipeHandler.instance.getRecipeFromOutput(itemstack);
		if(recipe != null) {
			ItemStack[] ingredients = MinechemHelper.convertChemicalArrayIntoItemStackArray(recipe.getShapedRecipe());
			int pos = 0;
			for(ItemStack ingredient : ingredients) {
				if(ingredient != null) {
					synthesisSlots[pos] = new GuiFakeSlot(this, this.player);
					
				}
				pos++;
			}
		}
	}

	@Override
	protected void drawTooltips(int mouseX, int mouseY) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int x = (width - this.xSize) / 2;
		int y = (height - this.ySize) / 2;
		
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			GL11.glPushMatrix();
				GL11.glScalef(2.0F, 2.0F, 2.0F);
				int textureID = mc.renderEngine.getTexture(ModMinechem.proxy.JOURNAL_GUI_PNG);
				mc.renderEngine.bindTexture(textureID);
				drawTexturedModalRect(0, 0, 0, 0, this.xSize/2, this.ySize/2);
			GL11.glPopMatrix();
			
			scrollBar.draw();
			drawSlots(x, y);
			drawSlotTooltips();
		GL11.glPopMatrix();
		
	}
	
	private void drawSlots(int x, int y) {
		GL11.glPushMatrix();
		ScissorHelper.startScissor(mc, x + 9, y + 7, 140, 176);
		int ypos = (int) ((listHeight-150) * scrollBar.getScrollValue());
		GL11.glTranslatef(0, -ypos, 0);
		for(GuiFakeSlot slot : slots) {
			slot.setYOffset(-ypos);
			slot.draw();
		}
		ScissorHelper.endScissor();
		GL11.glPopMatrix();
	}
	
	private void drawSlotTooltips() {
		for(GuiFakeSlot slot : slots) {
			slot.drawTooltip(mouseX + 10, mouseY);
		}
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		scrollBar.handleMouseInput();
	}

	@Override
	public boolean isScrollBarActive() {
		return true;
	}

	@Override
	public int getScreenWidth() {
		return width;
	}

	@Override
	public int getScreenHeight() {
		return height;
	}

	@Override
	public int getGuiWidth() {
		return xSize;
	}

	@Override
	public int getGuiHeight() {
		return ySize;
	}

	@Override
	public int getScrollAmount() {
		return 5;
	}

}
