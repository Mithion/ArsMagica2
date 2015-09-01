package am2.api.spell;

import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISpellIconManager {
	/**
	 * Returns the IIcon registered to the specified spell name.  Returns the missing IIcon if one doesn't exist.
	 */
	public IIcon getIcon(String spellName);
	
	/**
	 * Registers an IIcon to be associated with a spell component
	 * @param spellName  The unlocalized name of the spell used when it was registered
	 * @param IIcon The IIcon of the spell
	 */
	public void registerIcon(String spellName, IIcon IIcon);
}
