package am2.entities.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class EntityAITargetNearbyInanimateSmeltable extends EntityAITargetNearbyInanimate{

	public EntityAITargetNearbyInanimateSmeltable(EntityCreature taskOwner, float targetDistance, boolean needsLineofSight){
		super(taskOwner, targetDistance, needsLineofSight, EntityItem.class);
	}

	@Override
	protected boolean isSuitableTarget(Entity target){
		if (super.isSuitableTarget(target)){
			if (target instanceof EntityItem){
				if (((EntityItem)target).getEntityItem().stackSize > 1) return false;
				ItemStack smelted = FurnaceRecipes.instance().getSmeltingResult(((EntityItem)target).getEntityItem());
				return smelted != null;
			}
		}
		return false;
	}

}
