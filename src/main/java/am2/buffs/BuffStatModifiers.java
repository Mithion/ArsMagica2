package am2.buffs;

import am2.utility.KeyValuePair;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.potion.Potion;

import java.util.UUID;

public class BuffStatModifiers{
	public static final BuffStatModifiers instance = new BuffStatModifiers();

	public void applyStatModifiersBasedOnBuffs(EntityLivingBase entity){
		//entangled
		applyOrRemoveModifiersForBuff(entity, BuffList.entangled.id, new KeyValuePair(SharedMonsterAttributes.movementSpeed, entangled));
		//frost slow
		applyOrRemoveModifiersForBuff(entity, BuffList.frostSlowed.id, 0, new KeyValuePair(SharedMonsterAttributes.movementSpeed, frostSlow_Diminished));
		applyOrRemoveModifiersForBuff(entity, BuffList.frostSlowed.id, 1, new KeyValuePair(SharedMonsterAttributes.movementSpeed, frostSlow_Normal));
		applyOrRemoveModifiersForBuff(entity, BuffList.frostSlowed.id, 2, new KeyValuePair(SharedMonsterAttributes.movementSpeed, frostSlow_Augmented));
		//fury
		applyOrRemoveModifiersForBuff(entity, BuffList.fury.id, new KeyValuePair(SharedMonsterAttributes.movementSpeed, furyMoveMod), new KeyValuePair(SharedMonsterAttributes.attackDamage, furyDmgMod));
		//haste
		applyOrRemoveModifiersForBuff(entity, BuffList.haste.id, 0, new KeyValuePair(SharedMonsterAttributes.movementSpeed, hasteSpeedBoost_Diminished));
		applyOrRemoveModifiersForBuff(entity, BuffList.haste.id, 1, new KeyValuePair(SharedMonsterAttributes.movementSpeed, hasteSpeedBoost_Normal));
		applyOrRemoveModifiersForBuff(entity, BuffList.haste.id, 2, new KeyValuePair(SharedMonsterAttributes.movementSpeed, hasteSpeedBoost_Augmented));
	}

	private void applyOrRemoveModifiersForBuff(EntityLivingBase entity, int buffID, KeyValuePair<IAttribute, AttributeModifier>... modifiers){
		if (entity.isPotionActive(buffID)){
			applyAllModifiers(entity, modifiers);
		}else{
			clearAllModifiers(entity, modifiers);
		}
	}

	private void applyOrRemoveModifiersForBuff(EntityLivingBase entity, int buffID, int magnitude, KeyValuePair<IAttribute, AttributeModifier>... modifiers){
		if (entity.isPotionActive(buffID)){
			if (entity.getActivePotionEffect(Potion.potionTypes[buffID]).getAmplifier() == magnitude)
				applyAllModifiers(entity, modifiers);
		}else{
			clearAllModifiers(entity, modifiers);
		}
	}

	private void applyAllModifiers(EntityLivingBase entity, KeyValuePair<IAttribute, AttributeModifier>... modifiers){
		for (KeyValuePair<IAttribute, AttributeModifier> entry : modifiers){
			IAttributeInstance inst = entity.getEntityAttribute(entry.getKey());
			if (inst == null)
				continue;
			if (inst.getModifier(entry.getValue().getID()) == null)
				inst.applyModifier(entry.getValue());
		}
	}

	private void clearAllModifiers(EntityLivingBase entity, KeyValuePair<IAttribute, AttributeModifier>... modifiers){
		for (KeyValuePair<IAttribute, AttributeModifier> entry : modifiers){
			IAttributeInstance inst = entity.getEntityAttribute(entry.getKey());
			if (inst == null)
				continue;
			inst.removeModifier(entry.getValue());
		}
	}

	//*  Fury  *//
	private static final UUID furyMoveID = UUID.fromString("03B0A79B-9769-43AE-BFE3-830D993D4A69");
	private static final AttributeModifier furyMoveMod = (new AttributeModifier(furyMoveID, "Fury (Movement)", 2, 2));

	private static final UUID furyDmgID = UUID.fromString("03B0A79B-9769-43AE-BFE3-830D993D4A70");
	private static final AttributeModifier furyDmgMod = (new AttributeModifier(furyDmgID, "Fury (Damage)", 5, 2));

	//*  Entangle  *//
	private static final UUID entangledID = UUID.fromString("F5047292-E5F9-4EB1-986E-9A5DFE832203");
	private static final AttributeModifier entangled = (new AttributeModifier(entangledID, "Entangled", -10, 2));

	//*  Haste *//
	private static final UUID hasteID = UUID.fromString("CA4D2B5D-D509-49C0-9B2C-C0A338883AB1");
	private static final AttributeModifier hasteSpeedBoost_Diminished = (new AttributeModifier(hasteID, "Haste Speed Boost (Diminished)", 0.3D, 2));
	private static final AttributeModifier hasteSpeedBoost_Normal = (new AttributeModifier(hasteID, "Haste Speed Boost (Normal)", 0.6D, 2));
	private static final AttributeModifier hasteSpeedBoost_Augmented = (new AttributeModifier(hasteID, "Haste Speed Boost (Augmented)", 1.2D, 2));

	//*  Frost Slow *//
	private static final UUID frostSlowID = UUID.fromString("03B0A79B-9569-43AE-BFE3-820D993D4A64");
	private static final AttributeModifier frostSlow_Diminished = (new AttributeModifier(frostSlowID, "Frost Slow (Diminished)", -0.2, 2));
	private static final AttributeModifier frostSlow_Normal = (new AttributeModifier(frostSlowID, "Frost Slow (Normal)", -0.5, 2));
	private static final AttributeModifier frostSlow_Augmented = (new AttributeModifier(frostSlowID, "Frost Slow (Augmented)", -0.8, 2));
}
