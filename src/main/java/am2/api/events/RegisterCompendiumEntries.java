package am2.api.events;

import am2.api.ILoreHelper;
import cpw.mods.fml.common.eventhandler.Event;

public class RegisterCompendiumEntries extends Event {
	public final ILoreHelper loreHelper;
	
	public RegisterCompendiumEntries(ILoreHelper helper){
		this.loreHelper = helper;
	}
}
