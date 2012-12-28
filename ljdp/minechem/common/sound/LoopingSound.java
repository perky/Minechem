package ljdp.minechem.common.sound;

import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LoopingSound {
	
	private String sound;
	private int soundLength;
	private int timer;
	private World world;
	private Entity entity;
	private float volume;
	private float pitch;
	private double x;
	private double y;
	private double z;
	
	public LoopingSound(String sound, int soundLength) {
		this.sound = sound;
		this.soundLength = soundLength;
		this.volume = 1.0F;
		this.pitch = 1.0F;
		this.timer = soundLength;
	}
	
	public LoopingSound(World world, Entity entity, String sound, int soundLength) {
		this(sound, soundLength);
		this.world = world;
		this.timer = soundLength;
	}
	
	public LoopingSound(World world, double x, double y, double z, String sound, int soundLength) {
		this(sound, soundLength);
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void play(World world, double x, double y, double z) {
		if(timer == soundLength) {
			timer = 0;
			if(this.entity == null) {
				world.playSoundEffect(x, y, z, this.sound, this.volume, this.pitch);
			} else {
				world.playSoundAtEntity(this.entity, this.sound, this.volume, this.pitch);
			}
		}
		timer++;
	}

}
