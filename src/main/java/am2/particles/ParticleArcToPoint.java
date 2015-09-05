package am2.particles;

import am2.api.math.AMVector3;
import am2.utility.MathUtilities;

public class ParticleArcToPoint extends ParticleController{

	private final AMVector3 start;
	private final AMVector3 target;
	private AMVector3 firstControl;
	private AMVector3 secondControl;
	private float percent;
	private float speed;
	private final float offsetFactor;
	private final float halfOffsetFactor;

	public ParticleArcToPoint(AMParticle particleEffect, int priority, double startX, double startY, double startZ, double endX, double endY, double endZ, boolean exclusive){
		super(particleEffect, priority, exclusive);
		start = new AMVector3(startX, startY, startZ);
		target = new AMVector3(endX, endY, endZ);
		percent = 0.0f;
		speed = 0.03f;
		offsetFactor = 10;
		halfOffsetFactor = offsetFactor / 2;
		generateControlPoints();
	}

	public ParticleArcToPoint(AMParticle particleEffect, int priority, double endX, double endY, double endZ, boolean exclusive){
		this(particleEffect, priority, particleEffect.posX, particleEffect.posY, particleEffect.posZ, endX, endY, endZ, exclusive);
	}

	public ParticleArcToPoint generateControlPoints(){
		firstControl = new AMVector3(
				start.x + ((target.x - start.x) / 3),
				start.y + ((target.y - start.y) / 3),
				start.z + ((target.z - start.z) / 3));

		secondControl = new AMVector3(
				start.x + ((target.x - start.x) / 3 * 2),
				start.y + ((target.y - start.y) / 3 * 2),
				start.z + ((target.z - start.z) / 3 * 2));

		double offsetX = (rand.nextFloat() * offsetFactor) - halfOffsetFactor;
		double offsetZ = (rand.nextFloat() * offsetFactor) - halfOffsetFactor;
		double offsetY = (rand.nextFloat() * offsetFactor) - halfOffsetFactor;

		AMVector3 offset = new AMVector3(offsetX, offsetY, offsetZ);

		firstControl = firstControl.add(offset);
		secondControl = secondControl.add(offset);

		return this;
	}

	public ParticleArcToPoint specifyControlPoints(AMVector3 first, AMVector3 second){
		this.firstControl = first;
		this.secondControl = second;
		return this;
	}

	public ParticleArcToPoint SetSpeed(float speed){
		this.speed = speed;
		return this;
	}

	@Override
	public void doUpdate(){
		percent += speed;
		if (percent >= 1.0f){
			this.finish();
			return;
		}
		AMVector3 bez = MathUtilities.bezier(start, firstControl, secondControl, target, percent);
		particle.setPosition(bez.x, bez.y, bez.z);
	}

	@Override
	public ParticleController clone(){
		return new ParticleArcToPoint(particle, priority, target.x, target.y, target.z, exclusive).SetSpeed(speed).specifyControlPoints(firstControl, secondControl);
	}

}
