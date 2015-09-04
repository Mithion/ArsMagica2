package am2.buffs;

import am2.AMCore;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class BuffEffectRegeneration extends BuffEffect{

	private int tickCounter;

	public BuffEffectRegeneration(int duration, int amplifier){
		super(BuffList.regeneration.id, duration, amplifier);
		tickCounter = 0;
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){

	}

	public boolean onUpdate(EntityLivingBase entityliving){

		World world = entityliving.worldObj;
		double ticks = 80 / Math.pow(2, this.getAmplifier());

		if (getDuration() != 0 && (getDuration() % ticks) == 0){
			if (!world.isRemote){
				entityliving.heal(1);
			}else{
				AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(world, "hr_sparkles_1", entityliving.posX + rand.nextDouble() * entityliving.height - (entityliving.height / 2), entityliving.posY + rand.nextDouble() * entityliving.height - (entityliving.height / 2), entityliving.posZ + rand.nextDouble() * entityliving.height - (entityliving.height / 2));
				if (effect != null){
					effect.setMaxAge(15 + rand.nextInt(10));
					effect.setIgnoreMaxAge(false);
					effect.AddParticleController(new ParticleFadeOut(effect, 1, true));
					effect.setRGBColorF(0.15f, 0.92f, 0.37f);
				}
			}
		}

		return super.onUpdate(entityliving);
	}

	@Override
	protected String spellBuffName(){
		return null;
	}

}
