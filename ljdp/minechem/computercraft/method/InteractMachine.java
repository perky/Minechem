package ljdp.minechem.computercraft.method;

import ljdp.minechem.common.tileentity.TileEntitySynthesis;
import ljdp.minechem.computercraft.IMinechemMachinePeripheral;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.core.Position;
import dan200.turtle.api.ITurtleAccess;

public class InteractMachine {
	
	public IMinechemMachinePeripheral getMachineInFront(ITurtleAccess turtle) {
		Vec3 vector = turtle.getPosition();
		ForgeDirection direction = ForgeDirection.getOrientation(turtle.getFacingDir());
		Position position = new Position(vector.xCoord, vector.yCoord, vector.zCoord, direction);
		position.moveForwards(1.0F);
		World world = turtle.getWorld();
		TileEntity tileEntity = world.getBlockTileEntity((int)position.x, (int)position.y, (int)position.z);
		if(tileEntity != null && tileEntity instanceof IMinechemMachinePeripheral)
			return (IMinechemMachinePeripheral) tileEntity;
		else
			return null;
	}
	
	public boolean tryPut(ItemStack beforeStack, ItemStack afterStack, ITurtleAccess turtle) {
		int selectedSlot = turtle.getSelectedSlot();
		beforeStack.stackSize  -= afterStack.stackSize;
		if(beforeStack.stackSize <= 0)
			turtle.setSlotContents(selectedSlot, null);
		else
			turtle.setSlotContents(selectedSlot, beforeStack);
		if(afterStack.stackSize > 0)
			return true;
		else
			return false;
	}
	
}
