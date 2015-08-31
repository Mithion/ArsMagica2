package am2.particles;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import am2.AMCore;
import am2.api.spell.enums.Affinity;
import am2.texture.ResourceManager;

public class AMParticleIcons {

	public static final AMParticleIcons instance = new AMParticleIcons();

	private final HashMap<String, IIcon> icons;
	private final HashMap<String, IIcon> hiddenIcons;
	private static final Random rand = new Random();

	private AMParticleIcons(){
		icons = new HashMap<String, IIcon>();
		hiddenIcons = new HashMap<String, IIcon>();
	}

	public int numParticles(){
		return icons.size();
	}

	public void init(IIconRegister IIconRegister){
		icons.clear();

		loadAndInitIcon("arcane", "arcane", IIconRegister);
		loadAndInitIcon("clock", "clock", IIconRegister);
		loadAndInitIcon("ember", "ember", IIconRegister);
		loadAndInitIcon("explosion_2", "explosion_2", IIconRegister);
		loadAndInitIcon("ghost", "ghost", IIconRegister);
		loadAndInitIcon("heart", "heart", IIconRegister);
		loadAndInitIcon("leaf", "leaf", IIconRegister);
		loadAndInitIcon("lens_flare", "lens_flare", IIconRegister);
		loadAndInitIcon("lights", "lights", IIconRegister);
		loadAndInitIcon("plant", "plant", IIconRegister);
		loadAndInitIcon("pulse", "pulse", IIconRegister);
		loadAndInitIcon("rock", "rock", IIconRegister);
		loadAndInitIcon("rotating_rings", "rotating_rings", IIconRegister);
		loadAndInitIcon("smoke", "smoke", IIconRegister);
		loadAndInitIcon("sparkle", "sparkle", IIconRegister);
		loadAndInitIcon("sparkle2", "sparkle2", IIconRegister);
		loadAndInitIcon("water_ball", "water_ball", IIconRegister);
		loadAndInitIcon("wind", "wind", IIconRegister);

		loadAndInitIcon("air_hand", "air_hand", IIconRegister);
		loadAndInitIcon("arcane_hand", "arcane_hand", IIconRegister);
		loadAndInitIcon("earth_hand", "earth_hand", IIconRegister);
		loadAndInitIcon("ender_hand", "ender_hand", IIconRegister);
		loadAndInitIcon("fire_hand", "fire_hand", IIconRegister);
		loadAndInitIcon("ice_hand", "ice_hand", IIconRegister);
		loadAndInitIcon("life_hand", "life_hand", IIconRegister);
		loadAndInitIcon("lightning_hand", "lightning_hand", IIconRegister);
		loadAndInitIcon("nature_hand", "nature_hand", IIconRegister);
		loadAndInitIcon("none_hand", "none_hand", IIconRegister);
		loadAndInitIcon("water_hand", "water_hand", IIconRegister);

		loadAndInitIcon("beam", "beam", IIconRegister, false);
		loadAndInitIcon("beam1", "beam1", IIconRegister, false);
		loadAndInitIcon("beam2", "beam2", IIconRegister, false);

		for (int i = 1; i <= 28; ++i){
			loadAndInitIcon("Symbols" + i, "symbols/Symbols" + i, IIconRegister, false);
		}

		for (int i = 1; i <= 9; ++i){
			loadAndInitIcon("snowflake" + i, "snowflakes/snowflake" + i, IIconRegister, false);
		}

		icons.put("symbols", null);
		icons.put("snowflakes", null);

		AMParticle.particleTypes = icons.keySet().toArray(new String[icons.size() + 1]);
		AMParticle.particleTypes[AMParticle.particleTypes.length-1] = "radiant";

		AMCore.config.clientInit();
	}

	private void loadAndInitIcon(String name, String IIconPath, IIconRegister register){
		loadAndInitIcon(name, IIconPath, register, true);
	}

	private void loadAndInitIcon(String name, String IIconPath, IIconRegister register, boolean registerName){
		IIcon icon = ResourceManager.RegisterTexture("particles/" + IIconPath, register);
		if (registerName){
			if (icon != null){
				icons.put(name, icon);
			}
		}else{
			if (icon != null){
				hiddenIcons.put(name, icon);
			}
		}
	}

	public void RegisterIcon(String name, IIcon IIcon){
		icons.put(name, IIcon);
	}

	public IIcon getIconByName(String name){
		IIcon icon = null;
		if (name.equals("symbols")){
			icon = hiddenIcons.get("Symbols" + (rand.nextInt(28) + 1));
		}else if (name.equals("snowflakes")){
			icon = hiddenIcons.get("snowflake" + (rand.nextInt(9) + 1));
		}else{
			icon = icons.get(name);
		}
		if (icon == null) return icons.get("lights");
		return icon;
	}

	public IIcon getHiddenIconByName(String name){
		IIcon icon = hiddenIcons.get(name);
		if (icon == null) return icons.get("lights");
		return icon;
	}

	public String getParticleForAffinity(Affinity aff){
		switch(aff){
		case AIR:
			return "wind";
		case ARCANE:
			return "arcane";
		case EARTH:
			return "rock";
		case ENDER:
			return "pulse";
		case FIRE:
			return "explosion_2";
		case ICE:
			return "ember";
		case LIFE:
			return "sparkle";
		case LIGHTNING:
			return "lightning_hand";
		case NATURE:
			return "plant";
		case WATER:
			return "water_ball";
		case NONE:
		default:
			return "lens_flare";

		}
	}
	
	public String getSecondaryParticleForAffinity(Affinity aff){
		switch(aff){
		case AIR:
			return "air_hand";
		case ARCANE:
			return "symbols";
		case EARTH:
			return "earth_hand";
		case ENDER:
			return "ghost";
		case FIRE:
			return "smoke";
		case ICE:
			return "snowflakes";
		case LIFE:
			return "sparkle2";
		case LIGHTNING:
			return "lightning_hand";
		case NATURE:
			return "leaf";
		case WATER:
			return "water_hand";
		case NONE:
		default:
			return "lights";

		}
	}
}
