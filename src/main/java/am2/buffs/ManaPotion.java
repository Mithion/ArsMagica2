package am2.buffs;

import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.EntityLivingBase;

public class ManaPotion extends ArsMagicaPotion{

	protected ManaPotion(int par1, boolean par2, int par3){
		super(par1, par2, par3);
	}

	@Override
	public boolean isInstant(){
		return true;
	}

	public boolean isReady(int par1, int par2){
		return par1 >= 1;
	}

	private float getManaRestored(int amplifier){
		float manaRestored = 0;
		if (this.id == BuffList.greaterManaPotion.id){
			manaRestored = 500;
		}else if (this.id == BuffList.epicManaPotion.id){
			manaRestored = 1000;
		}else if (this.id == BuffList.legendaryManaPotion.id){
			manaRestored = 2500;
		}

		if (amplifier == 1) manaRestored *= 1.25f;

		return manaRestored;
	}

	@Override
	public void affectEntity(EntityLivingBase par1EntityLiving, EntityLivingBase par2EntityLiving, int amplifier, double distanceToImpact){
		float manaRestored = getManaRestored(amplifier);

		ExtendedProperties.For(par2EntityLiving).setCurrentMana(ExtendedProperties.For(par2EntityLiving).getCurrentMana() + manaRestored);
		ExtendedProperties.For(par2EntityLiving).forceSync();
	}

	@Override
	public void performEffect(EntityLivingBase par1EntityLiving, int amplifier){
		float manaRestored = getManaRestored(amplifier);

		ExtendedProperties.For(par1EntityLiving).setCurrentMana(ExtendedProperties.For(par1EntityLiving).getCurrentMana() + manaRestored);
	}
}
