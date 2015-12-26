package am2.particles;

import net.minecraft.world.World;

import java.util.Random;

public abstract class ParticleController{

	protected AMParticle particle;

	protected int priority;
	protected boolean exclusive;
	private boolean finished;
	private boolean killParticleOnFinish;
	protected boolean firstTick = true;

	public static String[] AuraControllerOptions = new String[]{
			"fade",
			"float",
			"sink",
			"orbit",
			"arc",
			"flee",
			"forward",
			"pendulum",
			"grow"
	};

	public ParticleController(AMParticle particleEffect, int priority, boolean exclusive){
		this.particle = particleEffect;
		this.priority = priority;
		this.exclusive = exclusive;
		this.killParticleOnFinish = false;
	}

	protected ParticleController targetNewParticle(AMParticle particle){
		if (this.particle != null)
			this.particle.RemoveParticleController(this);
		particle.AddParticleController(this);
		this.particle = particle;
		return this;
	}

	public ParticleController setKillParticleOnFinish(boolean kill){
		this.killParticleOnFinish = kill;
		return this;
	}

	public boolean getKillParticleOnFinish(){
		return this.killParticleOnFinish;
	}

	public abstract void doUpdate();

	@Override
	public abstract ParticleController clone();

	public void onUpdate(World world){
		if (!world.isRemote){
			//spawned a particle on a server world...
			if (particle != null) particle.setDead();
			return;
		}
		if (particle != null){
			doUpdate();
		}
		if (firstTick){
			firstTick = false;
		}
	}

	public int getPriority(){
		return priority;
	}

	protected void finish(){
		this.finished = true;
		if (killParticleOnFinish && particle != null){
			particle.setDead();
		}
	}

	public boolean getExclusive(){
		return exclusive;
	}

	public boolean getFinished(){
		return finished;
	}
}
