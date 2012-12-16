package ljdp.minechem.common.network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketMinechem {
	
	protected PacketType packetType;
	protected boolean isChunkDataPacket;
	
	public PacketMinechem(boolean isChunkDataPacket) {
		this.isChunkDataPacket = isChunkDataPacket;
		this.packetType = PacketType.getTypeOf(this);
	}
	
	public PacketMinechem() {}
	
	public void setPacketType(PacketType packetType) {
		this.packetType = packetType;
	}

	protected Packet250CustomPayload writePacket() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeByte(this.packetType.ordinal());
			writeData(outputStream);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = PacketHandler.MINECHEM_PACKET_CHANNEL;
		packet.data	   = bos.toByteArray();
		packet.length  = bos.size();
		packet.isChunkDataPacket = this.isChunkDataPacket;
		return packet;
	}
	
	public void readData(DataInputStream inputStream) throws IOException { }
	public void writeData(DataOutputStream outputStream) throws IOException { };

}
