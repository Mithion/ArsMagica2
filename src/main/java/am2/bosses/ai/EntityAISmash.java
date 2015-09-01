package am2.bosses.ai;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import am2.AMCore;
import am2.bosses.BossActions;
import am2.bosses.IArsMagicaBoss;
import am2.damage.DamageSources;
import am2.entities.EntityShockwave;
import am2.network.AMNetHandler;

public class EntityAISmash extends EntityAIBase{

	EntityLiving host;
	private EntityLivingBase target;
	private final float moveSpeed;
	private int cooldownTicks = 0;
	private final DamageSources.DamageSourceTypes damageType;

	public EntityAISmash(IArsMagicaBoss host, float moveSpeed, DamageSources.DamageSourceTypes damageType){
		this.host = (EntityLiving) host;
		this.moveSpeed = moveSpeed;
		this.setMutexBits(1);
		this.damageType = damageType;
	}

	@Override
	public boolean shouldExecute() {
		if (cooldownTicks-- > 0 || ((IArsMagicaBoss)host).getCurrentAction() != BossActions.IDLE || !((IArsMagicaBoss)host).isActionValid(BossActions.SMASH)) return false;
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget == null || AITarget.isDead) return false;
		if (AITarget != null && host.getDistanceSqToEntity(AITarget) > 4D){
			if (!host.getNavigator().tryMoveToEntityLiving(AITarget, moveSpeed))
				return false;
		}
		this.target = AITarget;
		return true;
	}

	@Override
	public boolean continueExecuting() {
		EntityLivingBase AITarget = host.getAttackTarget();
		if (AITarget != null && host.getDistanceSqToEntity(AITarget) > 4D){
			if (host.onGround)
				return host.getNavigator().tryMoveToEntityLiving(AITarget, moveSpeed); 
		}
		if (AITarget == null || AITarget.isDead || (((IArsMagicaBoss)host).getCurrentAction() == BossActions.SMASH && ((IArsMagicaBoss)host).getTicksInCurrentAction() > ((IArsMagicaBoss)host).getCurrentAction().getMaxActionTime())){
			((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
			cooldownTicks = 100;
			return false;
		}
		return true;
	}

	@Override
	public void updateTask() {
		host.getLookHelper().setLookPositionWithEntity(host.getAttackTarget(), 30, 30);		
		host.getNavigator().tryMoveToEntityLiving(target, moveSpeed);
		if (host.getDistanceSqToEntity(target) < 16)
			if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.SMASH)
				((IArsMagicaBoss)host).setCurrentAction(BossActions.SMASH);

		if (((IArsMagicaBoss)host).getCurrentAction() == BossActions.SMASH && ((IArsMagicaBoss)host).getTicksInCurrentAction() == 18){
			
			if (!host.worldObj.isRemote)
				host.worldObj.playSoundAtEntity(host, ((IArsMagicaBoss)host).getAttackSound(), 1.0f, 1.0f);
			
			List<EntityLivingBase> aoeEntities = host.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, host.boundingBox.expand(4, 2, 4));
			for (EntityLivingBase ent : aoeEntities){
				if (ent == host) continue;
				ent.attackEntityFrom(DamageSources.causeDamage(damageType, host, true), 8);					
				if (ent instanceof EntityPlayer){
					AMNetHandler.INSTANCE.sendVelocityAddPacket(host.worldObj, ent, 0, 1.3f, 0);
				}else{
					ent.addVelocity(0, 1.4f, 0);
				}				
			}
			if (!host.worldObj.isRemote){
				for (int i = 0; i < 4; ++i){
					EntityShockwave shockwave = new EntityShockwave(host.worldObj);
					shockwave.setPosition(host.posX, host.posY, host.posZ);
					shockwave.setMoveSpeedAndAngle(0.5f, MathHelper.wrapAngleTo180_float(host.rotationYaw + (90 * i)));
					host.worldObj.spawnEntityInWorld(shockwave);
				}
			}
		}
	}
}
