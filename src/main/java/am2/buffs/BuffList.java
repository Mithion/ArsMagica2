package am2.buffs;

import am2.AMCore;
import am2.LogHelper;
import am2.api.potion.IBuffHelper;
import am2.particles.AMParticle;
import am2.particles.ParticleLiveForBuffDuration;
import am2.texture.ResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.potion.Potion;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuffList implements IBuffHelper{
	//buff "potions"
	public static ArsMagicaPotion waterBreathing;
	public static ArsMagicaPotion flight;
	public static ArsMagicaPotion slowfall;
	public static ArsMagicaPotion haste;
	public static ArsMagicaPotion trueSight;
	public static ArsMagicaPotion regeneration;
	public static ArsMagicaPotion magicShield;
	public static ArsMagicaPotion charmed;
	public static ArsMagicaPotion frostSlowed;
	public static ArsMagicaPotion temporalAnchor;
	public static ArsMagicaPotion manaRegen;
	public static ArsMagicaPotion entangled;
	public static ArsMagicaPotion wateryGrave;
	public static ArsMagicaPotion spellReflect;
	public static ArsMagicaPotion silence;
	public static ArsMagicaPotion swiftSwim;
	public static ArsMagicaPotion agility;
	public static ArsMagicaPotion leap;
	public static ArsMagicaPotion gravityWell;
	public static ArsMagicaPotion astralDistortion;
	public static ArsMagicaPotion levitation;
	public static ArsMagicaPotion clarity;
	public static ArsMagicaPotion illumination;
	public static ArsMagicaPotion manaBoost;
	public static ArsMagicaPotion manaShield;
	public static ArsMagicaPotion fury;
	public static ArsMagicaPotion scrambleSynapses;
	public static ArsMagicaPotion shrink;
	public static ArsMagicaPotion burnoutReduction;

	public static ArsMagicaPotion greaterManaPotion;
	public static ArsMagicaPotion epicManaPotion;
	public static ArsMagicaPotion legendaryManaPotion;

	private static final int maxParticlesPerBuff = 100;
	public static final int default_buff_duration = 600;
	private static int potionDefaultOffset = 0;
	public static HashMap<Integer, Integer> particlesForBuffID;
	private static HashMap<String, Class> classesForBuffID;

	//since sync code only applies the PotionEffect class, any inherited bonuses are lost.
	//These dummy buffs can be used to apply the tick of the potion when the class type
	//isn't transferred.
	private static final HashMap<String, BuffEffect> utilityBuffs = new HashMap<String, BuffEffect>();
	private static final ArrayList<ArsMagicaPotion> arsMagicaPotions = new ArrayList<ArsMagicaPotion>();
	private static ArrayList<ResourceLocation> dispelBlacklist;
	

	public static final BuffList instance = new BuffList();

	private static HashMap<String, Potion> ourInitialPotionAllocations;

	private BuffList(){

	}

	private static ArsMagicaPotion createAMPotion(String registryName, String name, int IIconRow, int iconCol, boolean isBadEffect, Class buffEffectClass){
		LogHelper.info("Potion %s is ID %s", name, registryName);
		ArsMagicaPotion potion = new ArsMagicaPotion(new ResourceLocation("arsmagica2", registryName), isBadEffect, 0x000000);
		potion.setPotionName(name);
		potion._setIconIndex(iconCol, IIconRow);
		classesForBuffID.put(registryName, buffEffectClass);
		arsMagicaPotions.add(potion);
		ourInitialPotionAllocations.put(registryName, potion);
		
		return potion;
	}

	private static ManaPotion createManaPotion(String registryName, String name, int IIconRow, int iconCol, boolean isBadEffect, int colour){
		LogHelper.info("Potion %s is ID %s", name, registryName);
		ManaPotion potion = new ManaPotion(new ResourceLocation("arsmagica2", registryName), isBadEffect, colour);
		potion.setPotionName(name);
		potion._setIconIndex(iconCol, IIconRow);
		ourInitialPotionAllocations.put(registryName, potion);
		
		return potion;
	}

	private static void createDummyBuff(Class buffEffectClass, String name){
		try{
			Constructor ctor = buffEffectClass.getConstructor(Integer.TYPE, Integer.TYPE);
			BuffEffect utilityBuff = (BuffEffect)ctor.newInstance(0, 0);
			utilityBuffs.put(name, utilityBuff);
		}catch (Throwable e){
			e.printStackTrace();
		}
	}

	public static void Init(){
		dispelBlacklist = new ArrayList<ResourceLocation>();
		particlesForBuffID = new HashMap<Integer, Integer>();
		classesForBuffID = new HashMap<String, Class>();
		ourInitialPotionAllocations = new HashMap<String, Potion>();

		int numBuffs = Potion.potionTypes.length;

		for (int i = 0; i < numBuffs; ++i){
			particlesForBuffID.put(i, 0);
		}
	}

	public static void postInit(){
	}

	public static void Instantiate(){
		waterBreathing = createAMPotion("water_breathing", "Water Breathing", 0, 0, false, BuffEffectWaterBreathing.class);
		flight = createAMPotion("flight", "Flight", 0, 1, false, BuffEffectFlight.class);
		slowfall = createAMPotion("slowfall", "Feather Fall", 0, 2, false, BuffEffectSlowfall.class);
		haste = createAMPotion("haste", "Haste", 0, 3, false, BuffEffectHaste.class);
		trueSight = createAMPotion("true_sight", "True Sight", 0, 4, false, BuffEffectTrueSight.class);
		regeneration = createAMPotion("regeneration", "Regeneration", 0, 6, false, BuffEffectRegeneration.class);
		magicShield = createAMPotion("magic_shield", "Magic Shield", 1, 1, false, BuffEffectMagicShield.class);
		charmed = createAMPotion("charmed", "Charmed", 1, 2, true, BuffEffectCharmed.class);
		frostSlowed = createAMPotion("frost_slow", "Frost Slow", 1, 3, true, BuffEffectFrostSlowed.class);
		temporalAnchor = createAMPotion("chrono_anchor", "Chrono Anchor", 1, 4, false, BuffEffectTemporalAnchor.class);
		manaRegen = createAMPotion("mana_regen", "Mana Regen", 1, 5, false, BuffEffectManaRegen.class);
		entangled = createAMPotion("entangled", "Entangled", 1, 7, true, BuffEffectEntangled.class);
		wateryGrave = createAMPotion("watery_grave", "Watery Grave", 2, 0, true, BuffEffectWateryGrave.class);
		spellReflect = createAMPotion("spell_reflect", "Spell Reflect", 2, 3, false, BuffEffectSpellReflect.class);
		silence = createAMPotion("silence", "Silence", 2, 6, true, BuffEffectSilence.class);
		swiftSwim = createAMPotion("swift_swim", "Swift Swim", 2, 7, false, BuffEffectSwiftSwim.class);
		agility = createAMPotion("agility", "Agility", 0, 0, false, BuffEffectAgility.class);
		leap = createAMPotion("leap", "Leap", 0, 2, false, BuffEffectLeap.class);
		manaBoost = createAMPotion("mana_boost", "Mana Boost", 1, 0, false, BuffMaxManaIncrease.class);
		astralDistortion = createAMPotion("astral_distortion", "Astral Distortion", 0, 4, true, BuffEffectAstralDistortion.class);
		manaShield = createAMPotion("mana_shield", "Mana Shield", 0, 7, false, BuffEffectManaShield.class);
		fury = createAMPotion("fury", "Fury", 1, 6, false, BuffEffectFury.class);
		scrambleSynapses = createAMPotion("scramble_synapses", "Scramble Synapses", 1, 7, true, BuffEffectScrambleSynapses.class);
		illumination = createAMPotion("illuminated", "Illuminated", 1, 0, false, BuffEffectIllumination.class);
		
		greaterManaPotion = createManaPotion("mana_restoration_greater", "Greater Mana Restoration", 0, 1, false, 0x40c6be);
		// greaterManaPotion = new ManaPotion(potionDefaultOffset + 24, false, 0x40c6be);
		// greaterManaPotion.setPotionName("Greater Mana Restoration");
		// greaterManaPotion._setIconIndex(0, 1);

		epicManaPotion = createManaPotion("mana_restoration_epic", "Epic Mana Restoration", 0, 1, false, 0xFF00FF);
		// epicManaPotion = new ManaPotion(potionDefaultOffset + 25, false, 0xFF00FF);
		// epicManaPotion.setPotionName("Epic Mana Restoration");
		// epicManaPotion._setIconIndex(0, 1);

		legendaryManaPotion = createManaPotion("mana_restoration_legendary", "Legendary Mana Restoration", 0, 1, false, 0xFFFF00);
		// legendaryManaPotion = new ManaPotion(potionDefaultOffset + 26, false, 0xFFFF00);
		// legendaryManaPotion.setPotionName("Legendary Mana Restoration");
		// legendaryManaPotion._setIconIndex(0, 1);

		gravityWell = createAMPotion("gravity_well", "Gravity Well", 0, 6, true, BuffEffectGravityWell.class);
		levitation = createAMPotion("levitation", "Levitation", 0, 7, false, BuffEffectLevitation.class);

		clarity = createAMPotion("clarity", "Clarity", 0, 5, false, BuffEffectClarity.class);
		shrink = createAMPotion("shrunken", "Shrunken", 0, 5, false, BuffEffectShrink.class);
		burnoutReduction = createAMPotion("burnout_reduction", "Burnout Redux", 1, 1, false, BuffEffectBurnoutReduction.class);

		for (Map.Entry<String, Class> e : classesForBuffID.entrySet()){
			createDummyBuff(e.getValue(), e.getKey());
		}
	}

	public static void setupTextureOverrides(){
		waterBreathing.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		flight.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		slowfall.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		haste.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		trueSight.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		regeneration.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		magicShield.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		charmed.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		frostSlowed.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		temporalAnchor.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		manaRegen.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		entangled.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		wateryGrave.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		;
		spellReflect.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		silence.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		swiftSwim.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		clarity.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		manaShield.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		manaBoost.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));
		fury.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_1.png"));

		agility.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
		leap.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
		astralDistortion.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
		gravityWell.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
		levitation.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
		illumination.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
		scrambleSynapses.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
		shrink.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
		burnoutReduction.setTextureSheet(ResourceManager.GetGuiTexturePath("buffs_2.png"));
	}
	
	public static boolean IDIsAMBuff(int potionID){
		for (ArsMagicaPotion i : arsMagicaPotions){
			if (i.id == potionID){
				return true;
			}
		}
		return false;
	}

	public static boolean addParticleToBuff(AMParticle particle, EntityLiving ent, int priority, boolean exclusive, int buffID){
		if (particlesForBuffID.get(buffID) >= maxParticlesPerBuff){
			return false;
		}
		int count = particlesForBuffID.get(buffID);
		count++;
		particlesForBuffID.put(buffID, count);
		particle.AddParticleController(new ParticleLiveForBuffDuration(particle, ent, buffID, priority, exclusive));
		return true;
	}

	public static BuffEffect buffEffectFromPotionID(int potionID, int duration, int amplifier){
		Class _class = classesForBuffID.get(potionID);
		if (_class == null) return null;

		Constructor buffMaker = _class.getDeclaredConstructors()[0];
		try{
			buffMaker.setAccessible(true);
			BuffEffect p = (BuffEffect)buffMaker.newInstance(duration, amplifier);
			return p;
		}catch (InstantiationException e){
			LogHelper.error("Could not create potion: " + e.getMessage());
		}catch (IllegalAccessException e){
			LogHelper.error("Could not create potion: " + e.getMessage());
		}catch (IllegalArgumentException e){
			LogHelper.error("Could not create potion: " + e.getMessage());
		}catch (InvocationTargetException e){
			LogHelper.error("Could not create potion: " + e.getMessage());
		}
		return null;
	}

	public static void buffEnding(int buffID){
		particlesForBuffID.put(buffID, 0);
	}

	@Override
	public void addDispelExclusion(ResourceLocation loc){
		if (dispelBlacklist.contains(loc)){
			LogHelper.info("Id %s was already on the dispel blacklist; skipping.", loc);
		}else{
			LogHelper.info("Added %s to the dispel blacklist.", loc);
			dispelBlacklist.add(loc);
		}
	}

	public static boolean isDispelBlacklisted(int id){
		return dispelBlacklist.contains(id);
	}
}
