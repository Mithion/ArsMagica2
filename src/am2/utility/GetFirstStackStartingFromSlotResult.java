package am2.utility;

import net.minecraft.item.ItemStack;

public class GetFirstStackStartingFromSlotResult{
	public final int slot;
	public final ItemStack stack;
	
	public GetFirstStackStartingFromSlotResult(int slot, ItemStack stack){
		this.slot = slot;
		this.stack = stack;
	}
}