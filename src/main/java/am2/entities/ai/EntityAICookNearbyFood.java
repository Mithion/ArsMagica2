package am2.entities.ai;

import am2.entities.EntityFireElemental;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class EntityAICookNearbyFood extends EntityAIBase{

	private EntityFireElemental entityHost;
	private int timeSpentCooking;

	public EntityAICookNearbyFood(EntityFireElemental entityHost){
		this.entityHost = entityHost;
	}

	@Override
	public boolean shouldExecute(){
		return ExtendedProperties.For(entityHost).getInanimateTarget() != null && !ExtendedProperties.For(entityHost).getInanimateTarget().isDead;
	}

	@Override
	public boolean continueExecuting(){
		return ExtendedProperties.For(entityHost).getInanimateTarget() != null && entityHost.getAttackTarget() != null && !ExtendedProperties.For(entityHost).getInanimateTarget().isDead;
	}

	@Override
	public void resetTask(){
		super.resetTask();
	}

	@Override
	public void updateTask(){
		Entity inanimate = ExtendedProperties.For(entityHost).getInanimateTarget();
		double distance = this.entityHost.getDistanceSqToEntity(inanimate);

		if (distance > 9D){
			this.entityHost.getNavigator().tryMoveToXYZ(inanimate.posX, inanimate.posY, inanimate.posZ, this.entityHost.getAIMoveSpeed());
		}else{
			this.entityHost.getNavigator().clearPathEntity();
			this.entityHost.getLookHelper().setLookPositionWithEntity(inanimate, 30.0F, 30.0F);

			if (timeSpentCooking < 30){
				timeSpentCooking++;
				if (entityHost.getDataWatcher().getWatchableObjectInt(19) != inanimate.getEntityId()){
					entityHost.getDataWatcher().updateObject(19, inanimate.getEntityId());
				}
			}else{
				ItemStack smelted = FurnaceRecipes.instance().getSmeltingResult(((EntityItem)inanimate).getEntityItem());

				timeSpentCooking = 0;
				ExtendedProperties.For(entityHost).setInanimateTarget(null);
				entityHost.getDataWatcher().updateObject(19, 0);
				if (smelted != null){
					EntityItem item = new EntityItem(inanimate.worldObj, inanimate.posX, inanimate.posY, inanimate.posZ, new ItemStack(smelted.getItem(), smelted.stackSize));
					entityHost.worldObj.spawnEntityInWorld(item);

					inanimate.setDead();
				}
			}
		}
	}
}
