package am2.configuration;

import am2.api.math.AMVector2;
import am2.particles.AMParticle;
import am2.particles.ParticleController;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class AMConfig extends Configuration{

	private final String KEY_PlayerSpellsDamageTerrain = "Player_Spells_Destroy_Terrain";
	private final String KEY_NPCSpellsDamageTerrain = "NPC_Spells_Destroy_Terrain";
	private final String KEY_TowergenGridSize = "Towergen_Grid_Size";
	private final String KEY_EnableWorldGen = "EnableWorldGen";
	private final String KEY_RetroactiveWorldGen = "RetroactiveWorldGen";

	private final String KEY_SecondarySkillTreeTierCap = "SecondarySkillTreeTierCap";
	private final String KEY_DigBreaksTEs = "DigBreaksTileEntities";
	private final String KEY_DisplayManaInInventory = "DisplayManaInInventory";
	private final String KEY_SpellBookUIPosition = "SpellBookUIPosition";
	private final String KEY_ManaCap = "Mana_Cap";
	private final String KEY_mageSpawnRate = "MageSpawnRate";
	private final String KEY_waterElementalSpawnRate = "WaterElementalSpawnRate";
	private final String KEY_hecateSpawnRate = "HecateSpawnRate";
	private final String KEY_dryadSpawnRate = "DryadSpawnRate";
	private final String KEY_manaElementalSpawnRate = "ManaElementalSpawnRate";
	private final String KEY_manaCreeperSpawnRate = "ManaCreeperSpawnRate";
	private final String KEY_darklingSpawnRate = "DarklingSpawnRate";
	private final String KEY_earthElementalSpawnRate = "EarthElementalSpawnRate";
	private final String KEY_fireElementalSpawnRate = "FireElementalSpawnRate";
	private final String KEY_flickerSpawnRate = "FlickerSpawnRate";

	private final String KEY_RandomSpellRecipes = "RandomSpellRecipes";
	private final String KEY_DamageMultiplier = "DamageMultiplier";

	private final String KEY_UseSpecialRenderers = "Use_Special_Renderers";
	private final String KEY_LowResParticles = "Low_Res_Particles";
	private final String KEY_FrictionCoefficient = "FrictionCoefficient";

	private final String KEY_MageVillagerProfessionID = "mage_villager_profession_id";

	private final String KEY_DigDisabledBlocks = "dig_blacklist";
	private final String KEY_WorldgenBlacklist = "worldgen_blacklist";

	private final String KEY_GetRandomSpellNames = "suggest_spell_names";
	private final String KEY_DisarmAffectsPlayers = "disarm_affects_players";

	private final String KEY_MMFBiomeID = "MMFBiomeID";
	private final String KEY_MMFDimensionID = "MMFDimensionID";
	private final String KEY_WitchwoodForestBiomeID = "WitchwoodForestBiomeID";
	private final String KEY_WitchwoodForestRarity = "Witchwood_Forest_Biome_Rarity";

	private final String KEY_ForgeSmeltsVillagers = "ForgeSmeltsVillagers";
	private final String KEY_EverstoneRepairRate = "EverstoneRepairRate";

	private final String KEY_witchwoodLeavesFall = "WitchwoodLeafParticles";
	private final String KEY_CandlesAreRovingLights = "CandlesAreRovingLights";
	private final String KEY_Appropriation_Block_Blacklist = "Appropriation_Block_Blacklist";
	private final String KEY_Appropriation_Mob_Blacklist = "Appropriation_Mob_Blacklist";

	private final String KEY_AllowVersionChecks = "Allow_Version_Checks";
	private final String KEY_AllowCompendiumUpdates = "Allow_Compendium_Updates";
	private final String KEY_MeteorMinSpawnLevel = "Meteor_Spawn_Min_Level";
	private final String KEY_HazardousGateways = "Hazardous_Gateways";
	private final String KEY_CanDryadsDespawn = "Can_Dryads_Despawn";

	private final String KEY_ArmorXPInfusionFactor = "Armor_XP_Infusion_Factor";

	private final String KEY_SavePowerOnWorldSave = "PND_File_WSave";

	private final String KEY_EnableWitchwoodForest = "Enable_Witchwood_Forests";

	/**
	 * Beta Particles
	 **/
	private final String KEY_AuraType = "AuraType";
	private final String KEY_AuraBehaviour = "AuraBehaviour";
	private final String KEY_AuraScale = "AuraScale";
	private final String KEY_AuraColor = "AuraColor";
	private final String KEY_AuraQuanity = "AuraQuantity";
	private final String KEY_AuraDelay = "AuraDelay";
	private final String KEY_AuraSpeed = "AuraSpeed";
	private final String KEY_AuraAlpha = "AuraAlpha";
	private final String KEY_AuraColorRandomize = "AuraColorRandomize";
	private final String KEY_AuraColorDefault = "AuraColorDefault";
	/** End Beta Particles **/

	/**
	 * GUI Config
	 **/
	private final String KEY_ManaHudPositionX = "ManaHudPositionX";
	private final String KEY_BurnoutHudPositionX = "BurnoutHudPositionX";
	private final String KEY_BuffsPositivePositionX = "BuffsPositivePositionX";
	private final String KEY_BuffsNegativePositionX = "BuffsNegativePositionX";
	private final String KEY_LevelPositionX = "LevelPositionX";
	private final String KEY_AffinityPositionX = "AffinityPositionX";
	private final String KEY_ArmorPositionHeadX = "ArmorPositionHeadX";
	private final String KEY_ArmorPositionChestX = "ArmorPositionChestX";
	private final String KEY_ArmorPositionLegsX = "ArmorPositionLegsX";
	private final String KEY_ArmorPositionBootsX = "ArmorPositionBootsX";

	private final String KEY_ManaHudPositionY = "ManaHudPositionY";
	private final String KEY_BurnoutHudPositionY = "BurnoutHudPositionY";
	private final String KEY_BuffsPositivePositionY = "BuffsPositivePositionY";
	private final String KEY_BuffsNegativePositionY = "BuffsNegativePositionY";
	private final String KEY_LevelPositionY = "LevelPositionY";
	private final String KEY_AffinityPositionY = "AffinityPositionY";
	private final String KEY_ArmorPositionHeadY = "ArmorPositionHeadY";
	private final String KEY_ArmorPositionChestY = "ArmorPositionChestY";
	private final String KEY_ArmorPositionLegsY = "ArmorPositionLegsY";
	private final String KEY_ArmorPositionBootsY = "ArmorPositionBootsY";
	private final String KEY_XPBarPositionX = "XPBarPositionX";
	private final String KEY_XPBarPositionY = "XPBarPositionY";
	private final String KEY_ContingencyPositionX = "ContingencyPositionX";
	private final String KEY_ContingencyPositionY = "ContingencyPositionY";
	private final String KEY_ManaNumericPositionX = "ManaNumericX";
	private final String KEY_ManaNumericPositionY = "ManaNumericY";
	private final String KEY_BurnoutNumericPositionX = "BurnoutNumericX";
	private final String KEY_BurnoutNumericPositionY = "BurnoutNumericY";
	private final String KEY_XPNumericPositionX = "XPNumericX";
	private final String KEY_XPNumericPositionY = "XPNumericY";
	private final String KEY_SpellBookPositionX = "SpellBookX";
	private final String KEY_SpellBookPositionY = "SpellBookY";
	private final String KEY_EnderAffinityAbilityCooldown = "EnderAffinityAbilityCD";

	private final String KEY_StagedCompendium = "Staged Compendium";

	private final String KEY_ShowHudMinimally = "ShowHudMinimally";
	private final String KEY_ShowArmorUI = "ShowArmorUI";
	private final String KEY_moonstoneMeteorsDestroyTerrain = "MoonstoneMeteorDestroyTerrain";

	private final String KEY_ShowBuffs = "ShowBuffTimers";
	private final String KEY_ShowNumerics = "ShowNumericValues";
	private final String KEY_ShowXPAlways = "ShowXPAlways";
	private final String KEY_ShowHUDBars = "ShowHUDBars";
	private final String KEY_ColourblindMode = "ColourblindMode";
	/**
	 * End GUI Config
	 **/

	private static final String CATEGORY_BETA = "beta";
	private static final String CATEGORY_MOBS = "mobs";
	private static final String CATEGORY_ENCHANTMENTS = "enchantments";
	private static final String CATEGORY_UI = "guis";
	private static final String CATEGORY_POTIONS = "potions";

	private int GFXLevel;
	private boolean PlayerSpellsDamageTerrain;
	private boolean NPCSpellsDamageTerrain;
	private float DamageMultiplier;
	private boolean UseSpecialRenderers;
	private boolean DisplayManaInInventory;
	private boolean IsImbueEnabled;
	private boolean RetroWorldGen;
	private boolean moonstoneMeteorsDestroyTerrain;
	private boolean suggestSpellNames;
	private boolean forgeSmeltsVillagers;
	private boolean witchwoodLeafParticles;
	private int everstoneRepairRate;

	private int witchwoodForestID;

	private float FrictionCoefficient;

	private int secondarySkillTreeTierCap;
	private int mageVillagerProfessionID;
	private String[] digBlacklist;
	private int[] worldgenBlacklist;
	private boolean enableWitchwoodForest;
	private int witchwoodForestRarity;

	private String[] appropriationBlockBlacklist;
	private Class[] appropriationMobBlacklist;

	private int AuraType;
	private int AuraBehaviour;
	private float AuraScale;
	private float AuraAlpha;
	private int AuraColor;
	private int AuraDelay;
	private int AuraQuantity;
	private double AuraSpeed;
	private boolean AuraRandomColor;
	private boolean AuraDefaultColor;
	private double ArmorXPInfusionFactor;
	private double manaCap;
	private int enderAffinityAbilityCooldown;

	private AMVector2 manaHudPosition;
	private AMVector2 burnoutHudPosition;
	private AMVector2 positiveBuffsPosition;
	private AMVector2 negativeBuffsPosition;
	private AMVector2 levelPosition;
	private AMVector2 affinityPosition;
	private AMVector2 armorPositionHead;
	private AMVector2 armorPositionChest;
	private AMVector2 armorPositionLegs;
	private AMVector2 armorPositionBoots;
	private AMVector2 xpBarPosition;
	private AMVector2 contingencyPosition;
	private AMVector2 manaNumericPosition;
	private AMVector2 burnoutNumericPosition;
	private AMVector2 XPNumericPosition;
	private AMVector2 SpellBookPosition;
	private boolean showBuffs;
	private boolean showNumerics;
	private boolean showHudMinimally;
	private boolean showXPAlways;
	private boolean showArmorUI;
	private boolean stagedCompendium;
	private boolean showHudBars;
	private boolean colourblindMode;
	private boolean candlesAreRovingLights;
	private int meteorMinSpawnLevel;
	private boolean hazardousGateways;
	private boolean disarmAffectsPlayers;
	private boolean digBreaksTileEntities;
	private boolean savePowerOnWorldSave;

	private boolean allowCompendiumUpdates;
	private boolean allowVersionChecks;
	private boolean canDryadsDespawn;


	public static final String DEFAULT_LANGUAGE = "en_US";

	public AMConfig(File file){
		super(file);
		load();
		addCustomCategoryComment(CATEGORY_BETA, "This applies to those who have beta auras unlocked only");
		addCustomCategoryComment(CATEGORY_ENCHANTMENTS, "Allows control over various enchantments in the mod.");
		addCustomCategoryComment(CATEGORY_MOBS, "Spawn control for different AM mobs.");
	}

	public void init(){

		PlayerSpellsDamageTerrain = get(CATEGORY_GENERAL, KEY_PlayerSpellsDamageTerrain, true).getBoolean(true);
		NPCSpellsDamageTerrain = get(CATEGORY_GENERAL, KEY_NPCSpellsDamageTerrain, false).getBoolean(false);

		DamageMultiplier = (float)get(CATEGORY_GENERAL, KEY_DamageMultiplier, 1.0, "How much the damage in Ars Magica is scaled.").getDouble(1.0);

		UseSpecialRenderers = get(CATEGORY_GENERAL, KEY_UseSpecialRenderers, true, "Render spell effects on equipped scrolls rather than the scroll itself (only applies to the in-game one, the one on your hotbar remains unchanged)").getBoolean(true);

		boolean def = !Loader.isModLoaded("NotEnoughItems");
		DisplayManaInInventory = get(CATEGORY_GENERAL, KEY_DisplayManaInInventory, def, "This will toggle mana display on and off in your inventory.  Default 'O' key in game.").getBoolean(def);

		FrictionCoefficient = (float)get(CATEGORY_GENERAL, KEY_FrictionCoefficient, 0.8, "This is the multiplier used to determine velocity lost when a spell projectile bounces. 0.0 is a complete stop, 1.0 is no loss.").getDouble(0.8);

		Property retroWorldGenProp = get(CATEGORY_GENERAL, KEY_RetroactiveWorldGen, false, "Set this to true to enable retroactive worldgen for Ars Magica structures and ores.  *WARNING* This may break your save!  Do a backup first!  Note: This will automatically turn off after running the game once.");
		RetroWorldGen = retroWorldGenProp.getBoolean(false);

		if (RetroWorldGen){
			retroWorldGenProp.set(false);
		}

		secondarySkillTreeTierCap = get(CATEGORY_GENERAL, KEY_SecondarySkillTreeTierCap, 99, "Sets how far a player may progress into secondary skill trees.").getInt();
		mageVillagerProfessionID = get(CATEGORY_GENERAL, KEY_MageVillagerProfessionID, 29).getInt();

		manaHudPosition = new AMVector2(get(CATEGORY_UI, KEY_ManaHudPositionX, 0.7104166746139526).getDouble(0.7104166746139526), get(CATEGORY_UI, KEY_ManaHudPositionY, 0.9137254953384399).getDouble(0.9137254953384399));
		burnoutHudPosition = new AMVector2(get(CATEGORY_UI, KEY_BurnoutHudPositionX, 0.13333334028720856).getDouble(0.13333334028720856), get(CATEGORY_UI, KEY_BurnoutHudPositionY, 0.9176470637321472).getDouble(0.9176470637321472));
		positiveBuffsPosition = new AMVector2(get(CATEGORY_UI, KEY_BuffsPositivePositionX, 0.5145833492279053).getDouble(0.5145833492279053), get(CATEGORY_UI, KEY_BuffsPositivePositionY, 0.47843137383461).getDouble(0.47843137383461));
		negativeBuffsPosition = new AMVector2(get(CATEGORY_UI, KEY_BuffsNegativePositionX, 0.46666666865348816).getDouble(0.46666666865348816), get(CATEGORY_UI, KEY_BuffsNegativePositionY, 0.47843137383461).getDouble(0.47843137383461));
		levelPosition = new AMVector2(get(CATEGORY_UI, KEY_LevelPositionX, 0.49791666865348816).getDouble(0.49791666865348816), get(CATEGORY_UI, KEY_LevelPositionY, 0.8117647171020508).getDouble(0.8117647171020508));
		affinityPosition = new AMVector2(get(CATEGORY_UI, KEY_AffinityPositionX, 0.9770833253860474).getDouble(0.9770833253860474), get(CATEGORY_UI, KEY_AffinityPositionY, 0.9).getDouble(0.9));
		armorPositionChest = new AMVector2(get(CATEGORY_UI, KEY_ArmorPositionChestX, 0.004166666883975267).getDouble(0.004166666883975267), get(CATEGORY_UI, KEY_ArmorPositionChestY, 0.5568627715110779).getDouble(0.5568627715110779));
		armorPositionHead = new AMVector2(get(CATEGORY_UI, KEY_ArmorPositionHeadX, 0.004166666883975267).getDouble(0.004166666883975267), get(CATEGORY_UI, KEY_ArmorPositionHeadY, 0.5176470875740051).getDouble(0.5176470875740051));
		armorPositionLegs = new AMVector2(get(CATEGORY_UI, KEY_ArmorPositionLegsX, 0.004166666883975267).getDouble(0.004166666883975267), get(CATEGORY_UI, KEY_ArmorPositionLegsY, 0.5960784554481506).getDouble(0.5960784554481506));
		armorPositionBoots = new AMVector2(get(CATEGORY_UI, KEY_ArmorPositionBootsX, 0.004166666883975267).getDouble(0.004166666883975267), get(CATEGORY_UI, KEY_ArmorPositionBootsY, 0.6352941393852234).getDouble(0.6352941393852234));
		xpBarPosition = new AMVector2(get(CATEGORY_UI, KEY_XPBarPositionX, 0.31041666865348816).getDouble(0.31041666865348816), get(CATEGORY_UI, KEY_XPBarPositionY, 0.7843137383460999).getDouble(0.7843137383460999));
		contingencyPosition = new AMVector2(get(CATEGORY_UI, KEY_ContingencyPositionX, 0.0020833334419876337).getDouble(0.0020833334419876337), get(CATEGORY_UI, KEY_ContingencyPositionY, 0.9333333373069763).getDouble(0.9333333373069763));

		manaNumericPosition = new AMVector2(get(CATEGORY_UI, KEY_ManaNumericPositionX, 0.7437499761581421).getDouble(0.7437499761581421), get(CATEGORY_UI, KEY_ManaNumericPositionY, 0.8941176533699036).getDouble(0.8941176533699036));
		burnoutNumericPosition = new AMVector2(get(CATEGORY_UI, KEY_BurnoutNumericPositionX, 0.21041665971279144).getDouble(0.21041665971279144), get(CATEGORY_UI, KEY_BurnoutNumericPositionY, 0.9058823585510254).getDouble(0.9058823585510254));
		XPNumericPosition = new AMVector2(get(CATEGORY_UI, KEY_XPNumericPositionX, 0.47083333134651184).getDouble(0.47083333134651184), get(CATEGORY_UI, KEY_XPNumericPositionY, 0.7450980544090271).getDouble(0.7450980544090271));
		SpellBookPosition = new AMVector2(get(CATEGORY_UI, KEY_SpellBookPositionX, 0.0).getDouble(0.0), get(CATEGORY_UI, KEY_SpellBookPositionY, 0.0).getDouble(0.0));

		showHudMinimally = get(CATEGORY_UI, KEY_ShowHudMinimally, false, "Set this to true to only show the AM HUD when a spell is equipped").getBoolean(false);
		showArmorUI = get(CATEGORY_UI, KEY_ShowArmorUI, true).getBoolean(true);
		showBuffs = get(CATEGORY_UI, KEY_ShowBuffs, true).getBoolean(true);
		showNumerics = get(CATEGORY_UI, KEY_ShowNumerics, false).getBoolean(false);
		showXPAlways = get(CATEGORY_UI, KEY_ShowXPAlways, false).getBoolean(false);
		showHudBars = get(CATEGORY_UI, KEY_ShowHUDBars, true).getBoolean(true);

		witchwoodForestID = get(CATEGORY_GENERAL, KEY_WitchwoodForestBiomeID, 100).getInt();
		witchwoodLeafParticles = get(CATEGORY_GENERAL, KEY_witchwoodLeavesFall, true, "Disable this if you experience low FPS in witchwood forests").getBoolean(true);
		enableWitchwoodForest = get(CATEGORY_GENERAL, KEY_EnableWitchwoodForest, true, "Disable this if you prefer the witchwood forest to not generate").getBoolean(true);
		witchwoodForestRarity = get(CATEGORY_GENERAL, KEY_WitchwoodForestRarity, 6, "Sets how rare witchwood forests are.  Lower is more rare.").getInt();

		moonstoneMeteorsDestroyTerrain = get(CATEGORY_GENERAL, KEY_moonstoneMeteorsDestroyTerrain, true, "Should moonstone meteors destroy terrain when landing?  Keep in mind they will never land on anything other than grass.").getBoolean(true);

		suggestSpellNames = get(CATEGORY_GENERAL, KEY_moonstoneMeteorsDestroyTerrain, true, "Set this to true to allow AM2 to get random spell names from Seventh Sanctum, and suggest them when naming spells.  Naturally, an internet connection is required.  Keep in mind, while I try to keep things family friendly, it's possible that not all names generated are so.").getBoolean(true);

		forgeSmeltsVillagers = get(CATEGORY_GENERAL, KEY_ForgeSmeltsVillagers, true, "Set this to true to have the forge component smelt villagers into emeralds.  This counts as an attack and lowers your reputation.").getBoolean(true);

		everstoneRepairRate = get(CATEGORY_GENERAL, KEY_EverstoneRepairRate, 180).getInt();

		stagedCompendium = get(CATEGORY_GENERAL, KEY_StagedCompendium, true, "Set this to false to have the compendium show everything, and not unlock as you go.").getBoolean(true);

		colourblindMode = get(CATEGORY_GENERAL, KEY_ColourblindMode, false, "Set this to true to have AM2 list out colours for skill points and essence types rather than showing them as a colour.").getBoolean(false);

		candlesAreRovingLights = get(CATEGORY_GENERAL, KEY_CandlesAreRovingLights, true, "Set this to false to disable candles being able to act as roving lights, which improves performance.").getBoolean(true);

		allowCompendiumUpdates = get(CATEGORY_GENERAL, KEY_AllowCompendiumUpdates, true, "If true, AM2 will automatically download compendium updates when available for your mod version.").getBoolean(true);
		allowVersionChecks = get(CATEGORY_GENERAL, KEY_AllowVersionChecks, true, "If true, AM2 will notify you via the compendium when new versions are available.  It will not spam chat on login.  You will not be notified of updates that are not for your current Minecraft version.").getBoolean(true);

		meteorMinSpawnLevel = get(CATEGORY_GENERAL, KEY_MeteorMinSpawnLevel, 10, "You must reach this magic level before Moonstone meteors will fall near you.").getInt();

		hazardousGateways = get(CATEGORY_GENERAL, KEY_HazardousGateways, true, "Set this to false in order to disable gateways sending you partial distances if you don't have enough power.").getBoolean(true);

		ArmorXPInfusionFactor = get(CATEGORY_GENERAL, KEY_ArmorXPInfusionFactor, 1.0, "Alter this to change the rate at which armor XP infuses.").getDouble();
		disarmAffectsPlayers = get(CATEGORY_GENERAL, KEY_DisarmAffectsPlayers, true, "If false, disarm won't work on players.").getBoolean(true);
		manaCap = get(CATEGORY_GENERAL, KEY_ManaCap, 0, "Sets the maximum mana a player can have (0 for no cap)").getDouble(0);

		digBreaksTileEntities = get(CATEGORY_GENERAL, KEY_DigBreaksTEs, true, "Can the dig component break blocks that have a tile entity?").getBoolean(true);

		savePowerOnWorldSave = get(CATEGORY_GENERAL, KEY_SavePowerOnWorldSave, true, "Set this to false if you are experiencing tick lage due to AM2 saving power data alongside the world save.  This will instead cache the power data in memory to be saved later.  This comes with more risk in the event of a crash, and a larger memory footprint, but increased performance. Can be used alongside chunk unload save config. Power data is still always saved at world unload (server shutdown).").getBoolean(true);

		canDryadsDespawn = get(CATEGORY_MOBS, KEY_CanDryadsDespawn, true, "Set this to false if you don't want dryads to despawn.").getBoolean(true);

		enderAffinityAbilityCooldown = get(CATEGORY_GENERAL, KEY_EnderAffinityAbilityCooldown, 100, "Set this to the number of ticks between ender affinity teleports.").getInt();

		String digBlacklistString = get(CATEGORY_GENERAL, KEY_DigDisabledBlocks, "", "Comma-separated list of block IDs that dig cannot break.  If a block is flagged as unbreackable in code, Dig will already be unable to break it.  There is no need to set it here (eg, bedrock, etc.).  Dig also makes use of Forge block harvest checks.  This is mainly for fine-tuning.").getString();
		digBlacklist = digBlacklistString.split(",");

		String worldgenBlackList = get(CATEGORY_GENERAL, KEY_WorldgenBlacklist, "-27,-28,-29", "Comma-separated list of dimension IDs that AM should *not* do worldgen in.").getString();
		String[] split = worldgenBlackList.split(",");
		worldgenBlacklist = new int[split.length];
		int count = 0;
		for (String s : split){
			if (s.equals("")) continue;
			try{
				worldgenBlacklist[count] = Integer.parseInt(s.trim());
			}catch (Throwable t){
				FMLLog.info("Ars Magica >> Malformed item in worldgen blacklist (%s).  Skipping.", s);
				t.printStackTrace();
				worldgenBlacklist[count] = -1;
			}finally{
				count++;
			}
		}

		String apBlockBL = get(CATEGORY_GENERAL, KEY_Appropriation_Block_Blacklist, "", "Comma-separated list of block IDs that appropriation cannot pick up.").getString();
		appropriationBlockBlacklist = apBlockBL.split(",");

		String apEntBL = get(CATEGORY_GENERAL, KEY_Appropriation_Mob_Blacklist, "", "Comma-separated list of *fully qualified* Entity class names that appropriation cannot pick up - example, am2.entities.EntityDryad.  They are case sensitive.").getString();
		split = apEntBL.split(",");
		appropriationMobBlacklist = new Class[split.length];
		count = 0;
		for (String s : split){
			if (s.equals("")) continue;
			try{
				appropriationMobBlacklist[count] = Class.forName(s);
			}catch (Throwable t){
				FMLLog.info("Ars Magica >> Malformed item in appropriation entity blacklist (%s).  Skipping.", s);
				t.printStackTrace();
				appropriationMobBlacklist[count] = null;
			}finally{
				count++;
			}
		}

		initDirectProperties();

		save();
	}

	public void clientInit(){
		AuraType = get(CATEGORY_BETA, KEY_AuraType, 15).getInt(15);
		AuraType %= AMParticle.particleTypes.length;
		AuraBehaviour = get(CATEGORY_BETA, KEY_AuraBehaviour, 0).getInt(0);
		AuraBehaviour %= ParticleController.AuraControllerOptions.length;
		AuraAlpha = (float)(get(CATEGORY_BETA, KEY_AuraAlpha, 1.0D)).getDouble(1.0D);
		AuraScale = (float)(get(CATEGORY_BETA, KEY_AuraScale, 1.0D).getDouble(1.0));
		AuraColor = get(CATEGORY_BETA, KEY_AuraColor, 0xFFFFFF).getInt(0xFFFFFF);
		AuraQuantity = get(CATEGORY_BETA, KEY_AuraQuanity, 1).getInt(1);
		AuraDelay = get(CATEGORY_BETA, KEY_AuraDelay, 5).getInt(5);
		AuraSpeed = get(CATEGORY_BETA, KEY_AuraSpeed, 0.02D).getDouble(0.02D);
		AuraRandomColor = get(CATEGORY_BETA, KEY_AuraColorRandomize, true).getBoolean(true);
		AuraDefaultColor = get(CATEGORY_BETA, KEY_AuraColorDefault, true).getBoolean(true);

		GFXLevel = 2 - Minecraft.getMinecraft().gameSettings.particleSetting;

		save();
	}

	//====================================================================================
	// Getters - Cached
	//====================================================================================

	public boolean FullGFX(){
		return GFXLevel == 2;
	}

	public boolean LowGFX(){
		return GFXLevel == 1;
	}

	public boolean NoGFX(){
		return GFXLevel == 0;
	}

	public boolean NPCSpellsDamageTerrain(){
		return NPCSpellsDamageTerrain;
	}

	public boolean PlayerSpellsDamageTerrain(){
		return PlayerSpellsDamageTerrain;
	}

	public int getGFXLevel(){
		return GFXLevel;
	}

	public float getDamageMultiplier(){
		return DamageMultiplier;
	}

	public boolean getIsImbueEnchantEnabled(){
		return IsImbueEnabled;
	}

	public int getImbueProcCost(int enchantID){
		return 0;
	}

	public boolean useSpecialRenderers(){
		return UseSpecialRenderers;
	}

	public boolean displayManaInInventory(){
		return DisplayManaInInventory;
	}

	public double getFrictionCoefficient(){
		return FrictionCoefficient;
	}

	public boolean retroactiveWorldgen(){
		return RetroWorldGen;
	}

	public int getSkillTreeSecondaryTierCap(){
		return secondarySkillTreeTierCap;
	}

	public int getVillagerProfessionID(){
		return mageVillagerProfessionID;
	}

	public AMVector2 getManaHudPosition(){
		return manaHudPosition;
	}

	public AMVector2 getBurnoutHudPosition(){
		return burnoutHudPosition;
	}

	public AMVector2 getPositiveBuffsPosition(){
		return positiveBuffsPosition;
	}

	public AMVector2 getNegativeBuffsPosition(){
		return negativeBuffsPosition;
	}

	public AMVector2 getLevelPosition(){
		return levelPosition;
	}

	public AMVector2 getAffinityPosition(){
		return affinityPosition;
	}

	public AMVector2 getArmorPositionHead(){
		return armorPositionHead;
	}

	public AMVector2 getArmorPositionChest(){
		return armorPositionChest;
	}

	public AMVector2 getArmorPositionLegs(){
		return armorPositionLegs;
	}

	public AMVector2 getArmorPositionBoots(){
		return armorPositionBoots;
	}

	public AMVector2 getXPBarPosition(){
		return xpBarPosition;
	}

	public AMVector2 getContingencyPosition(){
		return contingencyPosition;
	}

	public AMVector2 getManaNumericPosition(){
		return manaNumericPosition;
	}

	public AMVector2 getBurnoutNumericPosition(){
		return burnoutNumericPosition;
	}

	public AMVector2 getXPNumericPosition(){
		return XPNumericPosition;
	}

	public AMVector2 getSpellBookPosition(){
		return SpellBookPosition;
	}

	public boolean getShowBuffs(){
		return showBuffs;
	}

	public boolean getShowNumerics(){
		return showNumerics;
	}

	public String[] getDigBlacklist(){
		return digBlacklist;
	}

	public int[] getWorldgenBlacklist(){
		return worldgenBlacklist;
	}

	public boolean moonstoneMeteorsDestroyTerrain(){
		return moonstoneMeteorsDestroyTerrain;
	}

	public boolean suggestSpellNames(){
		return suggestSpellNames;
	}

	public int getWitchwoodForestID(){
		return witchwoodForestID;
	}

	public int getEverstoneRepairRate(){
		return everstoneRepairRate;
	}

	public boolean showHudMinimally(){
		return showHudMinimally;
	}

	public boolean stagedCompendium(){
		return stagedCompendium;
	}

	public boolean showXPAlways(){
		return showXPAlways;
	}

	public boolean showHudBars(){
		return showHudBars;
	}

	public boolean witchwoodLeafPFX(){
		return witchwoodLeafParticles;
	}

	public boolean colourblindMode(){
		return colourblindMode;
	}

	public String[] getAppropriationBlockBlacklist(){
		return appropriationBlockBlacklist;
	}

	public Class[] getAppropriationMobBlacklist(){
		return appropriationMobBlacklist;
	}

	public boolean allowVersionChecks(){
		return allowVersionChecks;
	}

	public boolean allowCompendiumUpdates(){
		return allowCompendiumUpdates;
	}

	public boolean getHazardousGateways(){
		return hazardousGateways;
	}

	public double getArmorXPInfusionFactor(){
		return ArmorXPInfusionFactor;
	}

	public boolean getDisarmAffectsPlayers(){
		return disarmAffectsPlayers;
	}

	public double getManaCap(){
		return manaCap;
	}

	public boolean getDigBreaksTileEntities(){
		return digBreaksTileEntities;
	}

	public boolean savePowerDataOnWorldSave(){
		return savePowerOnWorldSave;
	}


	public boolean canDraydsDespawn(){
		return canDryadsDespawn;
	}

	public int getMeteorMinSpawnLevel(){
		return meteorMinSpawnLevel;
	}

	public boolean forgeSmeltsVillagers(){
		return forgeSmeltsVillagers;
	}

	public boolean showArmorUI(){
		return this.showArmorUI;
	}

	public boolean candlesAreRovingLights(){
		return candlesAreRovingLights;
	}

	public int getEnderAffinityAbilityCooldown(){
		return this.enderAffinityAbilityCooldown;
	}

	public boolean getEnableWitchwoodForest(){
		return this.enableWitchwoodForest;
	}

	public int getWitchwoodForestRarity(){
		return this.witchwoodForestRarity;
	}

	//====================================================================================
	// Getters - Aura
	//====================================================================================

	public int getAuraIndex(){
		return AuraType;
	}

	public int getAuraBehaviour(){
		return AuraBehaviour;
	}

	public boolean getAuraColorRandom(){
		return AuraRandomColor;
	}

	public boolean getAuraColorDefault(){
		return AuraDefaultColor;
	}

	public float getAuraScale(){
		return AuraScale;
	}

	public int getAuraColor(){
		return AuraColor;
	}

	public int getAuraDelay(){
		return AuraDelay;
	}

	public int getAuraQuantity(){
		return AuraQuantity;
	}

	public float getAuraSpeed(){
		return (float)AuraSpeed;
	}

	public float getAuraAlpha(){
		return AuraAlpha;
	}


	//====================================================================================
	// Getters - Direct
	//====================================================================================
	//ping the direct properties once so that they show up in config
	public void initDirectProperties(){
		get(CATEGORY_MOBS, KEY_hecateSpawnRate, 2);
		get(CATEGORY_MOBS, KEY_mageSpawnRate, 1);
		get(CATEGORY_MOBS, KEY_waterElementalSpawnRate, 3);
		get(CATEGORY_MOBS, KEY_manaElementalSpawnRate, 2);
		get(CATEGORY_MOBS, KEY_dryadSpawnRate, 5);
		get(CATEGORY_MOBS, KEY_manaCreeperSpawnRate, 3);
		get(CATEGORY_MOBS, KEY_darklingSpawnRate, 5);
		get(CATEGORY_MOBS, KEY_earthElementalSpawnRate, 2);
		get(CATEGORY_MOBS, KEY_fireElementalSpawnRate, 2);
		get(CATEGORY_MOBS, KEY_flickerSpawnRate, 2);
	}

	public int getConfigurableEnchantmentID(String enchantmentName, int default_value){
		int val = get(CATEGORY_ENCHANTMENTS, enchantmentName, default_value).getInt(default_value);
		save();
		return val;
	}

	public void updateConfigurableEnchantmentID(String enchantmentName, int new_value){
		get(CATEGORY_ENCHANTMENTS, enchantmentName, new_value).set(new_value);
		save();
	}

	public int GetHecateSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_hecateSpawnRate, 2);
		return Math.max(prop.getInt(2), 0);
	}

	public int GetMageSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_mageSpawnRate, 1);
		return Math.max(prop.getInt(1), 0);
	}

	public int GetWaterElementalSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_waterElementalSpawnRate, 3);
		return Math.max(prop.getInt(3), 0);
	}

	public int GetManaElementalSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_manaElementalSpawnRate, 2);
		return Math.max(prop.getInt(2), 0);
	}

	public int GetDryadSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_dryadSpawnRate, 5);
		return Math.max(prop.getInt(5), 0);
	}

	public int GetManaCreeperSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_manaCreeperSpawnRate, 3);
		return Math.max(prop.getInt(3), 0);
	}

	public int GetDarklingSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_darklingSpawnRate, 5);
		return Math.max(prop.getInt(5), 0);
	}

	public int GetEarthElementalSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_earthElementalSpawnRate, 2);
		return Math.max(prop.getInt(2), 0);
	}

	public int GetFireElementalSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_fireElementalSpawnRate, 2);
		return Math.max(prop.getInt(2), 0);
	}

	public int GetFlickerSpawnRate(){
		Property prop = get(CATEGORY_MOBS, KEY_flickerSpawnRate, 4);
		return Math.max(prop.getInt(4), 0);
	}

	public int getConfigurablePotionID(String id, int default_value){
		Property prop = get(CATEGORY_POTIONS, id, default_value);
		int val = prop.getInt(default_value);
		save();
		return val;
	}

	//====================================================================================
	// Setters
	//====================================================================================

	public void setAuraIndex(int index){
		if (index < 0) index = 0;
		if (index >= AMParticle.particleTypes.length) index = AMParticle.particleTypes.length - 1;

		Property prop = get(CATEGORY_BETA, KEY_AuraType, 15);
		prop.set(index);

		this.AuraType = index;
	}

	public void setAuraBehaviour(int index){
		if (index < 0) index = 0;
		if (index >= ParticleController.AuraControllerOptions.length)
			index = ParticleController.AuraControllerOptions.length - 1;

		Property prop = get(CATEGORY_BETA, KEY_AuraBehaviour, 0);
		prop.set(index);

		this.AuraBehaviour = index;
	}

	public void setAuraColorRandom(boolean value){
		Property prop = get(CATEGORY_BETA, KEY_AuraColorRandomize, false);
		prop.set(value);

		this.AuraRandomColor = value;
	}

	public void setAuraColorDefault(boolean value){
		Property prop = get(CATEGORY_BETA, KEY_AuraColorDefault, true);
		prop.set(value);

		this.AuraDefaultColor = value;
	}

	public void setAuraScale(float scale){
		if (scale < 1) scale = 1;
		if (scale > 200) scale = 200;
		Property prop = get(CATEGORY_BETA, KEY_AuraScale, 50D);
		prop.set(scale);

		this.AuraScale = scale;
	}

	public void setAuraColor(int color){
		Property prop = get(CATEGORY_BETA, KEY_AuraColor, 0xFFFFFF);
		prop.set(color);

		this.AuraColor = color;
	}

	public void setAuraAlpha(float alpha){
		if (alpha < 0) alpha = 0;
		if (alpha > 100) alpha = 100;
		Property prop = get(CATEGORY_BETA, KEY_AuraAlpha, 255D);
		prop.set(alpha);

		this.AuraAlpha = alpha;
	}

	public void setAuraQuantity(int quantity){
		if (quantity < 1) quantity = 1;
		else if (quantity > 5) quantity = 5;
		Property prop = get(CATEGORY_BETA, KEY_AuraAlpha, 2);
		prop.set(quantity);

		this.AuraQuantity = quantity;
	}

	public void setAuraDelay(int delay){
		if (delay < 1) delay = 1;
		else if (delay > 200) delay = 200;

		Property prop = get(CATEGORY_BETA, KEY_AuraDelay, 5);
		prop.set(delay);

		this.AuraDelay = delay;
	}

	public void setAuraSpeed(float speed){
		if (speed < 0.01f) speed = 0.01f;
		else if (speed > 10f) speed = 10f;

		Property prop = get(CATEGORY_BETA, KEY_AuraSpeed, 0.02f);
		prop.set(speed);

		this.AuraSpeed = speed;
	}

	public void setDisplayManaInInventory(boolean value){
		boolean def = !Loader.isModLoaded("NotEnoughItems");
		Property prop = get(CATEGORY_GENERAL, KEY_DisplayManaInInventory, def, "This will toggle mana display on and off in your inventory.  Default 'O' key in game.");
		prop.set(value);

		this.DisplayManaInInventory = value;
	}

	public void disableRetroactiveWorldgen(){
		Property prop = get(CATEGORY_GENERAL, KEY_RetroactiveWorldGen, false, "Set this to true to enable retroactive worldgen for Ars Magica structures and ores.  *WARNING* This may break your save!  Do a backup first!");
		prop.set(false);

		this.RetroWorldGen = false;
	}

	public void setGuiPositions(AMVector2 manaHud, AMVector2 burnoutHud, AMVector2 levelHud, AMVector2 affinityHud, AMVector2 posBuffsHud, AMVector2 negBuffsHud, AMVector2 armorHead, AMVector2 armorChest, AMVector2 armorLegs, AMVector2 armorBoots, AMVector2 xpBar, AMVector2 contingency, AMVector2 manaNumeric, AMVector2 burnoutNumeric, AMVector2 XPNumeric, AMVector2 spellBookPos, boolean showBuffs, boolean showNumerics, boolean minimalHud, boolean showArmorUI, boolean showXPAlways, boolean showHudBars){
		manaHudPosition = manaHud;
		burnoutHudPosition = burnoutHud;
		levelPosition = levelHud;
		affinityPosition = affinityHud;
		positiveBuffsPosition = posBuffsHud;
		negativeBuffsPosition = negBuffsHud;
		armorPositionHead = armorHead;
		armorPositionChest = armorChest;
		armorPositionLegs = armorLegs;
		armorPositionBoots = armorBoots;
		xpBarPosition = xpBar;
		contingencyPosition = contingency;
		manaNumericPosition = manaNumeric;
		burnoutNumericPosition = burnoutNumeric;
		XPNumericPosition = XPNumeric;
		SpellBookPosition = spellBookPos;
		this.showBuffs = showBuffs;
		this.showNumerics = showNumerics;
		this.showHudMinimally = minimalHud;
		this.showArmorUI = showArmorUI;
		this.showXPAlways = showXPAlways;
		this.showHudBars = showHudBars;
	}

	public void saveGuiPositions(){
		updateAMVector2(KEY_ManaHudPositionX, KEY_ManaHudPositionY, manaHudPosition);
		updateAMVector2(KEY_BurnoutHudPositionX, KEY_BurnoutHudPositionY, burnoutHudPosition);
		updateAMVector2(KEY_LevelPositionX, KEY_LevelPositionY, levelPosition);
		updateAMVector2(KEY_AffinityPositionX, KEY_AffinityPositionY, affinityPosition);
		updateAMVector2(KEY_BuffsPositivePositionX, KEY_BuffsPositivePositionY, positiveBuffsPosition);
		updateAMVector2(KEY_BuffsNegativePositionX, KEY_BuffsNegativePositionY, negativeBuffsPosition);
		updateAMVector2(KEY_ArmorPositionHeadX, KEY_ArmorPositionHeadY, armorPositionHead);
		updateAMVector2(KEY_ArmorPositionChestX, KEY_ArmorPositionChestY, armorPositionChest);
		updateAMVector2(KEY_ArmorPositionLegsX, KEY_ArmorPositionLegsY, armorPositionLegs);
		updateAMVector2(KEY_ArmorPositionBootsX, KEY_ArmorPositionBootsY, armorPositionBoots);
		updateAMVector2(KEY_XPBarPositionX, KEY_XPBarPositionY, xpBarPosition);
		updateAMVector2(KEY_ContingencyPositionX, KEY_ContingencyPositionY, contingencyPosition);
		updateAMVector2(KEY_ManaNumericPositionX, KEY_ManaNumericPositionY, manaNumericPosition);
		updateAMVector2(KEY_BurnoutNumericPositionX, KEY_BurnoutNumericPositionY, burnoutNumericPosition);
		updateAMVector2(KEY_XPNumericPositionX, KEY_XPNumericPositionY, XPNumericPosition);
		updateAMVector2(KEY_SpellBookPositionX, KEY_SpellBookPositionY, SpellBookPosition);

		Property buffProp;
		buffProp = get(CATEGORY_UI, KEY_ShowBuffs, true);
		buffProp.set(showBuffs);

		Property numProp;
		numProp = get(CATEGORY_UI, KEY_ShowNumerics, false);
		numProp.set(showNumerics);

		Property armorProp;
		armorProp = get(CATEGORY_UI, KEY_ShowArmorUI, true);
		armorProp.set(showArmorUI);

		Property minimalProp;
		minimalProp = get(CATEGORY_UI, KEY_ShowHudMinimally, false);
		minimalProp.set(showHudMinimally);

		Property xpShow;
		xpShow = get(CATEGORY_UI, KEY_ShowXPAlways, false);
		xpShow.set(showXPAlways);

		Property barShow;
		barShow = get(CATEGORY_UI, KEY_ShowHUDBars, true);
		barShow.set(showHudBars);

		save();
	}

	public void setSkillTreeSecondaryTierCap(int skillTreeLock){
		this.secondarySkillTreeTierCap = skillTreeLock;
	}

	private void updateAMVector2(String keyX, String keyY, AMVector2 value){
		Property prop;
		prop = get(CATEGORY_UI, keyX, 0);
		prop.set(value.x);

		prop = get(CATEGORY_UI, keyY, 0);
		prop.set(value.y);
	}

	public void setManaCap(double cap){
		this.manaCap = cap;
	}
}
