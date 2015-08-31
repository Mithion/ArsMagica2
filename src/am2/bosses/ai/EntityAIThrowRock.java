package am2.bosses.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import am2.bosses.BossActions;
import am2.bosses.IArsMagicaBoss;
import am2.entities.EntityThrownRock;

public class EntityAIThrowRock extends EntityAIBase{
	private final EntityLiving host;	
	private final float moveSpeed;
	private EntityLivingBase target;
	private int cooldownTicks = 0;

	public EntityAIThrowRock(IArsMagicaBoss host, float moveSpeed){
		this.host = (EntityLiving) host;
		this.moveSpeed = moveSpeed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (cooldownTicks-- > 0 || ((IArsMagicaBoss) host).getCurrentAction() != BossActions.IDLE) return false;
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead) return false;
		this.target = AITarget;
		return true;
	}

	@Override
	public boolean continueExecuting() {
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead || (((IArsMagicaBoss) host).getCurrentAction() == BossActions.THROWING_ROCK && ((IArsMagicaBoss) host).getTicksInCurrentAction() > ((IArsMagicaBoss) host).getCurrentAction().getMaxActionTime())){
			((IArsMagicaBoss) host).setCurrentAction(BossActions.IDLE);
			cooldownTicks = 50;
			return false;
		}
		return true;
	}

	@Override
	public void updateTask() {
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		if (host.getDistanceSqToEntity(target) > 100){
			host.getNavigator().tryMoveToEntityLiving(target, moveSpeed);
		}else{
			host.getNavigator().clearPathEntity();
			if (((IArsMagicaBoss) host).getCurrentAction() != BossActions.THROWING_ROCK)
				((IArsMagicaBoss) host).setCurrentAction(BossActions.THROWING_ROCK);

			if (((IArsMagicaBoss) host).getTicksInCurrentAction() == 27){
				
				if (!host.worldObj.isRemote)
					host.worldObj.playSoundAtEntity(host, ((IArsMagicaBoss)host).getAttackSound(), 1.0f, 1.0f);
				
				host.faceEntity(target, 180, 180);
				if (!host.worldObj.isRemote){		
					EntityThrownRock projectile = new EntityThrownRock(host.worldObj, host, 2.0f);
					host.worldObj.spawnEntityInWorld(projectile);
				}
			}
		}
	}
}
