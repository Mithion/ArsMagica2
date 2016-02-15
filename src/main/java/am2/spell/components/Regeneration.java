package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.buffs.BuffEffectRegeneration;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleOrbitEntity;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Regeneration implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){
			if (ExtendedProperties.For((EntityLivingBase)target).getCanHeal()){
				int duration = SpellUtils.instance.getModifiedInt_Mul(240, stack, caster, target, world, 0, SpellModifiers.DURATION);
				duration = SpellUtils.instance.modifyDurationBasedOnArmor(caster, duration);
				if (!world.isRemote)
					((EntityLivingBase)target).addPotionEffect(new BuffEffectRegeneration(duration, SpellUtils.instance.countModifiers(SpellModifiers.BUFF_POWER, stack, 0)));
				ExtendedProperties.For((EntityLivingBase)target).setHealCooldown(60);
				return true;
			}
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 540;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return ArsMagicaApi.instance.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle", x, y - 1, z);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.1f, 1, false));
				particle.AddParticleController(new ParticleOrbitEntity(particle, target, 0.5f, 2, false).setIgnoreYCoordinate(true).SetTargetDistance(0.3f + rand.nextDouble() * 0.3));
				particle.setMaxAge(20);
				particle.setParticleScale(0.2f);
				particle.setRGBColorF(0.1f, 1f, 0.8f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.LIFE, Affinity.NATURE);
	}

	@Override
	public int getID(){
		return 46;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE),
				Items.golden_apple
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.05f;
	}
}
