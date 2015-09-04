package am2.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class EnchantMagicResist extends Enchantment{

	public EnchantMagicResist(int effectID, int weight){
		super(effectID, weight, EnumEnchantmentType.armor);
		setName("magicresist");
	}

	@Override
	public int getMaxLevel(){
		return 5;
	}

	public static int ApplyEnchantment(ItemStack[] inventory, int damage){
		int maxEnchantLevel = EnchantmentHelper.getMaxEnchantmentLevel(AMEnchantments.magicResist.effectId, inventory);

		if (maxEnchantLevel > 0){
			damage -= MathHelper.floor_float((float)damage * (float)maxEnchantLevel * 0.15F);
		}

		return damage;
	}

}
