package am2.api.events;

import am2.api.spell.ISpellIconManager;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RegisterSkillTreeIcons extends Event{
	public final ISpellIconManager manager;

	public RegisterSkillTreeIcons(ISpellIconManager manager){
		this.manager = manager;
	}
}
