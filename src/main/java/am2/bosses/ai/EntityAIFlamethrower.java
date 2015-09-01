package am2.bosses.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import am2.bosses.BossActions;
import am2.bosses.IArsMagicaBoss;

public class EntityAIFlamethrower extends EntityAIBase{

	private final EntityLiving host;
	private int cooldownTicks = 0;
	
	public EntityAIFlamethrower(IArsMagicaBoss host){
		this.host = (EntityLiving)host;
		this.setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {		
		boolean execute = ((IArsMagicaBoss)host).getCurrentAction() == BossActions.IDLE && host.getAttackTarget() != null && cooldownTicks-- <= 0;
		return execute;
	}
	
	@Override
	public boolean continueExecuting() {
		if (host.getAttackTarget() == null || host.getAttackTarget().isDead || ((IArsMagicaBoss)host).getTicksInCurrentAction() > 80){
			this.cooldownTicks = 40;
			((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
			return false;
		}
		return true;
	}
	
	@Override
	public void updateTask() {
		if (host.getDistanceSqToEntity(host.getAttackTarget()) < 64){
			if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.LONG_CASTING)
				((IArsMagicaBoss)host).setCurrentAction(BossActions.LONG_CASTING);
			host.getLookHelper().setLookPositionWithEntity(host.getAttackTarget(), 10, 10);
			host.getNavigator().clearPathEntity();
		}else{
			double deltaZ = host.getAttackTarget().posZ - host.posZ;
			double deltaX = host.getAttackTarget().posX - host.posX;
			
			double angle = -Math.atan2(deltaZ, deltaX);
			
			double newX = host.getAttackTarget().posX + (Math.cos(angle) * 4);
			double newZ = host.getAttackTarget().posZ + (Math.sin(angle) * 4);
			
			host.getNavigator().tryMoveToXYZ(newX, host.getAttackTarget().posY, newZ, 0.5f);
		}
	}

}
