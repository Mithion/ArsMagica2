package am2.buffs;

public class BuffEffectMagicShield extends BuffEffectShield{

	public BuffEffectMagicShield(int duration,
								 int amplifier){
		super(BuffList.magicShield.id, duration, amplifier);
	}

	@Override
	protected String spellBuffName(){
		return "Magic Shield";
	}

}
