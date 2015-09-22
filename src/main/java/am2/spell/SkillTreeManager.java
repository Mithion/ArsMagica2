package am2.spell;

import am2.AMCore;
import am2.LogHelper;
import am2.api.SkillTreeEntry;
import am2.api.spell.ISkillTreeManager;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.enums.SkillPointTypes;
import am2.api.spell.enums.SkillTrees;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

public class SkillTreeManager implements ISkillTreeManager{

	private final ArrayList<SkillTreeEntry> offenseTree = new ArrayList<SkillTreeEntry>();
	private final ArrayList<SkillTreeEntry> defenseTree = new ArrayList<SkillTreeEntry>();
	private final ArrayList<SkillTreeEntry> utilityTree = new ArrayList<SkillTreeEntry>();
	private final ArrayList<SkillTreeEntry> talentTree = new ArrayList<SkillTreeEntry>();
	private final HashMap<Integer, SkillPointTypes> skillPointTypeList;

	private int highestSkillDepth = 0;

	public static final SkillTreeManager instance = new SkillTreeManager();

	private SkillTreeManager(){
		skillPointTypeList = new HashMap<Integer, SkillPointTypes>();
	}

	public SkillPointTypes getSkillPointTypeForPart(ISkillTreeEntry part){
		int id = SkillManager.instance.getShiftedPartID(part);
		return getSkillPointTypeForPart(id);
	}

	public SkillPointTypes getSkillPointTypeForPart(int id){
		return skillPointTypeList.get(id);
	}

	@Override
	public void RegisterPart(ISkillTreeEntry item, int x, int y, SkillTrees tree, SkillPointTypes requiredPoint, ISkillTreeEntry... prerequisites){

		//get appropriate skill tree
		ArrayList<SkillTreeEntry> treeListing = tree == SkillTrees.Defense ? defenseTree : tree == SkillTrees.Offense ? offenseTree : tree == SkillTrees.Utility ? utilityTree : talentTree;

		//check for prereq
		ArrayList<SkillTreeEntry> locatedPrerequisites = new ArrayList<SkillTreeEntry>();
		if (prerequisites != null && prerequisites.length > 0){
			for (Object prerequisite : prerequisites){
				for (SkillTreeEntry entry : treeListing){
					if (entry.registeredItem == prerequisite){
						locatedPrerequisites.add(entry);
						break;
					}
				}
			}

			if (locatedPrerequisites.size() == 0)
				throw new InvalidParameterException(String.format("Unable to locate one or more prerequisite items in the specified tree (%s).", tree.toString()));
		}

		boolean enabled = true;
		if (item == null){
			item = SkillManager.instance.missingComponent;
		}else{
			enabled = AMCore.skillConfig.isSkillEnabled(SkillManager.instance.getSkillName(item));
		}

		SkillTreeEntry newEntry = new SkillTreeEntry(x, y, tree, locatedPrerequisites.toArray(new SkillTreeEntry[locatedPrerequisites.size()]), item, enabled);
		treeListing.add(newEntry);

		skillPointTypeList.put(SkillManager.instance.getShiftedPartID(item), requiredPoint);
	}

	@SuppressWarnings("incomplete-switch")
	public ArrayList<SkillTreeEntry> getTree(SkillTrees tree){
		ArrayList<SkillTreeEntry> safeCopy = new ArrayList<SkillTreeEntry>();
		switch (tree){
		case Defense:
			safeCopy.addAll(defenseTree);
			break;
		case Offense:
			safeCopy.addAll(offenseTree);
			break;
		case Utility:
			safeCopy.addAll(utilityTree);
			break;
		case Talents:
			safeCopy.addAll(talentTree);
		}

		return safeCopy;
	}

	public SkillTreeEntry getSkillTreeEntry(ISkillTreeEntry part){
		ArrayList<SkillTreeEntry> treeEntries = SkillTreeManager.instance.getTree(SkillTrees.Offense);
		for (SkillTreeEntry st_entry : treeEntries){
			ISkillTreeEntry item = st_entry.registeredItem;
			if (item != null && item == part){
				return st_entry;
			}
		}
		treeEntries = SkillTreeManager.instance.getTree(SkillTrees.Defense);
		for (SkillTreeEntry st_entry : treeEntries){
			ISkillTreeEntry item = st_entry.registeredItem;
			if (item != null && item == part){
				return st_entry;
			}
		}
		treeEntries = SkillTreeManager.instance.getTree(SkillTrees.Utility);
		for (SkillTreeEntry st_entry : treeEntries){
			ISkillTreeEntry item = st_entry.registeredItem;
			if (item != null && item == part){
				return st_entry;
			}
		}
		treeEntries = SkillTreeManager.instance.getTree(SkillTrees.Talents);
		for (SkillTreeEntry st_entry : treeEntries){
			ISkillTreeEntry item = st_entry.registeredItem;
			if (item != null && item == part){
				return st_entry;
			}
		}

		return null;
	}

	public void init(){

		//offense tree
		offenseTree.clear();
		RegisterPart(SkillManager.instance.getSkill("Projectile"), 300, 45, SkillTrees.Offense, SkillPointTypes.BLUE);
		RegisterPart(SkillManager.instance.getSkill("PhysicalDamage"), 300, 90, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));
		RegisterPart(SkillManager.instance.getSkill("Gravity"), 255, 70, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));
		RegisterPart(SkillManager.instance.getSkill("Bounce"), 345, 70, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));

		RegisterPart(SkillManager.instance.getSkill("FireDamage"), 210, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("PhysicalDamage"));
		RegisterPart(SkillManager.instance.getSkill("LightningDamage"), 255, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("FireDamage"));
		RegisterPart(SkillManager.instance.getSkill("Ignition"), 165, 135, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FireDamage"));
		RegisterPart(SkillManager.instance.getSkill("Forge"), 120, 135, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Ignition"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Fire"), 165, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Ignition"));

		RegisterPart(SkillManager.instance.getSkill("MagicDamage"), 390, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("PhysicalDamage"));
		RegisterPart(SkillManager.instance.getSkill("FrostDamage"), 345, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Drown"), 435, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Blind"), 233, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FireDamage"), SkillManager.instance.getSkill("LightningDamage"));
		RegisterPart(SkillManager.instance.getSkill("AoE"), 300, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("PhysicalDamage"), SkillManager.instance.getSkill("FrostDamage"), SkillManager.instance.getSkill("FireDamage"), SkillManager.instance.getSkill("LightningDamage"), SkillManager.instance.getSkill("MagicDamage"));
		RegisterPart(SkillManager.instance.getSkill("Freeze"), 345, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FrostDamage"));
		RegisterPart(SkillManager.instance.getSkill("Knockback"), 390, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Solar"), 210, 225, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Blind"));

		RegisterPart(SkillManager.instance.getSkill("Storm"), 255, 225, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("LightningDamage"));
		RegisterPart(SkillManager.instance.getSkill("AstralDistortion"), 367, 215, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("MagicDamage"), SkillManager.instance.getSkill("FrostDamage"));
		RegisterPart(SkillManager.instance.getSkill("Silence"), 345, 245, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("AstralDistortion"));

		RegisterPart(SkillManager.instance.getSkill("Fling"), 390, 245, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Knockback"));
		RegisterPart(SkillManager.instance.getSkill("VelocityAdded"), 390, 290, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Fling"));
		RegisterPart(SkillManager.instance.getSkill("WateryGrave"), 435, 245, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Drown"));

		RegisterPart(SkillManager.instance.getSkill("Piercing"), 323, 215, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Freeze"));

		RegisterPart(SkillManager.instance.getSkill("Beam"), 300, 270, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("AoE"));
		RegisterPart(SkillManager.instance.getSkill("Damage"), 300, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"));
		RegisterPart(SkillManager.instance.getSkill("Fury"), 255, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"), SkillManager.instance.getSkill("Storm"));
		RegisterPart(SkillManager.instance.getSkill("Wave"), 367, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"), SkillManager.instance.getSkill("Fling"));

		RegisterPart(SkillManager.instance.getSkill("Blizzard"), 75, 45, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("FallingStar"), 75, 90, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("FireRain"), 75, 135, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Dismembering"), 75, 180, SkillTrees.Offense, SkillPointTypes.SILVER);

		//defense tree
		defenseTree.clear();
		RegisterPart(SkillManager.instance.getSkill("Self"), 267, 45, SkillTrees.Defense, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("Leap"), 222, 90, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Self"));
		RegisterPart(SkillManager.instance.getSkill("Regeneration"), 357, 90, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Self"));

		RegisterPart(SkillManager.instance.getSkill("Shrink"), 402, 90, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Regeneration"));
		RegisterPart(SkillManager.instance.getSkill("Slowfall"), 222, 135, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Leap"));
		RegisterPart(SkillManager.instance.getSkill("Heal"), 357, 135, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Regeneration"));
		RegisterPart(SkillManager.instance.getSkill("LifeTap"), 312, 135, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Heal"));
		RegisterPart(SkillManager.instance.getSkill("Healing"), 402, 135, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Heal"));

		RegisterPart(SkillManager.instance.getSkill("Summon"), 267, 135, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeTap"));
		RegisterPart(SkillManager.instance.getSkill("Contingency_Damage"), 447, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Healing"));

		RegisterPart(SkillManager.instance.getSkill("Haste"), 177, 155, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Slowfall"));
		RegisterPart(SkillManager.instance.getSkill("Slow"), 132, 155, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Slowfall"));

		RegisterPart(SkillManager.instance.getSkill("GravityWell"), 222, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Slowfall"));
		RegisterPart(SkillManager.instance.getSkill("LifeDrain"), 312, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeTap"));
		RegisterPart(SkillManager.instance.getSkill("Dispel"), 357, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Heal"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Fall"), 267, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("GravityWell"));

		RegisterPart(SkillManager.instance.getSkill("SwiftSwim"), 177, 200, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Haste"));
		RegisterPart(SkillManager.instance.getSkill("Repel"), 132, 200, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Slow"));

		RegisterPart(SkillManager.instance.getSkill("Levitate"), 222, 225, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("GravityWell"));
		RegisterPart(SkillManager.instance.getSkill("ManaDrain"), 312, 225, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeDrain"));
		RegisterPart(SkillManager.instance.getSkill("Zone"), 357, 225, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Dispel"));

		RegisterPart(SkillManager.instance.getSkill("Wall"), 87, 200, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Repel"));
		RegisterPart(SkillManager.instance.getSkill("Accelerate"), 177, 245, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("SwiftSwim"));
		RegisterPart(SkillManager.instance.getSkill("Entangle"), 132, 245, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Repel"));
		RegisterPart(SkillManager.instance.getSkill("Appropriation"), 87, 245, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Entangle"));

		RegisterPart(SkillManager.instance.getSkill("Flight"), 222, 270, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Levitate"));
		RegisterPart(SkillManager.instance.getSkill("Shield"), 357, 270, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Zone"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Health"), 402, 270, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Shield"));

		RegisterPart(SkillManager.instance.getSkill("Rune"), 157, 315, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Accelerate"), SkillManager.instance.getSkill("Entangle"));

		RegisterPart(SkillManager.instance.getSkill("RuneProcs"), 157, 360, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rune"));

		RegisterPart(SkillManager.instance.getSkill("Speed"), 202, 315, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Accelerate"), SkillManager.instance.getSkill("Flight"));

		RegisterPart(SkillManager.instance.getSkill("Reflect"), 357, 315, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Shield"));
		RegisterPart(SkillManager.instance.getSkill("ChronoAnchor"), 312, 315, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Reflect"));

		RegisterPart(SkillManager.instance.getSkill("Duration"), 312, 360, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("ChronoAnchor"));

		RegisterPart(SkillManager.instance.getSkill("ManaLink"), 30, 45, SkillTrees.Defense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("ManaShield"), 30, 90, SkillTrees.Defense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("BuffPower"), 30, 135, SkillTrees.Defense, SkillPointTypes.SILVER);

		//utility tree
		utilityTree.clear();
		RegisterPart(SkillManager.instance.getSkill("Touch"), 275, 75, SkillTrees.Utility, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("Dig"), 275, 120, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Touch"));
		RegisterPart(SkillManager.instance.getSkill("WizardsAutumn"), 315, 120, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("TargetNonSolid"), 230, 75, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Touch"));

		RegisterPart(SkillManager.instance.getSkill("PlaceBlock"), 185, 93, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("FeatherTouch"), 230, 137, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("MiningPower"), 185, 137, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FeatherTouch"));

		RegisterPart(SkillManager.instance.getSkill("Light"), 275, 165, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("NightVision"), 185, 165, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Light"));

		RegisterPart(SkillManager.instance.getSkill("Binding"), 275, 210, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Light"));
		RegisterPart(SkillManager.instance.getSkill("Disarm"), 230, 210, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Charm"), 315, 235, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("TrueSight"), 185, 210, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("NightVision"));

		RegisterPart(SkillManager.instance.getSkill("Lunar"), 145, 210, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("TrueSight"));

		RegisterPart(SkillManager.instance.getSkill("HarvestPlants"), 365, 120, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Plow"), 365, 165, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Plant"), 365, 210, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("CreateWater"), 365, 255, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Drought"), 365, 300, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));

		RegisterPart(SkillManager.instance.getSkill("BanishRain"), 365, 345, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Drought"));
		RegisterPart(SkillManager.instance.getSkill("WaterBreathing"), 410, 345, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Drought"));

		RegisterPart(SkillManager.instance.getSkill("Grow"), 410, 210, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Drought"), SkillManager.instance.getSkill("CreateWater"), SkillManager.instance.getSkill("Plant"), SkillManager.instance.getSkill("Plow"), SkillManager.instance.getSkill("HarvestPlants"));

		RegisterPart(SkillManager.instance.getSkill("Chain"), 455, 210, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Grow"));

		RegisterPart(SkillManager.instance.getSkill("Rift"), 275, 255, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Invisibility"), 185, 255, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("TrueSight"));

		RegisterPart(SkillManager.instance.getSkill("RandomTeleport"), 185, 300, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Invisibility"));
		RegisterPart(SkillManager.instance.getSkill("Attract"), 245, 300, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rift"));
		RegisterPart(SkillManager.instance.getSkill("Telekinesis"), 305, 300, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rift"));

		RegisterPart(SkillManager.instance.getSkill("Blink"), 185, 345, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("RandomTeleport"));
		RegisterPart(SkillManager.instance.getSkill("Range"), 140, 345, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Blink"));
		RegisterPart(SkillManager.instance.getSkill("Channel"), 275, 345, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Attract"), SkillManager.instance.getSkill("Telekinesis"));

		RegisterPart(SkillManager.instance.getSkill("Radius"), 275, 390, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Channel"));
		RegisterPart(SkillManager.instance.getSkill("Transplace"), 185, 390, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Blink"));

		RegisterPart(SkillManager.instance.getSkill("Mark"), 155, 435, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Transplace"));
		RegisterPart(SkillManager.instance.getSkill("Recall"), 215, 435, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Transplace"));

		RegisterPart(SkillManager.instance.getSkill("DivineIntervention"), 172, 480, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Recall"), SkillManager.instance.getSkill("Mark"));
		RegisterPart(SkillManager.instance.getSkill("EnderIntervention"), 198, 480, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Recall"), SkillManager.instance.getSkill("Mark"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Death"), 198, 524, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("EnderIntervention"));

		RegisterPart(SkillManager.instance.getSkill("Daylight"), 75, 45, SkillTrees.Utility, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Moonrise"), 75, 90, SkillTrees.Utility, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Prosperity"), 75, 135, SkillTrees.Utility, SkillPointTypes.SILVER);

		//talent tree
		RegisterPart(SkillManager.instance.getSkill("ManaRegenI"), 275, 75, SkillTrees.Talents, SkillPointTypes.BLUE);
		RegisterPart(SkillManager.instance.getSkill("Colour"), 230, 75, SkillTrees.Talents, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("AffinityGains"), 365, 120, SkillTrees.Talents, SkillPointTypes.BLUE, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("ManaRegenII"), 275, 120, SkillTrees.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("SpellMotion"), 230, 120, SkillTrees.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenII"));
		RegisterPart(SkillManager.instance.getSkill("AugmentedCasting"), 230, 165, SkillTrees.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("SpellMotion"));
		RegisterPart(SkillManager.instance.getSkill("ManaRegenIII"), 275, 165, SkillTrees.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("ManaRegenII"));

		RegisterPart(SkillManager.instance.getSkill("ExtraSummon"), 230, 210, SkillTrees.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("AugmentedCasting"));

		RegisterPart(SkillManager.instance.getSkill("MageBandI"), 320, 120, SkillTrees.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("MageBandII"), 320, 165, SkillTrees.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("MageBandI"));

		calculateHighestOverallTier();

		checkAllPartIDs(SkillManager.instance.getAllShapes());
		checkAllPartIDs(SkillManager.instance.getAllComponents());
		checkAllPartIDs(SkillManager.instance.getAllModifiers());
		checkAllPartIDs(SkillManager.instance.getAllTalents());

		AMCore.skillConfig.save();
	}

	public int[] getDisabledSkillIDs(){
		ArrayList<Integer> disableds = new ArrayList<Integer>();
		for (SkillTreeEntry entry : offenseTree)
			if (!entry.enabled)
				disableds.add(SkillManager.instance.getShiftedPartID(entry.registeredItem));
		for (SkillTreeEntry entry : defenseTree)
			if (!entry.enabled)
				disableds.add(SkillManager.instance.getShiftedPartID(entry.registeredItem));
		for (SkillTreeEntry entry : utilityTree)
			if (!entry.enabled)
				disableds.add(SkillManager.instance.getShiftedPartID(entry.registeredItem));
		for (SkillTreeEntry entry : talentTree)
			if (!entry.enabled)
				disableds.add(SkillManager.instance.getShiftedPartID(entry.registeredItem));

		int[] toReturn = new int[disableds.size()];

		for (int i = 0; i < disableds.size(); ++i)
			toReturn[i] = disableds.get(i);

		return toReturn;

	}

	private void checkAllPartIDs(ArrayList<Integer> partIDs){
		for (Integer i : partIDs){
			ISkillTreeEntry part = SkillManager.instance.getSkill(i);
			if (getSkillTreeEntry(part) == null){
				LogHelper.warn("Unregistered spell part in skill trees: " + part.toString());
			}
		}
	}

	private void calculateHighestOverallTier(){
		int offense = calculateHighestTier(offenseTree);
		int defense = calculateHighestTier(defenseTree);
		int utility = calculateHighestTier(utilityTree);

		highestSkillDepth = Math.max(offense, Math.max(defense, utility));
	}

	private int calculateHighestTier(ArrayList<SkillTreeEntry> tree){
		int highest = 0;
		for (SkillTreeEntry entry : tree)
			if (entry.tier > highest)
				highest = entry.tier;

		return highest;
	}

	public int getHighestTier(){
		return this.highestSkillDepth;
	}


	public void disableAllSkillsIn(int[] disabledSkills){
		//enable all skills
		for (SkillTreeEntry entry : offenseTree)
			entry.enabled = true;
		for (SkillTreeEntry entry : defenseTree)
			entry.enabled = true;
		for (SkillTreeEntry entry : utilityTree)
			entry.enabled = true;
		for (SkillTreeEntry entry : talentTree)
			entry.enabled = true;
		//disable all server-disabled skills
		for (int i : disabledSkills){
			SkillTreeEntry entry = getSkillTreeEntry(SkillManager.instance.getSkill(i));
			if (entry != null){
				entry.enabled = false;
				LogHelper.info("Disabling %s as per server configs", SkillManager.instance.getSkillName(entry.registeredItem));
			}else{
				LogHelper.warn("Could not disable skill ID %d as per server configs!");
			}
		}
	}

	public boolean isSkillDisabled(ISkillTreeEntry component){
		SkillTreeEntry entry = getSkillTreeEntry(component);
		if (entry == null)
			return false;
		return !entry.enabled;
	}
}
