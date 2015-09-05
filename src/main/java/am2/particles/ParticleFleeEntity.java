package am2.particles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MathHelper;

public class ParticleFleeEntity extends ParticleController{

	private Entity target;
	private double fleeSpeed;
	private double targetDistance;

	public ParticleFleeEntity(AMParticle particleEffect, Entity fleeEntity, double fleeSpeed, double targetDistance, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);
		this.target = fleeEntity;
		this.fleeSpeed = fleeSpeed;
		this.targetDistance = targetDistance;
	}

	@Override
	public void doUpdate(){

		double posX;
		double posZ;
		double posY = particle.posY;
		double angle;

		double distanceToTarget = particle.getDistanceToEntity(target);
		double deltaZ = particle.posZ - target.posZ;
		double deltaX = particle.posX - target.posX;
		angle = Math.atan2(deltaZ, deltaX);

		double radians = angle;

		posX = particle.posX + (fleeSpeed * Math.cos(radians));
		posZ = particle.posZ + (fleeSpeed * Math.sin(radians));
		double deltaY;

		if (target instanceof EntityLiving){
			EntityLiving entityliving = (EntityLiving)target;
			deltaY = posY - (entityliving.posY + (double)entityliving.getEyeHeight());
		}else{
			deltaY = (target.boundingBox.minY + target.boundingBox.maxY) / 2D - posY;
		}
		double horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		float pitchRotation = (float)(-Math.atan2(deltaY, horizontalDistance));
		double pitchRadians = pitchRotation;

		posY = particle.posY + (fleeSpeed * Math.sin(pitchRadians));

		if (distanceToTarget > targetDistance){
			this.finish();
		}else{
			particle.setPosition(posX, posY, posZ);
		}
	}

	@Override
	public ParticleController clone(){
		return new ParticleFleeEntity(particle, target, fleeSpeed, targetDistance, priority, exclusive);
	}

}
