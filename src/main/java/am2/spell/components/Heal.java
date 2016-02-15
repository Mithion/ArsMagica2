package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;
import am2.particles.*;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Heal implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){
			if (((EntityLivingBase)target).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
				int healing = SpellUtils.instance.getModifiedInt_Mul(10, stack, caster, target, world, 0, SpellModifiers.HEALING);
				target.setFire(2);
				return SpellHelper.instance.attackTargetSpecial(stack, target, DamageSources.causeEntityHolyDamage(caster), healing * (0.5f + 2 * AffinityData.For(caster).getAffinityDepth(Affinity.LIFE)));
			}else{
				int healing = SpellUtils.instance.getModifiedInt_Mul(2, stack, caster, target, world, 0, SpellModifiers.HEALING);
				if (ExtendedProperties.For((EntityLivingBase)target).getCanHeal()){
					((EntityLivingBase)target).heal(healing);
					ExtendedProperties.For((EntityLivingBase)target).setHealCooldown(60);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 225f;
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
		if (target instanceof EntityLivingBase && ((EntityLivingBase)target).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
			for (int i = 0; i < 25; ++i){
				AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "symbols", x, y - 1, z);
				if (particle != null){
					particle.addRandomOffset(1, 1, 1);
					particle.AddParticleController(new ParticleHoldPosition(particle, 20, 1, true));
					particle.AddParticleController(new ParticleFloatUpward(particle, 0, -0.01f, 2, false));
					particle.AddParticleController(new ParticleFadeOut(particle, 2, false).setFadeSpeed(0.02f));
					particle.setParticleScale(0.1f);
					particle.setRGBColorF(1f, 0.2f, 0.2f);
				}
			}
		}else{
			for (int i = 0; i < 25; ++i){
				AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle", x, y - 1, z);
				if (particle != null){
					particle.addRandomOffset(1, 1, 1);
					particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.1f, 1, false));
					particle.AddParticleController(new ParticleOrbitEntity(particle, target, 0.5f, 2, false).setIgnoreYCoordinate(true).SetTargetDistance(0.3f + rand.nextDouble() * 0.3));
					particle.setMaxAge(20);
					particle.setParticleScale(0.2f);
					particle.setRGBColorF(0.1f, 1f, 0.1f);
					if (colorModifier > -1){
						particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
					}
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.LIFE);
	}

	@Override
	public int getID(){
		return 25;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_GREEN),
				BlocksCommonProxy.aum
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.05f;
	}
}
