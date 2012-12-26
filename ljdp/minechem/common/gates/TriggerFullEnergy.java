package ljdp.minechem.common.gates;

import net.minecraft.tileentity.TileEntity;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.gates.Trigger;

public class TriggerFullEnergy extends Trigger {

	public TriggerFullEnergy(int id) {
		super(id);
	}

	@Override
	public String getTextureFile() {
		return null;
	}
	
	@Override
	public boolean isTriggerActive(TileEntity tile, ITriggerParameter parameter) {
		IMinechemTriggerProvider triggerProvider = (IMinechemTriggerProvider)tile;
		return triggerProvider.hasFullEnergy();
	}
	
	@Override
	public String getDescription() {
		return "Has Full Energy?";
	}
	
	

}
