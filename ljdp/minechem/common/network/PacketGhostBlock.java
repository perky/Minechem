package ljdp.minechem.common.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ljdp.minechem.common.blueprint.MinechemBlueprint;
import ljdp.minechem.common.tileentity.TileEntityGhostBlock;

public class PacketGhostBlock extends PacketTileEntityUpdate {

	private TileEntityGhostBlock ghostBlock;

	public PacketGhostBlock(TileEntityGhostBlock ghostBlock) {
		super(ghostBlock);
		this.ghostBlock = ghostBlock;
	}
	
	public PacketGhostBlock() {
		super();
	}
	
	@Override
	public void writeData(DataOutputStream outputStream) throws IOException {
		super.writeData(outputStream);
		MinechemBlueprint blueprint = ghostBlock.getBlueprint();
		outputStream.writeInt(blueprint.id);
		outputStream.writeInt(ghostBlock.getBlockID());
	}
	
	@Override
	public void readData(DataInputStream inputStream) throws IOException {
		super.readData(inputStream);
		if(tileEntity instanceof TileEntityGhostBlock) {
			TileEntityGhostBlock ghostBlock = (TileEntityGhostBlock) tileEntity;
			int blueprintID = inputStream.readInt();
			int blockID = inputStream.readInt();
			MinechemBlueprint blueprint = MinechemBlueprint.blueprints.get(blueprintID);
			ghostBlock.setBlueprint(blueprint);
			ghostBlock.setBlockID(blockID);
		}
	}
}
