package am2.buffs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import am2.particles.AMParticle;

public class BuffEffectShield extends BuffEffect {

	private List<AMParticle> particles;
	private int maxParticles = 50;

	public BuffEffectShield(int buffID, int duration,
			int amplifier) {
		super(buffID, duration, amplifier);
		particles = new ArrayList<AMParticle>();
	}

	public void AddParticle(AMParticle particle){
		if (particles.size() < maxParticles){
			particles.add(particle);
		}
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving) {
	}
	@Override
	public void stopEffect(EntityLivingBase entityliving) {
		BuffList.buffEnding(this.getPotionID());
	}
	@Override
	protected String spellBuffName() {
		return "Magic Shield";
	}
}
