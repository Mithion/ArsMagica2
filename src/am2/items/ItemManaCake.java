package am2.items;

import am2.AMCore;
import am2.buffs.BuffEffectManaRegen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemManaCake extends ItemFood{

	public ItemManaCake() {
		super(3, 0.6f, false);
	}
	
	public ItemManaCake setUnlocalizedAndTextureName(String name){
		this.setUnlocalizedName(name);
		setTextureName(name);
		return this;
	}
	
	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.addPotionEffect(new BuffEffectManaRegen(600, 0));
		return super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
	}

}
