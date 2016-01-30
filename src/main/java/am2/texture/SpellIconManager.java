package am2.texture;

import am2.api.spell.ISpellIconManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class SpellIconManager implements ISpellIconManager{
	private static HashMap<String, TextureAtlasSprite> icons;

	public static final SpellIconManager instance = new SpellIconManager();

	private SpellIconManager(){
		icons = new HashMap<String, TextureAtlasSprite>();
	}

	@Override
	public void registerIcon(String skillName, TextureAtlasSprite IIcon){
		icons.put(skillName, IIcon);
	}

	@Override
	public TextureAtlasSprite getIcon(String skillName){
		return icons.get(skillName);
	}
}
