package am2.spell.components;

import am2.AMCore;
import am2.RitualShapeHelper;
import am2.api.ArsMagicaApi;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.buffs.BuffEffectWateryGrave;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import am2.particles.*;
import am2.spell.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class WateryGrave implements ISpellComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){
			int duration = SpellUtils.instance.getModifiedInt_Mul(BuffList.default_buff_duration, stack, caster, target, world, 0, SpellModifiers.DURATION);
			duration = SpellUtils.instance.modifyDurationBasedOnArmor(caster, duration);

			int x = (int)Math.floor(target.posX);
			int y = (int)Math.floor(target.posY);
			int z = (int)Math.floor(target.posZ);
			if (RitualShapeHelper.instance.checkForRitual(this, world, x, y, z) != null){
				duration += (3600 * (SpellUtils.instance.countModifiers(SpellModifiers.BUFF_POWER, stack, 0) + 1));
				RitualShapeHelper.instance.consumeRitualReagents(this, world, x, y, z);
			}

			if (!world.isRemote)
				((EntityLivingBase)target).addPotionEffect(new BuffEffectWateryGrave(duration, SpellUtils.instance.countModifiers(SpellModifiers.BUFF_POWER, stack, 0)));
			return true;
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 80;
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
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "water_ball", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 2, 1);
				particle.AddParticleController(new ParticleOrbitEntity(particle, target, 0.2f, 1, false));
				particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
				particle.AddParticleController(new ParticleLeaveParticleTrail(particle, "water_ball", false, 5, 2, false)
						.addControllerToParticleList(new ParticleFloatUpward(particle, 0, -0.3f, 1, false))
						.setParticleRGB_F(1, 1, 1)
						.setTicksBetweenSpawns(2));
				particle.setMaxAge(20);
				particle.setParticleScale(0.01f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.WATER);
	}

	@Override
	public int getID(){
		return 58;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLUE),
				Blocks.stone,
				Items.leather_boots
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.05f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.hourglass;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(Blocks.stone)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}
}
