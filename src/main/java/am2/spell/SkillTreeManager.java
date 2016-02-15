package am2.spell;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.util.ResourceLocation;
import am2.AMCore;
import am2.LogHelper;
import am2.api.SkillTreeEntry;
import am2.api.spell.ISkillTreeManager;
import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.enums.SkillPointTypes;
import am2.api.spell.enums.SkillTree;

public class SkillTreeManager implements ISkillTreeManager{

	private final ArrayList<SkillTree> trees = new ArrayList<SkillTree>();
	private final HashMap<Integer, ArrayList<SkillTreeEntry>> treeSkills = new HashMap<Integer, ArrayList<SkillTreeEntry>>();
	private final HashMap<Integer, SkillPointTypes> skillPointTypeList;

	private int highestSkillDepth = 0;

	public static final SkillTreeManager instance = new SkillTreeManager();

	private SkillTreeManager(){
		skillPointTypeList = new HashMap<Integer, SkillPointTypes>();
		trees.set(0, SkillTree.Offense);
		trees.set(1, SkillTree.Defense);
		trees.set(2, SkillTree.Utility);
		trees.set(3, SkillTree.Talents);
		trees.set(4, SkillTree.Affinity);
	}

	public SkillPointTypes getSkillPointTypeForPart(ISkillTreeEntry part){
		int id = SkillManager.instance.getShiftedPartID(part);
		return getSkillPointTypeForPart(id);
	}

	public SkillPointTypes getSkillPointTypeForPart(int id){
		return skillPointTypeList.get(id);
	}

	@Override
	public void RegisterPart(ISkillTreeEntry item, int x, int y, SkillTree tree, SkillPointTypes requiredPoint, ISkillTreeEntry... prerequisites){

		//get appropriate skill tree
		
		if (tree == null) throw new InvalidParameterException("Tree must not be null");
		ArrayList<SkillTreeEntry> treeListing =  treeSkills.get(tree.getId());
		
		
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
				throw new InvalidParameterException(String.format("Unable to locate one or more prerequisite items in the specified tree (%s).", tree.getName()));
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

	public ArrayList<SkillTreeEntry> getTree(SkillTree tree){
		ArrayList<SkillTreeEntry> safeCopy = new ArrayList<SkillTreeEntry>();
		if (tree != null && treeSkills.get(tree.getId()) != null) {
			safeCopy.addAll(treeSkills.get(tree.getId()));
		}
		return safeCopy;
	}
	
	public ArrayList<SkillTree> getTrees() {
		ArrayList<SkillTree> trees = new ArrayList<SkillTree>();
		trees.addAll(this.trees);
		return trees;
	}
	
	public SkillTreeEntry getSkillTreeEntry(ISkillTreeEntry part){
		for (Entry<Integer, ArrayList<SkillTreeEntry>> entry : treeSkills.entrySet()) {
			for (SkillTreeEntry check : entry.getValue()) {
				if (check.equals(part))
					return check;
			}
		}
		return null;
	}

	public void init(){
		treeSkills.clear();
		treeSkills.put(4, new ArrayList<SkillTreeEntry>());
		
		//offense tree
		treeSkills.put(0, new ArrayList<SkillTreeEntry>());
		RegisterPart(SkillManager.instance.getSkill("Projectile"), 300, 45, SkillTree.Offense, SkillPointTypes.BLUE);
		RegisterPart(SkillManager.instance.getSkill("PhysicalDamage"), 300, 90, SkillTree.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));
		RegisterPart(SkillManager.instance.getSkill("Gravity"), 255, 70, SkillTree.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));
		RegisterPart(SkillManager.instance.getSkill("Bounce"), 345, 70, SkillTree.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));

		RegisterPart(SkillManager.instance.getSkill("FireDamage"), 210, 135, SkillTree.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("PhysicalDamage"));
		RegisterPart(SkillManager.instance.getSkill("LightningDamage"), 255, 135, SkillTree.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("FireDamage"));
		RegisterPart(SkillManager.instance.getSkill("Ignition"), 165, 135, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FireDamage"));
		RegisterPart(SkillManager.instance.getSkill("Forge"), 120, 135, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Ignition"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Fire"), 165, 180, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Ignition"));

		RegisterPart(SkillManager.instance.getSkill("MagicDamage"), 390, 135, SkillTree.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("PhysicalDamage"));
		RegisterPart(SkillManager.instance.getSkill("FrostDamage"), 345, 135, SkillTree.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Drown"), 435, 135, SkillTree.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Blind"), 233, 180, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FireDamage"), SkillManager.instance.getSkill("LightningDamage"));
		RegisterPart(SkillManager.instance.getSkill("AoE"), 300, 180, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("PhysicalDamage"), SkillManager.instance.getSkill("FrostDamage"), SkillManager.instance.getSkill("FireDamage"), SkillManager.instance.getSkill("LightningDamage"), SkillManager.instance.getSkill("MagicDamage"));
		RegisterPart(SkillManager.instance.getSkill("Freeze"), 345, 180, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FrostDamage"));
		RegisterPart(SkillManager.instance.getSkill("Knockback"), 390, 180, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Solar"), 210, 225, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Blind"));

		RegisterPart(SkillManager.instance.getSkill("Storm"), 255, 225, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("LightningDamage"));
		RegisterPart(SkillManager.instance.getSkill("AstralDistortion"), 367, 215, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("MagicDamage"), SkillManager.instance.getSkill("FrostDamage"));
		RegisterPart(SkillManager.instance.getSkill("Silence"), 345, 245, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("AstralDistortion"));

		RegisterPart(SkillManager.instance.getSkill("Fling"), 390, 245, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Knockback"));
		RegisterPart(SkillManager.instance.getSkill("VelocityAdded"), 390, 290, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Fling"));
		RegisterPart(SkillManager.instance.getSkill("WateryGrave"), 435, 245, SkillTree.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Drown"));

		RegisterPart(SkillManager.instance.getSkill("Piercing"), 323, 215, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Freeze"));

		RegisterPart(SkillManager.instance.getSkill("Beam"), 300, 270, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("AoE"));
		RegisterPart(SkillManager.instance.getSkill("Damage"), 300, 315, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"));
		RegisterPart(SkillManager.instance.getSkill("Fury"), 255, 315, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"), SkillManager.instance.getSkill("Storm"));
		RegisterPart(SkillManager.instance.getSkill("Wave"), 367, 315, SkillTree.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"), SkillManager.instance.getSkill("Fling"));

		RegisterPart(SkillManager.instance.getSkill("Blizzard"), 75, 45, SkillTree.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("FallingStar"), 75, 90, SkillTree.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("FireRain"), 75, 135, SkillTree.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Dismembering"), 75, 180, SkillTree.Offense, SkillPointTypes.SILVER);

		//defense tree
		treeSkills.put(1, new ArrayList<SkillTreeEntry>());
		RegisterPart(SkillManager.instance.getSkill("Self"), 267, 45, SkillTree.Defense, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("Leap"), 222, 90, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Self"));
		RegisterPart(SkillManager.instance.getSkill("Regeneration"), 357, 90, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Self"));

		RegisterPart(SkillManager.instance.getSkill("Shrink"), 402, 90, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Regeneration"));
		RegisterPart(SkillManager.instance.getSkill("Slowfall"), 222, 135, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Leap"));
		RegisterPart(SkillManager.instance.getSkill("Heal"), 357, 135, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Regeneration"));
		RegisterPart(SkillManager.instance.getSkill("LifeTap"), 312, 135, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Heal"));
		RegisterPart(SkillManager.instance.getSkill("Healing"), 402, 135, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Heal"));

		RegisterPart(SkillManager.instance.getSkill("Summon"), 267, 135, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeTap"));
		RegisterPart(SkillManager.instance.getSkill("Contingency_Damage"), 447, 180, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Healing"));

		RegisterPart(SkillManager.instance.getSkill("Haste"), 177, 155, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Slowfall"));
		RegisterPart(SkillManager.instance.getSkill("Slow"), 132, 155, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Slowfall"));

		RegisterPart(SkillManager.instance.getSkill("GravityWell"), 222, 180, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Slowfall"));
		RegisterPart(SkillManager.instance.getSkill("LifeDrain"), 312, 180, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeTap"));
		RegisterPart(SkillManager.instance.getSkill("Dispel"), 357, 180, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Heal"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Fall"), 267, 180, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("GravityWell"));

		RegisterPart(SkillManager.instance.getSkill("SwiftSwim"), 177, 200, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Haste"));
		RegisterPart(SkillManager.instance.getSkill("Repel"), 132, 200, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Slow"));

		RegisterPart(SkillManager.instance.getSkill("Levitate"), 222, 225, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("GravityWell"));
		RegisterPart(SkillManager.instance.getSkill("ManaDrain"), 312, 225, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeDrain"));
		RegisterPart(SkillManager.instance.getSkill("Zone"), 357, 225, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Dispel"));

		RegisterPart(SkillManager.instance.getSkill("Wall"), 87, 200, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Repel"));
		RegisterPart(SkillManager.instance.getSkill("Accelerate"), 177, 245, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("SwiftSwim"));
		RegisterPart(SkillManager.instance.getSkill("Entangle"), 132, 245, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Repel"));
		RegisterPart(SkillManager.instance.getSkill("Appropriation"), 87, 245, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Entangle"));

		RegisterPart(SkillManager.instance.getSkill("Flight"), 222, 270, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Levitate"));
		RegisterPart(SkillManager.instance.getSkill("Shield"), 357, 270, SkillTree.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Zone"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Health"), 402, 270, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Shield"));

		RegisterPart(SkillManager.instance.getSkill("Rune"), 157, 315, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Accelerate"), SkillManager.instance.getSkill("Entangle"));

		RegisterPart(SkillManager.instance.getSkill("RuneProcs"), 157, 360, SkillTree.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rune"));

		RegisterPart(SkillManager.instance.getSkill("Speed"), 202, 315, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Accelerate"), SkillManager.instance.getSkill("Flight"));

		RegisterPart(SkillManager.instance.getSkill("Reflect"), 357, 315, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Shield"));
		RegisterPart(SkillManager.instance.getSkill("ChronoAnchor"), 312, 315, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Reflect"));

		RegisterPart(SkillManager.instance.getSkill("Duration"), 312, 360, SkillTree.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("ChronoAnchor"));

		RegisterPart(SkillManager.instance.getSkill("ManaLink"), 30, 45, SkillTree.Defense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("ManaShield"), 30, 90, SkillTree.Defense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("BuffPower"), 30, 135, SkillTree.Defense, SkillPointTypes.SILVER);

		//utility tree
		treeSkills.put(2, new ArrayList<SkillTreeEntry>());
		RegisterPart(SkillManager.instance.getSkill("Touch"), 275, 75, SkillTree.Utility, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("Dig"), 275, 120, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Touch"));
		RegisterPart(SkillManager.instance.getSkill("WizardsAutumn"), 315, 120, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("TargetNonSolid"), 230, 75, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Touch"));

		RegisterPart(SkillManager.instance.getSkill("PlaceBlock"), 185, 93, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("FeatherTouch"), 230, 137, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("MiningPower"), 185, 137, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FeatherTouch"));

		RegisterPart(SkillManager.instance.getSkill("Light"), 275, 165, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("NightVision"), 185, 165, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Light"));

		RegisterPart(SkillManager.instance.getSkill("Binding"), 275, 210, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Light"));
		RegisterPart(SkillManager.instance.getSkill("Disarm"), 230, 210, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Charm"), 315, 235, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("TrueSight"), 185, 210, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("NightVision"));

		RegisterPart(SkillManager.instance.getSkill("Lunar"), 145, 210, SkillTree.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("TrueSight"));

		RegisterPart(SkillManager.instance.getSkill("HarvestPlants"), 365, 120, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Plow"), 365, 165, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Plant"), 365, 210, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("CreateWater"), 365, 255, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Drought"), 365, 300, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));

		RegisterPart(SkillManager.instance.getSkill("BanishRain"), 365, 345, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Drought"));
		RegisterPart(SkillManager.instance.getSkill("WaterBreathing"), 410, 345, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Drought"));

		RegisterPart(SkillManager.instance.getSkill("Grow"), 410, 210, SkillTree.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Drought"), SkillManager.instance.getSkill("CreateWater"), SkillManager.instance.getSkill("Plant"), SkillManager.instance.getSkill("Plow"), SkillManager.instance.getSkill("HarvestPlants"));

		RegisterPart(SkillManager.instance.getSkill("Chain"), 455, 210, SkillTree.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Grow"));

		RegisterPart(SkillManager.instance.getSkill("Rift"), 275, 255, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Invisibility"), 185, 255, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("TrueSight"));

		RegisterPart(SkillManager.instance.getSkill("RandomTeleport"), 185, 300, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Invisibility"));
		RegisterPart(SkillManager.instance.getSkill("Attract"), 245, 300, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rift"));
		RegisterPart(SkillManager.instance.getSkill("Telekinesis"), 305, 300, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rift"));

		RegisterPart(SkillManager.instance.getSkill("Blink"), 185, 345, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("RandomTeleport"));
		RegisterPart(SkillManager.instance.getSkill("Range"), 140, 345, SkillTree.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Blink"));
		RegisterPart(SkillManager.instance.getSkill("Channel"), 275, 345, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Attract"), SkillManager.instance.getSkill("Telekinesis"));

		RegisterPart(SkillManager.instance.getSkill("Radius"), 275, 390, SkillTree.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Channel"));
		RegisterPart(SkillManager.instance.getSkill("Transplace"), 185, 390, SkillTree.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Blink"));

		RegisterPart(SkillManager.instance.getSkill("Mark"), 155, 435, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Transplace"));
		RegisterPart(SkillManager.instance.getSkill("Recall"), 215, 435, SkillTree.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Transplace"));

		RegisterPart(SkillManager.instance.getSkill("DivineIntervention"), 172, 480, SkillTree.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Recall"), SkillManager.instance.getSkill("Mark"));
		RegisterPart(SkillManager.instance.getSkill("EnderIntervention"), 198, 480, SkillTree.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Recall"), SkillManager.instance.getSkill("Mark"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Death"), 198, 524, SkillTree.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("EnderIntervention"));

		RegisterPart(SkillManager.instance.getSkill("Daylight"), 75, 45, SkillTree.Utility, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Moonrise"), 75, 90, SkillTree.Utility, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Prosperity"), 75, 135, SkillTree.Utility, SkillPointTypes.SILVER);

		//talent tree
		treeSkills.put(3, new ArrayList<SkillTreeEntry>());
		RegisterPart(SkillManager.instance.getSkill("ManaRegenI"), 275, 75, SkillTree.Talents, SkillPointTypes.BLUE);
		RegisterPart(SkillManager.instance.getSkill("Colour"), 230, 75, SkillTree.Talents, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("AffinityGains"), 365, 120, SkillTree.Talents, SkillPointTypes.BLUE, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("ManaRegenII"), 275, 120, SkillTree.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("SpellMotion"), 230, 120, SkillTree.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenII"));
		RegisterPart(SkillManager.instance.getSkill("AugmentedCasting"), 230, 165, SkillTree.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("SpellMotion"));
		RegisterPart(SkillManager.instance.getSkill("ManaRegenIII"), 275, 165, SkillTree.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("ManaRegenII"));

		RegisterPart(SkillManager.instance.getSkill("ExtraSummon"), 230, 210, SkillTree.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("AugmentedCasting"));

		RegisterPart(SkillManager.instance.getSkill("MageBandI"), 320, 120, SkillTree.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("MageBandII"), 320, 165, SkillTree.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("MageBandI"));

		calculateHighestOverallTier();

		checkAllPartIDs(SkillManager.instance.getAllShapes());
		checkAllPartIDs(SkillManager.instance.getAllComponents());
		checkAllPartIDs(SkillManager.instance.getAllModifiers());
		checkAllPartIDs(SkillManager.instance.getAllTalents());

		AMCore.skillConfig.save();
	}

	public int[] getDisabledSkillIDs(){
		ArrayList<Integer> disableds = new ArrayList<Integer>();
		for (Entry<Integer, ArrayList<SkillTreeEntry>> entry : treeSkills.entrySet()) {
			for (SkillTreeEntry entry2 : entry.getValue())
				if (!entry2.enabled)
					disableds.add(SkillManager.instance.getShiftedPartID(entry2.registeredItem));
			
		}

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
		for (Entry<Integer, ArrayList<SkillTreeEntry>> entry : treeSkills.entrySet()) {
			highestSkillDepth = Math.max(highestSkillDepth, calculateHighestTier(entry.getValue()));
		}
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
		for (Entry<Integer, ArrayList<SkillTreeEntry>> entry : treeSkills.entrySet()) {
			for (SkillTreeEntry entry2 : entry.getValue())
				entry2.enabled = true;			
		}
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

	@Override
	public SkillTree RegisterSkillTree(String name, ResourceLocation background, ResourceLocation icon) {
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).getName().equals(name))
				LogHelper.warn("Registered %s twice this is a mistake", name);
		}
		int id = trees.size();
		SkillTree tree = new SkillTree(id, name, background, icon);
		trees.set(id, tree);
		treeSkills.put(id, new ArrayList<SkillTreeEntry>());
		return tree;
	}
}
