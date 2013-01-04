package ljdp.minechem.common.items;

import ljdp.minechem.common.GuiHandler;
import ljdp.minechem.common.ModMinechem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemChemistJournal extends Item {

	public ItemChemistJournal(int id) {
		super(id);
		setItemName("minechem.itemChemistJournal");
		setCreativeTab(ModMinechem.minechemTab);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack,
			EntityPlayer entityPlayer, World world, int x, int y,
			int z, int side, float par8, float par9, float par10)
	{
		entityPlayer.openGui(ModMinechem.instance, GuiHandler.GUI_ID_JOURNAL, world, x, y, z);
		return false;
	}
}
