package am2.entities.ai;

import am2.AMCore;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import java.util.Random;

public class EntityAIManaDrainBolt extends EntityAIBase{

	World worldObj;

	/**
	 * The entity the AI instance has been applied to
	 */
	EntityCreature entityHost;
	EntityLivingBase attackTarget;

	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	int rangedAttackTime;
	float moveSpeed;
	int stuckTime;
	int damage;
	int manaDrainedPerCasterLevel;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	int maxRangedAttackTime;

	public EntityAIManaDrainBolt(EntityCreature par1EntityLiving, float moveSpeed, int attackTime, int damage, int manaDrained){
		rangedAttackTime = 0;
		stuckTime = 0;
		entityHost = par1EntityLiving;
		worldObj = par1EntityLiving.worldObj;
		this.moveSpeed = moveSpeed;
		maxRangedAttackTime = attackTime;
		setMutexBits(3);
		this.damage = damage;
		this.manaDrainedPerCasterLevel = manaDrained;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute(){
		EntityLivingBase entityliving = entityHost.getAttackTarget();

		if (entityliving == null){
			return false;
		}else{
			attackTarget = entityliving;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting(){
		return shouldExecute() || !entityHost.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask(){
		attackTarget = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask(){
		double d = 25D; //5 blocks away
		double d1 = entityHost.getDistanceSq(attackTarget.posX, attackTarget.boundingBox.minY, attackTarget.posZ);
		boolean flag = entityHost.getEntitySenses().canSee(attackTarget);

		if (flag){
			stuckTime++;
		}else{
			stuckTime = 0;
		}

		if (d1 > d || stuckTime < 20){
			entityHost.getNavigator().tryMoveToEntityLiving(attackTarget, moveSpeed);
		}else{
			entityHost.getNavigator().clearPathEntity();
		}

		entityHost.getLookHelper().setLookPositionWithEntity(attackTarget, 30F, 30F);
		rangedAttackTime = Math.max(rangedAttackTime - 1, 0);

		if (rangedAttackTime > 0){
			return;
		}

		if (d1 > d || !flag){
			return;
		}else{
			doRangedAttack();
			rangedAttackTime = maxRangedAttackTime;
			return;
		}
	}

	/**
	 * Performs a ranged attack according to the AI's rangedAttackID.
	 */
	private void doRangedAttack(){
		//43% chance to "miss"
		Random rand = new Random();
		int chanceToMiss = entityHost.isPotionActive(Potion.moveSpeed) ? 10 : 43;
		if (rand.nextInt(100) < chanceToMiss){
			AMCore.instance.proxy.particleManager.BoltFromPointToPoint(worldObj, entityHost.posX, entityHost.posY + entityHost.getEyeHeight(), entityHost.posZ, attackTarget.posX + rand.nextFloat() - 0.5f, attackTarget.posY + attackTarget.getEyeHeight() + rand.nextFloat() - 0.5f, attackTarget.posZ + rand.nextFloat() - 0.5f, 2, -1);
		}else{
			AMCore.instance.proxy.particleManager.BoltFromEntityToEntity(worldObj, entityHost, entityHost, attackTarget, this.damage, 2, -1);
			float manaDrained = this.manaDrainedPerCasterLevel * ExtendedProperties.For(attackTarget).getMagicLevel();
			ExtendedProperties.For(attackTarget).setCurrentMana(ExtendedProperties.For(attackTarget).getCurrentMana() - (manaDrained));
			ExtendedProperties.For(attackTarget).forceSync();

			attackTarget.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entityHost, entityHost), this.damage);

			if (manaDrained > 100){
				entityHost.heal(1);
				if (entityHost.worldObj.difficultySetting == EnumDifficulty.HARD){
					attackTarget.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 40, 1, true));
					entityHost.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 40, 3, true));
				}
			}
		}
	}

}
