package am2.particles;


public class ParticleMoveOnHeading extends ParticleController{

	private double yaw;
	private double pitch;
	private double speed;

	public ParticleMoveOnHeading(AMParticle particleEffect, double yaw, double pitch, double speed, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);

		this.yaw = Math.toRadians(yaw);
		this.pitch = Math.toRadians(pitch);
		this.speed = speed;
	}

	@Override
	public void doUpdate(){
		double movementX = Math.cos(yaw) * speed;
		double movementZ = Math.sin(yaw) * speed;
		double movementY = -Math.sin(pitch) * speed;

		particle.moveEntity(movementX, movementY, movementZ);
	}

	@Override
	public ParticleController clone(){
		return new ParticleMoveOnHeading(particle, yaw, pitch, speed, priority, exclusive);
	}
}
