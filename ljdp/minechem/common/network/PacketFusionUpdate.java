package ljdp.minechem.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntityFusion;

public class PacketFusionUpdate extends PacketTileEntityUpdate {
	
	protected TileEntityFusion fusion;
	
	public PacketFusionUpdate(TileEntityFusion fusion) {
		super(fusion);
		this.fusion = fusion;
	}
	
	public PacketFusionUpdate() {
		super();
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		super.writeData(outputStream);
		outputStream.writeInt(fusion.getEnergyStored());
	}
	
	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		super.readData(inputStream);
		if(tileEntity instanceof TileEntityDecomposer) {
			int energyStored = inputStream.readInt();
			fusion.setEnergyStored(energyStored);
		}
	}
}
