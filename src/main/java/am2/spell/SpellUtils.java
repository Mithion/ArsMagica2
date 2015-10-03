package am2.spell;

import am2.AMCore;
import am2.api.ArsMagicaApi;
import am2.api.events.AffinityChangingEvent;
import am2.api.events.ModifierCalculatedEvent;
import am2.api.events.ModifierCalculatedEvent.OperationType;
import am2.api.spell.ISpellUtils;
import am2.api.spell.component.interfaces.*;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.armor.ArmorHelper;
import am2.armor.ArsMagicaArmorMaterial;
import am2.armor.infusions.GenericImbuement;
import am2.enchantments.AMEnchantmentHelper;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.SkillData;
import am2.spell.components.Summon;
import am2.spell.shapes.MissingShape;
import am2.utility.KeyValuePair;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

public class SpellUtils implements ISpellUtils{

	//Shape groups
	private static final String CurShapeGroup_Identifier = "CurrentShapeGroup";
	private static final String NumShapeGroups_Identifier = "NumShapeGroups";
	private static final String ShapeGroup_Identifier = "ShapeGroup_";
	private static final String ShapeGroupMeta_Identifier = "ShapeGroupMeta_";

	//Stage Counter
	private static final String Stages_Identifier = "NumStages";

	//Shapes/Components/Modifiers for each stage
	private static final String Shape_Prefix = "ShapeOrdinal_";
	private static final String Component_Prefix = "SpellComponentIDs_";
	private static final String Modifier_Prefix = "SpellModifierIDs_";

	//Shape/Component/Modifier meta for each stage
	private static final String Shape_Meta_Prefix = "ShapeMeta_";
	private static final String Component_Meta_Prefix = "SpellComponentMeta_";
	private static final String Modifier_Meta_Prefix = "SpellModifierMeta_";

	//global spell metadata
	private static final String Global_Spell_Meta = "spellMetadata";

	private static final String BaseManaCostIdentifier = "BMC_";
	private static final String BaseBurnoutIdentifier = "BB_";

	private static final String BaseReagentsIdentifier = "BRR";
	private static final String ForcedAffinity = "ForcedAffinity";

	public static SpellUtils instance = new SpellUtils();

	private SpellUtils(){
	}

	//==============================================================================
	// API Satisfaction
	//==============================================================================

	public double getModifiedDouble_Mul(double defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers check){
		return getModifiedDouble_Mul(defaultValue, stack, caster, target, world, 0, check);
	}

	public int getModifiedInt_Mul(int defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers check){
		return getModifiedInt_Mul(defaultValue, stack, caster, target, world, 0, check);
	}

	public double getModifiedDouble_Mul(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world){
		return getModifiedDouble_Mul(check, stack, caster, target, world, 0);
	}

	public int getModifiedInt_Mul(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world){
		return getModifiedInt_Mul(check, stack, caster, target, world, 0);
	}

	public double getModifiedDouble_Add(double defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers check){
		return getModifiedDouble_Add(defaultValue, stack, caster, target, world, 0, check);
	}

	public int getModifiedInt_Add(int defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers check){
		return getModifiedInt_Add(defaultValue, stack, caster, target, world, 0, check);
	}

	public double getModifiedDouble_Add(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world){
		return getModifiedDouble_Add(check, stack, caster, target, world, 0);
	}

	public int getModifiedInt_Add(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world){
		return getModifiedInt_Add(check, stack, caster, target, world, 0);
	}

	public boolean modifierIsPresent(SpellModifiers check, ItemStack stack){
		return modifierIsPresent(check, stack, 0);
	}

	public int countModifiers(SpellModifiers check, ItemStack stack){
		return countModifiers(check, stack, 0);
	}

	//==============================================================================
	// Modifiers
	//==============================================================================
	public double getModifiedDouble_Mul(double defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage, SpellModifiers check){
		int ordinalCount = 0;
		double modifiedValue = defaultValue;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				byte[] meta = getModifierMetadataFromStack(stack, modifier, stage, ordinalCount++);
				modifiedValue *= modifier.getModifier(check, caster, target, world, meta);
			}
		}

		if (caster instanceof EntityPlayer){
			if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")))){
				modifiedValue *= 1.1f;
			}
		}

		ModifierCalculatedEvent event = new ModifierCalculatedEvent(stack, caster, check, defaultValue, modifiedValue, OperationType.MULTIPLY);
		MinecraftForge.EVENT_BUS.post(event);

		return event.modifiedValue;
	}

	public int getModifiedInt_Mul(int defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage, SpellModifiers check){
		int ordinalCount = 0;
		int modifiedValue = defaultValue;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				byte[] meta = getModifierMetadataFromStack(stack, modifier, stage, ordinalCount++);
				modifiedValue *= modifier.getModifier(check, caster, target, world, meta);
			}
		}

		if (caster instanceof EntityPlayer){
			if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")))){
				modifiedValue *= 1.1f;
			}
		}

		ModifierCalculatedEvent event = new ModifierCalculatedEvent(stack, caster, check, defaultValue, modifiedValue, OperationType.MULTIPLY);
		MinecraftForge.EVENT_BUS.post(event);

		return (int)event.modifiedValue;
	}

	public double getModifiedDouble_Mul(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage){
		int ordinalCount = 0;
		double modifiedValue = check.defaultValue;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				byte[] meta = getModifierMetadataFromStack(stack, modifier, stage, ordinalCount++);
				modifiedValue *= modifier.getModifier(check, caster, target, world, meta);
			}
		}

		if (caster instanceof EntityPlayer){
			if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")))){
				modifiedValue *= 1.1f;
			}
		}

		ModifierCalculatedEvent event = new ModifierCalculatedEvent(stack, caster, check, check.defaultValue, modifiedValue, OperationType.MULTIPLY);
		MinecraftForge.EVENT_BUS.post(event);

		return event.modifiedValue;
	}

	public int getModifiedInt_Mul(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage){
		int ordinalCount = 0;
		int modifiedValue = check.defaultValueInt;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				byte[] meta = getModifierMetadataFromStack(stack, modifier, stage, ordinalCount++);
				modifiedValue *= modifier.getModifier(check, caster, target, world, meta);
			}
		}

		if (caster instanceof EntityPlayer){
			if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")))){
				modifiedValue *= 1.1f;
			}
		}

		ModifierCalculatedEvent event = new ModifierCalculatedEvent(stack, caster, check, check.defaultValue, modifiedValue, OperationType.MULTIPLY);
		MinecraftForge.EVENT_BUS.post(event);

		return (int)event.modifiedValue;
	}

	public double getModifiedDouble_Add(double defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage, SpellModifiers check){
		int ordinalCount = 0;
		double modifiedValue = defaultValue;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				byte[] meta = getModifierMetadataFromStack(stack, modifier, stage, ordinalCount++);
				modifiedValue += modifier.getModifier(check, caster, target, world, meta);
			}
		}

		if (caster instanceof EntityPlayer){
			if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")))){
				modifiedValue *= 1.1f;
			}
		}

		ModifierCalculatedEvent event = new ModifierCalculatedEvent(stack, caster, check, defaultValue, modifiedValue, OperationType.ADD);
		MinecraftForge.EVENT_BUS.post(event);

		return event.modifiedValue;
	}

	public int getModifiedInt_Add(int defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage, SpellModifiers check){
		int ordinalCount = 0;

		double modifiedValue = defaultValue;

		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				byte[] meta = getModifierMetadataFromStack(stack, modifier, stage, ordinalCount++);
				modifiedValue += modifier.getModifier(check, caster, target, world, meta);
			}
		}

		if (caster instanceof EntityPlayer){
			if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")))){
				modifiedValue *= 1.1f;
			}
		}

		ModifierCalculatedEvent event = new ModifierCalculatedEvent(stack, caster, check, defaultValue, modifiedValue, OperationType.ADD);
		MinecraftForge.EVENT_BUS.post(event);

		return (int)Math.ceil(event.modifiedValue);
	}

	public double getModifiedDouble_Add(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage){
		int ordinalCount = 0;
		double modifiedValue = check.defaultValue;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				byte[] meta = getModifierMetadataFromStack(stack, modifier, stage, ordinalCount++);
				modifiedValue += modifier.getModifier(check, caster, target, world, meta);
			}
		}

		if (caster instanceof EntityPlayer){
			if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")))){
				modifiedValue *= 1.1f;
			}
		}

		ModifierCalculatedEvent event = new ModifierCalculatedEvent(stack, caster, check, check.defaultValue, modifiedValue, OperationType.ADD);
		MinecraftForge.EVENT_BUS.post(event);

		return event.modifiedValue;
	}

	public int getModifiedInt_Add(SpellModifiers check, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage){
		int ordinalCount = 0;
		int modifiedValue = check.defaultValueInt;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				byte[] meta = getModifierMetadataFromStack(stack, modifier, stage, ordinalCount++);
				modifiedValue += modifier.getModifier(check, caster, target, world, meta);
			}
		}

		if (caster instanceof EntityPlayer){
			if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AugmentedCasting")))){
				modifiedValue *= 1.1f;
			}
		}

		ModifierCalculatedEvent event = new ModifierCalculatedEvent(stack, caster, check, check.defaultValue, modifiedValue, OperationType.ADD);
		MinecraftForge.EVENT_BUS.post(event);

		return (int)event.modifiedValue;
	}

	public boolean modifierIsPresent(SpellModifiers check, ItemStack stack, int stage){
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				return true;
			}
		}
		return false;
	}

	public int countModifiers(SpellModifiers check, ItemStack stack, int stage){
		int count = 0;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(check)){
				count++;
			}
		}
		return count;
	}
	//==============================================================================
	// End Modifiers
	//==============================================================================

	public int modifyDurationBasedOnArmor(EntityLivingBase caster, int baseDuration){
		if (!(caster instanceof EntityPlayer)) return baseDuration;
		int armorSet = ArmorHelper.getFullArsMagicaArmorSet((EntityPlayer)caster);
		if (armorSet == ArsMagicaArmorMaterial.MAGE.getMaterialID()){
			baseDuration *= 1.25f;
		}else if (armorSet == ArsMagicaArmorMaterial.BATTLEMAGE.getMaterialID()){
			baseDuration *= 1.1f;
		}else if (armorSet == ArsMagicaArmorMaterial.ARCHMAGE.getMaterialID()){
			baseDuration *= 2f;
		}
		return baseDuration;
	}

	//==============================================================================
	// Spell Requirements
	//==============================================================================
	public SpellRequirements getSpellRequirements(ItemStack stack, EntityLivingBase caster){
		if (!spellRequirementsPresent(stack, caster)){
			writeSpellRequirements(stack, caster, calculateSpellRequirements(stack, caster));
		}

		return modifySpellRequirementsByAffinity(stack, caster, parseSpellRequirements(stack, caster));
	}

	public SpellRequirements modifySpellRequirementsByAffinity(ItemStack stack, EntityLivingBase caster, SpellRequirements reqs){
		HashMap<Affinity, Float> affinities = this.AffinityFor(stack);
		AffinityData affData = AffinityData.For(caster);

		if (affData == null)
			return reqs;

		float manaCost = reqs.manaCost;

		for (Affinity aff : affinities.keySet()){
			float depth = affData.getAffinityDepth(aff);
			//calculate the modifier based on the player's affinity depth as well as how much of the spell is that affinity
			float effectiveness = affinities.get(aff); //how much does this affinity affect the overall mana cost?
			float multiplier = 0.5f * depth; //how much will the player's affinity reduce the mana cost? (max 50%)

			float manaMod = manaCost * effectiveness;
			manaCost -= manaMod * multiplier;
		}

		return new SpellRequirements(manaCost, reqs.burnout, reqs.reagents);
	}

	private SpellRequirements calculateSpellRequirements(ItemStack stack, EntityLivingBase caster){
		float manaCost = 0;
		float burnout = 0;
		ArrayList<ItemStack> reagents = new ArrayList<ItemStack>();
		int stages = numStages(stack);

		for (int i = stages - 1; i >= 0; --i){
			float stageManaCost = 0;
			float stageBurnout = 0;

			ISpellShape shape = getShapeForStage(stack, i);
			ISpellComponent[] components = getComponentsForStage(stack, i);
			ISpellModifier[] modifiers = getModifiersForStage(stack, i);

			for (ISpellComponent component : components){
				ItemStack[] componentReagents = component.reagents(caster);
				if (componentReagents != null)
					for (ItemStack reagentStack : componentReagents) reagents.add(reagentStack);
				stageManaCost += component.manaCost(caster);
				stageBurnout += component.burnout(caster);
			}

			HashMap<ISpellModifier, Integer> modifierWithQuantity = new HashMap<ISpellModifier, Integer>();
			for (ISpellModifier modifier : modifiers){
				if (modifierWithQuantity.containsKey(modifier)){
					Integer qty = modifierWithQuantity.get(modifier);
					if (qty == null) qty = 1;
					qty++;
					modifierWithQuantity.put(modifier, qty);
				}else{
					modifierWithQuantity.put(modifier, 1);
				}
			}

			for (ISpellModifier modifier : modifierWithQuantity.keySet()){
				stageManaCost *= modifier.getManaCostMultiplier(stack, i, modifierWithQuantity.get(modifier));
			}

			manaCost += (stageManaCost * shape.manaCostMultiplier(stack));
			burnout += stageBurnout;
		}

		return new SpellRequirements(manaCost, burnout, reagents);
	}

	private SpellRequirements parseSpellRequirements(ItemStack stack, EntityLivingBase caster){
		float burnoutPct = (ExtendedProperties.For(caster).getCurrentFatigue() / ExtendedProperties.For(caster).getMaxFatigue()) + 1f;

		float manaCost = stack.stackTagCompound.getFloat(BaseManaCostIdentifier) * burnoutPct;
		float burnout = stack.stackTagCompound.getFloat(BaseBurnoutIdentifier);

		int[] reagentList = stack.stackTagCompound.getIntArray(BaseReagentsIdentifier);
		ArrayList<ItemStack> reagents = new ArrayList<ItemStack>();
		for (int i = 0; i < reagentList.length; i += 3){
			reagents.add(new ItemStack(Item.getItemById(reagentList[i]), reagentList[i + 1], reagentList[i + 2]));
		}

		return new SpellRequirements(manaCost, burnout, reagents);
	}

	private void writeSpellRequirements(ItemStack stack, EntityLivingBase caster, SpellRequirements requirements){
		if (!stack.hasTagCompound()) return;

		stack.stackTagCompound.setFloat(BaseManaCostIdentifier, requirements.manaCost);
		stack.stackTagCompound.setFloat(BaseBurnoutIdentifier, requirements.burnout);

		int[] reagentList = new int[requirements.reagents.size() * 3];
		int count = 0;
		for (ItemStack reagentStack : requirements.reagents){
			reagentList[count++] = Item.getIdFromItem(reagentStack.getItem());
			reagentList[count++] = reagentStack.stackSize;
			reagentList[count++] = reagentStack.getMetadata();
		}

		stack.stackTagCompound.setIntArray(BaseReagentsIdentifier, reagentList);
		writeModVersionToStack(stack);
	}

	private boolean spellRequirementsPresent(ItemStack stack, EntityLivingBase caster){
		if (!stack.hasTagCompound()) return false;
		if (isOldVersionSpell(stack)) return false;
		return stack.stackTagCompound.hasKey(BaseManaCostIdentifier) && stack.stackTagCompound.hasKey(BaseBurnoutIdentifier) && stack.stackTagCompound.hasKey(BaseReagentsIdentifier);
	}
	//==============================================================================
	// End Spell Requirements
	//==============================================================================

	/**
	 * Gets the shape group parts from the passed in ItemStack, based on the stack's internal state of current shape group
	 */
	public int[] getShapeGroupParts(ItemStack stack){
		if (!stack.hasTagCompound() || !stack.stackTagCompound.hasKey(CurShapeGroup_Identifier))
			return new int[0];

		int currentShapeGroup = stack.stackTagCompound.getInteger(CurShapeGroup_Identifier);
		return stack.stackTagCompound.getIntArray(String.format("%s%d", ShapeGroup_Identifier, currentShapeGroup));
	}

	public int[] getShapeGroupParts(ItemStack stack, int index){
		if (!stack.hasTagCompound())
			return new int[0];
		return stack.stackTagCompound.getIntArray(String.format("%s%d", ShapeGroup_Identifier, index));
	}

	public void setShapeGroup(ItemStack stack, int shapeGroup){
		if (!stack.hasTagCompound())
			return;
		if (shapeGroup < 0 || shapeGroup >= stack.stackTagCompound.getInteger(NumShapeGroups_Identifier))
			shapeGroup = 0;
		stack.stackTagCompound.setInteger(CurShapeGroup_Identifier, shapeGroup);
		changeEnchantmentsForShapeGroup(stack);
	}

	/**
	 * Gets the next shape group index for the passed in item stack, wrapping around back to 0 when reaching the end
	 */
	public int cycleShapeGroup(ItemStack stack){
		if (!stack.hasTagCompound())
			return 0;
		int current = stack.stackTagCompound.getInteger(CurShapeGroup_Identifier);
		int max = stack.stackTagCompound.getInteger(NumShapeGroups_Identifier);
		if (max == 0)
			return 0;
		return (current + 1) % max;
	}

	public void changeEnchantmentsForShapeGroup(ItemStack stack){
		ItemStack constructed = constructSpellStack(stack);
		int looting = 0;
		int silkTouch = 0;
		for (int i = 0; i < SpellUtils.instance.numStages(constructed); ++i){
			looting += SpellUtils.instance.countModifiers(SpellModifiers.FORTUNE_LEVEL, constructed, i);
			silkTouch += SpellUtils.instance.countModifiers(SpellModifiers.SILKTOUCH_LEVEL, constructed, i);
		}

		AMEnchantmentHelper.fortuneStack(stack, looting);
		AMEnchantmentHelper.lootingStack(stack, looting);
		AMEnchantmentHelper.silkTouchStack(stack, silkTouch);
	}

	public void addShapeGroup(int[] shapeGroupParts, byte[][] metaDatas, ItemStack stack){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		int numShapeGroups = stack.stackTagCompound.getInteger(NumShapeGroups_Identifier) + 1;
		stack.stackTagCompound.setInteger(NumShapeGroups_Identifier, numShapeGroups);
		stack.stackTagCompound.setInteger(CurShapeGroup_Identifier, 0);
		stack.stackTagCompound.setIntArray(String.format("%s%d", ShapeGroup_Identifier, numShapeGroups - 1), shapeGroupParts);
		for (int i = 0; i < metaDatas.length; ++i){
			stack.stackTagCompound.setByteArray(String.format("%s%d%d", ShapeGroupMeta_Identifier, numShapeGroups - 1, i), metaDatas[i]);
		}
	}

	/**
	 * Takes an item stack, looks at the current shape group assigned to it, and constructs a "classic" spell definition from the shape group and the rest of the main spell def
	 *
	 * @param stack The item stack contaning the entire spell def, including all start shape groups
	 * @return An ItemStack that contains a classic spell definition that the casting system can handle.  This is what should be passed to the spell helper class.
	 */
	public ItemStack constructSpellStack(ItemStack stack){
		if (!stack.hasTagCompound() || !stack.stackTagCompound.hasKey(CurShapeGroup_Identifier))
			return stack.copy();

		ItemStack classicStack = new ItemStack(ItemsCommonProxy.spell);
		classicStack.setTagCompound(new NBTTagCompound());

		if (stack.stackTagCompound.hasKey(Global_Spell_Meta))
			classicStack.stackTagCompound.setTag(Global_Spell_Meta, stack.stackTagCompound.getTag(Global_Spell_Meta));

		//this is the starter shapes
		int[] shapeGroup = getShapeGroupParts(stack);

		ArrayList<SpellStageDefinition> newStages = new ArrayList<SpellStageDefinition>();
		int currentShapeGroup = stack.stackTagCompound.getInteger(CurShapeGroup_Identifier);

		//count the number of shapes in the selected shape group - there are this many more stages present
		int shapeGroupElementCount = 0;
		for (int i : shapeGroup){
			ISkillTreeEntry entry = SkillManager.instance.getSkill(i);
			if (entry instanceof ISpellShape){
				newStages.add(new SpellStageDefinition());
				newStages.get(newStages.size() - 1).shape = entry.getID();
			}else if (entry instanceof ISpellModifier){
				byte[] meta = stack.stackTagCompound.getByteArray(String.format("%s%d%d", ShapeGroupMeta_Identifier, currentShapeGroup, shapeGroupElementCount));
				if (meta == null)
					meta = new byte[0];
				newStages.get(newStages.size() - 1).definition.addModifier(SkillManager.instance.getShiftedPartID(entry), meta);
			}
			shapeGroupElementCount++;
		}

		//check the old spell definition - if it starts with MissingShape, then inject the components and modifiers into the last stage from the shape group
		if (numStages(stack) > 0 && newStages.size() > 0){
			SpellStageDefinition last = newStages.get(newStages.size() - 1);
			int firstShape = stack.stackTagCompound.getInteger(Shape_Prefix + "0");
			if (firstShape == SkillManager.instance.missingShape.getID()){
				int[] components = stack.stackTagCompound.getIntArray(Component_Prefix + "0");
				ISpellModifier[] modifiers = getModifiersForStage(stack, 0);
				for (int i : components)
					last.definition.addComponent(i);

				HashMap<Integer, Integer> ordinals = new HashMap<Integer, Integer>();
				for (ISpellModifier modifier : modifiers){
					int ordinal = 0;
					if (ordinals.containsKey(modifier.getID()))
						ordinal = ordinals.get(modifier.getID());
					last.definition.addModifier(modifier.getID() + SkillManager.MODIFIER_OFFSET, getModifierMetadataFromStack(stack, modifier, 0, ordinal));
				}
			}
		}

		//inject the new stages in to the spell scroll
		for (SpellStageDefinition stage : newStages){
			addSpellStageToScroll(classicStack, stage.shape, stage.definition.getComponents(), stage.definition.getModifiers());
			ISkillTreeEntry entry = SkillManager.instance.getSkill(stage.shape);
		}


		//copy the other stages from the original to the new one
		for (int i = 0; i < numStages(stack); ++i){
			SpellStageDefinition def = new SpellStageDefinition();
			def.shape = stack.stackTagCompound.getInteger(Shape_Prefix + i);
			//skip a stage of missing shape as we will have handled it already
			if (def.shape == SkillManager.instance.missingShape.getID())
				continue;
			int[] components = stack.stackTagCompound.getIntArray(Component_Prefix + i);
			for (int c : components)
				def.definition.addComponent(c);
			ISpellModifier[] modifiers = getModifiersForStage(stack, i);
			HashMap<Integer, Integer> ordinals = new HashMap<Integer, Integer>();
			for (ISpellModifier modifier : modifiers){
				int ordinal = 0;
				if (ordinals.containsKey(modifier.getID()))
					ordinal = ordinals.get(modifier.getID());
				def.definition.addModifier(SkillManager.instance.getShiftedPartID(modifier), getModifierMetadataFromStack(stack, modifier, i, ordinal));
				ordinals.put(modifier.getID(), ordinal++);
			}

			addSpellStageToScroll(classicStack, def.shape, def.definition.getComponents(), def.definition.getModifiers());
		}

		return classicStack;
	}

	public ItemStack popStackStage(ItemStack stack){
		if (!stack.hasTagCompound())
			return stack;

		ItemStack workingStack = stack.copy();
		int stages = numStages(workingStack);
		if (stages == 0) return workingStack;
		for (int i = 1; i < stages; ++i){
			workingStack.stackTagCompound.setIntArray(Component_Prefix + (i - 1), workingStack.stackTagCompound.getIntArray(Component_Prefix + i));
			workingStack.stackTagCompound.setIntArray(Modifier_Prefix + (i - 1), workingStack.stackTagCompound.getIntArray(Modifier_Prefix + i));
			workingStack.stackTagCompound.setInteger(Shape_Prefix + (i - 1), workingStack.stackTagCompound.getInteger(Shape_Prefix + i));
		}
		workingStack.stackTagCompound.setInteger(Stages_Identifier, stages - 1);

		workingStack.stackTagCompound.removeTag(Component_Prefix + (stages - 1));
		workingStack.stackTagCompound.removeTag(Modifier_Prefix + (stages - 1));
		workingStack.stackTagCompound.removeTag(Shape_Prefix + (stages - 1));

		return workingStack;
	}

	public int numStages(ItemStack stack){
		if (stack == null || !stack.hasTagCompound())
			return 0;
		int numStages = stack.stackTagCompound.hasKey(Stages_Identifier) ? stack.stackTagCompound.getInteger(Stages_Identifier) : stack.stackTagCompound.getInteger("ShapeOrdinal_");
		return numStages;
	}

	public int numShapeGroups(ItemStack stack){
		if (!stack.hasTagCompound())
			return 0;
		return stack.stackTagCompound.getInteger(NumShapeGroups_Identifier);
	}

	public ISpellComponent[] getComponentsForStage(ItemStack stack, int stage){
		if (stack == null || !stack.hasTagCompound())
			return new ISpellComponent[0];
		int[] componentIDs = stack.stackTagCompound.getIntArray(Component_Prefix + stage);
		ISpellComponent[] components = new ISpellComponent[componentIDs.length];
		int count = 0;
		for (int i : componentIDs){
			ISkillTreeEntry component = SkillManager.instance.getSkill(i);
			if (SkillTreeManager.instance.isSkillDisabled(component)){
				components[count++] = SkillManager.instance.missingComponent;
				continue;
			}
			components[count++] = component != null && component instanceof ISpellComponent ? (ISpellComponent)component : SkillManager.instance.missingComponent;
		}
		return components;
	}

	public ISpellModifier[] getModifiersForStage(ItemStack stack, int stage){
		if (stack == null || !stack.hasTagCompound())
			return new ISpellModifier[0];

		int[] modifierIDs = stack.stackTagCompound.getIntArray(Modifier_Prefix + stage);
		ISpellModifier[] modifiers = new ISpellModifier[modifierIDs.length];
		int count = 0;
		for (int i : modifierIDs){
			ISkillTreeEntry modifier = SkillManager.instance.getSkill(i);
			if (SkillTreeManager.instance.isSkillDisabled(modifier)){
				modifiers[count++] = SkillManager.instance.missingModifier;
				continue;
			}
			modifiers[count++] = modifier != null && modifier instanceof ISpellModifier ? (ISpellModifier)modifier : SkillManager.instance.missingModifier;
		}
		return modifiers;
	}

	public ISpellShape getShapeForStage(ItemStack stack, int stage){
		if (stack == null || !stack.hasTagCompound()) return SkillManager.instance.missingShape;
		int shapeIndex = stack.stackTagCompound.getInteger(Shape_Prefix + stage);
		ISkillTreeEntry shape = SkillManager.instance.getSkill(shapeIndex);

		if (SkillTreeManager.instance.isSkillDisabled(shape))
			return SkillManager.instance.missingShape;

		return shape != null && shape instanceof ISpellShape ? (ISpellShape)shape : SkillManager.instance.missingShape;
	}

	public boolean casterHasAllReagents(EntityLivingBase caster, ArrayList<ItemStack> reagents){
		if (caster instanceof EntityPlayer && ((EntityPlayer)caster).capabilities.isCreativeMode)
			return true;
		return true;
	}

	public boolean casterHasMana(EntityLivingBase caster, float mana){
		if (caster instanceof EntityPlayer && ((EntityPlayer)caster).capabilities.isCreativeMode)
			return true;
		return ExtendedProperties.For(caster).getCurrentMana() + ExtendedProperties.For(caster).getBonusCurrentMana() >= mana;
	}

	public void addSpellStageToScroll(ItemStack scrollStack, int shape, int[] components, ListMultimap<Integer, byte[]> modifiers){
		if (scrollStack.stackTagCompound == null){
			scrollStack.stackTagCompound = new NBTTagCompound();
		}
		//stages are 0-based
		int nextStage = numStages(scrollStack);
		scrollStack.stackTagCompound.setInteger(Stages_Identifier, nextStage + 1);
		scrollStack.stackTagCompound.setInteger(Shape_Prefix + nextStage, shape);
		scrollStack.stackTagCompound.setIntArray(Component_Prefix + nextStage, components);

		int[] modifierarray = new int[modifiers.values().size()];
		int index = 0;
		for (Integer modifierID : modifiers.keySet()){
			int ordinalCount = 0;
			for (byte[] meta : modifiers.get(modifierID)){
				ISpellModifier modifier = SkillManager.instance.getModifier(modifierID);
				if (modifier == SkillManager.instance.missingModifier)
					continue;
				modifierarray[index++] = modifierID.intValue();


				if (meta == null) meta = new byte[0];
				writeModifierMetadataToStack(scrollStack, modifier, nextStage, ordinalCount++, meta);
			}
		}

		scrollStack.stackTagCompound.setIntArray(Modifier_Prefix + nextStage, modifierarray);
	}

	public Affinity mainAffinityFor(ItemStack stack){

		if (!stack.hasTagCompound())
			return Affinity.NONE;

		if (stack.stackTagCompound.hasKey(ForcedAffinity)){
			int aff = stack.stackTagCompound.getInteger(ForcedAffinity);
			return Affinity.values()[aff];
		}

		HashMap<Integer, Integer> affinityFrequency = new HashMap<Integer, Integer>();

		for (int i = 0; i < numStages(stack); ++i){
			for (ISpellComponent comp : getComponentsForStage(stack, i)){
				EnumSet<Affinity> affList = comp.getAffinity();
				for (Affinity affinity : affList){
					if (!affinityFrequency.containsKey(affinity.ordinal())){
						affinityFrequency.put(affinity.ordinal(), 1);
					}else{
						int old = affinityFrequency.get(affinity.ordinal());
						affinityFrequency.put(affinity.ordinal(), old + 1);
					}
				}
			}
		}

		int highestCount = 0;
		int highestID = 0;

		for (Integer key : affinityFrequency.keySet()){
			int count = affinityFrequency.get(key);
			if (count > highestCount){
				highestID = key;
				highestCount = count;
			}
		}

		return Affinity.values()[highestID];
	}

	public void doAffinityShift(EntityLivingBase caster, ISpellComponent component, ISpellShape governingShape){
		if (!(caster instanceof EntityPlayer)) return;
		AffinityData aff = AffinityData.For(caster);
		EnumSet<Affinity> affList = component.getAffinity();
		for (Affinity affinity : affList){
			float shift = component.getAffinityShift(affinity) * aff.getDiminishingReturnsFactor() * 5;
			float xp = 0.05f * aff.getDiminishingReturnsFactor();
			if (governingShape.isChanneled()){
				shift /= 4;
				xp /= 4;
			}

			if (caster instanceof EntityPlayer){
				if (SkillData.For((EntityPlayer)caster).isEntryKnown(SkillTreeManager.instance.getSkillTreeEntry(SkillManager.instance.getSkill("AffinityGains")))){
					shift *= 1.1f;
					xp *= 0.9f;
				}
				ItemStack chestArmor = ((EntityPlayer)caster).getCurrentArmor(2);
				if (chestArmor != null && ArmorHelper.isInfusionPreset(chestArmor, GenericImbuement.magicXP))
					xp *= 1.25f;
			}

			if (shift > 0){
				AffinityChangingEvent event = new AffinityChangingEvent((EntityPlayer)caster, affinity, shift);
				MinecraftForge.EVENT_BUS.post(event);
				if (!event.isCanceled())
					aff.incrementAffinity(affinity, event.amount);
			}
			if (xp > 0){
				xp *= caster.getAttributeMap().getAttributeInstance(ArsMagicaApi.xpGainModifier).getAttributeValue();
				ExtendedProperties.For(caster).addMagicXP(xp);
			}
		}
		aff.addDiminishingReturns(governingShape.isChanneled());
	}

	public HashMap<Affinity, Float> AffinityFor(ItemStack stack){
		HashMap<Affinity, Integer> affinityFrequency = new HashMap<Affinity, Integer>();
		HashMap<Affinity, Float> affinities = new HashMap<Affinity, Float>();
		float totalAffinityEntries = 0;

		if (stack.stackTagCompound.hasKey(ForcedAffinity)){
			int aff = stack.stackTagCompound.getInteger(ForcedAffinity);
			affinities.put(Affinity.values()[aff], 100f);
			return affinities;
		}

		for (int i = 0; i < numStages(stack); ++i){
			for (ISpellComponent comp : getComponentsForStage(stack, i)){
				if (comp == SkillManager.instance.missingComponent)
					continue;
				EnumSet<Affinity> affList = comp.getAffinity();
				for (Affinity affinity : affList){
					totalAffinityEntries++;
					if (!affinityFrequency.containsKey(affinity)){
						affinityFrequency.put(affinity, 1);
					}else{
						int old = affinityFrequency.get(affinity);
						affinityFrequency.put(affinity, old + 1);
					}
				}
			}
		}

		for (Affinity key : affinityFrequency.keySet()){
			int count = affinityFrequency.get(key);
			float percent = (totalAffinityEntries > 0) ? count / totalAffinityEntries : 0;
			affinities.put(key, percent);
		}

		return affinities;
	}

	public void addSpellStageToScroll(ItemStack scrollStack, String shape, String[] components, String[] modifiers){
		int spell_shape = SkillManager.instance.getShiftedPartID(SkillManager.instance.getSkill(shape));
		int[] spell_components = new int[components.length];
		ListMultimap<Integer, byte[]> spell_modifiers = ArrayListMultimap.create();
		for (int i = 0; i < spell_components.length; ++i){
			if (components[i].equals("")) continue;
			spell_components[i] = SkillManager.instance.getShiftedPartID(SkillManager.instance.getSkill(components[i]));
		}
		for (int i = 0; i < modifiers.length; ++i){
			if (modifiers[i].equals("")) continue;
			int modifierID = SkillManager.instance.getShiftedPartID(SkillManager.instance.getSkill(modifiers[i]));
			byte[] meta = new byte[0];
			spell_modifiers.put(modifierID, meta);
		}

		addSpellStageToScroll(scrollStack, spell_shape, spell_components, spell_modifiers);
	}

	/**
	 * Modifies the damage based on the caster's magic level
	 *
	 * @return
	 */
	public float modifyDamage(EntityLivingBase caster, float damage){
		float factor = (float)(ExtendedProperties.For(caster).getMagicLevel() < 20 ?
				0.5 + (0.5 * ExtendedProperties.For(caster).getMagicLevel() / 19) :
				1.0 + (1.0 * (ExtendedProperties.For(caster).getMagicLevel() - 20) / 79));
		return damage * factor;
	}

	public void writeModVersionToStack(ItemStack stack){
		if (!stack.hasTagCompound()) return;
		stack.stackTagCompound.setString("spell_mod_version", AMCore.instance.getVersion());
	}

	public void writeModifierMetadataToStack(ItemStack stack, ISpellModifier modifier, int stage, int ordinal, byte[] meta){
		if (!stack.hasTagCompound()) return;
		String identifier = String.format("%s%d_%d_%d", Modifier_Meta_Prefix, modifier.getID(), stage, ordinal);
		stack.stackTagCompound.setByteArray(identifier, meta);
	}

	public byte[] getModifierMetadataFromStack(ItemStack stack, ISpellModifier modifier, int stage, int ordinal){
		if (!stack.hasTagCompound()) return new byte[0];
		String identifier = String.format("%s%d_%d_%d", Modifier_Meta_Prefix, modifier.getID(), stage, ordinal);
		return stack.stackTagCompound.getByteArray(identifier);
	}

	public int getNextOrdinalForModifier(ItemStack stack, int stage, EnumSet<SpellModifiers> enumSet){
		int ordinalCount = 0;
		for (ISpellModifier modifier : getModifiersForStage(stack, stage)){
			if (modifier.getAspectsModified().contains(enumSet)){
				ordinalCount++;
			}
		}
		return ordinalCount;
	}

	public boolean isOldVersionSpell(ItemStack stack){
		if (!stack.hasTagCompound()) return false;
		String version = stack.stackTagCompound.getString("spell_mod_version");
		return version != AMCore.instance.getVersion();
	}

	public class SpellRequirements{
		public final float manaCost;
		public final float burnout;
		public final ArrayList<ItemStack> reagents;

		public SpellRequirements(float mana, float burnout, ArrayList<ItemStack> reagents){
			manaCost = mana;
			this.burnout = burnout;
			this.reagents = reagents;
		}
	}

	public boolean componentIsPresent(ItemStack stack, Class clazz, int stage){
		if (!stack.hasTagCompound()) return false;

		ISpellComponent[] components = getComponentsForStage(stack, stage);
		for (ISpellComponent comp : components){
			if (comp.getClass() == clazz)
				return true;
		}
		return false;
	}

	public boolean spellIsChanneled(ItemStack stack){
		ISpellShape shape = SpellUtils.instance.getShapeForStage(stack, 0);
		if (numShapeGroups(stack) == 0 || !(shape instanceof MissingShape)){
			return shape.isChanneled();
		}else{
			int[] parts = getShapeGroupParts(stack);
			ISpellShape finalShape = null;
			for (int i : parts){
				ISkillTreeEntry entry = SkillManager.instance.getSkill(i);
				if (entry instanceof ISpellShape){
					finalShape = (ISpellShape)entry;
					break;
				}
			}

			if (finalShape != null)
				return finalShape.isChanneled();
		}
		return false;
	}

	public ItemStack createSpellStack(ArrayList<ArrayList<KeyValuePair<ISpellPart, byte[]>>> shapeGroups, ArrayList<KeyValuePair<ISpellPart, byte[]>> spell){
		ArrayList<KeyValuePair<ISpellPart, byte[]>> recipeCopy = (ArrayList<KeyValuePair<ISpellPart, byte[]>>)spell.clone();

		if (recipeCopy.size() > 0 && !(recipeCopy.get(0).getKey() instanceof ISpellShape))
			recipeCopy.add(0, new KeyValuePair<ISpellPart, byte[]>(SkillManager.instance.missingShape, new byte[0]));

		ItemStack stack = new ItemStack(ItemsCommonProxy.spell);
		boolean hasSummon = false;
		while (recipeCopy.size() > 0){
			ISpellShape shape;
			ArrayList<Integer> components = new ArrayList<Integer>();
			ArrayListMultimap<Integer, byte[]> modifiers = ArrayListMultimap.create();

			KeyValuePair<ISpellPart, byte[]> part = recipeCopy.get(0);
			recipeCopy.remove(0);
			if (part.getKey() instanceof ISpellShape){
				shape = (ISpellShape)part.getKey();

				part = recipeCopy.size() > 0 ? recipeCopy.get(0) : null;
				while (part != null && !(part.getKey() instanceof ISpellShape)){
					recipeCopy.remove(0);
					if (part.getKey() instanceof ISpellComponent){
						components.add(SkillManager.instance.getShiftedPartID(part.getKey()));
						if (part.getKey() instanceof Summon){
							hasSummon = true;
						}
					}else if (part.getKey() instanceof ISpellModifier){
						modifiers.put(SkillManager.instance.getShiftedPartID(part.getKey()), part.getValue());
					}
					part = recipeCopy.size() > 0 ? recipeCopy.get(0) : null;
				}

				if (hasSummon){
					((Summon)SkillManager.instance.getSkill("Summon")).setSummonType(stack, EntitySkeleton.class);
				}

				SpellUtils.instance.addSpellStageToScroll(stack, shape.getID(), ArrayListToIntArray(components), modifiers);
			}
		}

		for (int i = 0; i < shapeGroups.size(); ++i){
			ArrayList<KeyValuePair<ISpellPart, byte[]>> shapeGroup = shapeGroups.get(i);
			if (shapeGroup.size() == 0)
				continue;
			int[] sgp = new int[shapeGroup.size()];
			byte[][] sgp_m = new byte[shapeGroup.size()][];
			for (int n = 0; n < shapeGroup.size(); ++n){
				sgp[n] = SkillManager.instance.getShiftedPartID(shapeGroup.get(n).getKey());
				sgp_m[n] = shapeGroup.get(n).getValue();
			}
			SpellUtils.instance.addShapeGroup(sgp, sgp_m, stack);
		}

		SpellUtils.instance.writeModVersionToStack(stack);

		ItemStack checkStack = constructSpellStack(stack);

		int silkTouchLevel = 0;
		int fortuneLevel = 0;

		for (int i = 0; i < numStages(checkStack); ++i){
			int st = countModifiers(SpellModifiers.SILKTOUCH_LEVEL, checkStack, 0);
			int fn = countModifiers(SpellModifiers.FORTUNE_LEVEL, checkStack, 0);

			if (st > silkTouchLevel)
				silkTouchLevel = st;
			if (fn > fortuneLevel)
				fortuneLevel = fn;
		}

		if (fortuneLevel > 0){
			AMEnchantmentHelper.fortuneStack(stack, fortuneLevel);
			AMEnchantmentHelper.lootingStack(stack, fortuneLevel);
		}
		if (silkTouchLevel > 0)
			AMEnchantmentHelper.silkTouchStack(stack, silkTouchLevel);

		return stack;
	}

	private int[] ArrayListToIntArray(ArrayList<Integer> list){
		int[] arr = new int[list.size()];
		for (int i = 0; i < arr.length; ++i){
			arr[i] = list.get(i).intValue();
		}
		return arr;
	}

	public void setForcedAffinity(ItemStack stack, Affinity aff){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		stack.stackTagCompound.setInteger(ForcedAffinity, aff.ordinal());
	}

	public String getSpellMetadata(ItemStack stack, String key){
		if (!stack.hasTagCompound() || !stack.stackTagCompound.hasKey(Global_Spell_Meta))
			return "";
		NBTTagCompound metaComp = stack.stackTagCompound.getCompoundTag(Global_Spell_Meta);
		return metaComp.getString(key);
	}

	public void setSpellMetadata(ItemStack stack, String string, String s){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound meta = stack.stackTagCompound.getCompoundTag(Global_Spell_Meta);
		meta.setString(string, s);
		stack.stackTagCompound.setTag(Global_Spell_Meta, meta);
	}
}
