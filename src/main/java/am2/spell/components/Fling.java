package am2.spell.components;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.spell.SpellUtils;

public class Fling implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, int blockx, int blocky, int blockz, int blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target) {
		double velocity = SpellUtils.instance.getModifiedDouble_Add(1.05f, stack, caster, target, world, 0, SpellModifiers.VELOCITY_ADDED);
		if (target instanceof EntityPlayer){
			AMNetHandler.INSTANCE.sendVelocityAddPacket(world, (EntityPlayer)target, 0.0f, velocity, 0.0f);
		}
		target.addVelocity(0.0, velocity, 0.0);
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 20;
	}

	@Override
	public float burnout(EntityLivingBase caster) {
		return 5;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier) {
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(world, "wind", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 2, 1);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.3f + rand.nextFloat() * 0.3f, 1, false));
				particle.setMaxAge(20);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier& 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public EnumSet<Affinity> getAffinity() {
		return EnumSet.of(Affinity.AIR);
	}
	
	@Override
	public int getID() {
		return 17;
	}
	
	@Override
	public Object[] getRecipeItems() {
		return new Object[]{
			new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE),
			Blocks.piston
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.01f;
	}
}
