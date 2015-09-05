package am2.api;

import am2.api.enchantment.IAMEnchantmentHelper;
import am2.api.entities.IEntityManager;
import am2.api.flickers.IFlickerRegistry;
import am2.api.items.IKeystoneHelper;
import am2.api.items.armor.IImbuementRegistry;
import am2.api.potion.IBuffHelper;
import am2.api.power.IObeliskFuelHelper;
import am2.api.spell.ISkillTreeManager;
import am2.api.spell.ISpellIconManager;
import am2.api.spell.ISpellPartManager;
import am2.api.spell.ISpellUtils;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.enums.SkillPointTypes;
import am2.api.spell.enums.SkillTrees;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.IIcon;

public class ArsMagicaApi{
	public static final ArsMagicaApi instance = new ArsMagicaApi();

	/**
	 * Easy access to this mod's ID
	 */
	public static final String AM2ModID = "arsmagica2";

	private static String extendedPropertiesIdentifier;
	private static String affinityDataIdentifier;
	private static String skillDataIdentifier;
	private static String riftStorageIdentifier;

	/**
	 * Attributes for AM2's properties
	 */
	//Bonus to max mana.  Applied additively.
	public static final IAttribute maxManaBonus = new RangedAttribute("am2.maxManaBonus", 0.0f, 0.0f, Double.MAX_VALUE).setDescription("Mana Bonus").setShouldWatch(true);
	//Bonus to max burnout.  Applied additively.
	public static final IAttribute maxBurnoutBonus = new RangedAttribute("am2.maxBurnoutBonus", 0.0f, 0.0f, Double.MAX_VALUE).setDescription("Burnout Bonus").setShouldWatch(true);
	//Bonus to XP gained.  Applied multiplicatively.
	public static final IAttribute xpGainModifier = new RangedAttribute("am2.xpMultiplier", 1.0f, 0.0f, Double.MAX_VALUE).setDescription("XP Mutiplier").setShouldWatch(true);
	//Bonus to mana regen rate.  Applied multiplicatively.
	public static final IAttribute manaRegenTimeModifier = new RangedAttribute("am2.manaRegenModifier", 1.0f, 0.5f, 2.0f).setDescription("Mana Regen Rate Multiplier").setShouldWatch(true);
	//Bonus to burnout reduction rate.  Applied multiplicatively.
	public static final IAttribute burnoutReductionRate = new RangedAttribute("am2.burnoutReduction", 1.0f, 0.1f, 2.0f).setDescription("Burnout Reduction Rate").setShouldWatch(true);

	private static final float manaBurnoutRatio = 0.38f;

	private ArsMagicaApi(){

	}

	//================================================================================
	// Getters
	//================================================================================

	/**
	 * Gets the spell part manager.  Used to register new shapes/components/modifiers/talents
	 */
	public final ISpellPartManager getSpellPartManager(){
		return spellPartManager;
	}

	/**
	 * Gets the enchantment helper.  Used to resolve current enchantment IDs.
	 */
	public final IAMEnchantmentHelper getEnchantHelper(){
		return enchantmentHelper;
	}

	/**
	 * Gets the skill tree manager.  Used to register spell parts into the skill trees (accessible via occulus)
	 */
	public final ISkillTreeManager getSkillTreeManager(){
		return this.skillTreeManager;
	}

	/**
	 * Helper method to calculate the burnout for a spell based on mana cost.
	 */
	public static final float getBurnoutFromMana(float manaCost){
		return manaCost * manaBurnoutRatio;
	}

	/**
	 * Gets the keystone helper instance.  Used to check inventories for keys and check access to blocks lockable by keystone.
	 */
	public final IKeystoneHelper getKeystoneHelper(){
		return this.keystoneHelper;
	}

	/**
	 * Gets the entity manager.  Used to blacklist things from the progeny and butchery flicker operators
	 */
	public final IEntityManager getEntityManager(){
		return this.entityManager;
	}

	/**
	 * Gets the fuel helper instance.  Used to register new fuels for the Obelisk.
	 */
	public final IObeliskFuelHelper getFuelHelper(){
		return this.obeliskFuelHelper;
	}

	/**
	 * Gets the flicker registry.  Used to register new flicker operators into the habitat.
	 */
	public final IFlickerRegistry getFlickerOperatorRegistry(){
		return flickerOperatorRegistry;
	}

	/**
	 * Gets the armor imbuement registry.  Used to register new imbuements and check if imbuements are present on a given item stack.
	 */
	public final IImbuementRegistry getArmorInfusionRegistry(){
		return this.armorInfusionRegistry;
	}

	/**
	 * Gets the AM recipe manager.  Used to register new essence refiner recipes.
	 */
	public final IAMRecipeManager getEssenceRecipeManager(){
		return this.essenceRecipes;
	}

	/**
	 * Is AM2 running in colourblind mode?
	 */
	public final boolean getColourblindMode(){
		return this.colourblindMode;
	}

	/**
	 * Returns the extended properties for the passed in entity living
	 */
	public final IExtendedProperties getExtendedProperties(EntityLivingBase entity){
		return (IExtendedProperties)entity.getExtendedProperties(extendedPropertiesIdentifier);
	}

	/**
	 * Returns the skill data instance (known shapes/modifiers/components/talents)
	 */
	public final ISkillData getSkillData(EntityPlayer entity){
		return (ISkillData)entity.getExtendedProperties(skillDataIdentifier);
	}

	/**
	 * Returns the affinity data for the passed in entity
	 */
	public final IAffinityData getAffinityData(EntityLivingBase entity){
		return (IAffinityData)entity.getExtendedProperties(affinityDataIdentifier);
	}

	/**
	 * Returns the rift storage for the passed in player.
	 */
	public final IInventory getRiftStorage(EntityPlayer entity){
		return (IInventory)entity.getExtendedProperties(riftStorageIdentifier);
	}

	/**
	 * Registers a skill tree entry (Shape, Component, Modifier, or Talent) into the mod
	 *
	 * @param entry    The skill tree entry to register
	 * @param name     The unlocalized name that is used to identify this entry
	 * @param tree     Which skill tree the entry should be located in
	 * @param x        The x coordinate that the entry should be located at in the tree.  Typical increments are of 45 to space the items.
	 * @param y        The y coordinate that the entry should be located at in the tree.  Typical increments are of 45 to space the items.
	 * @param point    The type of skill point needed to unlock this entry
	 * @param prerequs Any skill tree entries that are required as prerequisites for this one.  All prerequisites must be registered before the current entry.
	 */
	public final void registerSkillTreeEntry(ISkillTreeEntry entry, String name, SkillTrees tree, int x, int y, SkillPointTypes point, ISkillTreeEntry... prereqs){
		spellPartManager.registerSkillTreeEntry(entry, name);
		skillTreeManager.RegisterPart(entry, x, y, tree, point, prereqs);
	}

	/**
	 * Registers the IIcon to use for the specified skill.  This should be called during the API's registerSkillTreeIcons event for each entry that you registered.  You get a reference to the ISpellIconManager which has the same method, so this is only here for convenience.
	 *
	 * @param name  The unlocalized name that was used to register the spell.  See {@link #registerSkillTreeEntry(ISkillTreeEntry, String, SkillTrees, int, int, SkillPointTypes, ISkillTreeEntry...) registerSkillTreeEntry} method for more info on the name.
	 * @param IIcon The IIcon to use.  You need to register these IIcons with the game beforehand during your mod's load phase - AM won't do it for you.
	 */
	@SideOnly(Side.CLIENT)
	public final void registerSkillIcon(String name, IIcon IIcon){
		spellIconManager.registerIcon(name, IIcon);
	}

	/**
	 * Returns the IIcon manager.  This is used to register spell IIcons.
	 */
	@SideOnly(Side.CLIENT)
	public final ISpellIconManager getSpellIconManager(){
		return spellIconManager;
	}

	/**
	 * Returns the buff helper which can be used to query AM for potion IDs, and add dispel blacklists
	 */
	public final IBuffHelper getBuffHelper(){
		return this.buffHelper;
	}

	/**
	 * Gets the spell utils class
	 */
	public final ISpellUtils getSpellUtils(){
		return this.spellUtils;
	}

	//================================================================================
	// Setters - for AM use only, do not call these.
	//================================================================================
	public final void setSpellPartManager(ISpellPartManager manager){
		if (this.spellPartManager == null){
			this.spellPartManager = manager;
		}
	}

	public final void setEnchantmentHelper(IAMEnchantmentHelper helper){
		if (this.enchantmentHelper == null){
			this.enchantmentHelper = helper;
		}
	}

	public final void setSkillTreeManager(ISkillTreeManager manager){
		if (this.skillTreeManager == null)
			this.skillTreeManager = manager;
	}

	@SideOnly(Side.CLIENT)
	public final void setSpellIconManager(ISpellIconManager manager){
		if (this.spellIconManager == null)
			this.spellIconManager = manager;
	}

	public final void setKeystoneHelper(IKeystoneHelper helper){
		if (this.keystoneHelper == null){
			this.keystoneHelper = helper;
		}
	}

	public final void setExtendedPropertiesID(String identifier){
		if (extendedPropertiesIdentifier == null)
			extendedPropertiesIdentifier = identifier;
	}

	public final void setAffinityDataID(String identifier){
		if (affinityDataIdentifier == null)
			affinityDataIdentifier = identifier;
	}

	public final void setSkillDataID(String identifier){
		if (skillDataIdentifier == null)
			skillDataIdentifier = identifier;
	}

	public final void setRiftStorageID(String identifier){
		if (riftStorageIdentifier == null)
			riftStorageIdentifier = identifier;
	}

	public final void setEntityManager(IEntityManager manager){
		if (entityManager == null)
			entityManager = manager;
	}

	public final void setObeliskFuelHelper(IObeliskFuelHelper manager){
		if (obeliskFuelHelper == null)
			obeliskFuelHelper = manager;
	}

	public final void setFlickerOperatorRegistry(IFlickerRegistry registry){
		if (flickerOperatorRegistry == null)
			flickerOperatorRegistry = registry;
	}

	public final void setInfusionRegistry(IImbuementRegistry registry){
		if (this.armorInfusionRegistry == null)
			this.armorInfusionRegistry = registry;
	}

	public final void setEssenceRecipeHandler(IAMRecipeManager manager){
		if (this.essenceRecipes != null)
			this.essenceRecipes = manager;
	}

	public final void setColourblindMode(boolean colourblindMode){
		this.colourblindMode = colourblindMode;
	}

	public final void setBuffHelper(IBuffHelper helper){
		if (this.buffHelper == null)
			this.buffHelper = helper;
	}

	public final void setSpellUtils(ISpellUtils utilsHelper){
		if (this.spellUtils == null){
			this.spellUtils = utilsHelper;
		}
	}

	private ISpellPartManager spellPartManager;
	private ISpellUtils spellUtils;
	private ISkillTreeManager skillTreeManager;
	private IAMEnchantmentHelper enchantmentHelper;
	private IEntityManager entityManager;
	private IKeystoneHelper keystoneHelper;
	private IObeliskFuelHelper obeliskFuelHelper;
	private IFlickerRegistry flickerOperatorRegistry;
	private IImbuementRegistry armorInfusionRegistry;
	private IAMRecipeManager essenceRecipes;
	@SideOnly(Side.CLIENT)
	private ISpellIconManager spellIconManager;
	private IBuffHelper buffHelper;
	private boolean colourblindMode;

}
