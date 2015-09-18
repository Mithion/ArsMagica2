package am2.api.items;

import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public interface IKeystoneHelper{
	/**
	 * Gets all the keys from all keystones in the entity's inventory
	 */
	public ArrayList<Long> GetKeysInInvenory(EntityLivingBase ent);

	/**
	 * Returns the key combination made by the specified runes
	 *
	 * @param runes An array of runes no larger than three elements (anything above will be ignored)
	 * @return
	 */
	public long getKeyFromRunes(ItemStack[] runes);

	/**
	 * Returns true or false based on whether the passed in player meets the keystone requirements needed to access the specified container
	 *
	 * @param inventory The inventory the player is trying to access
	 * @param player    The player attempting the access check
	 * @param accessMode The operation which you're trying to carry out - this determines the error message if you lack the requisite keystone.
	 * @return True if the conditions are met, otherwise false
	 */
	public boolean canPlayerAccess(IKeystoneLockable inventory, EntityPlayer player, KeystoneAccessType accessMode);
}
