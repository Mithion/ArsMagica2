package am2.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;

public class EntityAIRangedAttackSpell extends EntityAIBase {
	World worldObj;

	/** The entity the AI instance has been applied to */
	EntityCreature entityHost;
	EntityLivingBase attackTarget;

	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
	 * maxRangedAttackTime.
	 */
	int rangedAttackTime;
	float field_48370_e;
	int field_48367_f;

	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	int maxRangedAttackTime;

	ItemStack spellStack;

	public EntityAIRangedAttackSpell(EntityCreature host, float moveSpeed, int cooldown, String shape, String[] components, String[] modifiers)
	{
		rangedAttackTime = 0;
		field_48367_f = 0;
		entityHost = host;
		worldObj = host.worldObj;
		field_48370_e = moveSpeed;
		maxRangedAttackTime = cooldown;
		setMutexBits(3);

		this.spellStack = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(spellStack, shape, components, modifiers);
	}

	public EntityAIRangedAttackSpell(EntityCreature host, float moveSpeed, int cooldown, ItemStack spellStack)
	{
		rangedAttackTime = 0;
		field_48367_f = 0;
		entityHost = host;
		worldObj = host.worldObj;
		field_48370_e = moveSpeed;
		maxRangedAttackTime = cooldown;
		this.spellStack = spellStack;
		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		EntityLivingBase entityliving = entityHost.getAttackTarget();

		if (entityliving == null)
		{
			return false;
		}
		else
		{
			attackTarget = entityliving;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		return shouldExecute() && !entityHost.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		attackTarget = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		double d = 225D;
		double d1 = entityHost.getDistanceSq(attackTarget.posX, attackTarget.boundingBox.minY, attackTarget.posZ);
		boolean flag = entityHost.getEntitySenses().canSee(attackTarget);

		if (flag)
		{
			field_48367_f++;
		}
		else
		{
			field_48367_f = 0;
		}

		if (d1 > d || field_48367_f > 20)
		{
			double deltaZ = attackTarget.posZ - entityHost.posZ;
			double deltaX = attackTarget.posX - entityHost.posX;

			double angle = -Math.atan2(deltaZ, deltaX);

			double newX = attackTarget.posX + (Math.cos(angle) * 6);
			double newZ = attackTarget.posZ + (Math.sin(angle) * 6);

			if (!entityHost.getNavigator().tryMoveToXYZ(newX, attackTarget.posY, newZ, 0.5f)){
				entityHost.getNavigator().clearPathEntity();
				entityHost.setAttackTarget(null);
			}
		}
		else
		{
			entityHost.getNavigator().clearPathEntity();
		}

		entityHost.getLookHelper().setLookPositionWithEntity(attackTarget, 30F, 30F);

		rangedAttackTime = Math.max(rangedAttackTime - 1, 0);

		if (rangedAttackTime > 0)
		{
			return;
		}

		if (d1 > d || !flag)
		{
			return;
		}
		else
		{
			doRangedAttack();
			rangedAttackTime = maxRangedAttackTime;
			return;
		}
	}

	/**
	 * Performs a ranged attack according to the AI's rangedAttackID.
	 */
	private void doRangedAttack()
	{
		if (entityHost.getAttackTarget() == null)
			return;
		entityHost.faceEntity(entityHost.getAttackTarget(), 180, 180);
		SpellHelper.instance.applyStackStage(spellStack, entityHost, entityHost.getAttackTarget(), entityHost.posX, entityHost.posY, entityHost.posZ, 0, entityHost.worldObj, true, false, 0);
		entityHost.swingItem();
	}
}
