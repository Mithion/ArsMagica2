package am2.buffs;

import net.minecraft.entity.EntityLivingBase;

public class BuffEffectManaShield extends BuffEffect{

	public BuffEffectManaShield(int duration, int amplifier){
		super(BuffList.manaShield.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	protected String spellBuffName(){
		return "Mana Shield";
	}

}
