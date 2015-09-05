package am2.bosses.ai;

import am2.api.math.AMVector3;
import am2.bosses.BossActions;
import am2.bosses.EntityEnderGuardian;
import am2.bosses.IArsMagicaBoss;
import am2.utility.MathUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class EntityAIEnderRush extends AIAnimation{
	private int cooldownTicks = 0;

	public EntityAIEnderRush(IAnimatedEntity entity){
		super(entity);
	}

	@Override
	public int getAnimID(){
		return BossActions.CHARGE.ordinal();
	}

	@Override
	public boolean isAutomatic(){
		return false;
	}

	@Override
	public int getDuration(){
		return 30;
	}

	@Override
	public boolean shouldAnimate(){
		//accessor method in AIAnimation that gives access to the entity
		EntityLiving living = getEntity();

		//must have an attack target
		if (living.getAttackTarget() == null) return false;

		return cooldownTicks-- <= 0;
	}

	@Override
	public void resetTask(){
		cooldownTicks = 60;
		super.resetTask();
	}

	@Override
	public void updateTask(){
		EntityEnderGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			guardian.getLookHelper().setLookPositionWithEntity(guardian.getAttackTarget(), 30, 30);
		}
		if (guardian.getTicksInCurrentAction() >= 18 && guardian.getTicksInCurrentAction() <= 30 && guardian.getAttackTarget() != null){
			AMVector3 a = new AMVector3(guardian);
			AMVector3 b = new AMVector3(guardian.getAttackTarget());
			if (a.distanceSqTo(b) > 4){
				AMVector3 movement = MathUtilities.GetMovementVectorBetweenPoints(a, b);
				float speed = -5f;
				guardian.moveEntity(movement.x * speed, movement.y * speed, movement.z * speed);
			}else{
				guardian.worldObj.playSoundAtEntity(guardian, ((IArsMagicaBoss)guardian).getAttackSound(), 1.0f, (float)(0.5 + guardian.getRNG().nextDouble() * 0.5f));
				if (guardian.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(guardian), 15) && guardian.getAttackTarget().getHealth() <= 0)
					guardian.heal(200);
			}
		}
	}
}
