package am2.particles;

import net.minecraft.util.MathHelper;
import am2.api.math.AMVector3;

public class ParticleFleePoint extends ParticleController {

	private AMVector3 target;
	private double fleeSpeed;
	private double targetDistance;

	public ParticleFleePoint(AMParticle particleEffect, AMVector3 fleePoint, double fleeSpeed, double targetDistance, int priority, boolean exclusive) {
		super(particleEffect, priority, exclusive);
		this.target = fleePoint;
		this.fleeSpeed = fleeSpeed;
		this.targetDistance = targetDistance;
	}

	@Override
	public void doUpdate() {

		double posX;
		double posZ;
		double posY = particle.posY;
		double angle;

		double distanceToTarget = new AMVector3(particle).distanceTo(target);
		double deltaZ = particle.posZ - target.z;
		double deltaX = particle.posX - target.x;
		angle = Math.atan2(deltaZ, deltaX);

		double radians = angle;

		posX = particle.posX + (fleeSpeed * Math.cos(radians));
		posZ = particle.posZ + (fleeSpeed * Math.sin(radians));
		double deltaY = target.y - posY;
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
	public ParticleController clone() {
		return new ParticleFleePoint(particle, target, fleeSpeed, targetDistance, priority, exclusive);
	}

}
