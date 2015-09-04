package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectGravityWell extends BuffEffect{

	public BuffEffectGravityWell(int duration, int amplifier){
		super(BuffList.gravityWell.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	public void performEffect(EntityLivingBase entityliving){

	}

	@Override
	protected String spellBuffName(){
		return "Gravity Well";
	}

}
