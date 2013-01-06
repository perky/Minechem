package ljdp.minechem.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.classloading.FMLForgePlugin;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.server.FMLServerHandler;
import ljdp.easypacket.EasyPacket;
import ljdp.easypacket.EasyPacketData;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.utils.MinechemHelper;

public class PacketActiveJournalItem extends EasyPacket {
	
	@EasyPacketData
	int itemID;
	@EasyPacketData
	int itemDMG;
	@EasyPacketData
	int slot;
	
	public PacketActiveJournalItem(ItemStack activeStack, EntityPlayer player) {
		this.itemID  = activeStack.itemID;
		this.itemDMG = activeStack.getItemDamage();
		this.slot = player.inventory.currentItem;
	}
	
	public PacketActiveJournalItem() {
		
	}
	
	@Override
	public boolean isChunkDataPacket() {
		return false;
	}

	@Override
	public void onReceive(Player player) {
		EntityPlayer entityPlayer = (EntityPlayer) player;
		ItemStack journal = entityPlayer.inventory.mainInventory[this.slot];
		if(journal != null && journal.itemID == MinechemItems.journal.shiftedIndex) {
			ItemStack activeStack = new ItemStack(this.itemID, 1, this.itemDMG);
			MinechemItems.journal.setActiveStack(activeStack, journal);
		}
	}

}
