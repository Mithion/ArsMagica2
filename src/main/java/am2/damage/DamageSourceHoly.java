package am2.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;

public class DamageSourceHoly extends EntityDamageSource{
	public DamageSourceHoly(EntityLivingBase source){
		super("DamageAM2Holy", source);
		this.setDamageBypassesArmor();
	}

	@Override
	public IChatComponent func_151519_b(EntityLivingBase par1EntityLivingBase){
		if (par1EntityLivingBase instanceof EntityPlayer){
			return new ChatComponentText(String.format("Seriously?  How did %s manage to die to healing?  This shouldn't ever happen!", ((EntityPlayer)par1EntityLivingBase).getCommandSenderName()));
		}
		return super.func_151519_b(par1EntityLivingBase);
	}

	@Override
	public boolean canHarmInCreative(){
		return false;
	}
}
