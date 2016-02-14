package am2.api.blocks;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface IKeystoneLockable<T extends TileEntity & IInventory>{
	/**
	 * Return a three element array of the three runes in the inventory that make up the current keystone combination.
	 * Elements can be NULL if there is no rune present.
	 */
	public ItemStack[] getRunesInKey();

	/**
	 * Does the keystone need to be the currently held item?  Trumps {@link #keystoneMustBeInActionBar()}.
	 */
	public boolean keystoneMustBeHeld();

	/**
	 * Does the keystone need to be in the action bar?  Or can it be anywhere in the inventory?
	 */
	public boolean keystoneMustBeInActionBar();
}
