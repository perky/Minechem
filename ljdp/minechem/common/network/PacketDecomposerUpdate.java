package ljdp.minechem.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ljdp.minechem.common.MinechemPowerProvider;
import ljdp.minechem.common.tileentity.TileEntityDecomposer;

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
		MinechemPowerProvider provider = (MinechemPowerProvider)decomposer.getPowerProvider();
		outputStream.writeByte(decomposer.getState().ordinal());
		outputStream.writeFloat(provider.getEnergyStored());
		outputStream.writeFloat(provider.getCurrentEnergyUsage());
	}
	
	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		super.readData(inputStream);
		if(tileEntity instanceof TileEntityDecomposer) {
			int state = inputStream.readByte();
			float energyStored = inputStream.readFloat();
			float energyUsage  = inputStream.readFloat();
			decomposer = (TileEntityDecomposer) tileEntity;
			decomposer.setState(state);
			MinechemPowerProvider provider = (MinechemPowerProvider) decomposer.getPowerProvider(); 
			provider.setEnergyStored(energyStored);
			provider.setCurrentEnergyUsage(energyUsage);
			decomposer.worldObj.markBlockForRenderUpdate2(x, y, z);
			//decomposer.worldObj.markBlockForUpdate(x, y, z);
		}
	}

}
