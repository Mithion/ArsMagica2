package am2.bosses.ai;

import am2.bosses.BossActions;
import am2.bosses.IArsMagicaBoss;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;

import java.util.List;

public class EntityAISpinAttack<T> extends EntityAIBase{

	private final EntityLiving host;
	private final float moveSpeed;
	private EntityLivingBase target;
	private int cooldownTicks = 0;
	private final float damage;

	public EntityAISpinAttack(IArsMagicaBoss host, float moveSpeed, float damage){
		this.host = ((EntityLiving)host);
		this.moveSpeed = moveSpeed;
		this.setMutexBits(1);
		this.damage = damage;
	}

	@Override
	public boolean shouldExecute(){
		if (cooldownTicks-- > 0 || ((IArsMagicaBoss)host).getCurrentAction() != BossActions.IDLE || !((IArsMagicaBoss)host).isActionValid(BossActions.SPINNING))
			return false;
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead || AITarget.getDistanceSqToEntity(host) > 25) return false;
		this.target = AITarget;
		((IArsMagicaBoss)host).setCurrentAction(BossActions.SPINNING);
		return true;
	}

	@Override
	public boolean continueExecuting(){
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead || ((IArsMagicaBoss)host).getTicksInCurrentAction() > BossActions.SPINNING.getMaxActionTime()){
			resetTask();
			return false;
		}
		return true;
	}

	@Override
	public void resetTask(){
		((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
		cooldownTicks = 150;
		
		/*if (host.worldObj.isRemote)
			SoundHelper.instance.stopSound("arsmagica2:mob.natureguardian.whirlloop");*/

		super.resetTask();
	}

	@Override
	public void updateTask(){
		host.getLookHelper().setLookPositionWithEntity(target, 30, 30);
		host.getNavigator().tryMoveToEntityLiving(target, moveSpeed);
		List<EntityLivingBase> nearbyEntities = host.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, host.getEntityBoundingBox().expand(2, 2, 2));
		for (EntityLivingBase ent : nearbyEntities){
			if (ent == host) continue;
			ent.attackEntityFrom(DamageSource.causeMobDamage(host), damage);
		}

		if (((IArsMagicaBoss)host).getTicksInCurrentAction() % 50 == 0){
			if (!host.worldObj.isRemote)
				host.worldObj.playSoundAtEntity(host, "arsmagica2:mob.natureguardian.whirlloop", 1.0f, 1.0f);
		}
	}
}
