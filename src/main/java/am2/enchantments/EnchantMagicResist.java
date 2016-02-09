package am2.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class EnchantMagicResist extends Enchantment{

	public EnchantMagicResist(int effectID, ResourceLocation name, int weight){
		super(effectID, name, weight, EnumEnchantmentType.ARMOR);
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
