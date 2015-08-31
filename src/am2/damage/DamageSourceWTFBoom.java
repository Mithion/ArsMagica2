package am2.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class DamageSourceWTFBoom extends DamageSource{

	protected DamageSourceWTFBoom(String par1String) {
		super(par1String);
		this.setDamageAllowedInCreativeMode();
		this.setDamageBypassesArmor();
	}
	
	public DamageSourceWTFBoom(){
		this("cactus");
	}
	
	@Override
	public IChatComponent func_151519_b(EntityLivingBase par1EntityLivingBase) {
		if (par1EntityLivingBase instanceof EntityPlayer){
			return new ChatComponentText(String.format(StatCollector.translateToLocal("am2.death.wtfboom"), ((EntityPlayer)par1EntityLivingBase).getCommandSenderName()));
		}
		return super.func_151519_b(par1EntityLivingBase);
	}
}
