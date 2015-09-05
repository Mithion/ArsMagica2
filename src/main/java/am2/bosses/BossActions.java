package am2.bosses;

public enum BossActions{
	IDLE(-1),
	STRIKE(15),
	SMASH(20),
	THROWING_SICKLE(15),
	SHIELD_BASH(15),
	SPINNING(160),
	CASTING(-1),
	THROWING_ROCK(30),
	LONG_CASTING(-1),
	LAUNCHING(20),
	CLONE(30),
	CHARGE(-1);

	private final int maxActionTime;

	private BossActions(int maxTime){
		maxActionTime = maxTime;
	}

	public int getMaxActionTime(){
		return maxActionTime;
	}
}
