package am2.api.spell;

import am2.api.spell.component.interfaces.ISkillTreeEntry;

public interface ISpellPartManager{
	/**
	 * Registers the skill tree entry into the skill manager.
	 *
	 * @param component The skill tree entry to register
	 * @param name      The unlocalized name you want to register it to.  Local translations are am2.spell.unlocalized_name, so register this into the language registry for localization.
	 * @return The shifted numeric ID that is used, this should be stored somewhere
	 */
	public int registerSkillTreeEntry(ISkillTreeEntry component, String name);

	/**
	 * @param ID The shifted ID of the skill.
	 * @return The skill tree entry or null if not found.
	 */
	public ISkillTreeEntry getSkill(int ID);

	/**
	 * @param name The unlocalized name of the skill
	 * @return The skill tree entry or null if not found.
	 */
	public ISkillTreeEntry getSkill(String name);
}
