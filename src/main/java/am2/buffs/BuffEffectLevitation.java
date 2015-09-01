package am2.buffs;

import am2.AMCore;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class BuffEffectLevitation extends BuffEffect {

	public BuffEffectLevitation(int duration, int amplifier) {
		super(BuffList.levitation.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving) {
		if (entityliving instanceof EntityPlayer){
			((EntityPlayer)entityliving).capabilities.allowFlying = true;
		}
	}
	
	@Override
	public void performEffect(EntityLivingBase entityliving) {
		if (entityliving instanceof EntityPlayer){
			((EntityPlayer)entityliving).capabilities.allowFlying = true;
			if (((EntityPlayer)entityliving).capabilities.isFlying){
				float factor = 0.4f;
				entityliving.motionX *= factor;
				entityliving.motionZ *= factor;
				entityliving.motionY *= 0.0001f;
			}
		}
		
		if (entityliving instanceof EntityPlayerMP){
			((EntityPlayer)entityliving).capabilities.allowFlying = true;
			if (getDuration() % 20 == 0)
				AMNetHandler.INSTANCE.sendCapabilityChangePacket((EntityPlayerMP) entityliving, 1, true);
		}
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving) {
		if (entityliving instanceof EntityPlayerMP){
			if (!((EntityPlayer)entityliving).capabilities.isCreativeMode){
				((EntityPlayer)entityliving).capabilities.allowFlying = false;
				((EntityPlayer)entityliving).capabilities.isFlying = false;
				((EntityPlayer)entityliving).fallDistance = 0f;
				AMNetHandler.INSTANCE.sendCapabilityChangePacket((EntityPlayerMP) entityliving, 1, false);
				AMNetHandler.INSTANCE.sendCapabilityChangePacket((EntityPlayerMP) entityliving, 2, false);
			}
		}
	}

	@Override
	protected String spellBuffName() {
		return "Levitation";
	}

}
