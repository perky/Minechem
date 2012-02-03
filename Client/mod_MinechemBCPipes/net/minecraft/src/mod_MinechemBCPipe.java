package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.buildcraft.core.Utils;
import net.minecraft.src.buildcraft.transport.BlockGenericPipe;
import net.minecraft.src.buildcraft.transport.ItemPipe;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.forge.ICustomItemRenderer;
import net.minecraft.src.forge.MinecraftForgeClient;

public class mod_MinechemBCPipe extends BaseMod implements ICustomItemRenderer {
	
	@MLProp static int minechemPipeID = 3010;
	static Item minechemPipe;
	
	@Override
	public String getVersion() {
		return "1.1.2";
	}

	@Override
	public void load() {
	}

	@Override
	public String getPriorities() {
		return "after:mod_Minechem;mod_BuildcraftTransport";
	}

	@Override
	public void ModsLoaded() {
		super.ModsLoaded();
		BuildCraftTransport.initialize();
		
		minechemPipe = createPipe(minechemPipeID, TileMinechemPipe.class, "Minechem Pipe");
		ModLoader.AddRecipe(new ItemStack(minechemPipe, 10), new Object[]{
			" T ",
			"DGD",
			Character.valueOf('T'), mod_Minechem.tableOfElements,
			Character.valueOf('D'), Item.diamond,
			Character.valueOf('G'), Block.glass
		});
		
		ModLoader.AddRecipe(new ItemStack(minechemPipe, 1), new Object[]{
			"T",
			"P",
			Character.valueOf('T'), mod_Minechem.tableOfElements,
			Character.valueOf('P'), BuildCraftTransport.pipeItemsDiamond
		});
		
		MinecraftForgeClient.registerCustomItemRenderer(minechemPipe.shiftedIndex, this);
	}
	
	protected static Item createPipe(int id, Class <? extends Pipe> pipeClass, String name) {
		Item newPipe = BlockGenericPipe.registerPipe(id, pipeClass);
		newPipe.setItemName(pipeClass.getSimpleName());
		ModLoader.AddName(newPipe, name);
		return newPipe;
	}
	
	@Override
	public void renderInventory(RenderBlocks renderblocks, int itemID,
			int meta) {
		Tessellator tessellator = Tessellator.instance;

		Block block = BuildCraftTransport.genericPipeBlock;
		int textureID = ((ItemPipe) Item.itemsList [itemID]).getTextureIndex();
		
		block.setBlockBounds(Utils.pipeMinPos, 0.0F, Utils.pipeMinPos,
				Utils.pipeMaxPos, 1.0F, Utils.pipeMaxPos);
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.setColorOpaque(255, 0, 0);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, textureID);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		
		//RenderManager.instance.getFontRenderer().drawString("M", 0, 0, -1);
	}
}
