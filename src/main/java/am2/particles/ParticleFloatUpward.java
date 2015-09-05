package am2.particles;


public class ParticleFloatUpward extends ParticleController{

	private final float jitter;
	private final float halfJitter;
	private final float speed;

	public ParticleFloatUpward(AMParticle particleEffect, float jitter, float speed, int priority,
							   boolean exclusive){
		super(particleEffect, priority, exclusive);
		this.jitter = jitter;
		this.halfJitter = jitter / 2;
		this.speed = speed;
	}

	@Override
	public void doUpdate(){
		if (particle.posY > 384){
			this.finish();
			return;
		}
		particle.moveEntity((rand.nextDouble() * jitter) - halfJitter, speed, (rand.nextDouble() * jitter) - halfJitter);
	}

	@Override
	public ParticleController clone(){
		return new ParticleFloatUpward(particle, jitter, speed, priority, exclusive);
	}

}
