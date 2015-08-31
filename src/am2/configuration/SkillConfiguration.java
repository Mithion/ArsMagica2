package am2.configuration;

import java.io.File;
import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;


public class SkillConfiguration extends Configuration {
	private final HashMap<String, Boolean> cache;

	public SkillConfiguration(File file){
		super(file);
		cache = new HashMap<String, Boolean>();
		load();
		addCustomCategoryComment(CATEGORY_GENERAL, "Set any of these to false to disable the skill in-game.");
	}

	public boolean isSkillEnabled(String identifier){
		if (cache.containsKey(identifier))
			return cache.get(identifier);
		boolean value = get(CATEGORY_GENERAL, identifier, true).getBoolean(true);
		cache.put(identifier, value);
		return value;
	}

	public void init() {

	}
}
