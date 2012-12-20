package ljdp.minechem.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;
import ljdp.minechem.common.tileentity.TileEntitySynthesis;

public class PacketSynthesisUpdate extends PacketTileEntityUpdate {
	
	protected TileEntitySynthesis synthesis;
	
	public PacketSynthesisUpdate(TileEntitySynthesis synthesis) {
		super(synthesis);
		this.synthesis = synthesis;
	}
	
	public PacketSynthesisUpdate() {
		super();
	}
	
	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		super.writeData(outputStream);
		MinechemPowerProvider provider = (MinechemPowerProvider)synthesis.getPowerProvider();
		outputStream.writeFloat(provider.getEnergyStored());
	}
	
	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		super.readData(inputStream);
		if(tileEntity instanceof TileEntitySynthesis) {
			float energyStored = inputStream.readFloat();
			synthesis = (TileEntitySynthesis) tileEntity;
			MinechemPowerProvider provider = (MinechemPowerProvider) synthesis.getPowerProvider(); 
			provider.setEnergyStored(energyStored);
		}
	}
	
}
