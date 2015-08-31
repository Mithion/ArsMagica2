package am2.api.potion;

public interface IBuffHelper {
	/*
	 * Potion Names:
	
	waterBreathing
	flight
	slowfall
	haste
	trueSight
	regeneration
	magicShield
	charmed
	frostSlowed
	temporalAnchor
	manaRegen
	entangled
	wateryGrave
	spellReflect
	silence
	swiftSwim
	agility
	leap
	gravityWell
	astralDistortion
	levitation
	clarity
	illumination
	manaBoost
	manaShield
	fury
	scrambleSynapses
	shrink
	
	 */
	
	/**
	 * Returns the ID of an AM buff, or -1 if not found.
	 * @param name The name of the potion to find.  Case sensitive.
	 */
	public int getPotionID(String name);
	
	/**
	 * Adds a potion to the dispel blacklist, essentially making it un-dispellable.
	 * @param id The id to cause dispel to ignore.
	 * @return True on success, otherwise false.
	 */
	public void addDispelExclusion(int id);
}
