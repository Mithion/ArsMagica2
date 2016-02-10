package am2.api.potion;

import net.minecraft.util.ResourceLocation;

public interface IBuffHelper{
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
	 * Adds a potion to the dispel blacklist, essentially making it un-dispellable.
	 *
	 * @param potionName The id to cause dispel to ignore.
	 * @return True on success, otherwise false.
	 */
	void addDispelExclusion(ResourceLocation potionName);
}
