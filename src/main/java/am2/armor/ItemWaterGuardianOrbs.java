package am2.armor;

import am2.items.ItemsCommonProxy;
import am2.proxy.gui.ModelLibrary;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemWaterGuardianOrbs extends AMArmor{

	public ItemWaterGuardianOrbs(ArmorMaterial inheritFrom, ArsMagicaArmorMaterial enumarmormaterial, int par3, int par4){
		super(inheritFrom, enumarmormaterial, par3, par4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot){
		return ModelLibrary.instance.waterOrbs;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
		return 0;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.water_orbs"));
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type){
		return "arsmagica2:" + ResourceManager.getMobTexturePath("bosses/water_guardian.png");
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(ItemsCommonProxy.waterOrbsEnchanted.copy());
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}
}
