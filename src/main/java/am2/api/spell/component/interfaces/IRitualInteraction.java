package am2.api.spell.component.interfaces;

import am2.api.blocks.MultiblockStructureDefinition;
import net.minecraft.item.ItemStack;

public interface IRitualInteraction<T extends ISkillTreeEntry>{
	/**
	 * Gets a MultiblockStructureDefinition outlining the ritual blocks needed and where they should be relative to a given location.
	 * This should be instantiated on mod load and cached somewhere, and NOT instantiated upon request.
	 */
	public MultiblockStructureDefinition getRitualShape();

	/**
	 * Gets a list of items that need to be present in {@link #getReagentSearchRadius() getReagentSearchRadius()} for the ritual to match.
	 * Items on the ground as well as items attached to Wizard's Chalk are included.
	 */
	public ItemStack[] getReagents();

	/**
	 * Gets the radius to search for reagents when checking the ritual.
	 */
	public int getReagentSearchRadius();
}
