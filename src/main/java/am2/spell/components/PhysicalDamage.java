package am2.spell.components;

import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class PhysicalDamage implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityLivingBase)) return false;
		float baseDamage = 8;
		double damage = SpellUtils.instance.getModifiedDouble_Add(baseDamage, stack, caster, target, world, 0, SpellModifiers.DAMAGE);
		return SpellHelper.instance.attackTargetSpecial(stack, target, DamageSources.causeEntityPhysicalDamage(caster), SpellUtils.instance.modifyDamage(caster, (float)damage));
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 40;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 10;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 5; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "ember", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 0.5, 1);
				particle.addVelocity(rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.2, rand.nextDouble() * 0.2 - 0.1);
				particle.setAffectedByGravity();
				particle.setDontRequireControllers();
				particle.setMaxAge(5);
				particle.setParticleScale(0.1f);
				particle.setRGBColorF(0.8f, 0.2f, 0.2f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.EARTH);
	}

	@Override
	public int getID(){
		return 40;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED),
				Items.iron_sword
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}
}
