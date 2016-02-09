package am2.api.spell;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISpellIconManager{
	/**
	 * Returns the IIcon registered to the specified spell name.  Returns the missing IIcon if one doesn't exist.
	 */
	public TextureAtlasSprite getIcon(String spellName);

	/**
	 * Registers an IIcon to be associated with a spell component
	 *
	 * @param spellName The unlocalized name of the spell used when it was registered
	 * @param IIcon     The IIcon of the spell
	 */
	public void registerIcon(String spellName, TextureAtlasSprite IIcon);
}
