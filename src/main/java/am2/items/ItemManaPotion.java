package am2.items;

import am2.buffs.BuffEffectManaRegen;
import am2.playerextensions.ExtendedProperties;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemManaPotion extends ArsMagicaItem{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private String[] textureFiles;

	public ItemManaPotion(){
		super();
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		textureFiles = new String[]{"mana_potion_lesser", "mana_potion_standard", "mana_potion_greater", "mana_potion_epic", "mana_potion_legendary"};
		icons = new IIcon[textureFiles.length];

		for (int i = 0; i < textureFiles.length; ++i){
			icons[i] = ResourceManager.RegisterTexture(textureFiles[i], par1IconRegister);
		}
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

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1){
		if (this == ItemsCommonProxy.lesserManaPotion){
			return icons[0];
		}else if (this == ItemsCommonProxy.standardManaPotion){
			return icons[1];
		}else if (this == ItemsCommonProxy.greaterManaPotion){
			return icons[2];
		}else if (this == ItemsCommonProxy.epicManaPotion){
			return icons[3];
		}else if (this == ItemsCommonProxy.legendaryManaPotion){
			return icons[4];
		}
		return icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return false;
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
		return EnumAction.drink;
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		par1ItemStack = new ItemStack(Items.glass_bottle);
		ExtendedProperties.For(par3EntityPlayer).setCurrentMana(ExtendedProperties.For(par3EntityPlayer).getCurrentMana() + getManaRestored());
		ExtendedProperties.For(par3EntityPlayer).forceSync();

		if (!par2World.isRemote){
			par3EntityPlayer.addPotionEffect(new BuffEffectManaRegen(getManaRegenDuration(), getManaRegenLevel()));
		}

		return par1ItemStack;
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
