package am2.buffs;

import am2.AMCore;
import am2.proxy.CommonProxy;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

import java.util.Random;

public abstract class BuffEffect extends PotionEffect{
	protected boolean InitialApplication;
	protected boolean HasNotified;
	private static float maxExtendDuration = 900; //30 seconds

	public BuffEffect(int buffID, int duration, int amplifier){
		super(buffID, duration, amplifier > 0 ? amplifier - 1 : amplifier);
		InitialApplication = true;
		HasNotified = ((duration / 20) > 5) ? false : true; //disable notification for effects that last less than 5 seconds
	}

	public static boolean SetAmplifier(PotionEffect pe, int amplifier){
		ReflectionHelper.setPrivateValue(PotionEffect.class, pe, amplifier, 2);
		return true;
	}

	public boolean shouldNotify(){
		return true;
	}

	//effect that is performed on initial tick
	public abstract void applyEffect(EntityLivingBase entityliving);

	//effect that is performed when the duration ends
	public abstract void stopEffect(EntityLivingBase entityliving);

	private void effectEnding(EntityLivingBase entityliving){
		BuffList.buffEnding(this.getPotionID());
		stopEffect(entityliving);
	}

	//Effect that is performed on intermediate ticks
	public void performEffect(EntityLivingBase entityliving){
	}

	public void combine(PotionEffect potioneffect){
		//don't combine "potion effects" with other buff effects
		if (!(potioneffect instanceof BuffEffect)){
			return;
		}
		int thisAmplifier = this.getAmplifier();
		if (thisAmplifier >= potioneffect.getAmplifier()){
			super.combine(potioneffect);
			this.HasNotified = false;
		}
	}

	public boolean onUpdate(EntityLivingBase entityliving){
		//check for if we are for the first time applying the effect
		if (InitialApplication){
			InitialApplication = false;
			applyEffect(entityliving);
		}
		//check if we are for the last time applying the effect
		else if (getDuration() <= 1){
			effectEnding(entityliving);
		}else if ((getDuration() / 20) < 5 && !HasNotified && shouldNotify() && !entityliving.worldObj.isRemote){
			HasNotified = true;
		}
		performEffect(entityliving);
		if (AMCore.instance.proxy instanceof CommonProxy){
			//run the base
			return super.onUpdate(entityliving);
		}else{
			return true;
		}
	}

	public boolean isReady(int i, int j){
		int k = 25 >> j;
		if (k > 0){
			return i % k == 0;
		}else{
			return true;
		}
	}

	protected abstract String spellBuffName();

	public String getEffectName(){
		return String.format("Spell: %s", spellBuffName());
	}
}
