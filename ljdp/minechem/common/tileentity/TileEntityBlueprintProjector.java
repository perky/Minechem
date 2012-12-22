package ljdp.minechem.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import buildcraft.api.core.Position;
import ljdp.minechem.common.MinechemBlocks;
import ljdp.minechem.common.blueprint.BlueprintFusion;
import ljdp.minechem.common.blueprint.MinechemBlueprint;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityBlueprintProjector extends TileEntity {
	
	private static int air;
	MinechemBlueprint blueprint;
	
	enum BlockStatus {
		CORRECT, INCORRECT
	}
	
	boolean isComplete = false;
	
	public TileEntityBlueprintProjector() {
		this.blueprint = new BlueprintFusion();
	}
	
	@Override
	public void updateEntity() {
		if(!isComplete)
			projectBlueprint();
	}
	
	private void projectBlueprint() {
		int facing = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		ForgeDirection direction = ForgeDirection.NORTH;
		Position position = new Position(xCoord, yCoord, zCoord, direction);
		position.moveForwards(blueprint.zSize + 1);
		position.moveLeft(Math.floor(blueprint.xSize / 2));
		
		boolean shouldProjectGhostBlocks = true;
		
		int totalIncorrectCount = blueprint.totalSize;
		for(int y = 0; y < blueprint.ySize; y++) {
			int horizontalIncorrectCount = blueprint.horizontalSize;
			Integer[][] blueprintSlice = blueprint.getHorizontalSlice(y);
			
			for(int x = 0; x < blueprint.xSize; x++) {
				for(int z = 0; z < blueprint.zSize; z++) {
					if(shouldProjectGhostBlocks) {
						BlockStatus blockStatus = projectGhostBlock(x, y, z, position);
						if(blockStatus == BlockStatus.CORRECT) {
							horizontalIncorrectCount--;
							totalIncorrectCount--;
						}
					} else {
						destroyGhostBlock(x, y, z, position);
					}
				}
			}
			if(horizontalIncorrectCount != 0)
				shouldProjectGhostBlocks = false;
		}
		
		if(totalIncorrectCount == 0)
			buildStructure();
	}

	private void buildStructure() {
		if(!isComplete) {
			isComplete = true;
			System.out.println("STRUCTURE BUILT!");
			List players = worldObj.playerEntities;
			for(EntityPlayer player : (ArrayList<EntityPlayer>)players) {
				player.addChatMessage("STRUCTURE BUILT!");
			}
		}
	}

	private BlockStatus projectGhostBlock(int x, int y, int z, Position position) {
		int worldX = (int) (position.x + x);
		int worldY = (int) (position.y + y);
		int worldZ = (int) (position.z + z);
		Integer ghostBlockID = blueprint.structure[y][x][z];
		int blockID = worldObj.getBlockId(worldX, worldY, worldZ);
		if(ghostBlockID == blueprint.air) {
			if(blockID == air)
				return BlockStatus.CORRECT;
			else
				return BlockStatus.INCORRECT;
		} else {
			int ghostBlockType = getGhostBlockType(ghostBlockID);
			if(blockID == air) {
				worldObj.setBlockAndMetadata(worldX, worldY, worldZ, MinechemBlocks.ghostBlock.blockID, ghostBlockType);
				return BlockStatus.INCORRECT;
			} else if(blockID == Block.blockSteel.blockID) {
				return BlockStatus.CORRECT;
			} else {
				return BlockStatus.INCORRECT;
			}
		}
	}
	
	private void destroyGhostBlock(int x, int y, int z, Position position) {
		int worldX = (int) (position.x + x);
		int worldY = (int) (position.y + y);
		int worldZ = (int) (position.z + z);
		int blockID = worldObj.getBlockId(worldX, worldY, worldZ);
		if(blockID == MinechemBlocks.ghostBlock.blockID) {
			worldObj.setBlock(worldX, worldY, worldZ, 0);
		}
	}
	
	private int getGhostBlockType(int blockID) {
		return 0;
	}

	public boolean isPowered() {
		//TODO: stub method.
		return true;
	}
}
