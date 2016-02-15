package am2.spell.components;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class Repel implements ISpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing facing, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (target == null)
			return false;
		if (target == caster){
			EntityLivingBase source = caster;

			if (target instanceof EntityLivingBase)
				source = (EntityLivingBase)target;

			List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, source.getCollisionBoundingBox().expand(2, 2, 2));

			for (Entity e : ents){
				performRepel(world, caster, e);
			}
			return true;
		}

		performRepel(world, caster, target);

		return true;
	}

	private void performRepel(World world, EntityLivingBase caster, Entity target){
		Vec3 casterPos = new Vec3(caster.posX, caster.posY, caster.posZ);
		Vec3 targetPos = new Vec3(target.posX, target.posY, target.posZ);
		double distance = casterPos.distanceTo(targetPos) + 0.1D;

		Vec3 delta = new Vec3(targetPos.xCoord - casterPos.xCoord, targetPos.yCoord - casterPos.yCoord, targetPos.zCoord - casterPos.zCoord);

		double dX = delta.xCoord / 2.5D / distance;
		double dY = delta.yCoord / 2.5D / distance;
		double dZ = delta.zCoord / 2.5D / distance;
		if (target instanceof EntityPlayer){
			AMNetHandler.INSTANCE.sendVelocityAddPacket(world, (EntityPlayer)target, dX, dY, dZ);
		}
		target.motionX += dX;
		target.motionY += dY;
		target.motionZ += dZ;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 5.0f;
	}

	@Override
	public float burnout(EntityLivingBase caster){
		return ArsMagicaApi.getBurnoutFromMana(manaCost(caster));
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "sparkle", x, y, z);
		if (particle != null){
			particle.addRandomOffset(1, 2, 1);
			double dx = caster.posX - target.posX;
			double dz = caster.posZ - target.posZ;
			double angle = Math.toDegrees(Math.atan2(-dz, -dx));
			particle.AddParticleController(new ParticleMoveOnHeading(particle, angle, 0, 0.1 + rand.nextDouble() * 0.5, 1, false));
			particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f));
			particle.setMaxAge(20);
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
		return 47;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_WHITE),
				Items.water_bucket
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0;
	}
}
