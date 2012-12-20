package ljdp.minechem.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ljdp.minechem.common.ModMinechem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketTileEntityUpdate extends PacketMinechem {

	protected TileEntity tileEntity;
	int x;
	int y;
	int z;
	
	public PacketTileEntityUpdate(TileEntity tileEntity) {
		super(true);
		this.tileEntity = tileEntity;
	}
	
	public PacketTileEntityUpdate() {
		super();
	}

	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		outputStream.writeInt(tileEntity.xCoord);
		outputStream.writeInt(tileEntity.yCoord);
		outputStream.writeInt(tileEntity.zCoord);
	}
	
	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		x = inputStream.readInt();
		y = inputStream.readInt();
		z = inputStream.readInt();
		World world = ModMinechem.proxy.getClientWorld();
		this.tileEntity = world.getBlockTileEntity(x, y, z);
	}

}
