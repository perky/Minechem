package ljdp.minechem.client;

import net.minecraft.client.gui.Gui;

public class TabHelp extends Tab {

	public TabHelp(Gui gui) {
		super(gui);
		this.maxHeight = 100;
		this.maxWidth = 120;
	}

	@Override
	public void draw(int x, int y) {
		drawBackground(x, y);
		if(!isFullyOpened())
			return;
		
		fontRenderer.drawSplitString("testing one one one, hello my name is luke perkin, why HELLLOO THAR!", x + 8, y + 10, this.maxWidth, 0xFFFFFF);
	}

	@Override
	public String getTooltip() {
		return "Help";
	}

}
