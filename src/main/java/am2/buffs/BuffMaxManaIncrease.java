package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffMaxManaIncrease extends BuffEffect{

	public BuffMaxManaIncrease(int duration, int amplifier) {
		super(BuffList.manaBoost.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving) {
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving) {
	}

	@Override
	protected String spellBuffName() {
		return "Mana Boost";
	}

}
