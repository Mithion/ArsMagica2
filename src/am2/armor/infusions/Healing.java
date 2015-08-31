package am2.armor.infusions;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.buffs.BuffEffectRegeneration;

public class Healing implements IArmorImbuement{

	@Override
	public String getID() {
		return "healing";
	}

	@Override
	public int getIconIndex() {
		return 13;
	}

	@Override
	public ImbuementTiers getTier() {
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes() {
		return EnumSet.of(ImbuementApplicationTypes.ON_HIT);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params) {

		if (world.isRemote)
			return false;

		if (player.getHealth() < (player.getMaxHealth() * 0.25f)){
			player.addPotionEffect(new BuffEffectRegeneration(240, 2));
			return true;
		}
		return false;
	}

	@Override
	public int[] getValidSlots() {
		return new int[] {ImbuementRegistry.SLOT_CHEST};
	}

	@Override
	public boolean canApplyOnCooldown() {
		return false;
	}

	@Override
	public int getCooldown() {
		return 6400;
	}

	@Override
	public int getArmorDamage() {
		return 30;
	}
}
