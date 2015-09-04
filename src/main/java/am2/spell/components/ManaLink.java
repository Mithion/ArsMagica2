package am2.spell.components;

import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.particles.AMLineArc;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class ManaLink implements ISpellComponent{

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				BlocksCommonProxy.manaBattery,
				BlocksCommonProxy.essenceConduit,
				ItemsCommonProxy.crystalWrench,
				ItemsCommonProxy.manaFocus
		};
	}

	@Override
	public int getID(){
		return 67;
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){
			ExtendedProperties.For((EntityLivingBase)target).updateManaLink(caster);
			return true;
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 0;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 0;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		AMLineArc arc = (AMLineArc)AMCore.proxy.particleManager.spawn(world, "textures/blocks/wipblock2.png", caster, target);
		if (arc != null){
			arc.setExtendToTarget();
			arc.setIgnoreAge(false);
			arc.setRBGColorF(0.17f, 0.88f, 0.88f);
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.LIGHTNING, Affinity.ENDER, Affinity.ARCANE);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.25f;
	}

}
