package am2.api.items.armor;

import cpw.mods.fml.common.eventhandler.Event;


public class ArmorTextureEvent extends Event{
	public final int slot;
	public final int renderIndex;

	public String texture;

	public ArmorTextureEvent(int slot, int renderIndex){
		this.slot = slot;
		this.renderIndex = renderIndex;
	}
}
