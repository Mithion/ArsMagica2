package am2.items;

import am2.buffs.BuffEffectManaRegen;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemManaPotion extends ArsMagicaItem{

	public ItemManaPotion(){
		super();
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack){
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		ExtendedProperties props = ExtendedProperties.For(par3EntityPlayer);
		if (props.getCurrentMana() < props.getMaxMana()){
			par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		}
		return par1ItemStack;
	}

	private float getManaRestored(){
		float manaRestored = 0;
		if (this == ItemsCommonProxy.lesserManaPotion){
			manaRestored = 100;
		}else if (this == ItemsCommonProxy.standardManaPotion){
			manaRestored = 250;
		}else if (this == ItemsCommonProxy.greaterManaPotion){
			manaRestored = 2000;
		}else if (this == ItemsCommonProxy.epicManaPotion){
			manaRestored = 5000;
		}else if (this == ItemsCommonProxy.legendaryManaPotion){
			manaRestored = 10000;
		}

		return manaRestored;
	}

	private int getManaRegenLevel(){
		if (this == ItemsCommonProxy.lesserManaPotion){
			return 0;
		}else if (this == ItemsCommonProxy.standardManaPotion){
			return 0;
		}else if (this == ItemsCommonProxy.greaterManaPotion){
			return 1;
		}else if (this == ItemsCommonProxy.epicManaPotion){
			return 1;
		}else if (this == ItemsCommonProxy.legendaryManaPotion){
			return 2;
		}
		return 0;
	}

	private int getManaRegenDuration(){
		if (this == ItemsCommonProxy.lesserManaPotion){
			return 600;
		}else if (this == ItemsCommonProxy.standardManaPotion){
			return 1200;
		}else if (this == ItemsCommonProxy.greaterManaPotion){
			return 1800;
		}else if (this == ItemsCommonProxy.epicManaPotion){
			return 2400;
		}else if (this == ItemsCommonProxy.legendaryManaPotion){
			return 3000;
		}
		return 600;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack){
		return EnumAction.DRINK;
	}

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityPlayer player) {
        stack = new ItemStack(Items.glass_bottle);
        ExtendedProperties.For(player).setCurrentMana(ExtendedProperties.For(player).getCurrentMana() + getManaRestored());
        ExtendedProperties.For(player).forceSync();

        if (!world.isRemote){
            player.addPotionEffect(new BuffEffectManaRegen(getManaRegenDuration(), getManaRegenLevel()));
        }

        return stack;
    }

    @Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack){
		return 32;
	}

	@Override
	public boolean getHasSubtypes(){
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
	}

}
