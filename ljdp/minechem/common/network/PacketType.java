package ljdp.minechem.common.network;

public enum PacketType {
	TileEntityUpdate (PacketTileEntityUpdate.class), 
	DecomposerUpdate (PacketDecomposerUpdate.class),
	SynthesisUpdate  (PacketSynthesisUpdate.class),
	FusionUpdate	 (PacketFusionUpdate.class),
	GhostBlockUpdate (PacketGhostBlock.class),
	;
	
	public static PacketType[] packetTypes = PacketType.values();
	private Class<? extends PacketMinechem> packetMinechem;
	private PacketType(Class<? extends PacketMinechem> packetMinechem) {
		this.packetMinechem = packetMinechem;
	}
	
	public Class<? extends PacketMinechem> getPacketClass() {
		return this.packetMinechem;
	}
	
	public PacketMinechem newPacket() {
		try {
			PacketMinechem packet = packetMinechem.newInstance();
			packet.setPacketType(this);
			return packet;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PacketType getTypeOf(PacketMinechem packetMinechem) {
		for(PacketType packetType : packetTypes) {
			if(packetMinechem.getClass().equals(packetType.packetMinechem))
				return packetType;
		}
		return null;
	}
}
