package am2.texture;

import java.util.HashMap;

import net.minecraft.util.IIcon;
import am2.api.spell.ISpellIconManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpellIconManager implements ISpellIconManager {	
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
