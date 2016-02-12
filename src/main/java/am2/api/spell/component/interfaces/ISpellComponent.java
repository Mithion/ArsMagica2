package am2.api.spell.component.interfaces;

import am2.api.spell.enums.Affinity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public interface ISpellComponent extends ISpellPart{
	/**
	 * Apply the effect to a single block
	 *
	 * @param stack     The item stack that contains the effect data
	 * @param world     The world the effect is in
	 * @param pos       The position of the block
	 * @param facing The face of the block that was targeted
	 * @param impactX   The x coordinate of the impact
	 * @param impactY   The y coordinate of the impact
	 * @param impactZ   The z coordinate of the impact
	 * @param caster    The caster of the spell
	 * @return True if the effect was successfully applied to the block
	 */
    boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster);

	/**
	 * Apply the effect to a single entity
	 *
	 * @param stack  The stack representing the spell
	 * @param world  The world the spell was cast in
	 * @param caster The caster of the spell
	 * @param target The current target of the spell
	 * @return True if the effect was applied successfully to the entity
	 */
	boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target);

	/**
	 * Gets the mana cost of the spell
	 */
	float manaCost(EntityLivingBase caster);

	/**
	 * Gets the burnout of the spell
	 */
	float burnout(EntityLivingBase caster);

	/**
	 * Gets any reagents that must be present in the caster's inventory in order
	 * to cast the spell.
	 */
	ItemStack[] reagents(EntityLivingBase caster);

	/**
	 * Spawn visual effects for the component
	 *
	 * @param colorModifier The color from the color modifier.  -1 if missing.
	 */
	void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier);

	/**
	 * Gets the affinity of the spell
	 */
	EnumSet<Affinity> getAffinity();

	/**
	 * Gets the amount (before diminishing returns) that this component, when successfully applied,
	 * shifts the caster's affinity
	 *
	 * @param affinity The affinity being shifted
	 */
	float getAffinityShift(Affinity affinity);
}
