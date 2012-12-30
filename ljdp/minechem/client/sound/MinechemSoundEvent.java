package ljdp.minechem.client.sound;

import ljdp.minechem.common.ModMinechem;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class MinechemSoundEvent {
	
	private static String SOUND_DIR = "/ljdp/minechem/sounds/";
	
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
		event.manager.soundPoolSounds.addSound(
				"ljdp/minechem/projector.ogg", 
				ModMinechem.class.getResource(SOUND_DIR + "projector.ogg")
		);
	}

}
