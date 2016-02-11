package am2.api.spell;

import net.minecraft.util.ResourceLocation;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.enums.SkillPointTypes;
import am2.api.spell.enums.SkillTree;

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
	 * @param icon          The icon that the part will use
	 * @param prerequisites The prerequisite skills needed for the shape.
	 */
	public void RegisterPart(ISkillTreeEntry part, int x, int y, SkillTree tree, SkillPointTypes requiredPoint, ISkillTreeEntry... prerequisites);
	
	
	/**
	 * 
	 * @param name       Name of the tree
	 * @param background Background location
	 * @param icon       Icon Location
	 * @return 
	 */
	public SkillTree RegisterSkillTree (String name, ResourceLocation background, ResourceLocation icon);
}
