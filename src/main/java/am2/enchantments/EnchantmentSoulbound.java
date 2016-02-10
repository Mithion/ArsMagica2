package am2.enchantments;

import am2.items.ItemSpellBook;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentSoulbound extends Enchantment{

	public EnchantmentSoulbound(int id, ResourceLocation name, int weight){
		super(id, name, weight, EnumEnchantmentType.ALL);
		setName("soulbound");
	}

	@Override
	public int getMinEnchantability(int par1){
		return 1;
	}

	@Override
	public int getMaxEnchantability(int par1){
		return 50;
	}

	@Override
	public int getMinLevel(){
		return 1;
	}

	@Override
	public int getMaxLevel(){
		return 1;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack){
		return stack != null && stack.getItem() instanceof ItemSpellBook;
	}
}
