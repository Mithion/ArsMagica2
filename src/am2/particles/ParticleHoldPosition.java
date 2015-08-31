package am2.particles;

public class ParticleHoldPosition extends ParticleController {

	private int ticksRun;
	private int delay;

	public ParticleHoldPosition(AMParticle particleEffect, int delay, int priority, boolean exclusive) {
		super(particleEffect, priority, exclusive);
		ticksRun = 0;
		this.delay = delay;
	}

	@Override
	public void doUpdate() {
		ticksRun++;
		if (ticksRun == delay || delay <= 0){
			this.finish();
		}
	}

	@Override
	public ParticleController clone() {
		return new ParticleHoldPosition(this.particle, delay, this.priority, this.exclusive);
	}

}
