package am2.spell.components;

import am2.AMCore;
import am2.RitualShapeHelper;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.spell.component.interfaces.IRitualInteraction;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.buffs.BuffEffectCharmed;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.playerextensions.ExtendedProperties;
import am2.spell.SpellUtils;
import am2.utility.EntityUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Charm implements ISpellComponent, IRitualInteraction{

	@Override
	public int getID(){
		return 59;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_RED),
				new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE),
				new ItemStack(ItemsCommonProxy.crystalPhylactery, 1, ItemsCommonProxy.crystalPhylactery.META_EMPTY)
		};
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing side, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityCreature) || ((EntityCreature)target).isPotionActive(BuffList.charmed) || EntityUtilities.isSummon((EntityCreature)target)){
			return false;
		}

		int duration = SpellUtils.instance.getModifiedInt_Mul(BuffList.default_buff_duration, stack, caster, target, world, 0, SpellModifiers.DURATION);
		duration = SpellUtils.instance.modifyDurationBasedOnArmor(caster, duration);

		if (RitualShapeHelper.instance.checkForRitual(this, world, caster.getPosition()) != null){
			duration += (3600 * (SpellUtils.instance.countModifiers(SpellModifiers.BUFF_POWER, stack, 0) + 1));
			RitualShapeHelper.instance.consumeRitualReagents(this, world, caster.getPosition());
		}

		if (target instanceof EntityAnimal){
			((EntityAnimal)target).setInLove(null);
			return true;
		}

		if (ExtendedProperties.For(caster).getCanHaveMoreSummons()){
			if (caster instanceof EntityPlayer){
				if (target instanceof EntityCreature){
					BuffEffectCharmed charmBuff = new BuffEffectCharmed(duration, BuffEffectCharmed.CHARM_TO_PLAYER);
					charmBuff.setCharmer(caster);
					((EntityCreature)target).addPotionEffect(charmBuff);
				}
				return true;
			}else if (caster instanceof EntityLiving){
				if (target instanceof EntityCreature){
					BuffEffectCharmed charmBuff = new BuffEffectCharmed(duration, BuffEffectCharmed.CHARM_TO_MONSTER);
					charmBuff.setCharmer(caster);
					((EntityCreature)target).addPotionEffect(charmBuff);
				}
				return true;
			}
		}else{
			if (caster instanceof EntityPlayer){
				((EntityPlayer)caster).addChatMessage(new ChatComponentText("You cannot have any more summons."));
			}
			return true;
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 300;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 300;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 10; ++i){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "heart", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 2, 1);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.05f + rand.nextFloat() * 0.1f, 1, false));
				particle.setMaxAge(20);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.LIFE);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.1f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.hourglass;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(Items.wheat),
				new ItemStack(Items.wheat_seeds),
				new ItemStack(Items.carrot)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}
}
