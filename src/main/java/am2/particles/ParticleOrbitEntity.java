package am2.particles;
import java.util.Random;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public final class ParticleOrbitEntity extends ParticleController {

	private final Entity target;
	private double distance;
	private final boolean rotateClockwise;
	private double targetY;
	private double curYOffset;
	private double targetDistance;
	private final double orbitSpeed;
	private double orbitAngle;
	private double orbitY = -512;
	private boolean ignoreYCoordinate = false;

	public ParticleOrbitEntity(AMParticle particleEffect, Entity orbitTarget, double orbitSpeed, int priority, boolean exclusive) {
		super(particleEffect, priority, exclusive);
		target = orbitTarget;
		orbitAngle = rand.nextInt(360);
		rotateClockwise = rand.nextBoolean();
		generateNewTargetY();
		targetDistance = 1 + (rand.nextDouble() * 0.5);
		this.orbitSpeed = orbitSpeed;
	}

	public ParticleOrbitEntity setOrbitY(double orbitY){
		this.orbitY = orbitY;
		return this;
	}

	public ParticleOrbitEntity SetTargetDistance(double targetDistance){
		this.targetDistance = targetDistance;
		return this;
	}

	private void generateNewTargetY(){
		if (target != null){
			targetY = (new Random().nextDouble() * target.height);
		}else{
			targetY = 0;
		}
	}

	private void generateNewDistance(){
		if (target != null){
			targetDistance = new Random().nextDouble() * 2;
		}else{
			targetDistance = 0;
		}
	}

	@Override
	public void doUpdate() {

		if (firstTick){
			curYOffset = particle.posY - (target.posY + target.getEyeHeight());
		}

		if (target == null || target.isDead){
			this.finish();
			return;
		}

		double posX;
		double posZ;
		double posY = particle.posY;

		if (Math.abs(targetY - curYOffset) < 0.1){
			generateNewTargetY();
		}

		posX = target.posX + (Math.cos(orbitAngle) * targetDistance);
		posZ = target.posZ + (Math.sin(orbitAngle) * targetDistance);

		if (targetY < curYOffset){
			curYOffset -= orbitSpeed / 4;
		}
		else if (targetY > curYOffset){
			curYOffset += orbitSpeed / 4;
		}

		if (rotateClockwise){
			orbitAngle += orbitSpeed;
		}else{
			orbitAngle -= orbitSpeed;
		}
		if (orbitAngle > 360){
			orbitAngle -= 360;
		}else if (orbitAngle < 0){
			orbitAngle += 360;
		}

		if (!ignoreYCoordinate){
			if (orbitY != -512){
				posY = (target.posY + target.getEyeHeight()) + orbitY;
			}else{
				int offset = 0;
				if (target instanceof EntityPlayer && !(target instanceof EntityClientPlayerMP))
					offset += 2 * target.height;
				posY = target.posY - target.getEyeHeight() + curYOffset + offset;
			}
		}

		particle.setPosition(posX, posY, posZ);
		if (firstTick){
			particle.prevPosX = posX;
			particle.prevPosY = posY;
			particle.prevPosZ = posZ;
		}
	}

	@Override
	public ParticleController clone() {
		ParticleOrbitEntity clone = new ParticleOrbitEntity(particle, target, orbitSpeed, priority, rotateClockwise).SetTargetDistance(targetDistance);
		if (orbitY != -512){
			clone.setOrbitY(orbitY);
		}
		clone.setIgnoreYCoordinate(ignoreYCoordinate);
		return clone;
	}

	public ParticleOrbitEntity setIgnoreYCoordinate(boolean b) {
		ignoreYCoordinate = b;
		return this;
	}

}
