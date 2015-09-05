package am2.api;

import net.minecraft.item.ItemStack;

public interface IAMRecipeManager{
	/**
	 * Adds a recipe to the essence refiner.
	 *
	 * @param output     The output created
	 * @param components The components required.  Must be a length of 5.
	 */
	public void addRefinerRecipe(ItemStack output, ItemStack[] components);
}
