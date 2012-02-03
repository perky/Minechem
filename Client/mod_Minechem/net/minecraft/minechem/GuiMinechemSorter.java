package net.minecraft.minechem;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.src.Container;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.PipeLogicMinechem;
import net.minecraft.src.Slot;
import net.minecraft.src.StringTranslate;

public class GuiMinechemSorter extends GuiScreen {
	
	private GuiTextField[] textField;
	private TileEntityMinechemSorter delegate;
	private String[] formulas;
	private static final int[] colors = new int[]{
		0xFF000000, //black
		0xFFFF0000, //red
		0xFF0000FF, //blue
		0xFF00FF00, //green
		0xFFFFFF00, //yellow
	};
	private static final String[] colorNames = new String[]{
		"black (down)",
		"red",
		"blue",
		"green",
		"yellow"
	};
	
	public GuiMinechemSorter(TileEntityMinechemSorter delegate) {
		super();
		this.delegate = delegate;
		textField = new GuiTextField[5];
		this.formulas = new String[5];
		for(int i = 0; i < 5; i++) {
			this.formulas[i] = delegate.formulas[i];
 		}
	}

	@Override
	public void initGui() {
		super.initGui();
		StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
		controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height - 20, stringtranslate.translateKey("addServer.add")));
		
        for(int i = 0; i < 5; i++) {
        	textField[i] = new GuiTextField(this, fontRenderer, width/ 2 - 75, (height / 8) + 25 + (i*35), 150, 12, "");
        	textField[i].setText(formulas[i]);
        }
	}
	
	@Override
	protected void keyTyped(char c, int i)
    {
		for(int i1 = 0; i1 < 5; i1++){
			textField[i1].textboxKeyTyped(c, i);
		}
    }
	
	@Override
	protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        for(GuiTextField tf : textField)
        	tf.mouseClicked(i, j, k);
    }
	
	@Override
	public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
	
	@Override
	public void updateScreen()
    {
		for(GuiTextField tf : textField) {
			tf.updateCursorCounter();
		}
    }
	
	@Override
	public void drawScreen(int i, int j, float f)
    {
		drawDefaultBackground();
        super.drawScreen(i, j, f);
        
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        
        drawCenteredString(fontRenderer, "Enter formulas", width / 2, 10, 0xffffff);
        for(GuiTextField tf : textField)
        	tf.drawTextBox();
        for(int y = 0; y < 5; y++) {
        	int xpos = (width/2 - 140)+35;
        	int ypos = (height / 8) + 25 + (y*35);
        	drawRect(xpos, ypos, xpos+20, ypos+20, colors[y]);
        	drawString(fontRenderer, colorNames[y], xpos + 30, ypos + 18, 0xff505050);
        }
        
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
    }

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled)
        {
            return;
        }
        if (guibutton.id == 0)
        {
        	String[] formulas = new String[5];
        	for(int i = 0; i < 5; i++) {
        		formulas[i] = textField[i].getText();
        	}
        	delegate.onFormulasChanged(formulas);
        	
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
	}
	
	

}
