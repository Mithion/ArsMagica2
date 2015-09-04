package am2.api.items.armor;

import net.minecraft.item.ItemStack;


public interface IImbuementRegistry{
	/**
	 * Registers a new imbuement instance into the system
	 */
	void registerImbuement(IArmorImbuement imbuementInstance);

	/**
	 * Locates the specified imbuement registered to the passed-in ID.
	 * Returns null if not found.
	 */
	IArmorImbuement getImbuementByID(String ID);

	/**
	 * Returns all imbuements registered into the specified tier for the given armor type (slot).
	 */
	IArmorImbuement[] getImbuementsForTier(ImbuementTiers tier, int armorType);

	/**
	 * Is the given imbuement instance present on the passed-in item stack?
	 */
	boolean isImbuementPresent(ItemStack stack, IArmorImbuement imbuement);

	/**
	 * Is the given imbuement ID present on the passed-in item stack?
	 */
	boolean isImbuementPresent(ItemStack stack, String id);
}
