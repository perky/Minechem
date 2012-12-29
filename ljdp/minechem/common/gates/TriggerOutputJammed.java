package ljdp.minechem.common.gates;

import net.minecraft.tileentity.TileEntity;
import ljdp.minechem.common.ModMinechem;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.gates.Trigger;

public class TriggerOutputJammed extends Trigger {

	public TriggerOutputJammed(int id) {
		super(id);
	}

	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ICONS_PNG;
	}
	
	@Override
	public int getIndexInTexture() {
		return 6;
	}
	
	@Override
	public String getDescription() {
		return "Output Jammed";
	}
	
	@Override
	public boolean isTriggerActive(TileEntity tile, ITriggerParameter parameter) {
		IMinechemTriggerProvider triggerProvider = (IMinechemTriggerProvider)tile;
		return triggerProvider.isJammed();
	}

}
