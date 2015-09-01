package am2.bosses.ai;

import java.util.List;

import am2.bosses.BossActions;
import am2.bosses.EntityNatureGuardian;
import am2.bosses.EntityWaterGuardian;
import am2.bosses.IArsMagicaBoss;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;

public class EntityAICloneSelf extends EntityAIBase{
	private final EntityWaterGuardian host;	
	private int cooldownTicks = 0;

	public EntityAICloneSelf(EntityWaterGuardian host){
		this.host =  host;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (cooldownTicks-- > 0 || host.getCurrentAction() != BossActions.IDLE || !host.isActionValid(BossActions.CLONE)) return false;
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead) return false;
		return true;
	}

	@Override
	public boolean continueExecuting() {
		if (host.getCurrentAction() == BossActions.CLONE && host.getTicksInCurrentAction() > host.getCurrentAction().getMaxActionTime()){
			host.setCurrentAction(BossActions.IDLE);
			cooldownTicks = 200;
			return false;
		}
		return true;
	}

	@Override
	public void updateTask() {
		if (host.getCurrentAction() != BossActions.CLONE)
			host.setCurrentAction(BossActions.CLONE);
		
		if (!host.worldObj.isRemote && host.getCurrentAction() == BossActions.CLONE && host.getTicksInCurrentAction() == 30){
			EntityWaterGuardian clone1 = spawnClone();
			EntityWaterGuardian clone2 = spawnClone();
			
			host.setClones(clone1, clone2);
		}
	}
	
	private EntityWaterGuardian spawnClone(){
		EntityWaterGuardian clone = new EntityWaterGuardian(host.worldObj);
		clone.setMaster(host);
		clone.setPosition(host.posX, host.posY, host.posZ);
		host.worldObj.spawnEntityInWorld(clone);
		return clone;
	}
}
