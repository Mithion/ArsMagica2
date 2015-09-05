package am2.api.spell;

import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.enums.SkillPointTypes;
import am2.api.spell.enums.SkillTrees;

public interface ISkillTreeManager{
	/**
	 * Registers a spell part into the skill tree (where it can be unlocked).
	 * Pass a null prerequisite for no prerequisite.
	 * Throws an exception if the prerequisite passed in is not a registered entry in the skill tree, or if it is in a different tree.
	 *
	 * @param shape         The shape to register
	 * @param x             The x-coordinate in the skill tree to display the shape IIcon
	 * @param y             The y-coordinate in the skill tree to display the shape IIcon
	 * @param tree          The skill tree to register the shape into
	 * @param prerequisites The prerequisite skills needed for the shape.
	 */
	public void RegisterPart(ISkillTreeEntry part, int x, int y, SkillTrees tree, SkillPointTypes requiredPoint, ISkillTreeEntry... prerequisites);
}
