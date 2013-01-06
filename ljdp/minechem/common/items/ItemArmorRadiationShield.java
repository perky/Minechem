package ljdp.minechem.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ljdp.minechem.api.core.IRadiationShield;
import ljdp.minechem.common.ModMinechem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.IArmorTextureProvider;

public class ItemArmorRadiationShield extends ItemArmor implements IRadiationShield, IArmorTextureProvider {
	
	public static EnumArmorMaterial armorHazmat = EnumHelper.addArmorMaterial("MINECHEMHAZMAT", 10, new int[]{50,50,50,50}, 0);
	private float radiationShieldFactor;
	
	public ItemArmorRadiationShield(int id, int part, float radiationShieldFactor) {
		super(id, armorHazmat, 2, part);
		this.radiationShieldFactor = radiationShieldFactor;
		setItemName("minechem.itemArmorRadiationShield");
		setCreativeTab(ModMinechem.minechemTab);
	}

	public ItemArmorRadiationShield setRadiationShieldFactor(float value) {
		radiationShieldFactor = value;
		return this;
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ITEMS_PNG;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		int percentile = (int) (radiationShieldFactor * 100);
		String info = String.format("%d%% Radiation Shielding", percentile);
		list.add(info);
	}

	@Override
	public float getRadiationReductionFactor(int baseDamage, ItemStack itemstack, EntityPlayer player) {
		itemstack.damageItem(baseDamage / 4, player);
		return radiationShieldFactor;
	}

	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		return ModMinechem.proxy.HAZMAT_PNG;
	}
	
	
}
