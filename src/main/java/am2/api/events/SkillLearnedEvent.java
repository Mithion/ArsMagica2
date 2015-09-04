package am2.api.events;

import am2.api.spell.component.interfaces.ISkillTreeEntry;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Raised when a player learns a skill at the Occulus
 *
 * @author Mithion
 */
public class SkillLearnedEvent extends Event{
	public final EntityPlayer player;
	public final ISkillTreeEntry skill;

	public SkillLearnedEvent(EntityPlayer player, ISkillTreeEntry entry){
		this.player = player;
		this.skill = entry;
	}
}
