package ljdp.minechem.client;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.utils.MinechemHelper;
import net.minecraft.client.gui.Gui;

public class TabHelp extends Tab {
	
	String helpString;
	int stringWidth;
	
	public TabHelp(Gui gui, String helpString) {
		super(gui);
		this.helpString = helpString;
		this.maxWidth = 120;
		this.stringWidth = this.maxWidth - 10;
		this.maxHeight = MinechemHelper.getSplitStringHeight(fontRenderer, helpString, this.stringWidth) + 20;
		this.overlayColor = 0x88BBBB;
	}

	@Override
	public void draw(int x, int y) {
		drawBackground(x, y);
		if(!isFullyOpened()) {
			drawIcon(ModMinechem.proxy.ICONS_PNG, 8, x + 2, y + 3);
			return;
		}
		fontRenderer.drawSplitString(helpString, x + 6, y + 10, this.stringWidth, 0x000000);
	}

	@Override
	public String getTooltip() {
		if(!isOpen())
			return "Help";
		else
			return null;
	}

}
