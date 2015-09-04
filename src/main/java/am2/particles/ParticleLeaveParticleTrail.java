package am2.particles;

import am2.AMCore;

import java.util.ArrayList;


public class ParticleLeaveParticleTrail extends ParticleController{

	private int ticksBetweenSpawns;
	private int updateTicks;
	private final String particleName;
	private final boolean ignoreMaxAge;
	private final int maxAge;
	private float r, g, b;
	private final ArrayList<ParticleController> controllers;
	private float offsetX, offsetY, offsetZ;
	private boolean childAffectedByGravity = false;

	public ParticleLeaveParticleTrail(AMParticle particleEffect, String particleName, boolean ignoreMaxAge, int maxAge, int priority, boolean exclusive){
		super(particleEffect, priority, exclusive);
		controllers = new ArrayList<ParticleController>();
		ticksBetweenSpawns = 2;
		this.particleName = particleName;
		this.ignoreMaxAge = ignoreMaxAge;
		this.maxAge = maxAge;
		updateTicks = 0;
		r = b = g = 1;
	}

	public ParticleLeaveParticleTrail setChildAffectedByGravity(){
		this.childAffectedByGravity = true;
		return this;
	}

	public ParticleLeaveParticleTrail setTicksBetweenSpawns(int ticks){
		ticksBetweenSpawns = ticks;
		return this;
	}

	public ParticleLeaveParticleTrail addControllerToParticleList(ParticleController controller){
		controllers.add(controller);
		return this;
	}

	public ParticleLeaveParticleTrail setParticleRGB_F(float red, float green, float blue){
		this.r = red;
		this.g = green;
		this.b = blue;
		return this;
	}

	public ParticleLeaveParticleTrail setParticleRGB_I(int color){
		this.r = ((color >> 16) & 0xFF) / 255.0f;
		this.g = ((color >> 8) & 0xFF) / 255.0f;
		this.b = (color & 0xFF) / 255.0f;
		return this;
	}

	public ParticleLeaveParticleTrail addRandomOffset(float x, float y, float z){
		offsetX = x;
		offsetY = y;
		offsetZ = z;

		return this;
	}

	@Override
	public void doUpdate(){
		updateTicks++;
		if (updateTicks == ticksBetweenSpawns){
			updateTicks = 0;
			AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(particle.worldObj, particleName, particle.posX, particle.posY, particle.posZ);
			if (effect != null){
				effect.setMaxAge(this.maxAge);
				effect.setIgnoreMaxAge(this.ignoreMaxAge);
				effect.setRGBColorF(r, g, b);
				effect.addRandomOffset(offsetX, offsetY, offsetZ);
				if (this.childAffectedByGravity)
					effect.setAffectedByGravity();
				for (ParticleController pmc : this.controllers){
					effect.AddParticleController(pmc.clone().setKillParticleOnFinish(pmc.getKillParticleOnFinish()).targetNewParticle(effect));
				}
			}
		}
	}

	@Override
	public ParticleController clone(){
		ParticleLeaveParticleTrail clone = new ParticleLeaveParticleTrail(particle, particleName, ignoreMaxAge, maxAge, priority, exclusive)
				.setParticleRGB_F(r, g, b).setTicksBetweenSpawns(this.ticksBetweenSpawns).addRandomOffset(offsetX, offsetY, offsetZ);

		for (ParticleController pmc : this.controllers){
			clone.addControllerToParticleList(pmc);
		}

		if (this.childAffectedByGravity)
			clone.setChildAffectedByGravity();

		return clone;
	}

}
