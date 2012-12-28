package ljdp.minechem.client.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import ljdp.minechem.client.gui.tabs.TabHelp;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.blueprint.BlueprintBlock;
import ljdp.minechem.common.blueprint.MinechemBlueprint;
import ljdp.minechem.common.containers.ContainerProjector;
import ljdp.minechem.common.tileentity.TileEntityBlueprintProjector;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class GuiProjector extends GuiContainerTabbed {
	
	TileEntityBlueprintProjector projector;
	
	public GuiProjector(InventoryPlayer inventoryPlayer, TileEntityBlueprintProjector projector) {
		super(new ContainerProjector(inventoryPlayer, projector));
		this.projector = projector;
		addTab(new TabHelp(this, MinechemHelper.getLocalString("help.projector")));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.PROJECTOR_GUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
		float scale = 1.0F;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0F);
		ItemStack blueprintStack = projector.getStackInSlot(0);
		if(blueprintStack != null) {
			drawBlueprintInfo(blueprintStack);
		}
		GL11.glPopMatrix();
	} 
	
	private HashMap<Integer, Integer> getBlockCount(MinechemBlueprint blueprint) {
		HashMap<Integer, Integer> blockCount = new HashMap<Integer, Integer>();
		
		for(int x = 0; x < blueprint.xSize; x++) {
			for(int y = 0; y < blueprint.ySize; y++) {
				for(int z = 0; z < blueprint.zSize; z++) {
					int structureID = blueprint.getStructure()[y][x][z];
					int count = 0;
					if(blockCount.get(structureID) != null)
						count = (int) blockCount.get(structureID);
					count++;
					blockCount.put(structureID, count);
				}
			}
		}
		
		return blockCount;
	}

	private void drawBlueprintInfo(ItemStack blueprintStack) {
		MinechemBlueprint blueprint = MinechemItems.blueprint.getBlueprint(blueprintStack);
		if(blueprint == null) {
			System.out.println("NULL blueprint");
			return;
		}
		String name = blueprintStack.getDisplayName().replace("Blueprint", "");
		this.fontRenderer.drawStringWithShadow(name, 64, 12, 0xFFFFFF);
		HashMap<Integer, Integer> blockCount = getBlockCount(blueprint);
		HashMap<Integer, BlueprintBlock> blockLookup = blueprint.getBlockLookup();
		int y = 23;
		Iterator it = blockCount.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<Integer, Integer> entry = (Map.Entry) it.next();
			BlueprintBlock block = blockLookup.get(entry.getKey());
			if(block != null) {
				ItemStack itemstack = new ItemStack(block.block, 1, block.metadata);
				String info = String.format("%dx%s", entry.getValue(), itemstack.getDisplayName());
				this.fontRenderer.drawString(info, 64, y, 0xDDDDDD);
				y += 10;
			}
		}
	}

	@Override
	protected void drawTooltips(int mouseX, int mouseY) {
	}

}
