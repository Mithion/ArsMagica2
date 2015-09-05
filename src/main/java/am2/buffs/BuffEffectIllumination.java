package am2.buffs;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.entity.EntityLivingBase;

public class BuffEffectIllumination extends BuffEffect{

	public BuffEffectIllumination(int duration, int amplifier){
		super(BuffList.illumination.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	public void performEffect(EntityLivingBase entityliving){
		if (!entityliving.worldObj.isRemote && entityliving.ticksExisted % 10 == 0){
			if (entityliving.worldObj.isAirBlock((int)entityliving.posX, (int)(entityliving.posY + entityliving.getEyeHeight()), (int)entityliving.posZ)){
				entityliving.worldObj.setBlock((int)entityliving.posX, (int)(entityliving.posY + entityliving.getEyeHeight()), (int)entityliving.posZ, BlocksCommonProxy.invisibleUtility, getAmplifier(), 2);
			}
		}
	}

	@Override
	protected String spellBuffName(){
		return "Illumination";
	}

}
