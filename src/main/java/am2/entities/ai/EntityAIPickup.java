package am2.entities.ai;

import am2.api.math.AMVector3;
import am2.entities.EntityBroom;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;

public class EntityAIPickup extends EntityAIBase{

	private EntityBroom host;
	private AMVector3 lastLocation;
	private int stuckTicks = 0;

	public EntityAIPickup(EntityBroom host){
		this.host = host;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute(){
		Entity target = ExtendedProperties.For(host).getInanimateTarget();
		if (!(target instanceof EntityItem))
			return false;

		if (target == null || target.isDead)
			return false;

		return host.hasRoomInInventoryFor(((EntityItem)target).getEntityItem());
	}

	@Override
	public void resetTask(){
		super.resetTask();
		lastLocation = null;
		stuckTicks = 0;
	}

	@Override
	public boolean continueExecuting(){
		return shouldExecute();
	}

	@Override
	public void updateTask(){
		Entity target = ExtendedProperties.For(host).getInanimateTarget();
		if (target == null) return;

		if (checkStuck()){
			ExtendedProperties.For(host).setInanimateTarget(null);

			double x = host.posX + host.worldObj.rand.nextInt(8) - 4;
			double y = host.posY;
			double z = host.posZ + host.worldObj.rand.nextInt(8) - 4;

			while (host.worldObj.isAirBlock((int)x, (int)y, (int)z) && y > 5){
				y--;
			}

			host.getNavigator().tryMoveToXYZ(x, y, z, 0.4f);
			resetTask();
			return;
		}

		if (lastLocation.distanceSqTo(new AMVector3(target)) > 4){
			if (!host.getNavigator().tryMoveToEntityLiving(target, 0.4f) || !host.getEntitySenses().canSee(target)){
				ExtendedProperties.For(host).setInanimateTarget(null);
				resetTask();
			}
		}else{
			host.addItemStackToInventory(((EntityItem)target).getEntityItem());
			target.setDead();
			ExtendedProperties.For(host).setInanimateTarget(null);
		}
	}

	private boolean checkStuck(){
		AMVector3 loc = new AMVector3(host);
		if (lastLocation == null){
			lastLocation = loc;
			return false;
		}

		if (lastLocation.distanceSqTo(loc) < 1){
			stuckTicks++;
			if (stuckTicks > 40){
				return true;
			}
		}

		lastLocation = loc;
		return false;
	}

}
