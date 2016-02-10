package am2.items;

import am2.texture.ResourceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemOre extends ArsMagicaItem{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	@SideOnly(Side.CLIENT)
	private String[] textures;

	public static final int META_VINTEUMDUST = 0;
	public static final int META_ARCANECOMPOUND = 1;
	public static final int META_ARCANEASH = 2;
	public static final int META_PURIFIEDVINTEUM = 3;
	public static final int META_CHIMERITE = 4;
	public static final int META_BLUETOPAZ = 5;
	public static final int META_SUNSTONE = 6;
	public static final int META_MOONSTONE = 7;
	public static final int META_ANIMALFAT = 8;

	public ItemOre(){
		super();
		this.setHasSubtypes(true);
	}

	@Override
	public boolean isPotionIngredient(ItemStack stack){
		switch (stack.getItemDamage()){
		case META_VINTEUMDUST:
		case META_ARCANEASH:
		case META_PURIFIEDVINTEUM:
			return true;
		}
		return false;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack){
		int meta = par1ItemStack.getItemDamage();
		switch (meta){
		case META_VINTEUMDUST:
			return StatCollector.translateToLocal("item.arsmagica2:vinteumDust.name");
		case META_ARCANECOMPOUND:
			return StatCollector.translateToLocal("item.arsmagica2:arcaneCompound.name");
		case META_ARCANEASH:
			return StatCollector.translateToLocal("item.arsmagica2:arcaneAsh.name");
		case META_PURIFIEDVINTEUM:
			return StatCollector.translateToLocal("item.arsmagica2:purifiedVinteumDust.name");
		case META_CHIMERITE:
			return StatCollector.translateToLocal("item.arsmagica2:chimerite.name");
		case META_BLUETOPAZ:
			return StatCollector.translateToLocal("item.arsmagica2:blueTopaz.name");
		case META_SUNSTONE:
			return StatCollector.translateToLocal("item.arsmagica2:sunstone.name");
		case META_MOONSTONE:
			return StatCollector.translateToLocal("item.arsmagica2:moonstone.name");
		case META_ANIMALFAT:
			return StatCollector.translateToLocal("item.arsmagica2:animalfat.name");
		}

		return super.getItemStackDisplayName(par1ItemStack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister){
		textures = new String[]{"vinteum_dust", "arcane_compound", "arcane_ash", "purified_vinteum", "chimerite_gem", "blue_topaz_gem", "sunstone_gem", "moonstone_gem", "animalFat"};

		icons = new IIcon[textures.length];

		int count = 0;
		for (String s : textures){
			icons[count++] = ResourceManager.RegisterTexture(s, par1IconRegister);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta){
		return icons[meta % icons.length];
	}

	@Override
	public String getPotionEffect(ItemStack stack){
		switch (stack.getItemDamage()){
		case META_VINTEUMDUST:
			return "+0+1+2-3&4-4+13";
		case META_ARCANEASH:
			return "+0+1-2+3&4-4+13";
		case META_PURIFIEDVINTEUM:
			return "+0-1+2+3&4-4+13";
		}
		return "";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < icons.length; ++i){
			par3List.add(new ItemStack(this, 1, i));
		}
	}
}
