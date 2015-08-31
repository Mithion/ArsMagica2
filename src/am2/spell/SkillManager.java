package am2.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.util.StatCollector;
import am2.api.spell.ISpellPartManager;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellPart;
import am2.api.spell.component.interfaces.ISpellShape;
import am2.skills.AffinityGainsBoost;
import am2.skills.AugmentedCasting;
import am2.skills.ExtraSummon;
import am2.skills.MagePosseI;
import am2.skills.MagePosseII;
import am2.skills.ManaRegenBoostI;
import am2.skills.ManaRegenBoostII;
import am2.skills.ManaRegenBoostIII;
import am2.skills.SpellMovement;
import am2.spell.components.Accelerate;
import am2.spell.components.Appropriation;
import am2.spell.components.AstralDistortion;
import am2.spell.components.Attract;
import am2.spell.components.BanishRain;
import am2.spell.components.Blind;
import am2.spell.components.Blink;
import am2.spell.components.Blizzard;
import am2.spell.components.Charm;
import am2.spell.components.ChronoAnchor;
import am2.spell.components.CreateWater;
import am2.spell.components.Daylight;
import am2.spell.components.Dig;
import am2.spell.components.Disarm;
import am2.spell.components.Dispel;
import am2.spell.components.DivineIntervention;
import am2.spell.components.Drought;
import am2.spell.components.Drown;
import am2.spell.components.EnderIntervention;
import am2.spell.components.Entangle;
import am2.spell.components.FallingStar;
import am2.spell.components.FireDamage;
import am2.spell.components.FireRain;
import am2.spell.components.Flight;
import am2.spell.components.Fling;
import am2.spell.components.Forge;
import am2.spell.components.Freeze;
import am2.spell.components.FrostDamage;
import am2.spell.components.Fury;
import am2.spell.components.GravityWell;
import am2.spell.components.Grow;
import am2.spell.components.HarvestPlants;
import am2.spell.components.Haste;
import am2.spell.components.Heal;
import am2.spell.components.Ignition;
import am2.spell.components.Invisiblity;
import am2.spell.components.Knockback;
import am2.spell.components.Leap;
import am2.spell.components.Levitation;
import am2.spell.components.LifeDrain;
import am2.spell.components.LifeTap;
import am2.spell.components.Light;
import am2.spell.components.LightningDamage;
import am2.spell.components.MagicDamage;
import am2.spell.components.ManaDrain;
import am2.spell.components.ManaLink;
import am2.spell.components.ManaShield;
import am2.spell.components.Mark;
import am2.spell.components.MeltArmor;
import am2.spell.components.MissingComponent;
import am2.spell.components.Moonrise;
import am2.spell.components.Nauseate;
import am2.spell.components.NightVision;
import am2.spell.components.PhysicalDamage;
import am2.spell.components.PlaceBlock;
import am2.spell.components.Plant;
import am2.spell.components.Plow;
import am2.spell.components.RandomTeleport;
import am2.spell.components.Recall;
import am2.spell.components.Reflect;
import am2.spell.components.Regeneration;
import am2.spell.components.Repel;
import am2.spell.components.Rift;
import am2.spell.components.ScrambleSynapses;
import am2.spell.components.Shield;
import am2.spell.components.Shrink;
import am2.spell.components.Silence;
import am2.spell.components.Slow;
import am2.spell.components.Slowfall;
import am2.spell.components.Storm;
import am2.spell.components.Summon;
import am2.spell.components.SwiftSwim;
import am2.spell.components.Telekinesis;
import am2.spell.components.Transplace;
import am2.spell.components.TrueSight;
import am2.spell.components.WaterBreathing;
import am2.spell.components.WateryGrave;
import am2.spell.components.WizardsAutumn;
import am2.spell.modifiers.Bounce;
import am2.spell.modifiers.BuffPower;
import am2.spell.modifiers.Colour;
import am2.spell.modifiers.Damage;
import am2.spell.modifiers.Dismembering;
import am2.spell.modifiers.Duration;
import am2.spell.modifiers.FeatherTouch;
import am2.spell.modifiers.Gravity;
import am2.spell.modifiers.Healing;
import am2.spell.modifiers.Lunar;
import am2.spell.modifiers.MiningPower;
import am2.spell.modifiers.MissingModifier;
import am2.spell.modifiers.Piercing;
import am2.spell.modifiers.Prosperity;
import am2.spell.modifiers.Radius;
import am2.spell.modifiers.Range;
import am2.spell.modifiers.RuneProcs;
import am2.spell.modifiers.Solar;
import am2.spell.modifiers.Speed;
import am2.spell.modifiers.TargetNonSolidBlocks;
import am2.spell.modifiers.VelocityAdded;
import am2.spell.shapes.AoE;
import am2.spell.shapes.Beam;
import am2.spell.shapes.Binding;
import am2.spell.shapes.Chain;
import am2.spell.shapes.Channel;
import am2.spell.shapes.Contingency_Death;
import am2.spell.shapes.Contingency_Fall;
import am2.spell.shapes.Contingency_Fire;
import am2.spell.shapes.Contingency_Health;
import am2.spell.shapes.Contingency_Hit;
import am2.spell.shapes.MissingShape;
import am2.spell.shapes.Projectile;
import am2.spell.shapes.Rune;
import am2.spell.shapes.Self;
import am2.spell.shapes.Touch;
import am2.spell.shapes.Wall;
import am2.spell.shapes.Wave;
import am2.spell.shapes.Zone;
import cpw.mods.fml.common.FMLLog;

public class SkillManager implements ISpellPartManager{

	public static final int COMPONENT_OFFSET = 1000;
	public static final int MODIFIER_OFFSET = 5000;
	public static final int TALENT_OFFSET = 6000;

	private final HashMap<Integer, ISkillTreeEntry> registeredParts;
	private final CaseInsensitiveMap<String, Integer> registeredPartNames;
	private final HashMap<Integer, String> reversedPartNames;

	public static SkillManager instance = new SkillManager();

	public ISpellShape missingShape;
	public ISpellComponent missingComponent;
	public ISpellModifier missingModifier;

	private SkillManager(){
		registeredParts = new HashMap<Integer, ISkillTreeEntry>();
		registeredPartNames = new CaseInsensitiveMap<String, Integer>();
		reversedPartNames = new HashMap<Integer, String>();

		missingComponent = new MissingComponent();
		missingModifier = new MissingModifier();
		missingShape = new MissingShape();
	}

	public ArrayList<Integer> getAllShapes(){
		ArrayList<Integer> shapes = new ArrayList<Integer>();
		for (Integer i : registeredParts.keySet()){
			if (registeredParts.get(i) instanceof ISpellShape){
				shapes.add(i);
			}
		}
		return shapes;
	}

	public ArrayList<Integer> getAllComponents(){
		ArrayList<Integer> components = new ArrayList<Integer>();
		for (Integer i : registeredParts.keySet()){
			if (registeredParts.get(i) instanceof ISpellComponent){
				components.add(i);
			}
		}
		return components;
	}

	public ArrayList<Integer> getAllModifiers(){
		ArrayList<Integer> modifiers = new ArrayList<Integer>();
		for (Integer i : registeredParts.keySet()){
			if (registeredParts.get(i) instanceof ISpellModifier){
				modifiers.add(i);
			}
		}
		return modifiers;
	}

	public ArrayList<Integer> getAllTalents(){
		ArrayList<Integer> modifiers = new ArrayList<Integer>();
		for (Integer i : registeredParts.keySet()){
			if (!(registeredParts.get(i) instanceof ISpellShape) && !(registeredParts.get(i) instanceof ISpellComponent) && !(registeredParts.get(i) instanceof ISpellModifier)){
				modifiers.add(i);
			}
		}
		return modifiers;
	}

	public Set<String> getAllSkillNames() {
		return registeredPartNames.keySet();
	}

	@Override
	public int registerSkillTreeEntry(ISkillTreeEntry part, String name){

		int id = part.getID();

		if (part instanceof ISpellComponent)
			id += COMPONENT_OFFSET;
		else if (part instanceof ISpellModifier)
			id += MODIFIER_OFFSET;
		else if (!(part instanceof ISpellShape))
			id += TALENT_OFFSET;

		if (registeredParts.containsKey(id)){
			String existing = reversedPartNames.get(id);
			FMLLog.info("Ars Magica >> Attempted to register duplicate spell part name, %s (which would overwrite %s).  The part was NOT registered.", name, existing);
			return -1;
		}


		registeredParts.put(id, part);
		registeredPartNames.put(name, id);
		reversedPartNames.put(id, name);

		if (part instanceof ISpellPart)
			SpellRecipeManager.instance.RegisterRecipe((ISpellPart)part);

		return id;
	}

	@Override
	public ISkillTreeEntry getSkill(int id){
		ISkillTreeEntry component = registeredParts.get(id);
		if (component == null) return missingComponent;
		return component;
	}

	public ISpellModifier getModifier(int id){
		ISkillTreeEntry mod = registeredParts.get(id);
		if (mod == null || !(mod instanceof ISpellModifier)) return missingModifier;
		return (ISpellModifier) mod;
	}

	public ISpellShape getShape(int id){
		ISkillTreeEntry mod = registeredParts.get(id);
		if (mod == null || !(mod instanceof ISpellShape)) return missingShape;
		return (ISpellShape) mod;
	}

	public ISpellComponent getComponent(int id){
		ISkillTreeEntry mod = registeredParts.get(id);
		if (mod == null || !(mod instanceof ISpellComponent)) return missingComponent;
		return (ISpellComponent) mod;
	}

	@Override
	public ISkillTreeEntry getSkill(String name) {
		Integer ID = registeredPartNames.get(name);
		if (ID == null) return null;
		return registeredParts.get(ID);
	}

	public String getSkillName(ISkillTreeEntry component){
		int id = component.getID();
		if (component instanceof ISpellComponent)
			id += COMPONENT_OFFSET;
		else if (component instanceof ISpellModifier)
			id += MODIFIER_OFFSET;
		else if (!(component instanceof ISpellShape))
			id += TALENT_OFFSET;
		return reversedPartNames.get(id);
	}

	public int getSpellPartID(Class clazz){
		for (Integer id : registeredParts.keySet()){
			ISkillTreeEntry entry = registeredParts.get(id);
			if (clazz.isAssignableFrom(entry.getClass()))
				return id;
		}
		FMLLog.info("Ars Magica >> Spell Part not found!");
		return -1;
	}

	public int getShiftedPartID(ISkillTreeEntry part){
		if (part instanceof ISpellShape) return part.getID();
		if (part instanceof ISpellComponent) return part.getID() + COMPONENT_OFFSET;
		if (part instanceof ISpellModifier) return part.getID() + MODIFIER_OFFSET;
		return part.getID() + TALENT_OFFSET;
	}

	public void init() {
		//Shapes		
		registerSkillTreeEntry(new AoE(), "AoE");
		registerSkillTreeEntry(new Beam(), "Beam");
		registerSkillTreeEntry(new Binding(), "Binding");
		registerSkillTreeEntry(new Chain(), "Chain");
		registerSkillTreeEntry(new Channel(), "Channel");
		registerSkillTreeEntry(new Projectile(), "Projectile");
		registerSkillTreeEntry(new Rune(), "Rune");
		registerSkillTreeEntry(new Self(), "Self");
		registerSkillTreeEntry(new Summon(), "Summon");
		registerSkillTreeEntry(new Touch(), "Touch");
		registerSkillTreeEntry(new Zone(), "Zone");
		registerSkillTreeEntry(new Contingency_Fall(), "Contingency_Fall");
		registerSkillTreeEntry(new Contingency_Hit(), "Contingency_Damage");
		registerSkillTreeEntry(new Contingency_Fire(), "Contingency_Fire");
		registerSkillTreeEntry(new Contingency_Health(), "Contingency_Health");
		registerSkillTreeEntry(new Contingency_Death(), "Contingency_Death");
		registerSkillTreeEntry(new Wall(), "Wall");
		registerSkillTreeEntry(new Wave(), "Wave");

		//Components
		registerSkillTreeEntry(new Accelerate(), "Accelerate");
		registerSkillTreeEntry(new AstralDistortion(), "AstralDistortion");
		registerSkillTreeEntry(new Attract(), "Attract");
		registerSkillTreeEntry(new BanishRain(), "BanishRain");
		registerSkillTreeEntry(new Blind(), "Blind");
		registerSkillTreeEntry(new Blink(), "Blink");
		registerSkillTreeEntry(new ChronoAnchor(), "ChronoAnchor");
		registerSkillTreeEntry(new CreateWater(), "CreateWater");
		registerSkillTreeEntry(new Dig(), "Dig");
		registerSkillTreeEntry(new Disarm(), "Disarm");
		registerSkillTreeEntry(new Dispel(), "Dispel");
		registerSkillTreeEntry(new DivineIntervention(), "DivineIntervention");
		registerSkillTreeEntry(new Drought(), "Drought");
		registerSkillTreeEntry(new EnderIntervention(), "EnderIntervention");
		registerSkillTreeEntry(new Entangle(), "Entangle");
		registerSkillTreeEntry(new FireDamage(), "FireDamage");
		registerSkillTreeEntry(new Flight(), "Flight");
		registerSkillTreeEntry(new Fling(), "Fling");
		registerSkillTreeEntry(new Forge(), "Forge");
		registerSkillTreeEntry(new Freeze(), "Freeze");
		registerSkillTreeEntry(new FrostDamage(), "FrostDamage");
		registerSkillTreeEntry(new GravityWell(), "GravityWell");
		registerSkillTreeEntry(new Grow(), "Grow");
		registerSkillTreeEntry(new HarvestPlants(), "HarvestPlants");
		registerSkillTreeEntry(new Haste(), "Haste");
		registerSkillTreeEntry(new Heal(), "Heal");
		registerSkillTreeEntry(new Ignition(), "Ignition");
		registerSkillTreeEntry(new Invisiblity(), "Invisibility");
		registerSkillTreeEntry(new Knockback(), "Knockback");
		registerSkillTreeEntry(new Leap(), "Leap");
		registerSkillTreeEntry(new Levitation(), "Levitate");
		registerSkillTreeEntry(new LifeDrain(), "LifeDrain");
		registerSkillTreeEntry(new LifeTap(), "LifeTap");
		registerSkillTreeEntry(new Light(), "Light");
		registerSkillTreeEntry(new LightningDamage(), "LightningDamage");
		registerSkillTreeEntry(new MagicDamage(), "MagicDamage");
		registerSkillTreeEntry(new ManaDrain(), "ManaDrain");
		registerSkillTreeEntry(new Mark(), "Mark");
		registerSkillTreeEntry(new NightVision(), "NightVision");
		registerSkillTreeEntry(new PhysicalDamage(), "PhysicalDamage");
		registerSkillTreeEntry(new Plant(), "Plant");
		registerSkillTreeEntry(new Plow(), "Plow");
		registerSkillTreeEntry(new RandomTeleport(), "RandomTeleport");
		registerSkillTreeEntry(new Recall(), "Recall");
		registerSkillTreeEntry(new Reflect(), "Reflect");
		registerSkillTreeEntry(new Regeneration(), "Regeneration");
		registerSkillTreeEntry(new Repel(), "Repel");
		registerSkillTreeEntry(new Rift(), "Rift");
		registerSkillTreeEntry(new Shield(), "Shield");
		registerSkillTreeEntry(new Slow(), "Slow");
		registerSkillTreeEntry(new Slowfall(), "Slowfall");
		registerSkillTreeEntry(new Storm(), "Storm");
		registerSkillTreeEntry(new SwiftSwim(), "SwiftSwim");
		registerSkillTreeEntry(new Telekinesis(), "Telekinesis");
		registerSkillTreeEntry(new Transplace(), "Transplace");
		registerSkillTreeEntry(new TrueSight(), "TrueSight");
		registerSkillTreeEntry(new WaterBreathing(), "WaterBreathing");
		registerSkillTreeEntry(new WateryGrave(), "WateryGrave");
		registerSkillTreeEntry(new Charm(), "Charm");
		registerSkillTreeEntry(new MeltArmor(), "MeltArmor");
		registerSkillTreeEntry(new Drown(), "Drown");
		registerSkillTreeEntry(new Blizzard(), "Blizzard");
		registerSkillTreeEntry(new Daylight(), "Daylight");
		registerSkillTreeEntry(new FallingStar(), "FallingStar");
		registerSkillTreeEntry(new FireRain(), "FireRain");
		registerSkillTreeEntry(new ManaLink(), "ManaLink");
		registerSkillTreeEntry(new ManaShield(), "ManaShield");
		registerSkillTreeEntry(new Moonrise(), "Moonrise");
		registerSkillTreeEntry(new WizardsAutumn(), "WizardsAutumn");
		registerSkillTreeEntry(new Appropriation(), "Appropriation");
		registerSkillTreeEntry(new Fury(), "Fury");
		registerSkillTreeEntry(new Silence(), "Silence");
		registerSkillTreeEntry(new ScrambleSynapses(), "ScrambleSynapses");
		registerSkillTreeEntry(new PlaceBlock(), "PlaceBlock");
		registerSkillTreeEntry(new Shrink(), "Shrink");
		registerSkillTreeEntry(new Nauseate(), "Nauseate");

		//Modifiers
		registerSkillTreeEntry(new Bounce(), "Bounce");
		registerSkillTreeEntry(new Speed(), "Speed");
		registerSkillTreeEntry(new Gravity(), "Gravity");
		registerSkillTreeEntry(new Damage(), "Damage");
		registerSkillTreeEntry(new Healing(), "Healing");
		registerSkillTreeEntry(new VelocityAdded(), "VelocityAdded");
		registerSkillTreeEntry(new Radius(), "Radius");
		registerSkillTreeEntry(new Duration(), "Duration");
		registerSkillTreeEntry(new RuneProcs(), "RuneProcs");
		registerSkillTreeEntry(new Range(), "Range");
		registerSkillTreeEntry(new Lunar(), "Lunar");
		registerSkillTreeEntry(new TargetNonSolidBlocks(), "TargetNonSolid");
		registerSkillTreeEntry(new Solar(), "Solar");
		registerSkillTreeEntry(new Piercing(), "Piercing");
		registerSkillTreeEntry(new Colour(), "Colour");
		registerSkillTreeEntry(new MiningPower(), "MiningPower");
		registerSkillTreeEntry(new Prosperity(), "Prosperity");
		registerSkillTreeEntry(new BuffPower(), "BuffPower");
		registerSkillTreeEntry(new Dismembering(), "Dismembering");
		registerSkillTreeEntry(new FeatherTouch(), "FeatherTouch");

		//Skills
		registerSkillTreeEntry(new AffinityGainsBoost(), "AffinityGains");
		registerSkillTreeEntry(new AugmentedCasting(), "AugmentedCasting");
		registerSkillTreeEntry(new ExtraSummon(), "ExtraSummon");
		registerSkillTreeEntry(new MagePosseI(), "MageBandI");
		registerSkillTreeEntry(new MagePosseII(), "MageBandII");
		registerSkillTreeEntry(new ManaRegenBoostI(), "ManaRegenI");
		registerSkillTreeEntry(new ManaRegenBoostII(), "ManaRegenII");
		registerSkillTreeEntry(new ManaRegenBoostIII(), "ManaRegenIII");
		registerSkillTreeEntry(new SpellMovement(), "SpellMotion");
	}

	public String getDisplayName(ISkillTreeEntry part) {
		return StatCollector.translateToLocal(String.format("am2.spell.%s", getSkillName(part).toLowerCase()));
	}

	public String getDisplayName(String unlocalizedName) {
		return StatCollector.translateToLocal(String.format("am2.spell.%s", unlocalizedName.toLowerCase()));
	}

	public class CaseInsensitiveMap<K extends String, V> extends HashMap<K, V> {

		@Override
		public V put(K key, V value) {
			return super.put((K) key.toLowerCase(), value);
		}

		// not @Override because that would require the key parameter to be of type Object
		public V get(String key) {
			return super.get(key.toLowerCase());
		}
	}
}
