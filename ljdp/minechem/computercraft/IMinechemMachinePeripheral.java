package ljdp.minechem.computercraft;

import net.minecraft.item.ItemStack;

public interface IMinechemMachinePeripheral {
	
	public ItemStack takeEmptyTestTube();
	
	public ItemStack putEmptyTestTube(ItemStack testTube);
	
	public ItemStack takeOutput();
	
	public ItemStack putInput(ItemStack input);
	
	public ItemStack takeFusionStar();
	
	public ItemStack putFusionStar(ItemStack fusionStar);
	
	public ItemStack takeJournal();
	
	public ItemStack putJournal(ItemStack journal);
	
}