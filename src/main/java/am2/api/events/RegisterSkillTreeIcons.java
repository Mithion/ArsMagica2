package am2.api.events;

import cpw.mods.fml.common.eventhandler.Event;
import am2.api.spell.ISpellIconManager;

public class RegisterSkillTreeIcons extends Event{
	public final ISpellIconManager manager;

	public RegisterSkillTreeIcons(ISpellIconManager manager){
		this.manager = manager;
	}
}
