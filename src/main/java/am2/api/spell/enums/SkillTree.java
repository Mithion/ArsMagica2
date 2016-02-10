package am2.api.spell.enums;

import net.minecraft.util.ResourceLocation;

public class SkillTree{
	public static final SkillTree None = null;
	public static final SkillTree Offense = new SkillTree(0, "offense", new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Offense.png"), new ResourceLocation("arsmagica2", "textures/guis/occulus/offense.png"));
	public static final SkillTree Defense = new SkillTree(1, "defense", new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Defense.png"), new ResourceLocation("arsmagica2", "textures/guis/occulus/defense.png"));;
	public static final SkillTree Utility = new SkillTree(2, "utility", new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Utility.png"), new ResourceLocation("arsmagica2", "textures/guis/occulus/utility.png"));;
	public static final SkillTree Talents = new SkillTree(3, "talents", new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Talents.png"), new ResourceLocation("arsmagica2", "textures/guis/occulus/talents.png"));;
	public static final SkillTree Familiar = null;//new SkillTree(0, "offense", new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Offense.png"), new ResourceLocation("arsmagica2", "textures/guis/occulus/offense.png"));;
	public static final SkillTree Affinity = new SkillTree(4, "affinity", new ResourceLocation("arsmagica2", "textures/guis/SkillTree_Talents.png"), new ResourceLocation("arsmagica2", "textures/guis/occulus/affinity.png"));;
	
	private int id;
	private String name;
	private ResourceLocation background;
	private ResourceLocation icon;
	
	public SkillTree (int id, String name, ResourceLocation background, ResourceLocation icon) {
		this.name = name;
		this.background = background;
		this.icon = icon;
	}
	
	public String getName() {
		return name;
	}
	
	public ResourceLocation getBackground() {
		return background;
	}
	
	public int getId() {
		return id;
	}
	
	public ResourceLocation getIcon() {
		return icon;
	}
}
