package am2.texture;

import am2.api.spell.ISpellIconManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class SpellIconManager implements ISpellIconManager{
	private static HashMap<String, IIcon> icons;

	public static final SpellIconManager instance = new SpellIconManager();

	private SpellIconManager(){
		icons = new HashMap<String, IIcon>();
	}

	@Override
	public void registerIcon(String skillName, IIcon IIcon){
		icons.put(skillName, IIcon);
	}

	@Override
	public IIcon getIcon(String skillName){
		return icons.get(skillName);
	}
}
