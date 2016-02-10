package am2.api.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;

@Cancelable
public class ReconstructorRepairEvent extends Event{

	/**
	 * The item being repaired.
	 */
	public ItemStack item;

	/**
	 * Called when the arcane reconstructor ticks on repairing an item.
	 *
	 * @param item
	 */
	public ReconstructorRepairEvent(ItemStack item){
		this.item = item;
	}
}
