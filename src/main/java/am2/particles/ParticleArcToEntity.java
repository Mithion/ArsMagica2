package am2.particles;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.utility.MathUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;

public class ParticleArcToEntity extends ParticleController {

	private AMVector3 start;
	private Entity target;
	private AMVector3 firstControl;
	private AMVector3 secondControl;
	private float percent;
	private float speed;
	private float offsetFactor;
	private float halfOffsetFactor;
	
	public ParticleArcToEntity(AMParticle particleEffect, int priority, double startX, double startY, double startZ, Entity target, boolean exclusive) {
		super(particleEffect, priority, exclusive);
		start = new AMVector3(startX, startY, startZ);
		percent = 0.0f;
		speed = 0.03f;
		offsetFactor = 10;
		halfOffsetFactor = offsetFactor / 2;	
		this.target = target;
		
		generateControlPoints();
	}
	
	public ParticleArcToEntity(AMParticle particleEffect, int priority, Entity target, boolean exclusive) {
		this(particleEffect, priority, particleEffect.posX, particleEffect.posY, particleEffect.posZ, target, exclusive);
	}
	
	public ParticleArcToEntity generateControlPoints(){
		firstControl = new AMVector3(
				start.x + ((target.posX - start.x) / 3), 
				start.y + ((target.posY - start.y) / 3), 
				start.z + ((target.posZ - start.z) / 3));
		
		secondControl = new AMVector3(
				start.x + ((target.posX - start.x) / 3 * 2), 
				start.y + ((target.posY - start.y) / 3 * 2), 
				start.z + ((target.posZ - start.z) / 3 * 2));
		
		double offsetX = (rand.nextFloat() * offsetFactor) - halfOffsetFactor;
		double offsetZ = (rand.nextFloat() * offsetFactor) - halfOffsetFactor;
		
		AMVector3 offset = new AMVector3(offsetX, 0, offsetZ);
		
		firstControl = firstControl.add(offset);		
		secondControl = secondControl.add(offset);
		
		//addParticleAtPoint(start);
		//addParticleAtPoint(firstControl);
		//addParticleAtPoint(secondControl);
		//addParticleAtPoint(target);
		
		return this;
	}
	
	private void addParticleAtPoint(AMVector3 point){
		AMParticle p = (AMParticle) AMCore.instance.proxy.particleManager.spawn(particle.worldObj, "hr_smoke", point.x, point.y, point.z);
		if (p != null){
			p.setIgnoreMaxAge(false);
			p.setMaxAge(200);
			p.setParticleScale(1.5f);
			p.AddParticleController(new ParticleColorShift(p, 1, false));
		}
	}
	
	public ParticleArcToEntity specifyControlPoints(AMVector3 first, AMVector3 second){
		this.firstControl = first;
		this.secondControl = second;
		return this;
	}
	
	public ParticleArcToEntity SetSpeed(float speed){
		this.speed = speed;
		return this;
	}

	@Override
	public void doUpdate() {
		percent += speed;
		if (percent >= 1.0f){
			this.finish();
			return;
		}
		AMVector3 bez = MathUtilities.bezier(start, firstControl, secondControl, new AMVector3(target).add(new AMVector3(0.0, target.getEyeHeight(), 0.0)), percent);
		particle.setPosition(bez.x, bez.y, bez.z);
	}

	@Override
	public ParticleController clone() {
		return new ParticleArcToEntity(particle, priority, target, exclusive).SetSpeed(speed).specifyControlPoints(firstControl, secondControl);
	}

}
