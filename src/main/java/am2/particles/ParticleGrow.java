package am2.particles;

public class ParticleGrow extends ParticleController{

	private float growRate = 0.01f;

	public ParticleGrow(AMParticle particleEffect, float growRate, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);
		this.growRate = growRate;
	}

	@Override
	public void doUpdate(){
		float newScale = particle.getParticleScaleX() + growRate;
		if (newScale <= 0) newScale = 0.0001f;
		particle.setParticleScale(newScale);
	}

	@Override
	public ParticleController clone(){
		return new ParticleGrow(particle, growRate, priority, exclusive);
	}

}
