package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiMinechemPipe extends GuiScreen {
	
	private GuiTextField[] textField;
	private PipeLogicMinechem delegate;
	private String[] formulas;
	private static final int[] colors = new int[]{
		0xFF000000, //black
		0xFFFFFFFF, //white
		0xFFFF0000, //red
		0xFF0000FF, //blue
		0xFF00FF00, //green
		0xFFFFFF00, //yellow
	};
	private static final String[] colorNames = new String[]{
		"black (down)",
		"white (up)",
		"red",
		"blue",
		"green",
		"yellow"
	};
	
	public GuiMinechemPipe(PipeLogicMinechem delegate, String[] formulas) {
		this.delegate = delegate;
		textField = new GuiTextField[6];
		this.formulas = formulas;
	}
	
	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
		controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 32, stringtranslate.translateKey("addServer.add")));
		
        for(int i = 0; i < 6; i++) {
        	textField[i] = new GuiTextField(this, fontRenderer, width/ 2 - 100, 25 + (i*25), 200, 20, "");
        	textField[i].setText(formulas[i]);
        }
	}
	
	protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled)
        {
            return;
        }
        if (guibutton.id == 0)
        {
        	String[] formulas = new String[6];
        	for(int i = 0; i < 6; i++) {
        		formulas[i] = textField[i].getText();
        	}
        	delegate.onFormulasChanged(formulas);
        	
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
    }
	
	protected void keyTyped(char c, int i)
    {
		for(GuiTextField tf : textField)
			tf.textboxKeyTyped(c, i);
    }
	
	protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        for(GuiTextField tf : textField)
        	tf.mouseClicked(i, j, k);
    }
	
	public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
	
	public void updateScreen()
    {
		for(GuiTextField tf : textField)
			tf.updateCursorCounter();
    }
	
	public void drawScreen(int i, int j, float f)
    {
		
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Enter formulas", width / 2, (height / 4 - 60) + 10, 0xffffff);
        for(GuiTextField tf : textField)
        	tf.drawTextBox();
        for(int y = 0; y < 6; y++) {
        	int xpos = width/2 - 140;
        	int ypos = 25 + (y*25);
        	drawRect(xpos, ypos, xpos+20, ypos+20, colors[y]);
        	drawString(fontRenderer, colorNames[y], xpos + 245, ypos, -1);
        }
        
        super.drawScreen(i, j, f);
    }

}
