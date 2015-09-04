package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectBurnoutReduction extends BuffEffect{
	public BuffEffectBurnoutReduction(int duration, int amplifier){
		super(BuffList.burnoutReduction.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Burnout Reduction";
	}
}
