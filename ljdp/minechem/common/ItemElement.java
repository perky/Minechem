package ljdp.minechem.common;

import java.util.EnumMap;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItemElement extends Item {
	
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
	
	public static EnumClassification getRadioactivity(ItemStack itemstack) {
		int atomicNumber = itemstack.getItemDamage();
		return elements[atomicNumber].radioactivity();
	}
	
	@Override
	public String getItemNameIS(ItemStack par1ItemStack) {
		return "element." + getShortName(par1ItemStack);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return "§l" + getLongName(par1ItemStack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add("§9" + getShortName(par1ItemStack));
		
		String radioactivityColor;
		EnumClassification radioactivity = getRadioactivity(par1ItemStack);
		switch(radioactivity) {
		case stable:
			radioactivityColor = "§7";
			break;
		case hardlyRadioactive:
			radioactivityColor = "§a";
			break;
		case slightlyRadioactive:
			radioactivityColor = "§2";
			break;
		case radioactive:
			radioactivityColor = "§e";
			break;
		case highlyRadioactive:
			radioactivityColor = "§6";
			break;
		case extremelyRadioactive:
			radioactivityColor = "§4";
			break;
		default:
			radioactivityColor = "";
			break;
		}
		par3List.add(radioactivityColor + radioactivity.descriptiveName());
		
		par3List.add("Atomic Number " + (par1ItemStack.getItemDamage() + 1));
		par3List.add(getClassification(par1ItemStack));
		par3List.add(getRoomState(par1ItemStack));
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

	 
}
