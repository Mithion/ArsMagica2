package am2.armor.infusions;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;

public class LifeSaving implements IArmorImbuement{

	@Override
	public String getID() {
		return "lifesave";
	}

	@Override
	public int getIconIndex() {
		return 12;
	}

	@Override
	public ImbuementTiers getTier() {
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes() {
		return EnumSet.of(ImbuementApplicationTypes.ON_DEATH);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params) {
		LivingDeathEvent event = (LivingDeathEvent)params[0];
		event.setCanceled(true);
		player.setHealth(10);
		player.isDead = false;
		return true;
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
		return 8000;
	}

	@Override
	public int getArmorDamage() {
		return 75;
	}
}
