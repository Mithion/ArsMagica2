package am2.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemArcaneGuardianSpellbook extends ItemSpellBook{

	public ItemArcaneGuardianSpellbook(){
		super();
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
		par3List.add(StatCollector.translateToLocal("am2.tooltip.arcanespellbook"));
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
	}

	@Override
	public int getItemEnchantability(){
		return 0;
	}

	@Override
	public boolean isBookEnchantable(ItemStack bookStack, ItemStack enchantBook){
		return false;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(ItemsCommonProxy.arcaneSpellBookEnchanted.copy());
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return EnumRarity.epic;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack){
		ItemStack activeSpell = GetActiveItemStack(par1ItemStack);
		if (activeSpell != null){
			return String.format("\2477%s \2477(" + activeSpell.getDisplayName() + "\2477)", StatCollector.translateToLocal("item.arsmagica2:arcaneSpellBook.name"));
		}
		return StatCollector.translateToLocal("item.arsmagica2:arcaneSpellBook.name");
	}
}
