package ljdp.minechem.common.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ljdp.minechem.api.core.EnumMolecule;
import ljdp.minechem.api.util.Constants;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMolecule extends Item {
	
	private static Random random = new Random();
	
	public ItemMolecule(int par1) {
		super(par1);
		setCreativeTab(ModMinechem.minechemTab);
		setHasSubtypes(true);
		setItemName("minechem.itemMolecule");
		setIconIndex(16);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		int itemDamage = par1ItemStack.getItemDamage();
		return EnumMolecule.getById(itemDamage).descriptiveName();
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ITEMS_PNG;
	}
	
	public ArrayList<ItemStack> getElements(ItemStack itemstack) {
		EnumMolecule molecule = EnumMolecule.molecules[itemstack.getItemDamage()];
		return MinechemHelper.convertChemicalsIntoItemStacks(molecule.components());
	}
	
	@Override
	public String getItemNameIS(ItemStack par1ItemStack) {
		return getItemName() + "." + getMolecule(par1ItemStack).name();
	}
	
	public String getFormula(ItemStack itemstack) {
		ArrayList<ItemStack> components = getElements(itemstack);
		String formula = "";
		for(ItemStack component : components) {
			if(component.getItem() instanceof ItemElement) {
				formula += ItemElement.getShortName(component);
				if(component.stackSize > 1)
					formula += component.stackSize;
			} else if(component.getItem() instanceof ItemMolecule) {
				if(component.stackSize > 1)
					formula += "(";
				formula += getFormula(component);
				if(component.stackSize > 1)
					formula += ")" + component.stackSize;
			}
		}
		return formula;
	}
	
	public String getFormulaWithSubscript(ItemStack itemstack) {
		String formula = getFormula(itemstack);
		return subscriptNumbers(formula);
	}
	
	private static String subscriptNumbers(String string) {
		string = string.replace('0', '\u2080');
		string = string.replace('1', '\u2081');
		string = string.replace('2', '\u2082');
		string = string.replace('3', '\u2083');
		string = string.replace('4', '\u2084');
		string = string.replace('5', '\u2085');
		string = string.replace('6', '\u2086');
		string = string.replace('7', '\u2087');
		string = string.replace('8', '\u2088');
		string = string.replace('9', '\u2089');
		return string;
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add("\u00A79" + getFormulaWithSubscript(par1ItemStack));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int itemID, CreativeTabs par2CreativeTabs,
			List par3List) {
		for(EnumMolecule molecule : EnumMolecule.molecules) {
			par3List.add(new ItemStack(itemID, 1, molecule.id()));
		}
	}

	public EnumMolecule getMolecule(ItemStack itemstack) {
		return EnumMolecule.getById(itemstack.getItemDamage());
	}
	
	/**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.drink;
    }
    
    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    @Override
    public ItemStack onFoodEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
    	if (!entityPlayer.capabilities.isCreativeMode)
            --itemStack.stackSize;

        if (world.isRemote)
        	return itemStack;
        
    	EnumMolecule molecule = getMolecule(itemStack);
    	MinechemHelper.triggerPlayerEffect(molecule, entityPlayer);
    	
        return itemStack;
    }
    
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}
	
	/**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

}
