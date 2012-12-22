package ljdp.minechem.common.items;

import java.util.EnumMap;
import java.util.List;

import ljdp.minechem.api.core.EnumClassification;
import ljdp.minechem.api.core.EnumElement;
import ljdp.minechem.api.core.EnumRadioactivity;
import ljdp.minechem.api.util.Constants;
import ljdp.minechem.common.ModMinechem;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItemElement extends Item {
	
	
	private static final String textModifier = "\u00A7";
	private final static EnumElement[] elements = EnumElement.values();
	private final EnumMap classificationIndexes = new EnumMap<EnumClassification,Integer>(EnumClassification.class);
	
	
	
	public ItemElement(int par1) {
		super(par1);
		setCreativeTab(ModMinechem.minechemTab);
		setItemName("itemElement");
		setHasSubtypes(true);
		classificationIndexes.put(EnumClassification.nonmetal, 0);
		classificationIndexes.put(EnumClassification.halogen, 1);
		classificationIndexes.put(EnumClassification.inertGas, 2);
		classificationIndexes.put(EnumClassification.semimetallic, 3);
		classificationIndexes.put(EnumClassification.otherMetal, 4);
		classificationIndexes.put(EnumClassification.alkaliMetal, 5);
		classificationIndexes.put(EnumClassification.alkalineEarthMetal, 6);
		classificationIndexes.put(EnumClassification.transitionMetal, 7);
		classificationIndexes.put(EnumClassification.lanthanide, 8);
		classificationIndexes.put(EnumClassification.actinide, 9);
		classificationIndexes.put(EnumClassification.gas, 1);
		classificationIndexes.put(EnumClassification.solid, 17);
		classificationIndexes.put(EnumClassification.liquid, 33);
	}
	
	public void initiateRadioactivity(ItemStack itemstack, World world) {
		EnumRadioactivity radioactivity = getRadioactivity(itemstack);
		if(radioactivity != EnumRadioactivity.stable) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setLong("lastUpdate", world.getTotalWorldTime());
			tagCompound.setShort("life", (short) radioactivity.getLife());
			itemstack.setTagCompound(tagCompound);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int itemDamage) {
		EnumClassification roomState = elements[itemDamage].roomState();
		EnumClassification classification = elements[itemDamage].classification();
		int row = (Integer) classificationIndexes.get(roomState);
		int column = (Integer) classificationIndexes.get(classification);
		return row + column;
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ELEMENTBOTTLES_PNG;
	}
	
	public static String getShortName(ItemStack itemstack) {
		int atomicNumber = itemstack.getItemDamage();
		return elements[atomicNumber].name();
	}
	
	public static String getLongName(ItemStack itemstack) {
		int atomicNumber = itemstack.getItemDamage();
		return elements[atomicNumber].descriptiveName();
	}
	
	public static String getClassification(ItemStack itemstack) {
		int atomicNumber = itemstack.getItemDamage();
		return elements[atomicNumber].classification().descriptiveName();
	}
	
	public static String getRoomState(ItemStack itemstack) {
		int atomicNumber = itemstack.getItemDamage();
		return elements[atomicNumber].roomState().descriptiveName();
	}
	
	public static EnumRadioactivity getRadioactivity(ItemStack itemstack) {
		int atomicNumber = itemstack.getItemDamage();
		return elements[atomicNumber].radioactivity();
	}
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int slot, boolean isCurrentItem) {
		super.onUpdate(itemstack, world, entity, slot, isCurrentItem);
		if(getRadioactivity(itemstack) != EnumRadioactivity.stable)
			updateRadioactivity(itemstack, world, entity, slot, isCurrentItem);
	}
	
	public void updateRadioactivity(ItemStack itemstack, World world, Entity entity, int slot, boolean isCurrentItem) {
		NBTTagCompound tagCompound = itemstack.getTagCompound();
		if(tagCompound == null)
			return;
		
		long lastUpdate = tagCompound.getLong("lastUpdate");
		long tickDifference = world.getTotalWorldTime() - lastUpdate;
		int maxDamage = 0;
		String elementFrom = elements[itemstack.getItemDamage()].descriptiveName();
		
		while(tickDifference > 0) {
			EnumRadioactivity radioactivity = getRadioactivity(itemstack);
			if(radioactivity == EnumRadioactivity.stable)
				break;
			long life =  itemstack.getTagCompound().getShort("life");
			long lifeToRemove = Math.min(tickDifference, life);
			life -= lifeToRemove;
			tickDifference -= lifeToRemove;
			if(life <= 0) {
				maxDamage = Math.max(maxDamage, radioactivity.getDamage());
				int atomicId = itemstack.getItemDamage();
				itemstack.setItemDamage(atomicId - 1);
				initiateRadioactivity(itemstack, world);
				
			} else {
				tagCompound.setShort("life", (short)life);
				tagCompound.setLong("lastUpdate", world.getTotalWorldTime());
			}
		}
		
		if(maxDamage > 0) {
			entity.attackEntityFrom(DamageSource.generic, maxDamage);
			if(entity instanceof EntityPlayer && world.isRemote) {
				String elementTo = elements[itemstack.getItemDamage()].descriptiveName();
				String message = String.format("Radiation Damage! %s decayed into %s", elementFrom, elementTo);
				((EntityPlayer)entity).addChatMessage(message);
			}
		}
	}
	
	@Override
	public String getItemNameIS(ItemStack par1ItemStack) {
		return "element." + getShortName(par1ItemStack);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return textModifier + "l" + getLongName(par1ItemStack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(textModifier + "9" + getShortName(itemstack) + " (" + (itemstack.getItemDamage() + 1) + ")");
		
		String radioactivityColor;
		String timeLeft = getRadioactiveLife(itemstack);
		if(!timeLeft.equals("")) {
			timeLeft = "(" + timeLeft + ")";
		}
		EnumRadioactivity radioactivity = getRadioactivity(itemstack);
		switch(radioactivity) {
		case stable:
			radioactivityColor = textModifier + "7";
			break;
		case hardlyRadioactive:
			radioactivityColor = textModifier + "a";
			break;
		case slightlyRadioactive:
			radioactivityColor = textModifier + "2";
			break;
		case radioactive:
			radioactivityColor = textModifier + "e";
			break;
		case highlyRadioactive:
			radioactivityColor = textModifier + "6";
			break;
		case extremelyRadioactive:
			radioactivityColor = textModifier + "4";
			break;
		default:
			radioactivityColor = "";
			break;
		}
		par3List.add(radioactivityColor + radioactivity.getDescriptiveName() + " " + timeLeft);
		par3List.add(getClassification(itemstack));
		par3List.add(getRoomState(itemstack));
	}
	
	private String getRadioactiveLife(ItemStack itemstack) {
		String timeLeft = "";
		if(getRadioactivity(itemstack) != EnumRadioactivity.stable && itemstack.getTagCompound() != null ){
			NBTTagCompound tagCompound = itemstack.getTagCompound();
			int life = tagCompound.getShort("life");
			if(life < Constants.TICKS_PER_MINUTE)
				timeLeft = (life / Constants.TICKS_PER_SECOND) + "s";
			else if(life < Constants.TICKS_PER_HOUR)
				timeLeft = (life / Constants.TICKS_PER_MINUTE) + "m";
			else if(life < Constants.TICKS_PER_DAY)
				timeLeft = (life / Constants.TICKS_PER_HOUR) + "hr";
		}
		return timeLeft;
	}
	
	@Override
	public int getMetadata(int par1) {
		return par1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int itemID, CreativeTabs par2CreativeTabs,
			List par3List) {
		for(EnumElement element : EnumElement.values()) {
			par3List.add(new ItemStack(itemID, 1, element.ordinal()));
		}
	}
	
	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		initiateRadioactivity(par1ItemStack, par2World);
	}
	 
}
