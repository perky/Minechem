package net.minecraft.minechem;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.AchievementList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;

public class GuiChemistryBook extends GuiContainer {
	
	private float currentScroll;
    private boolean isScrolling;
    private boolean wasClicking;
    private ContainerChemistryBook containerChemistryBook;
    
	public GuiChemistryBook(EntityPlayer entityplayer) {
		super( new ContainerChemistryBook(entityplayer) );
		entityplayer.craftingInventory = inventorySlots;
		containerChemistryBook = (ContainerChemistryBook)inventorySlots;
		currentScroll = 0.0F;
        isScrolling = false;
        allowUserInput = true;
        ySize = 208;
        xSize = 220;
	}
	
	public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0)
        {
        	int listSize = 1;
            int j = (listSize / 8 - 8) + 1;
            if (i > 0)
            {
                i = 1;
            }
            if (i < 0)
            {
                i = -1;
            }
            currentScroll -= (double)i / (double)j;
            if (currentScroll < 0.0F)
            {
                currentScroll = 0.0F;
            }
            if (currentScroll > 1.0F)
            {
                currentScroll = 1.0F;
            }
            containerChemistryBook.scrollTo(currentScroll);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        boolean flag = Mouse.isButtonDown(0);
        int k = guiLeft;
        int l = guiTop;
        int i1 = k + 155;
        int j1 = l + 17;
        int k1 = i1 + 14;
        int l1 = j1 + 88 + 2;
        if (!wasClicking && flag && i >= i1 && j >= j1 && i < k1 && j < l1)
        {
            isScrolling = true;
        }
        if (!flag)
        {
            isScrolling = false;
        }
        wasClicking = flag;
        if (isScrolling)
        {
            currentScroll = (float)(j - (j1 + 8)) / ((float)(l1 - j1) - 16F);
            if (currentScroll < 0.0F)
            {
                currentScroll = 0.0F;
            }
            if (currentScroll > 1.0F)
            {
                currentScroll = 1.0F;
            }
            containerChemistryBook.scrollTo(currentScroll);
        }
        super.drawScreen(i, j, f);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		int l = guiLeft;
        int i1 = guiTop;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = mc.renderEngine.getTexture("/minechem/guiRecipeBook.png");
        mc.renderEngine.bindTexture(k);
        
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        int j1 = l + 155;
        int k1 = i1 + 17;
        int l1 = k1 + 88 + 2;
        drawTexturedModalRect(l + 154, i1 + 17 + (int)((float)(l1 - k1 - 17) * currentScroll), 0, 208, 16, 16);
        
        List<String> recipelist = containerChemistryBook.getRecipes();
        int y = 0;
        for(String recipe : recipelist) {
        	boolean didShorten = false;
        	while(fontRenderer.getStringWidth(recipe) > 140) {
        		recipe = recipe.substring(0, recipe.length()-4);
        		didShorten = true;
        	}
        	if(didShorten)
        		recipe += "..";
        	fontRenderer.drawStringWithShadow(recipe, l+10, (i1+19)+(11*y), -1);
        	y++;
        }

	}

}
