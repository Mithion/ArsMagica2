package am2.armor.infusions;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;

public class HungerBoost implements IArmorImbuement{

	@Override
	public String getID() {
		return "hungerup";
	}

	@Override
	public int getIconIndex() {
		return 20;
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
		if (player.getFoodStats().needFood()){
			player.getFoodStats().addStats(1, 1.0f);
			return true;
		}
		return false;
	}

	@Override
	public int[] getValidSlots() {
		return new int[] {ImbuementRegistry.SLOT_HELM};
	}

	@Override
	public boolean canApplyOnCooldown() {
		return true;
	}

	@Override
	public int getCooldown() {
		return 0;
	}

	@Override
	public int getArmorDamage() {
		return 3;
	}
}
