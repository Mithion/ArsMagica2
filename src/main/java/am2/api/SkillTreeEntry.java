package am2.api;

import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.enums.SkillTree;

public class SkillTreeEntry{
	public final int x;
	public final int y;
	public final SkillTree tree;
	public final SkillTreeEntry[] prerequisites;
	public final ISkillTreeEntry registeredItem;
	public final int tier;
	public boolean enabled;

	public SkillTreeEntry(int x, int y, SkillTree tree, SkillTreeEntry[] prerequisites, ISkillTreeEntry registeredItem, boolean enabled){
		this.x = x;
		this.y = y;
		this.tree = tree;
		this.prerequisites = prerequisites;
		this.registeredItem = registeredItem;
		this.enabled = enabled;

		int highestTier = 0;
		for (SkillTreeEntry entry : prerequisites){
			if (entry.tier >= highestTier){
				highestTier = entry.tier + 1;
			}
		}

		this.tier = highestTier;
	}
}
