package ljdp.minechem.client.gui;

import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.util.Tuple;
import ljdp.minechem.common.ModMinechem;

public class GuiMicroscopeSwitch extends GuiToggleSwitch {
	
	public GuiMicroscopeSwitch(GuiContainerTabbed container) {
		super();
		this.container = container;
		this.width  = 13;
		this.height = 13;
		this.numStates = 2;
		this.texture = ModMinechem.proxy.MICROSCOPE_GUI_PNG;
		ToggleButton buttonSynthesis = new ToggleButton();
		buttonSynthesis.u = 176;
		buttonSynthesis.v = 124;
		buttonSynthesis.tooltip = "gui.title.synthesis";
		
		ToggleButton buttonDecomposer = new ToggleButton();
		buttonDecomposer.u = 189;
		buttonDecomposer.v = 124;
		buttonDecomposer.tooltip = "gui.title.decomposer";
		this.buttons.put(0, buttonSynthesis);
		this.buttons.put(1, buttonDecomposer);
	}
}
