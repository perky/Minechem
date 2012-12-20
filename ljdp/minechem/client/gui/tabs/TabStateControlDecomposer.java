package ljdp.minechem.client.gui.tabs;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.PowerProvider;
import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntityDecomposer.State;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.gui.Gui;

public class TabStateControlDecomposer extends TabStateControl {
	
	private TileEntityDecomposer decomposer;
	
	enum TabState {
		jammed (MinechemHelper.getLocalString("tab.tooltip.jammed"), 0xAA0000, 6), 
		noBottles (MinechemHelper.getLocalString("tab.tooltip.nobottles"), 0xAA0000, 5), 
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
	
	public TabStateControlDecomposer(Gui gui, TileEntityDecomposer decomposer) {
		super(gui);
		this.decomposer = decomposer;
		this.state = TabState.unpowered;
		this.minWidth = 16 + 9;
		this.minHeight = 16 + 10;
		this.maxHeight = this.minHeight;
		this.maxWidth = this.minWidth;
	}
	
	@Override
	public void update() {
		super.update();
		MinechemPowerProvider provider = (MinechemPowerProvider) decomposer.getPowerProvider();
		State state = decomposer.getState();
		if(state == State.kProcessJammed)
			this.state = TabState.jammed;
		else if(decomposer.getState() == State.kProcessNoBottles)
			this.state = TabState.noBottles;
		else if(provider.getEnergyStored() > provider.getMinEnergyReceived() || provider.getCurrentEnergyUsage() > 0)
			this.state = TabState.powered;
		else
			this.state = TabState.unpowered;
		
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
