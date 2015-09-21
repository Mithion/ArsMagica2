package am2.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceLightning extends EntityDamageSource{
	public DamageSourceLightning(EntityLivingBase source){
		super("am2.lightning", source);
		this.setDamageBypassesArmor();
	}
}
