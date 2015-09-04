package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectShrink extends BuffEffect{

	public BuffEffectShrink(int duration, int amplifier){
		super(BuffList.shrink.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){

	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Shrunken";
	}

}
