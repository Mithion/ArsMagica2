package am2.particles;

public class ParticlePendulum extends ParticleController{

	private double angle = 0;
	private float amplitude = 0;
	private float speed = 0.1f;

	private double lastDeltaX;
	private double lastDeltaZ;
	private double lastDeltaY;

	private boolean stopOnCollide = true;

	public ParticlePendulum(AMParticle particleEffect, float amplitude, float speed, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);
		this.amplitude = amplitude;
		lastDeltaX = 0;
		lastDeltaZ = 0;
		generateNewAngle();
		this.speed = speed;
	}

	public ParticlePendulum setStopOnCollide(boolean stop){
		this.stopOnCollide = stop;
		return this;
	}

	private void generateNewAngle(){
		angle = Math.toRadians(rand.nextInt(360));
	}

	@Override
	public void doUpdate(){

		if (stopOnCollide && (particle.isCollidedVertically || particle.isCollidedHorizontally)){
			this.finish();
			return;
		}

		double deltaX = Math.sin(angle) * amplitude * Math.sin(particle.ticksExisted * speed);
		double deltaZ = Math.cos(angle) * amplitude * Math.sin(particle.ticksExisted * speed);

		double posX = deltaX - lastDeltaX;
		double posY = Math.sin(System.currentTimeMillis() / Float.MAX_VALUE);
		double posZ = deltaZ - lastDeltaZ;

		lastDeltaX = deltaX;
		lastDeltaZ = deltaZ;

		particle.moveEntity(posX, posY, posZ);
	}

	@Override
	public ParticleController clone(){
		return new ParticlePendulum(particle, amplitude, speed, priority, exclusive).setStopOnCollide(stopOnCollide);
	}

}
