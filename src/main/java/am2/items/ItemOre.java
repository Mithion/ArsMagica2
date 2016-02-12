package am2.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemOre extends ArsMagicaItem{
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
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		for (int i = 0; i < 9; ++i){
			par3List.add(new ItemStack(this, 1, i));
		}
	}
}
