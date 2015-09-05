package am2.entities.ai;

import am2.api.math.AMVector3;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIGuardSpawnLocation extends EntityAIBase{

	private final EntityCreature theGuard;
	World theWorld;
	private final float moveSpeed;
	private final PathNavigate guardPathfinder;
	private int field_48310_h;
	float maxDist;
	float minDist;
	private boolean field_48311_i;
	private final AMVector3 spawnLocation;

	public EntityAIGuardSpawnLocation(EntityCreature par1EntityMob, float moveSpeed, float minDist, float maxDist, AMVector3 spawn){
		theGuard = par1EntityMob;
		theWorld = par1EntityMob.worldObj;
		this.moveSpeed = moveSpeed;
		guardPathfinder = par1EntityMob.getNavigator();
		this.minDist = minDist;
		this.maxDist = maxDist;
		this.spawnLocation = spawn;
		setMutexBits(3);
	}

	public double getDistanceSqToSpawnXZ(){
		double d = theGuard.posX - spawnLocation.x;
		double d2 = theGuard.posZ - spawnLocation.z;
		return d * d + d2 * d2;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute(){
		if (getDistanceSqToSpawnXZ() < minDist * minDist){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting(){
		return !guardPathfinder.noPath() && getDistanceSqToSpawnXZ() > maxDist * maxDist;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting(){
		field_48310_h = 0;
		field_48311_i = theGuard.getNavigator().getAvoidsWater();
		theGuard.getNavigator().setAvoidsWater(false);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask(){
		guardPathfinder.clearPathEntity();
		theGuard.getNavigator().setAvoidsWater(field_48311_i);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask(){
		theGuard.getLookHelper().setLookPosition(spawnLocation.x, spawnLocation.y, spawnLocation.z, 10F, theGuard.getVerticalFaceSpeed());


		if (--field_48310_h > 0){
			return;
		}

		field_48310_h = 10;


		if (guardPathfinder.tryMoveToXYZ(spawnLocation.x, spawnLocation.y, spawnLocation.z, moveSpeed)){
			return;
		}

		if (getDistanceSqToSpawnXZ() < 144D){
			return;
		}

		int i = MathHelper.floor_double(spawnLocation.x) - 2;
		int j = MathHelper.floor_double(spawnLocation.z) - 2;
		int k = MathHelper.floor_double(spawnLocation.y);

		for (int l = 0; l <= 4; l++){
			for (int i1 = 0; i1 <= 4; i1++){
				Block block = theWorld.getBlock(i + l, k - 1, j + i1);
				Block otherBlock = theWorld.getBlock(i + l, k + 1, j + i1);
				if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.theWorld.doesBlockHaveSolidTopSurface(theWorld, i + l, k - 1, j + i1) && !otherBlock.isBlockNormalCube()){
					this.theGuard.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.theGuard.rotationYaw, this.theGuard.rotationPitch);
					this.guardPathfinder.clearPathEntity();
					return;
				}
			}
		}
	}

}
