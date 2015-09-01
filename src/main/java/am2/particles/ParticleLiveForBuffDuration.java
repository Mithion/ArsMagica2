package am2.particles;

import net.minecraft.entity.EntityLiving;

public class ParticleLiveForBuffDuration extends ParticleController {

	private int updateTicks;
	private int buffID;
	private EntityLiving entity;
	private int ticksWithoutBuff;
	
	public ParticleLiveForBuffDuration(AMParticle particleEffect, EntityLiving entity, int buffID, int priority, boolean exclusive) {
		super(particleEffect, priority, exclusive);
		this.entity = entity;
		this.buffID = buffID;
		ticksWithoutBuff = 0;
	}

	@Override
	public void doUpdate() {
		updateTicks++;
		if (updateTicks % 10 == 0){
			if (!entity.isPotionActive(buffID)){
				ticksWithoutBuff++;
				if (ticksWithoutBuff > 3)
					particle.setDead();
			}else{
				ticksWithoutBuff = 0;
			}
			updateTicks = 0;
		}
	}

	@Override
	public ParticleController clone() {
		return new ParticleLiveForBuffDuration(particle, entity, buffID, priority, exclusive);
	}

}
