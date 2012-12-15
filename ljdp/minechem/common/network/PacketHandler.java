package ljdp.minechem.common.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ljdp.minechem.common.ModMinechem;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {
	
	public static final String MINECHEM_PACKET_CHANNEL = "MineChem2";

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		try {
			byte packetID = inputStream.readByte();
			PacketType packetType = PacketType.packetTypes[packetID];
			if(packetType != null) {
				packetType.newPacket().readData(inputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendPacket(PacketMinechem packet) {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		Packet250CustomPayload customPacket = packet.writePacket();
		if(side == Side.SERVER) {
			PacketDispatcher.sendPacketToAllPlayers(customPacket);
		} else if(side == Side.CLIENT) {
			PacketDispatcher.sendPacketToServer(customPacket);
		}
	}

}
