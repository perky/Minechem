package ljdp.minechem.client.gui.tabs;

import net.minecraft.client.gui.Gui;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.client.gui.tabs.TabStateControlDecomposer.TabState;
import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityDecomposer.State;
import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import ljdp.minechem.common.utils.MinechemHelper;

public class TabStateControlSynthesis extends TabStateControl {
	
	TileEntitySynthesis synthesis;
	enum TabState {
		norecipe (MinechemHelper.getLocalString("tab.tooltip.norecipe"), 0xAA0000, 10),
		powered (MinechemHelper.getLocalString("tab.tooltip.powered"), 0x00CC00, -1), 
		unpowered (MinechemHelper.getLocalString("tab.tooltip.unpowered"), 0xAA0000, 7);
		public String tooltip;
		public int color;
		public int iconIndex;
		private TabState(String tooltip, int color, int iconIndex) {
			this.tooltip = tooltip;
			this.color = color;
			this.iconIndex = iconIndex;
		}
	}
	TabState state;
	
	public TabStateControlSynthesis(Gui gui, TileEntitySynthesis synthesis) {
		super(gui);
		this.synthesis = synthesis;
		this.state = TabState.norecipe;
		this.minWidth = 16 + 9;
		this.minHeight = 16 + 10;
		this.maxHeight = this.minHeight;
		this.maxWidth = this.minWidth;
	}
	
	@Override
	public void update() {
		super.update();
		SynthesisRecipe recipe = synthesis.getCurrentRecipe();
		MinechemPowerProvider provider = (MinechemPowerProvider) synthesis.getPowerProvider();
		if(recipe == null) {
			state = TabState.norecipe;
		} else {
			int energyCost = recipe.energyCost();
			if(provider.getEnergyStored() >= energyCost)
				state = TabState.powered;
			else
				state = TabState.unpowered;
		}

		this.overlayColor = this.state.color;
	}
	
	@Override
	public void draw(int x, int y) {
		drawBackground(x, y);
		if(this.state.iconIndex != -1)
			drawIcon(ModMinechem.proxy.ICONS_PNG, this.state.iconIndex, x+3, y+5);
		if(!isFullyOpened())
			return;
	}
	
	@Override
	public String getTooltip() {
		return this.state.tooltip;
	}

}
