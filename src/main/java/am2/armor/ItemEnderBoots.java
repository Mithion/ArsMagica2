package am2.armor;

import am2.playerextensions.ExtendedProperties;
import am2.texture.ResourceManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemEnderBoots extends AMArmor{

	public ItemEnderBoots(ArmorMaterial inheritFrom, ArsMagicaArmorMaterial enumarmormaterial, int par3, int par4){
		super(inheritFrom, enumarmormaterial, par3, par4);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
		return 0;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
		this.itemIcon = ResourceManager.RegisterTexture("ender_boots", par1IconRegister);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack){
		if (player.posY >= world.getActualHeight() && ExtendedProperties.For(player).getIsFlipped())
			ExtendedProperties.For(player).toggleFlipped();
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.ender_boots"));
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}
}
