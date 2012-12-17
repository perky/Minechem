package ljdp.minechem.client;

import ljdp.minechem.common.ContainerDecomposer;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.TileEntityDecomposer;
import ljdp.minechem.common.TileEntityDecomposer.State;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiDecomposer extends GuiContainerTabbed {
	
	TileEntityDecomposer decomposer;
	InventoryPlayer inventoryPlayer;
	int mouseX = 0;
	int mouseY = 0;
	int guiWidth = 176;
	int guiHeight = 166;
	
	public GuiDecomposer(InventoryPlayer inventoryPlayer, TileEntityDecomposer decomposer) {
		super(new ContainerDecomposer(inventoryPlayer, decomposer));
		this.decomposer = decomposer;
		this.inventoryPlayer = inventoryPlayer;
		addTab(new TabStateControlDecomposer(this, decomposer));
		addTab(new TabEnergy(this, decomposer));
		addTab(new TabHelp(this, "The Chemical Decomposer takes any item and decomposes it into its chemical parts, it will also split molecules into individual elements."));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		String info = "Chemical Decomposer";
		int infoWidth = fontRenderer.getStringWidth(info);
		fontRenderer.drawString(info, (guiWidth - infoWidth) / 2, 5, 0xCCCCCC);
		ItemStack currentHeldItem = inventoryPlayer.getItemStack();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.DECOMPOSER_GUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		drawTexturedModalRect(x, y, 0, 0, guiWidth, guiHeight);
	}


	@Override
	protected void drawTooltips(int mouseX, int mouseY) {
	}


}
