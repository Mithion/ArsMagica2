package am2.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceFrost extends EntityDamageSource{
	public DamageSourceFrost(EntityLivingBase source){
		super("am2.frost", source);
		this.setDamageBypassesArmor();
	}
}
