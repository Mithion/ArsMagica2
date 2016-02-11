package am2.items;

import am2.buffs.BuffList;
import am2.buffs.BuffMaxManaIncrease;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLiquidEssenceBottle extends ArsMagicaItem{

	public ItemLiquidEssenceBottle(){
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		if (!par3EntityPlayer.isPotionActive(BuffList.manaBoost))
			par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		stack = new ItemStack(Items.glass_bottle);
		playerIn.addPotionEffect(new BuffMaxManaIncrease(6000, 1));
		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack){
		return 32;
	}
}
