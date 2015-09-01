package am2.spell.components;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.buffs.BuffEffectScrambleSynapses;
import am2.spell.SpellUtils;

public class ScrambleSynapses implements ISpellComponent{

	@Override
	public Object[] getRecipeItems() {
		return new Object[0];
	}

	@Override
	public int getID() {
		return 74;
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		if (target instanceof EntityLivingBase){
			int duration = SpellUtils.instance.getModifiedInt_Mul(200, stack, caster, target, world, 0, SpellModifiers.DURATION);
			duration = SpellUtils.instance.modifyDurationBasedOnArmor(caster, duration);

			int x = (int)Math.floor(target.posX);
			int y = (int)Math.floor(target.posY);
			int z = (int)Math.floor(target.posZ);

			if (!world.isRemote)
				((EntityLivingBase)target).addPotionEffect(new BuffEffectScrambleSynapses(duration, 0));
			return true;
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 7000;
	}

	@Override
	public float burnout(EntityLivingBase caster) {
		return 100;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier) {
	}

	@Override
	public EnumSet<Affinity> getAffinity() {
		return EnumSet.of(Affinity.LIGHTNING);
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.05f;
	}

}
