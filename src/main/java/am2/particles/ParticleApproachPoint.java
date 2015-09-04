package am2.particles;

import net.minecraft.util.MathHelper;

public class ParticleApproachPoint extends ParticleController{

	private final double targetX, targetY, targetZ;
	private final double approachSpeed;
	private final double targetDistance;
	private boolean ignoreYCoord;

	public ParticleApproachPoint(AMParticle particleEffect, double targetX, double targetY, double targetZ, double approachSpeed, double targetDistance, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
		this.approachSpeed = approachSpeed;
		this.targetDistance = targetDistance;
	}

	private double getDistanceSqToPoint(double x, double y, double z){
		double var2 = particle.posX - x;
		double var4 = particle.posY - y;
		double var6 = particle.posZ - z;
		return var2 * var2 + var4 * var4 + var6 * var6;
	}

	public ParticleApproachPoint setIgnoreYCoordinate(boolean ignore){
		this.ignoreYCoord = ignore;
		return this;
	}

	@Override
	public void doUpdate(){

		double posX = particle.posX;
		double posZ = particle.posZ;
		double posY = particle.posY;
		double angle;

		double distanceToTarget = getDistanceSqToPoint(targetX, targetY, targetZ);
		double deltaZ = targetZ - particle.posZ;
		double deltaX = targetX - particle.posX;
		if (Math.abs(deltaX) > targetDistance || Math.abs(deltaZ) > targetDistance){
			angle = Math.atan2(deltaZ, deltaX);

			double radians = angle;

			posX = particle.posX + (approachSpeed * Math.cos(radians));
			posZ = particle.posZ + (approachSpeed * Math.sin(radians));

		}

		if (!ignoreYCoord){
			double deltaY = posY - targetY;

			double horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
			float pitchRotation = (float)(-Math.atan2(deltaY, horizontalDistance));
			double pitchRadians = pitchRotation;

			posY = particle.posY + (approachSpeed * Math.sin(pitchRadians));
		}

		if (distanceToTarget <= (targetDistance * targetDistance)){
			this.finish();
		}else{
			particle.setPosition(posX, posY, posZ);
		}
	}

	@Override
	public ParticleController clone(){
		return new ParticleApproachPoint(particle, targetX, targetY, targetZ, approachSpeed, targetDistance, priority, exclusive).setIgnoreYCoordinate(ignoreYCoord);
	}

}
