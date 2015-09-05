package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectScrambleSynapses extends BuffEffect{

	public BuffEffectScrambleSynapses(int duration, int amplifier){
		super(BuffList.scrambleSynapses.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Scramble Synapses";
	}

}
