package am2.particles;
import java.util.Random;


import net.minecraft.entity.Entity;

public final class ParticleFadeOut extends ParticleController {

	private float fadeSpeed;
	
	public ParticleFadeOut(AMParticle particleEffect,int priority, boolean exclusive) {
		super(particleEffect, priority, exclusive);
		fadeSpeed = 0.01f;
	}
	
	public ParticleFadeOut setFadeSpeed(float fadeSpeed){
		this.fadeSpeed = fadeSpeed;		
		return this;
	}
	
	@Override
	public void doUpdate() {
		float alpha = particle.GetParticleAlpha();
		if (alpha <= 0){
			this.finish();
			return;
		}
		particle.SetParticleAlpha(alpha - fadeSpeed);		
	}

	@Override
	public ParticleController clone() {
		return new ParticleFadeOut(particle, priority, exclusive).setFadeSpeed(fadeSpeed);
	}
}
