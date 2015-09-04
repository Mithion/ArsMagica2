package am2.api.power;

import net.minecraft.item.ItemStack;

public interface IObeliskFuelHelper{
	/**
	 * Registers a new fuel type for the obelisk to burn
	 *
	 * @param stack    The stack to use as a comparator (stackSize is ignored, just use 1)
	 * @param burnTime How long the obelisk should burn for when given one of these items
	 */
	public void registerFuelType(ItemStack stack, int burnTime);

	/**
	 * Retrieves the burn time for the specified stack.  Returns 0 if it is not a valid fuel.
	 */
	public int getFuelBurnTime(ItemStack stack);
}
