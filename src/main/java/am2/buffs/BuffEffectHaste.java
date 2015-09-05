package am2.buffs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

import java.util.UUID;

public class BuffEffectHaste extends BuffEffect{

	private static final UUID hasteID = UUID.fromString("CA4D2B5D-D509-49C0-9B2C-C0A338883AB1");
	private static final AttributeModifier hasteSpeedBoost_Diminished = (new AttributeModifier(hasteID, "Haste Speed Boost (Diminished)", 0.2D, 2));
	private static final AttributeModifier hasteSpeedBoost_Normal = (new AttributeModifier(hasteID, "Haste Speed Boost (Normal)", 0.45D, 2));
	private static final AttributeModifier hasteSpeedBoost_Augmented = (new AttributeModifier(hasteID, "Haste Speed Boost (Augmented)", 0.9D, 2));

	public BuffEffectHaste(int duration, int amplifier){
		super(BuffList.haste.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
		IAttributeInstance attributeinstance = entityliving.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (attributeinstance.getModifier(hasteID) != null){
			attributeinstance.removeModifier(hasteSpeedBoost_Diminished);
			attributeinstance.removeModifier(hasteSpeedBoost_Normal);
			attributeinstance.removeModifier(hasteSpeedBoost_Augmented);
		}

		switch (this.getAmplifier()){
		case 0:
			attributeinstance.applyModifier(hasteSpeedBoost_Diminished);
			break;
		case 1:
			attributeinstance.applyModifier(hasteSpeedBoost_Normal);
			break;
		case 2:
			attributeinstance.applyModifier(hasteSpeedBoost_Augmented);
			break;
		}
	}

	@Override
	public void performEffect(EntityLivingBase entityliving){

	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
		IAttributeInstance attributeinstance = entityliving.getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (attributeinstance.getModifier(hasteID) != null){
			attributeinstance.removeModifier(hasteSpeedBoost_Diminished);
			attributeinstance.removeModifier(hasteSpeedBoost_Normal);
			attributeinstance.removeModifier(hasteSpeedBoost_Augmented);
		}
	}

	@Override
	protected String spellBuffName(){
		return "Haste";
	}

}
