package am2.buffs;

public class BuffEffectTrueSight extends BuffEffectShield{

	public BuffEffectTrueSight(int duration,
							   int amplifier){
		super(BuffList.trueSight.id, duration, amplifier);
	}

	@Override
	protected String spellBuffName(){
		return "True Sight";
	}

}
