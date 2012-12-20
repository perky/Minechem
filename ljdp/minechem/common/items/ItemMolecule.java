package ljdp.minechem.common.items;

import java.util.ArrayList;
import java.util.List;

import ljdp.minechem.common.EnumMolecule;
import ljdp.minechem.common.ModMinechem;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItemMolecule extends Item {
	
	public ItemMolecule(int par1) {
		super(par1);
		setCreativeTab(ModMinechem.minechemTab);
		setHasSubtypes(true);
		setItemName("itemMolecule");
		setIconIndex(16);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		int itemDamage = par1ItemStack.getItemDamage();
		return EnumMolecule.getById(itemDamage).descriptiveName();
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ELEMENTBOTTLES_PNG;
	}
	
	public ArrayList<ItemStack> getElements(ItemStack itemstack) {
		EnumMolecule molecule = EnumMolecule.molecules[itemstack.getItemDamage()];
		ArrayList<ItemStack> elements = new ArrayList<ItemStack>();
		for(ItemStack element : molecule.components()) {
			elements.add(element.copy());
		}
		return elements;
	}
	
	private static String getFormula(ItemStack itemstack) {
		int itemDamage = itemstack.getItemDamage();
		EnumMolecule molecule = EnumMolecule.getById(itemDamage);
		ArrayList<ItemStack> components = molecule.components();
		String formula = "";
		for(ItemStack component : components) {
			if(component.getItem() instanceof ItemElement) {
				formula += ItemElement.getShortName(component);
				if(component.stackSize > 1)
					formula += component.stackSize;
			} else if(component.getItem() instanceof ItemMolecule) {
				if(component.stackSize > 1)
					formula += "(";
				formula += ItemMolecule.getFormula(component);
				if(component.stackSize > 1)
					formula += ")" + component.stackSize;
			}
		}
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
		par3List.add(getFormula(par1ItemStack));
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
    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World,
    		EntityPlayer par3EntityPlayer) {
    	
    	if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            --par1ItemStack.stackSize;
        }

        if (!par2World.isRemote)
        {
        	EnumMolecule molecule = getMolecule(par1ItemStack);

        	if(molecule == EnumMolecule.psilocybin) {
        		par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 30 * 20, 0));
        		par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.getId(), 20 * 20, 10));
        		par3EntityPlayer.attackEntityFrom(DamageSource.magic, 2);
        	} else {
        		par3EntityPlayer.attackEntityFrom(DamageSource.magic, 5);
        	}
        }

        return par1ItemStack;
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
