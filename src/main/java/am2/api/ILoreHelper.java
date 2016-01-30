package am2.api;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ILoreHelper{

	/**
	 * Adds an entry to the compendium.
	 *
	 * @param entryItem    The item that the compendium entry describes.  Can be an item, a block, an entity, or a spell part (incl shape/component/or modifier).
	 * @param entryKey     The key of the entry in the compendium.  Must be unique. This depends on what you are describing, and can have conditions.  See the following: <pre>
	 *                     For meta specific entries, you can add metadata using the format "key@meta".
	 *                     For non-AM items/blocks, must be prefixed with mod ID, "am2apidemo:forceRing" for example.
	 *                     For non-AM mobs, must be prefixed with mod ID dot mobID, for example "arsmagica2.EntityDryad".
	 *                     For Shapes/Components/Modifiers, they must be registered.  Just give their registered name, no prefix is necessary.  For example, "Explosion" (from am2apidemo).
	 *                     <p/>
	 *                     If you get this wrong, the entry will be blank.
	 *                     </pre>
	 * @param entryName    The human-readable name of the entry.
	 * @param entryDesc    The entry's text.  You can use Minecraft color codes by prefixing the color number with the '#' key (for example, #9This text would be blue #0 and now back to black)
	 * @param parent       The entry's parent entry.  You can set this to the key of the parent entry to make this a sub item.  Set to null for none.
	 * @param allowReplace If an entry already exists with the specified key, can it be replaced?
	 * @param relatedKeys  Related entries are shown at the bottom, and are clickable.
	 */
	public void AddCompenidumEntry(Object entryItem, String entryKey, String entryName, String entryDesc, String parent, boolean allowReplace, String... relatedKeys);
}
