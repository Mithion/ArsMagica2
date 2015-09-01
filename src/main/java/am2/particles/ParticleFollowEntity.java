package am2.particles;

import net.minecraft.entity.Entity;

public class ParticleFollowEntity extends ParticleController{

	private Entity followTarget;
	
	public ParticleFollowEntity(AMParticle particleEffect, int priority, Entity followTarget, boolean exclusive) {
		super(particleEffect, priority, exclusive);

		this.followTarget = followTarget;
	}

	@Override
	public void doUpdate() {
		this.particle.lastTickPosX = this.particle.posX;
		this.particle.lastTickPosY = this.particle.posY;
		this.particle.lastTickPosZ = this.particle.posZ;
		
		this.particle.posX = followTarget.posX;
		this.particle.posY = followTarget.posY;
		this.particle.posZ = followTarget.posZ;
	}

	@Override
	public ParticleController clone() {
		return new ParticleFollowEntity(particle, priority, followTarget, exclusive);
	}

}
