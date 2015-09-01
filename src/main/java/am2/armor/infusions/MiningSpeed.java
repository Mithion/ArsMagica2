package am2.armor.infusions;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;

public class MiningSpeed implements IArmorImbuement{

	@Override
	public String getID() {
		return "minespd";
	}

	@Override
	public int getIconIndex() {
		return 18;
	}

	@Override
	public ImbuementTiers getTier() {
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes() {
		return EnumSet.of(ImbuementApplicationTypes.ON_MINING_SPEED);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params) {
		BreakSpeed event = (BreakSpeed)params[0];
		event.newSpeed = event.originalSpeed * 1.5f;
		return true;
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
		return 1;
	}
}
