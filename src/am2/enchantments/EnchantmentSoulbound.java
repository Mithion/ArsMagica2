package am2.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import am2.items.ItemSpellBook;

public class EnchantmentSoulbound extends Enchantment{

	public EnchantmentSoulbound(int par1, int par2) {
		super(par1, par2, EnumEnchantmentType.all);
		setName("soulbound");
	}

	@Override
	public int getMinEnchantability(int par1) {
		return 1;
	}

	@Override
	public int getMaxEnchantability(int par1) {
		return 50;
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return stack != null && stack.getItem() instanceof ItemSpellBook;
	}
}
