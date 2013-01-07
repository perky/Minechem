package ljdp.minechem.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CommandPrintOreDict extends CommandBase {

	@Override
	public String getCommandName() {
		return "printOreDict";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		EntityPlayer player = (EntityPlayer) sender;
		ItemStack currentStack = player.inventory.getCurrentItem();
		if(currentStack != null) {
			int oreID = OreDictionary.getOreID(currentStack);
			String oreName = OreDictionary.getOreName(oreID);
			sender.sendChatToPlayer(oreName);
		}
	}

}
