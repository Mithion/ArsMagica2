package am2.spell.components;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleArcToEntity;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;

public class LifeDrain implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		if (world.isRemote || !(target instanceof EntityLivingBase) || ((EntityLivingBase)target).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
			return true;
		}
		int magnitude = SpellUtils.instance.getModifiedInt_Add(4, stack, caster, target, world, 0, SpellModifiers.DAMAGE);

		boolean success = SpellHelper.instance.attackTargetSpecial(stack, target, DamageSource.causeIndirectMagicDamage(caster, caster), SpellUtils.instance.modifyDamage(caster, magnitude));

		if (success){
			caster.heal((int)Math.ceil(magnitude / 4));
			return true;
		}

		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 300;
	}

	@Override
	public float burnout(EntityLivingBase caster) {
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier) {
		for (int i = 0; i < 15; ++i){
			AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(world, "ember", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 1, 1);
				particle.setIgnoreMaxAge(true);
				particle.AddParticleController(new ParticleArcToEntity(particle, 1, caster, false).SetSpeed(0.03f).generateControlPoints());
				particle.setRGBColorF(1, 0.2f, 0.2f);
				particle.SetParticleAlpha(0.5f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier& 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity() {
		return EnumSet.of(Affinity.LIFE);
	}

	@Override
	public int getID() {
		return 31;
	}

	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
				new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_SUNSTONE),
				BlocksCommonProxy.aum
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.01f;
	}
}
