package am2.buffs;

import am2.network.AMNetHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class BuffEffectFlight extends BuffEffect{

	public BuffEffectFlight(int duration, int amplifier){
		super(BuffList.flight.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){

	}

	@Override
	public void performEffect(EntityLivingBase entityliving){
		if (entityliving instanceof EntityPlayerMP){
			((EntityPlayer)entityliving).capabilities.allowFlying = true;
			if (getDuration() % 20 == 0)
				AMNetHandler.INSTANCE.sendCapabilityChangePacket((EntityPlayerMP)entityliving, 1, true);
		}
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
		if (entityliving instanceof EntityPlayerMP){
			if (!((EntityPlayer)entityliving).capabilities.isCreativeMode){
				((EntityPlayer)entityliving).capabilities.allowFlying = false;
				((EntityPlayer)entityliving).capabilities.isFlying = false;
				((EntityPlayer)entityliving).fallDistance = 0f;
				AMNetHandler.INSTANCE.sendCapabilityChangePacket((EntityPlayerMP)entityliving, 1, false);
				AMNetHandler.INSTANCE.sendCapabilityChangePacket((EntityPlayerMP)entityliving, 2, false);
			}
		}
	}

	@Override
	protected String spellBuffName(){
		return "Flight";
	}

}
