package am2.particles;

import java.util.Random;

public final class ParticleOrbitPoint extends ParticleController{

	private CoordStore target;
	private double distance;
	private boolean rotateClockwise;
	private double targetY;
	private double targetDistance;
	private double orbitSpeed;
	private double orbitAngle;
	private double orbitY = -512;
	private boolean useCurrentDistance;
	private boolean ignoreYCoord;

	private boolean shrinkingOrbit;
	private double shrinkSpeed = 0;
	private double shrinkTargetDistance = 0;

	private class CoordStore{
		public double x, y, z;

		public CoordStore(double x, double y, double z){
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	public ParticleOrbitPoint(AMParticle particleEffect, double orbitX, double orbitY, double orbitZ, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);
		target = new CoordStore(orbitX, orbitY, orbitZ);
		orbitAngle = particle.worldObj.rand.nextInt(360);
		rotateClockwise = particle.worldObj.rand.nextInt(10) < 5;
		generateNewTargetY();
		targetDistance = 1 + (particle.worldObj.rand.nextDouble() * 0.5);
	}

	public ParticleOrbitPoint setOrbitY(double orbitY){
		this.orbitY = orbitY;
		return this;
	}

	public ParticleOrbitPoint setIgnoreYCoordinate(boolean ignore){
		this.ignoreYCoord = ignore;
		return this;
	}

	public ParticleOrbitPoint SetTargetDistance(double targetDistance){
		this.targetDistance = targetDistance;
		this.useCurrentDistance = false;
		return this;
	}

	public ParticleOrbitPoint SetUseCurrentDistance(){
		this.useCurrentDistance = true;
		return this;
	}

	public ParticleOrbitPoint SetOrbitSpeed(double speed){
		this.orbitSpeed = speed;
		return this;
	}

	public ParticleOrbitPoint SetShrinkingOrbit(double shrinkSpeed, double newTargetDistance){
		this.shrinkingOrbit = true;
		this.shrinkSpeed = shrinkSpeed;
		this.shrinkTargetDistance = newTargetDistance;
		return this;
	}

	private void generateNewTargetY(){
		if (target != null){
			targetY = particle.worldObj.rand.nextDouble() * 2;
		}else{
			targetY = 0;
		}
	}

	private void generateNewDistance(){
		if (target != null){
			targetDistance = particle.worldObj.rand.nextDouble() * 2;
		}else{
			targetDistance = 0;
		}
	}

	/**
	 * Set true for clockwise, false for counterclockwise.
	 * Not calling this method will cause the direction to be random.
	 *
	 * @param clockwise
	 */
	public ParticleOrbitPoint setRotateDirection(boolean clockwise){
		this.rotateClockwise = clockwise;
		return this;
	}

	/**
	 * Sets the start angle of rotation, in radians
	 *
	 * @param angle
	 * @return
	 */
	public ParticleOrbitPoint setStartAngle(float angle){
		this.orbitAngle = angle;
		return this;
	}

	@Override
	public void doUpdate(){

		double posX;
		double posZ;
		double posY = particle.posY;

		double relativeTargetY = target.y + targetY;

		if (!ignoreYCoord){
			if (Math.abs(particle.posY - relativeTargetY) < 0.1){
				generateNewTargetY();
			}
		}

		if (useCurrentDistance){
			double deltaz = target.z - particle.posZ;
			double deltax = target.x - particle.posX;
			double currentDistance = Math.sqrt(deltaz * deltaz + deltax * deltax);
			posX = target.x + (Math.cos(orbitAngle) * currentDistance);
			posZ = target.z + (Math.sin(orbitAngle) * currentDistance);
		}else{
			if (shrinkingOrbit){
				if (targetDistance <= shrinkTargetDistance){
					shrinkingOrbit = false;
				}else{
					if (targetDistance < shrinkTargetDistance + shrinkSpeed * 10){
						double delta = targetDistance - shrinkTargetDistance;
						targetDistance -= delta * shrinkSpeed;
					}else{
						targetDistance -= shrinkSpeed;
					}
				}
			}
			posX = target.x + (Math.cos(orbitAngle) * targetDistance);
			posZ = target.z + (Math.sin(orbitAngle) * targetDistance);
		}

		if (!ignoreYCoord){
			if (particle.posY < relativeTargetY){
				particle.posY += 0.1;
			}else if (particle.posY > relativeTargetY){
				particle.posY -= 0.1;
			}
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

		if (orbitY != -512){
			posY = target.y + orbitY;
		}

		particle.setPosition(posX, posY, posZ);
		if (firstTick){
			particle.prevPosX = posX;
			particle.prevPosY = posY;
			particle.prevPosZ = posZ;
		}
	}

	@Override
	public ParticleController clone(){
		ParticleOrbitPoint clone = new ParticleOrbitPoint(particle, target.x, target.y, target.z, priority, exclusive);
		if (useCurrentDistance) clone.SetUseCurrentDistance();
		else clone.SetTargetDistance(targetDistance);

		if (orbitY != -512) clone.setOrbitY(orbitY);

		clone.SetOrbitSpeed(orbitSpeed);

		clone.setIgnoreYCoordinate(this.ignoreYCoord);

		clone.setRotateDirection(this.rotateClockwise);

		clone.setStartAngle((float)this.orbitAngle);

		if (this.shrinkingOrbit) clone.SetShrinkingOrbit(this.shrinkSpeed, this.shrinkTargetDistance);

		return clone;
	}

}
