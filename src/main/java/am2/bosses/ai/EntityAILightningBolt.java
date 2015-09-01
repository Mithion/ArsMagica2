package am2.bosses.ai;

import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;


import am2.AMCore;
import am2.bosses.BossActions;
import am2.bosses.EntityLightningGuardian;
import am2.damage.DamageSources;

public class EntityAILightningBolt extends AIAnimation {

	private int cooldownTicks = 0;

	public EntityAILightningBolt(IAnimatedEntity entity) {
		super(entity);
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldAnimate() {
		//accessor method in AIAnimation that gives access to the entity
		EntityLiving living = getEntity();

		//must have an attack target
		if(living.getAttackTarget() == null || !living.getEntitySenses().canSee(living.getAttackTarget())) return false;

		return cooldownTicks-- <= 0;
	}

	@Override
	public int getAnimID() {
		return BossActions.STRIKE.ordinal();
	}

	@Override
	public boolean isAutomatic() {
		return false;
	}

	@Override
	public int getDuration() {
		return 15;
	}

	@Override
	public void resetTask() {
		cooldownTicks = 3;
		super.resetTask();
	}

	@Override
	public void updateTask() {
		EntityLightningGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			guardian.getLookHelper().setLookPositionWithEntity(guardian.getAttackTarget(), 30, 30);
			if (guardian.getTicksInCurrentAction() == 7){
				doStrike();
				if (!guardian.worldObj.isRemote)
					guardian.worldObj.playSoundAtEntity(guardian, "arsmagica2:mob.lightningguardian.attack", 1.0f, guardian.getRNG().nextFloat() * 0.5f + 0.5f);
			}
		}
	}

	private void doStrike(){
		EntityLightningGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null && guardian.getEntitySenses().canSee(guardian.getAttackTarget())){
			if (guardian.getDistanceSqToEntity(guardian.getAttackTarget()) > 400){
				guardian.getNavigator().tryMoveToEntityLiving(guardian.getAttackTarget(), 0.5f);
				return;
			}
			guardian.getNavigator().clearPathEntity();
			if (guardian.getRNG().nextDouble() > 0.2f){
				AMCore.proxy.particleManager.BoltFromEntityToEntity(guardian.worldObj, guardian, guardian, guardian.getAttackTarget(), 0);
				guardian.getAttackTarget().attackEntityFrom(DamageSources.causeEntityLightningDamage(guardian), 3);
				if (guardian.getAttackTarget() instanceof EntityPlayer){
					EntityPlayer player = (EntityPlayer)guardian.getAttackTarget();
					if (player.capabilities.isFlying)
						player.capabilities.isFlying = false;
					if (player.isRiding())
						player.dismountEntity(player.ridingEntity);
				}
			}else{
				AMCore.proxy.particleManager.BoltFromEntityToPoint(guardian.worldObj, guardian, guardian.getAttackTarget().posX - 0.5 + guardian.getRNG().nextDouble(), guardian.getAttackTarget().posY - 0.5 + guardian.getRNG().nextDouble() + guardian.getAttackTarget().getEyeHeight(), guardian.getAttackTarget().posZ - 0.5 + guardian.getRNG().nextDouble());
			}
		}
	}

}
