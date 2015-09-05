package am2.buffs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class BuffEffectSwiftSwim extends BuffEffect{

	public BuffEffectSwiftSwim(int duration, int amplifier){
		super(BuffList.swiftSwim.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
	}

	@Override
	public void performEffect(EntityLivingBase entityliving){
		if (entityliving.isInWater()){
			if (!(entityliving instanceof EntityPlayer) || !((EntityPlayer)entityliving).capabilities.isFlying){
				entityliving.motionX *= (1.133f + 0.03 * this.getAmplifier());
				entityliving.motionZ *= (1.133f + 0.03 * this.getAmplifier());

				if (entityliving.motionY > 0){
					entityliving.motionY *= 1.134;
				}
			}
		}
	}

	@Override
	protected String spellBuffName(){
		return "Swift Swim";
	}

}
