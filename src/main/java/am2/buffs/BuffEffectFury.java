package am2.buffs;

import am2.AMCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BuffEffectFury extends BuffEffect{

	public BuffEffectFury(int duration, int amplifier){
		super(BuffList.fury.id, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
		if (!entityliving.worldObj.isRemote){
			AMCore.proxy.addDeferredPotionEffect(entityliving, new PotionEffect(Potion.hunger.id, 200, 1));
			AMCore.proxy.addDeferredPotionEffect(entityliving, new PotionEffect(Potion.confusion.id, 200, 1));
		}
	}

	@Override
	public void combine(PotionEffect potioneffect){
	}

	@Override
	protected String spellBuffName(){
		return "Fury";
	}

}
