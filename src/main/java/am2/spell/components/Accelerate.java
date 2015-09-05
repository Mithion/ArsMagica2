package am2.spell.components;

import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleOrbitEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class Accelerate implements ISpellComponent{

	private final Random rand = new Random();

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		if (rand.nextDouble() < 0.5){
			Block block = world.getBlock(blockx, blocky, blockz);
			if (block != Blocks.air){
				block.updateTick(world, blocky, blocky, blockz, rand);
			}
		}
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		caster.setAIMoveSpeed(caster.getAIMoveSpeed() * 1.6f);
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 6;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return 2;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle", x, y, z);
		if (particle != null){
			particle.AddParticleController(new ParticleOrbitEntity(particle, caster, 0.1f, 1, false).SetTargetDistance(rand.nextDouble() + 0.5));
			particle.setMaxAge(25 + rand.nextInt(10));
			if (colorModifier > -1){
				particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity(){
		return EnumSet.of(Affinity.NONE);
	}

	@Override
	public int getID(){
		return 0;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_YELLOW),
				Items.leather_boots,
				Items.redstone
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0;
	}

}
