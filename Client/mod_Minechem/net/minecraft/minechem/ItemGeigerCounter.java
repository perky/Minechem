package net.minecraft.minechem;

import java.util.Random;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

public class ItemGeigerCounter extends Item {

	private Random random;
	private int radiationLevel;
	private int lastRadiationLevel;
	private int currentTick;
	private int nextPop;
	public final int maxRadiationLevel = 40;
	
	public ItemGeigerCounter(int i) {
		super(i);
		setItemName("geigercounter");
		random = new Random();
		radiationLevel = 0;
		lastRadiationLevel = 0;
		currentTick = 0;
		nextPop = -1;
	}
	
	public void onRadiationTick(World world, int element) {
		radiationLevel += (element - 82);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		
		entityplayer.addChatMessage("Radiation level: "+lastRadiationLevel);
		
		return super.onItemRightClick(itemstack, world, entityplayer);
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity,
			int i, boolean flag) {
		
		currentTick++;
		if(nextPop != -1 && currentTick > nextPop) {
			currentTick = 0;
			nextPop = -1;
			EntityPlayer player = ModLoader.getMinecraftInstance().thePlayer;
			world.playSoundEffect((int)player.posX, (int)player.posY, (int)player.posZ, "note.hat", 1.0F, 2.0F);
		}
		
		if(radiationLevel == 0) {
			nextPop = -1;
			currentTick = 0;
		} else if(nextPop == -1) {
			lastRadiationLevel = radiationLevel;
			if(radiationLevel > maxRadiationLevel) radiationLevel = maxRadiationLevel;
			nextPop = maxRadiationLevel - radiationLevel;
			nextPop += MathHelper.getRandomIntegerInRange(random, -5, 5);
			if(nextPop < 0) nextPop = 0;
		}
		
		radiationLevel = 0;
	}
	
	

}
