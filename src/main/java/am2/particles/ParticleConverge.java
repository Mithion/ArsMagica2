package am2.particles;


public class ParticleConverge extends ParticleController{

	private final double originX;
	private final double originY;
	private final double originZ;

	private final double motionX;
	private final double motionY;
	private final double motionZ;

	public ParticleConverge(AMParticle particleEffect, double motionX, double motionY, double motionZ, int priority,
							boolean exclusive){
		super(particleEffect, priority, exclusive);

		particleEffect.setNoVelocityUpdates();

		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;

		this.originX = particleEffect.posX;
		this.originY = particleEffect.posY;
		this.originZ = particleEffect.posZ;
	}

	@Override
	public void doUpdate(){
		particle.prevPosX = particle.posX;
		particle.prevPosY = particle.posY;
		particle.prevPosZ = particle.posZ;
		int maxAge = particle.GetParticleMaxAge();
		if (maxAge == 0){
			maxAge = 1;
		}
		float ageFactor = (float)particle.GetParticleAge() / (float)maxAge;
		ageFactor = 1.0F - ageFactor;
		float verticalAgeFactor = 1.0F - ageFactor;
		verticalAgeFactor *= verticalAgeFactor;
		verticalAgeFactor *= verticalAgeFactor;
		particle.setPosition(
				this.originX + this.motionX * ageFactor,
				this.originY + this.motionY * ageFactor - verticalAgeFactor * 1.2F,
				this.originZ + this.motionZ * ageFactor);
	}

	@Override
	public ParticleController clone(){
		return new ParticleConverge(particle, motionX, motionY, motionZ, priority, exclusive);
	}

}
