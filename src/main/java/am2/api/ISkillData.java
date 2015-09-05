package am2.api;

import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.enums.LearnStates;
import am2.api.spell.enums.SkillPointTypes;
import net.minecraft.entity.player.EntityPlayer;

public interface ISkillData{
	/**
	 * Checks if the specified skill tree entry is known
	 */
	boolean isEntryKnown(SkillTreeEntry entry);

	/**
	 * Returns the number of skill points of the given type that this skill data has
	 */
	int getSpellPoints(SkillPointTypes type);

	/**
	 * Sets the number of skill points of the given type that this skill data has
	 */
	void setSpellPoints(int spellPoints, SkillPointTypes type);

	/**
	 * Sets the number of skill points for all types that this skill data has
	 */
	void setSpellPoints(int spellPoints_blue, int spellPoints_green, int spellPoints_red);

	/**
	 * Increments the skill points of the specified type.
	 */
	void incrementSpellPoints(SkillPointTypes type);

	/**
	 * Decrements the skill points of the specified type.  Cannot go below 0.
	 */
	void decrementSpellPoints(SkillPointTypes type);

	/**
	 * Learns the specified entry.  This will attempt to decrement skill points but is not bounded by it - make sure you check {@link #getLearnState(SkillTreeEntry, EntityPlayer)} first!
	 *
	 * @param entry
	 */
	void learn(ISkillTreeEntry entry);

	/**
	 * Returns an array of shifted IDs representing the known shapes.
	 */
	Integer[] getKnownShapes();

	/**
	 * Returns an array of shifted IDs representing the known components.
	 */
	Integer[] getKnownComponents();

	/**
	 * Returns an array of shifted IDs representing the known modifiers.
	 */
	Integer[] getKnownModifiers();

	/**
	 * Returns an array of shifted IDs representing the known talents.
	 */
	Integer[] getKnownTalents();

	/**
	 * Forces changes to sync to appropriate clients.  Chances are you won't have to call this, as the sync will happen at regular intervals if there are actually changes to sync.  This is for when you need the changes clientside immediately.
	 */
	void forceSync();

	/**
	 * Clears all spell knowledge and skill points.  Does not do any refunding -- this is NOT a respec!
	 */
	void clearAllKnowledge();

	/**
	 * Gets the learn state for the specified skill.
	 */
	LearnStates getLearnState(SkillTreeEntry entry, EntityPlayer player);
}
