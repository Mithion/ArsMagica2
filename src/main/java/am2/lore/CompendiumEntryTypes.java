package am2.lore;

import am2.guis.AMGuiIcons;
import am2.items.ItemsCommonProxy;
import am2.texture.SpellIconManager;

public class CompendiumEntryTypes{

	public final CompendiumEntryType GUIDE = new CompendiumEntryType("guides", "guide", "Guides", 0, CompendiumEntryGuide.class);
	public final CompendiumEntryType MECHANIC = new CompendiumEntryType("mechanics", "mechanic", "Mechanics", 1, CompendiumEntryMechanic.class);
	public final CompendiumEntryType ITEM = new CompendiumEntryType("items", "item", "Items", 2, CompendiumEntryItem.class);
	public final CompendiumEntryType BLOCK = new CompendiumEntryType("blocks", "block", "Blocks", 3, CompendiumEntryBlock.class);
	public final CompendiumEntryType SPELL_SHAPE = new CompendiumEntryType("shapes", "shape", "Shapes", 4, CompendiumEntrySpellShape.class);
	public final CompendiumEntryType SPELL_COMPONENT = new CompendiumEntryType("components", "component", "Components", 5, CompendiumEntrySpellComponent.class);
	public final CompendiumEntryType SPELL_MODIFIER = new CompendiumEntryType("modifiers", "modifier", "Modifiers", 6, CompendiumEntrySpellModifier.class);
	public final CompendiumEntryType TALENT = new CompendiumEntryType("talents", "talent", "Talents", 7, CompendiumEntryTalent.class);
	public final CompendiumEntryType MOB = new CompendiumEntryType("mobs", "mob", "Mobs", 8, CompendiumEntryMob.class);
	public final CompendiumEntryType STRUCTURE = new CompendiumEntryType("structures", "structure", "Structures", 9, CompendiumEntryStructure.class);
	public final CompendiumEntryType RITUAL = new CompendiumEntryType("structures", "ritual", "Structures", 9, CompendiumEntryRitual.class);
	public final CompendiumEntryType BOSS = new CompendiumEntryType("bosses", "boss", "Bosses", 10, CompendiumEntryBoss.class);

	public static final CompendiumEntryTypes instance = new CompendiumEntryTypes();
	private boolean initialized = false;

	public boolean hasInitialized(){
		return initialized;
	}

	public static CompendiumEntryType[] categoryList(){
		return new CompendiumEntryType[]{
				instance.GUIDE,
				instance.MECHANIC,
				instance.ITEM,
				instance.BLOCK,
				instance.SPELL_SHAPE,
				instance.SPELL_COMPONENT,
				instance.SPELL_MODIFIER,
				instance.TALENT,
				instance.MOB,
				instance.STRUCTURE,
				instance.BOSS
		};
	}

	private static CompendiumEntryType[] allValues(){
		return new CompendiumEntryType[]{
				instance.GUIDE,
				instance.MECHANIC,
				instance.ITEM,
				instance.BLOCK,
				instance.SPELL_SHAPE,
				instance.SPELL_COMPONENT,
				instance.SPELL_MODIFIER,
				instance.TALENT,
				instance.MOB,
				instance.STRUCTURE,
				instance.RITUAL,
				instance.BOSS
		};
	}

	public void initTextures(){
		GUIDE.setRepresentIcon(ItemsCommonProxy.arcaneCompendium.getIconFromDamage(0));
		MECHANIC.setRepresentIcon(ItemsCommonProxy.magitechGoggles.getIconFromDamage(0));
		ITEM.setRepresentIcon(ItemsCommonProxy.essence.getIconFromDamage(ItemsCommonProxy.essence.META_ICE));
		BLOCK.setRepresentIcon(ItemsCommonProxy.crystalWrench.getIconFromDamage(0));
		SPELL_SHAPE.setRepresentIcon(SpellIconManager.instance.getIcon("Binding"));
		SPELL_COMPONENT.setRepresentIcon(SpellIconManager.instance.getIcon("LifeTap"));
		SPELL_MODIFIER.setRepresentIcon(SpellIconManager.instance.getIcon("VelocityAdded"));
		TALENT.setRepresentIcon(SpellIconManager.instance.getIcon("AugmentedCasting"));
		MOB.setRepresentIcon(AMGuiIcons.fatigueIcon);
		STRUCTURE.setRepresentIcon(AMGuiIcons.gatewayPortal);
		RITUAL.setRepresentIcon(AMGuiIcons.gatewayPortal);
		BOSS.setRepresentIcon(AMGuiIcons.evilBook);

		initialized = true;
	}

	public static CompendiumEntryType getForSection(String category, String node){
		for (CompendiumEntryType type : instance.allValues()){
			if (type.getCategoryName().equals(category) && type.getNodeName().equals(node)){
				return type;
			}
		}
		return null;
	}
}
