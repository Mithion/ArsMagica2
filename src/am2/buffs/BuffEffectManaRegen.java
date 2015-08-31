package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectManaRegen extends BuffEffect {

	public BuffEffectManaRegen(int duration, int amplifier) {
		super(BuffList.manaRegen.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving) {
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving) {
	}

	@Override
	protected String spellBuffName() {
		return "Mana Regen";
	}

}
