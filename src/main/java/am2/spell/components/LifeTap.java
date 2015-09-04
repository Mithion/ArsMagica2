package am2.spell.components;

import am2.AMCore;
import am2.RitualShapeHelper;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleApproachEntity;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class LifeTap implements ISpellComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){

		if (world.getBlock(blockx, blocky, blockz) == Blocks.mob_spawner){
			ItemStack[] reagents = RitualShapeHelper.instance.checkForRitual(this, world, blockx, blocky, blockz);
			if (reagents != null){
				if (!world.isRemote){
					world.setBlockToAir(blockx, blocky, blockz);
					RitualShapeHelper.instance.consumeRitualReagents(this, world, blockx, blocky, blockz);
					RitualShapeHelper.instance.consumeRitualShape(this, world, blockx, blocky, blockz);
					EntityItem item = new EntityItem(world);
					item.setPosition(blockx + 0.5, blocky + 0.5, blockz + 0.5);
					item.setEntityItemStack(new ItemStack(BlocksCommonProxy.inertSpawner));
					world.spawnEntityInWorld(item);
				}else{

				}

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityLivingBase)) return false;
		if (!world.isRemote){
			double damage = SpellUtils.instance.getModifiedDouble_Mul(2, stack, caster, target, world, 0, SpellModifiers.DAMAGE);
			float manaRefunded = (float)(((damage * 0.01)) * ExtendedProperties.For(caster).getMaxMana());

			if ((caster).attackEntityFrom(DamageSource.outOfWorld, (int)Math.floor(damage))){
				ExtendedProperties.For(caster).setCurrentMana(Math.min(ExtendedProperties.For(caster).getCurrentMana() + manaRefunded, ExtendedProperties.For(caster).getMaxMana()));
				ExtendedProperties.For(caster).forceSync();
			}else{
				return false;
			}
		}
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 0;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 50;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle2", x, y, z);
			if (particle != null){
				particle.addRandomOffset(2, 2, 2);
				particle.setMaxAge(15);
				particle.setParticleScale(0.1f);
				particle.AddParticleController(new ParticleApproachEntity(particle, target, 0.1, 0.1, 1, false));
				if (rand.nextBoolean())
					particle.setRGBColorF(0.4f, 0.1f, 0.5f);
				else
					particle.setRGBColorF(0.1f, 0.5f, 0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.LIFE, Affinity.ENDER);
	}

	@Override
	public int getID(){
		return 32;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
				BlocksCommonProxy.aum
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.corruption;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemsCommonProxy.mobFocus),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}
}
