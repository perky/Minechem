package ljdp.minechem.common.items;

import java.util.EnumMap;
import java.util.List;

import ljdp.minechem.api.core.EnumClassification;
import ljdp.minechem.api.core.EnumElement;
import ljdp.minechem.api.core.EnumRadioactivity;
import ljdp.minechem.api.util.Constants;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.RadiationInfo;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemElement extends Item {
	
	private final static EnumElement[] elements = EnumElement.values();
	private final EnumMap classificationIndexes = new EnumMap<EnumClassification,Integer>(EnumClassification.class);
	
	public ItemElement(int par1) {
		super(par1);
		setCreativeTab(ModMinechem.minechemTab);
		setItemName("minechem.itemElement");
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
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ITEMS_PNG;
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
	
	public static EnumElement getElement(ItemStack itemstack) {
		return EnumElement.elements[itemstack.getItemDamage()];
	}
	
	public static void attackEntityWithRadiationDamage(ItemStack itemstack, int damage, Entity entity) {
		entity.attackEntityFrom(DamageSource.generic, damage);
	}
	
	@Override
	public String getItemNameIS(ItemStack par1ItemStack) {
		return "minechem.itemElement." + getShortName(par1ItemStack);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return Constants.TEXT_MODIFIER + "l" + getLongName(par1ItemStack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(Constants.TEXT_MODIFIER + "9" + getShortName(itemstack) + " (" + (itemstack.getItemDamage() + 1) + ")");
		
		String radioactivityColor;
		String timeLeft = getRadioactiveLife(itemstack);
		if(!timeLeft.equals("")) {
			timeLeft = "(" + timeLeft + ")";
		}
		EnumRadioactivity radioactivity = getRadioactivity(itemstack);
		switch(radioactivity) {
		case stable:
			radioactivityColor = Constants.TEXT_MODIFIER + "7";
			break;
		case hardlyRadioactive:
			radioactivityColor = Constants.TEXT_MODIFIER + "a";
			break;
		case slightlyRadioactive:
			radioactivityColor = Constants.TEXT_MODIFIER + "2";
			break;
		case radioactive:
			radioactivityColor = Constants.TEXT_MODIFIER + "e";
			break;
		case highlyRadioactive:
			radioactivityColor = Constants.TEXT_MODIFIER + "6";
			break;
		case extremelyRadioactive:
			radioactivityColor = Constants.TEXT_MODIFIER + "4";
			break;
		default:
			radioactivityColor = "";
			break;
		}
		
		String radioactiveName = MinechemHelper.getLocalString("element.property." + radioactivity.name());
		par3List.add(radioactivityColor + radioactiveName + " " + timeLeft);
		par3List.add(getClassification(itemstack));
		par3List.add(getRoomState(itemstack));
	}
	
	private String getRadioactiveLife(ItemStack itemstack) {
		String timeLeft = "";
		if(getRadioactivity(itemstack) != EnumRadioactivity.stable && itemstack.getTagCompound() != null ){
			NBTTagCompound tagCompound = itemstack.getTagCompound();
			long life = tagCompound.getLong("life");
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
	
	public static Object createStackOf(EnumElement element, int amount) {
		return new ItemStack(MinechemItems.element, amount, element.ordinal());
	}

	public RadiationInfo getRadiationInfo(ItemStack element, World world) {
		EnumRadioactivity radioactivity = getRadioactivity(element);
		if(radioactivity == EnumRadioactivity.stable) {
			return new RadiationInfo(element, radioactivity);
		} else {
			NBTTagCompound stackTag = element.getTagCompound();
			if(stackTag == null) {
				return initiateRadioactivity(element, world);
			} else {
				int dimensionID = stackTag.getInteger("dimensionID");
				long lastUpdate = stackTag.getLong("lastUpdate");
				long life       = stackTag.getLong("life");
				RadiationInfo info = new RadiationInfo(element, life, lastUpdate, dimensionID, radioactivity);
				return info;
			}
		}
	}
	
	public RadiationInfo initiateRadioactivity(ItemStack element, World world) {
		EnumRadioactivity radioactivity = getRadioactivity(element);
		int dimensionID = world.getWorldInfo().getDimension();
		long lastUpdate = world.getTotalWorldTime();
		long life = radioactivity.getLife();
		RadiationInfo info = new RadiationInfo(element, life, lastUpdate, dimensionID, radioactivity);
		setRadiationInfo(info, element);
		return info;
	}

	public void setRadiationInfo(RadiationInfo radiationInfo, ItemStack element) {
		NBTTagCompound stackTag = element.getTagCompound();
		if(stackTag == null)
			stackTag = new NBTTagCompound();
		stackTag.setLong("lastUpdate", radiationInfo.lastRadiationUpdate);
		stackTag.setLong("life", radiationInfo.radiationLife);
		stackTag.setInteger("dimensionID", radiationInfo.dimensionID);
		element.setTagCompound(stackTag);
	}

	public RadiationInfo decay(ItemStack element, World world) {
		int atomicMass = element.getItemDamage();
		element.setItemDamage(atomicMass - 1);
		return initiateRadioactivity(element, world);
	}
	 
}
