package am2.armor.infusions;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.buffs.BuffEffectWaterBreathing;
import am2.buffs.BuffList;

public class WaterBreathing implements IArmorImbuement{

	@Override
	public String getID() {
		return "wtrbrth";
	}

	@Override
	public int getIconIndex() {
		return 19;
	}

	@Override
	public ImbuementTiers getTier() {
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes() {
		return EnumSet.of(ImbuementApplicationTypes.ON_TICK);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params) {
		if (world.isRemote)
			return false;

		if (player.getAir() < 10){
			if (!player.isPotionActive(BuffList.waterBreathing.id)){
				BuffEffectWaterBreathing wb = new BuffEffectWaterBreathing(200, 0);
				player.addPotionEffect(wb);
				return true;
			}
		}
		return false;
	}

	@Override
	public int[] getValidSlots() {
		return new int[] {ImbuementRegistry.SLOT_HELM};
	}

	@Override
	public boolean canApplyOnCooldown() {
		return false;
	}

	@Override
	public int getCooldown() {
		return 4000;
	}

	@Override
	public int getArmorDamage() {
		return 100;
	}
}
