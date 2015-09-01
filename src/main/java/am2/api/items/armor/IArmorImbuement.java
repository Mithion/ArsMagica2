package am2.api.items.armor;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IArmorImbuement {
	/**
	 * Gets the ID of the infusion effect.  Must be unique.
	 */
	String getID();
	/**
	 * Gets the IIcon index for the texture in the GUI
	 */
	int getIconIndex();
	/**
	 * Gets the tier for this infusion
	 */
	ImbuementTiers getTier();
	/**
	 * Gets all situations under which this infusion applies
	 */
	EnumSet<ImbuementApplicationTypes> getApplicationTypes();
	/**
	 * Applies the effect.  This will be called when any of the application types is matched in an event.
	 * @param player The player wearing the armor
	 * @param world The world the player is in
	 * @param stack The itemstack that was matched
	 * @param matchedType The application type that was matched.
	 * @param params Depends on the application type.
	 * <br/>
	 * In the case of ON_TICK, it will be a 1-length array with the first element being the LivingEvent event <br/>
	 * In the case of ON_JUMP, it will be a 1-length array with the first element being the LivingJumpEvent event<br/>
	 * In the case of ON_HIT, it will be a 1-length array with the first element being the LivingHurtEvent event<br/>
	 * In the case of ON_MINING_SPEED, it will be a 1-length array with the first element being the BreakSpeed event <br/>
	 */
	boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object...params);

	/**
	 * Gets all armor slots that this effect can be applied to
	 */
	int[] getValidSlots();

	/**
	 * If the slot is on cooldown, can the effect still apply?
	 */
	boolean canApplyOnCooldown();

	/**
	 * Get the amount of cooldown to add to the slot once the effect applies
	 */
	int getCooldown();

	/**
	 * How much does the infusion damage the armor?
	 */
	int getArmorDamage();
}
