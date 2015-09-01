package am2.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class DamageSourceFrost extends EntityDamageSource{
	public DamageSourceFrost(EntityLivingBase source){
		super("DamageAMFrost", source);
		this.setDamageBypassesArmor();
	}
	
	@Override
	public IChatComponent func_151519_b(EntityLivingBase par1EntityLivingBase) {
		if (par1EntityLivingBase instanceof EntityPlayer){
			return new ChatComponentText(String.format(StatCollector.translateToLocal("am2.death.frost"), ((EntityPlayer)par1EntityLivingBase).getCommandSenderName()));
		}
		return super.func_151519_b(par1EntityLivingBase);
	}

	@Override
	public boolean canHarmInCreative() {
		return false;
	}
}
