package am2.buffs;

import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class ManaPotion extends ArsMagicaPotion{

	protected ManaPotion(ResourceLocation name, boolean par2, int par3){
		super(name, par2, par3);
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
		if ("mana_restoration_greater".equals(this.registryName.getResourcePath())){
			manaRestored = 500;
		}else if ("mana_restoration_epic".equals(this.registryName.getResourcePath())){
			manaRestored = 1000;
		}else if ("mana_restoration_legendary".equals(this.registryName.getResourcePath())){
			manaRestored = 2500;
		}

		if (amplifier == 1) manaRestored *= 1.25f;

		return manaRestored;
	}

	@Override
	public void affectEntity(Entity potion, Entity thrower, EntityLivingBase target, int amplifier, double distance){
		float manaRestored = getManaRestored(amplifier);

		ExtendedProperties.For(target).setCurrentMana(ExtendedProperties.For(target).getCurrentMana() + manaRestored);
		ExtendedProperties.For(target).forceSync();
	}

	@Override
	public void performEffect(EntityLivingBase par1EntityLiving, int amplifier){
		float manaRestored = getManaRestored(amplifier);

		ExtendedProperties.For(par1EntityLiving).setCurrentMana(ExtendedProperties.For(par1EntityLiving).getCurrentMana() + manaRestored);
	}
}
