package am2.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemLifeWard extends ArsMagicaItem{

	public ItemLifeWard(){
		super();
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5){
		if (par4 < 9 && par3Entity.ticksExisted % 80 == 0 && par3Entity instanceof EntityLivingBase){
			float abs = ((EntityLivingBase)par3Entity).getAbsorptionAmount();
			if (abs < 20){
				abs++;
				((EntityLivingBase)par3Entity).setAbsorptionAmount(abs);
			}
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.life_ward"));
		par3List.add(StatCollector.translateToLocal("am2.tooltip.life_ward2"));
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

}
