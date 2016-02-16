package am2.bosses.ai;

import am2.bosses.BossActions;
import am2.bosses.EntityEnderGuardian;
import am2.bosses.IArsMagicaBoss;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

public class EntityAIShadowstep extends AIAnimation{

	private int cooldownTicks = 0;

	public EntityAIShadowstep(EntityAIBase entity){
		super((IAnimatedEntity) entity);
	}

	@Override
	public int getAnimID(){
		return BossActions.SPINNING.ordinal();
	}

	@Override
	public boolean isAutomatic(){
		return false;
	}

	@Override
	public int getDuration(){
		return 9;
	}

	@Override
	public boolean shouldExecute(){
		//accessor method in AIAnimation that gives access to the entity
		EntityLiving living = getEntity();

		//must have an attack target
		if (living.getAttackTarget() == null) return false;

		return cooldownTicks-- <= 0;
	}

	@Override
	public void resetTask(){
		cooldownTicks = 30;
		EntityEnderGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			Vec3 facing = guardian.getAttackTarget().getLook(1.0f);
			double x = guardian.getAttackTarget().posX - facing.xCoord * 3;
			double y = guardian.getAttackTarget().posY;
			double z = guardian.getAttackTarget().posZ - facing.zCoord * 3;

			guardian.setPosition(x, y, z);
			guardian.lastTickPosX = x;
			guardian.lastTickPosY = y;
			guardian.lastTickPosZ = z;
			guardian.worldObj.playSoundAtEntity(guardian, ((IArsMagicaBoss)guardian).getAttackSound(), 1.0f, (float)(0.5 + guardian.getRNG().nextDouble() * 0.5f));
		}
		super.resetTask();
	}

	@Override
	public void updateTask(){
		EntityEnderGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			guardian.getLookHelper().setLookPositionWithEntity(guardian.getAttackTarget(), 30, 30);
		}
	}
}
