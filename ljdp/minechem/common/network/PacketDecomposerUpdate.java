package ljdp.minechem.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ljdp.minechem.common.TileEntityDecomposer;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;

public class PacketDecomposerUpdate extends PacketTileEntityUpdate {
	
	protected TileEntityDecomposer decomposer;
	
	public PacketDecomposerUpdate(TileEntityDecomposer decomposer) {
		super(decomposer);
		this.decomposer = decomposer;
	}
	
	public PacketDecomposerUpdate() {
		super();
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		super.writeData(outputStream);
		outputStream.writeByte(decomposer.getState());
	}
	
	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		super.readData(inputStream);
		int state = inputStream.readByte();
		decomposer = (TileEntityDecomposer) tileEntity;
		decomposer.setState(state);
	}

}
