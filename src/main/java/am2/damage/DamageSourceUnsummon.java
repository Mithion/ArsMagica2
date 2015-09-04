package am2.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class DamageSourceUnsummon extends DamageSource{

	public DamageSourceUnsummon(){
		super("DamageAMUnsummon");
	}

	@Override
	public IChatComponent func_151519_b(EntityLivingBase par1EntityLivingBase){
		if (par1EntityLivingBase instanceof EntityPlayer){
			return new ChatComponentText(String.format(StatCollector.translateToLocal("am2.death.backfire"), ((EntityPlayer)par1EntityLivingBase).getCommandSenderName()));
		}
		return super.func_151519_b(par1EntityLivingBase);
	}

	@Override
	public boolean canHarmInCreative(){
		return true;
	}
}
