package ljdp.minechem.common.gates;

import ljdp.minechem.common.ModMinechem;
import net.minecraft.tileentity.TileEntity;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.gates.Trigger;

public class TriggerFullEnergy extends Trigger {

	public TriggerFullEnergy(int id) {
		super(id);
	}

	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ICONS_PNG;
	}
	
	@Override
	public int getIndexInTexture() {
		return 9;
	}
	
	@Override
	public boolean isTriggerActive(TileEntity tile, ITriggerParameter parameter) {
		IMinechemTriggerProvider triggerProvider = (IMinechemTriggerProvider)tile;
		return triggerProvider.hasFullEnergy();
	}
	
	@Override
	public String getDescription() {
		return "Full Energy";
	}
	
	

}
