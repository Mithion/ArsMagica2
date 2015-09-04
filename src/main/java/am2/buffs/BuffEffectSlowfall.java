package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectSlowfall extends BuffEffect{

	public BuffEffectSlowfall(int duration, int amplifier){
		super(BuffList.slowfall.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Slowfall";
	}

}
