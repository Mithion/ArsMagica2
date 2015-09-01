package am2.entities.ai;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.pathfinding.PathEntity;
import am2.playerextensions.ExtendedProperties;

public class EntityAITargetNearbyInanimate extends EntityAITarget {

	private float targetDistance;

	public EntityAITargetNearbyInanimate(EntityCreature taskOwner, float targetDistance, boolean needsLineofSight, Class ... classes) {
		super(taskOwner, needsLineofSight);
		targetTypes = classes;
		this.targetDistance = targetDistance;
	}

	private Entity target;
	private int timeSinceLastSight;
	private Class[] targetTypes;

	@Override
	public boolean shouldExecute() {
		return (taskOwner.getAttackTarget() == null && ExtendedProperties.For(taskOwner).getInanimateTarget() == null);
	}

	@Override
	public boolean continueExecuting()
	{
		Entity inanimateTarget = ExtendedProperties.For(taskOwner).getInanimateTarget();

		if (this.taskOwner.getAttackTarget() != null || inanimateTarget == null || inanimateTarget.isDead)
		{
			return false;
		}

		else if (this.taskOwner.getDistanceSqToEntity(inanimateTarget) > this.targetDistance * this.targetDistance)
		{
			return false;
		}
		else
		{
			if (this.shouldCheckSight)
			{
				if (this.taskOwner.getEntitySenses().canSee(inanimateTarget))
				{
					this.timeSinceLastSight = 0;
				}
				else if (++this.timeSinceLastSight > 60)
				{
					return false;
				}
			}

			return true;
		}
	}

	@Override
	public void resetTask()
	{
		ExtendedProperties.For(taskOwner).setInanimateTarget(null);
		this.target = null;
	}

	protected boolean isSuitableTarget(Entity target){
		if (target.isDead) return false;
		for (Class c : targetTypes)
			if (target.getClass() == c) return true;
		return false;
	}

	@Override
	public void startExecuting()
	{
		double dist = 10000;
		for (Class c : targetTypes){
			List<Entity> potentialTargets = taskOwner.worldObj.getEntitiesWithinAABB(c, taskOwner.boundingBox.expand(targetDistance, 1, targetDistance));
			for (Entity e : potentialTargets){
				if (isSuitableTarget(e)){ //sanity check
					PathEntity pe = taskOwner.getNavigator().getPathToXYZ(e.posX, e.posY, e.posZ); //can we get to the item?
					if (pe != null){
						double eDist = taskOwner.getDistanceSqToEntity(e);
						if (eDist < dist)
						{
							this.target = e;
							dist = eDist;
						}
					}
				}
			}
		}
		if (this.target != null)
			ExtendedProperties.For(taskOwner).setInanimateTarget(this.target);
		super.startExecuting();
	}

}
