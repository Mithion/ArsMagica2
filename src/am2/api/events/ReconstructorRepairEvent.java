package am2.api.events;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

@Cancelable
public class ReconstructorRepairEvent extends Event{
	
	/**
	 * The item being repaired.
	 */
	public ItemStack item;
	
	/**
	 * Called when the arcane reconstructor ticks on repairing an item.
	 * @param item
	 */
	public ReconstructorRepairEvent(ItemStack item){
		this.item = item;
	}
}
