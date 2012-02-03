package net.minecraft.minechem;

import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.mod_Minechem;

public class GuiMinechemMachine extends GuiContainer {
	
	protected TileEntityMinechemMachine tileMachine;
	
	public GuiMinechemMachine(Container container) {
		super(container);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
	}
	
	protected void drawIC2Information() {
		if(!mod_Minechem.requireIC2Power)
			return;
		
		if(tileMachine.IC2Energy > 0) {
			drawRect(5, 5, 15, 15, 0xFF00FF00);
		} else {
			drawRect(5, 5, 15, 15, 0xFFFF0000);
		}
		
		String s = "Max input: " + tileMachine.maxIC2EnergyInput + " EU";
		fontRenderer.drawString(s, 80, (ySize - 96) + 2, 0xFF404040);
	}
	
	
}
