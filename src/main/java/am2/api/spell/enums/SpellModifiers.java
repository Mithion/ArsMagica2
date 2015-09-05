package am2.api.spell.enums;

public enum SpellModifiers{
	SPEED(1.0D),
	GRAVITY(0),
	BOUNCE(0),
	DAMAGE(4.0D),
	HEALING(1.0D),
	VELOCITY_ADDED(0.0D),
	RADIUS(1.0D),
	DURATION(1.0D),
	PROCS(1),
	RANGE(8.0D),
	TARGET_NONSOLID_BLOCKS(0),
	PIERCING(2),
	COLOR(0xFFFFFF),
	MINING_POWER(1),
	FORTUNE_LEVEL(1),
	SILKTOUCH_LEVEL(1),
	DISMEMBERING_LEVEL(1),
	BUFF_POWER(1);

	public double defaultValue = 0D;
	public int defaultValueInt = 0;

	private SpellModifiers(double defaultValue){
		this.defaultValue = defaultValue;
		this.defaultValueInt = (int)defaultValue;
	}
}
