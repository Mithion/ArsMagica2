package am2.bosses.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import am2.bosses.BossActions;
import am2.bosses.IArsMagicaBoss;
import am2.entities.EntityWhirlwind;

public class EntityAISpawnWhirlwind extends EntityAIBase{
	private final EntityLiving host;
	private int cooldownTicks = 0;
	private boolean hasCasted = false;
	private int windTicks = 0;

	public EntityAISpawnWhirlwind(IArsMagicaBoss host){
		this.host = (EntityLiving)host;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		cooldownTicks--;
		boolean execute = ((IArsMagicaBoss)host).getCurrentAction() == BossActions.IDLE && host.getAttackTarget() != null && cooldownTicks <= 0;
		if (execute) hasCasted = false;
		return execute;
	}

	@Override
	public boolean continueExecuting() {
		return !hasCasted && host.getAttackTarget() != null && !host.getAttackTarget().isDead;	
	}
	
	@Override
	public void resetTask() {
		((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
		cooldownTicks = 10;
		hasCasted = true;
		windTicks = 0;
	}

	@Override
	public void updateTask() {
		host.getLookHelper().setLookPositionWithEntity(host.getAttackTarget(), 30, 30);
		if (host.getDistanceSqToEntity(host.getAttackTarget()) > 64){
			
			double deltaZ = host.getAttackTarget().posZ - host.posZ;
			double deltaX = host.getAttackTarget().posX - host.posX;
			
			double angle = -Math.atan2(deltaZ, deltaX);
			
			double newX = host.getAttackTarget().posX + (Math.cos(angle) * 6);
			double newZ = host.getAttackTarget().posZ + (Math.sin(angle) * 6);
			
			host.getNavigator().tryMoveToXYZ(newX, host.getAttackTarget().posY, newZ, 0.5f);
		}else if (!host.canEntityBeSeen(host.getAttackTarget())){
			host.getNavigator().tryMoveToEntityLiving(host.getAttackTarget(), 0.5f);
		}else{
			if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.CASTING)
				((IArsMagicaBoss)host).setCurrentAction(BossActions.CASTING);		

			windTicks++;
			if (windTicks == 12 && !host.worldObj.isRemote){		
				if (!host.worldObj.isRemote)
					host.worldObj.playSoundAtEntity(host, ((IArsMagicaBoss)host).getAttackSound(), 1.0f, 1.0f);
				EntityWhirlwind whirlwind = new EntityWhirlwind(host.worldObj);
				whirlwind.setPosition(host.posX, host.posY + host.getEyeHeight(), host.posZ);
				host.worldObj.spawnEntityInWorld(whirlwind);
			}			
		}
		if (windTicks >= 23){
			resetTask();
		}
	}

}
