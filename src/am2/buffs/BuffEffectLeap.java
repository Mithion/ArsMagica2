package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectLeap extends BuffEffect{

	public BuffEffectLeap(int duration, int amplifier) {
		super(BuffList.leap.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving) {

	}

	@Override
	public void stopEffect(EntityLivingBase entityliving) {

	}

	@Override
	protected String spellBuffName() {
		return "Leap";
	}

}
