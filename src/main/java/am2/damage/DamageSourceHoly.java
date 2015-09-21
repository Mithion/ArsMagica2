package am2.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceHoly extends EntityDamageSource{
	public DamageSourceHoly(EntityLivingBase source){
		super("am2.holy", source);
		this.setDamageBypassesArmor();
	}
}
