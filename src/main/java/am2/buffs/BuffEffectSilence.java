package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectSilence extends BuffEffect{

	public BuffEffectSilence(int duration, int amplifier){
		super(BuffList.silence.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Silence";
	}

}
