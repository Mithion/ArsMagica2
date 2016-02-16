package am2.bosses.ai;

import am2.bosses.BossActions;
import am2.bosses.EntityWinterGuardian;
import am2.entities.EntityWinterGuardianArm;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class EntityWinterGuardianLaunchArm extends EntityAIBase{
	private final EntityWinterGuardian host;
	private final float moveSpeed;
	private EntityLivingBase target;
	private int cooldownTicks = 0;

	public EntityWinterGuardianLaunchArm(EntityWinterGuardian host, float moveSpeed){
		this.host = host;
		this.moveSpeed = moveSpeed;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute(){
		if (cooldownTicks-- > 0 || host.getAttackTarget() == null) return false;
		if (host.getAttackTarget().getDistanceSqToEntity(host) > 16D){
			target = host.getAttackTarget();
			return true;
		}
		List<EntityLivingBase> entities = host.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, host.getEntityBoundingBox().expand(20, 20, 20));
		if (entities.size() > 0){
			for (EntityLivingBase entity : entities){
				if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode) continue;
				if (entity.getDistanceSqToEntity(host) > 49D){
					target = entity;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean continueExecuting(){
		if (target == null || target.isDead || host.getDistanceSqToEntity(target) < 49D || host.getDistanceSqToEntity(target) > 225D || (host.getCurrentAction() == BossActions.LAUNCHING && host.getTicksInCurrentAction() > host.getCurrentAction().getMaxActionTime())){
			host.setCurrentAction(BossActions.IDLE);
			target = null;
			if (!host.hasLeftArm() && !host.hasRightArm())
				cooldownTicks = 20;
			return false;
		}
		return true;
	}

	@Override
	public void updateTask(){
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		if (host.getDistanceSqToEntity(target) > 144 && host.getCurrentAction() == BossActions.IDLE){
			host.getNavigator().tryMoveToEntityLiving(target, moveSpeed);
		}else{
			host.getNavigator().clearPathEntity();
			if (host.getCurrentAction() != BossActions.LAUNCHING)
				host.setCurrentAction(BossActions.LAUNCHING);

			if (host.getTicksInCurrentAction() == 14){
				host.faceEntity(target, 180, 180);
				if (!host.worldObj.isRemote){

					if (!host.worldObj.isRemote)
						host.worldObj.playSoundAtEntity(host, "arsmagica2:mob.winterguardian.launcharm", 1.0f, 1.0f);

					EntityWinterGuardianArm projectile = new EntityWinterGuardianArm(host.worldObj, host, 1.25f);
					projectile.setThrowingEntity(host);
					projectile.setProjectileSpeed(2.0);
					host.worldObj.spawnEntityInWorld(projectile);
				}
			}
		}
	}
}
