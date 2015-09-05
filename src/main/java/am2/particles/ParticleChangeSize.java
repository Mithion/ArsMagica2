package am2.particles;

public class ParticleChangeSize extends ParticleController{

	private float start;
	private float end;
	private int time;

	public ParticleChangeSize(AMParticle star, float start, float end, int time, int priority, boolean exclusive){
		super(star, priority, exclusive);
		this.start = start;
		this.end = end;
		this.time = time;
	}

	@Override
	public void doUpdate(){

		if (particle.ticksExisted > time){
			this.finish();
			return;
		}

		float pct = (float)particle.ticksExisted / (float)time;
		float scale = start + pct * (end - start);

		particle.setParticleScale(scale);
	}

	@Override
	public ParticleController clone(){
		return new ParticleChangeSize(particle, start, end, time, time, exclusive);
	}

}
