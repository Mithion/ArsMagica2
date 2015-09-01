package am2.buffs;

import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.playerextensions.ExtendedProperties;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class BuffEffectWaterBreathing extends BuffEffect {
	
	int breath;
	
	public BuffEffectWaterBreathing(int duration, int amplifier) {
		super(BuffList.waterBreathing.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving) {
		breath = entityliving.getAir();
	}

	@Override
	public void performEffect(EntityLivingBase entityliving) {
		if (entityliving.isInWater()){
			entityliving.setAir(breath);
		}else{
			breath = entityliving.getAir();
		}
	}
	
	@Override
	public void stopEffect(EntityLivingBase entityliving) {
		
	}

	@Override
	public String spellBuffName() {
		return "Water Breathing";
	}

}
