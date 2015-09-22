package am2.enchantments;

import am2.AMCore;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class AMEnchantments{
	public static EnchantMagicResist magicResist = registerEnchantment(EnchantMagicResist.class, 5, "magic_resist", 100);
	public static EnchantmentSoulbound soulbound = registerEnchantment(EnchantmentSoulbound.class, 5, "soulbound", 101);

	public void Init(){
		int defMR = AMCore.config.getConfigurableEnchantmentID("magic_resist", 100);

		Enchantment.addToBookList(magicResist);
		Enchantment.addToBookList(soulbound);

		LanguageRegistry.instance().addStringLocalization("enchantment.magicresist", "en_US", "Magic Resistance");
		LanguageRegistry.instance().addStringLocalization("enchantment.soulbound", "en_US", "Soulbound");
	}

	private static <T extends Enchantment> T registerEnchantment(Class<? extends Enchantment> enchantmentClass, int weight, String configID, int default_value){
		int start_value = AMCore.config.getConfigurableEnchantmentID(configID, default_value);
		int enchID = start_value;
		boolean fullcircle = true;
		do{
			if (Enchantment.enchantmentsList[enchID] == null){
				fullcircle = false;
				break;
			}
			enchID++;
			enchID %= Enchantment.enchantmentsList.length;
		}while (enchID != start_value);

		if (fullcircle){
			throw new ArrayIndexOutOfBoundsException("All enchantment IDs are in use...can't find a free one to take!");
		}

		AMCore.log.info("Attempting to set enchantment %s to ID %d (configured currently as %d)", configID, enchID, start_value);
		AMCore.config.updateConfigurableEnchantmentID(configID, enchID);

		try{
			T ench = (T)enchantmentClass.getConstructor(int.class, int.class).newInstance(enchID, weight);
			AMCore.log.info("Successfully registered enchanment!");
			return ench;
		}catch (Throwable t){
			AMCore.log.error("Failed to register enchantment %s!", configID);
			t.printStackTrace();
		}
		return null;
	}

	public static int GetEnchantmentLevelSpecial(int enchID, ItemStack stack){
		int baseEnchLvl = EnchantmentHelper.getEnchantmentLevel(enchID, stack);
		/*if (enchID == imbuedArmor.effectId || enchID == imbuedBow.effectId || enchID == imbuedWeapon.effectId){
			if (baseEnchLvl > 3)
				return (baseEnchLvl & 0x6000) >> 13;
		}*/
		return baseEnchLvl;
	}
}
