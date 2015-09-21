package am2.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceWind extends EntityDamageSource{
	public DamageSourceWind(EntityLivingBase source){
		super("DamageAMWind", source);
		this.setDamageBypassesArmor();
	}
}
