package am2.navigation;

import am2.api.math.AMVector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class PathNavigator{
	private BreadCrumb pathData;
	private Point3D currentWaypoint;
	private final EntityLivingBase pathEntity;
	private int PathUpdateTicks;
	private Point3D currentLocation;
	private Point3D lastLocation;
	private int ticksStuck;
	private Point3D longRangeWaypoint;
	private boolean pathIsLongRange;

	private static final int MaxPathDistance = 19;

	public PathNavigator(EntityLivingBase entity){
		this.pathEntity = entity;
		PathUpdateTicks = 0;
		currentWaypoint = null;
		pathData = null;
		pathIsLongRange = false;
	}

	public boolean HasWaypoint(){
		return currentWaypoint != null;
	}

	public boolean HasPath(){
		return pathData != null;
	}

	public void tryMoveFlying(World world, Entity entity){
		if (pathEntity.isDead){
			return;
		}
		getEntityLocation(world);
		if (!HasWaypoint() || !HasPath()){
			return;
		}
		if (PathUpdateTicks++ >= 20){
			PathUpdateTicks = 0;
			GetPathToWaypoint(world, entity);
		}

		if (!HasPath()){
			return;
		}

		FaceEntityToBreadCrumb();
		MoveEntityTowardBreadCrumbFlying();
		checkDistance(world, entity);
		checkStuck();
	}

	public void GenerateNewRandomWaypoint(World world, Entity entity){
		int newX, newY, newZ;

		for (int i = 0; i < 5; ++i){
			newX = (int)Math.round(pathEntity.posX + (world.rand.nextDouble() * MaxPathDistance - (MaxPathDistance / 2)));
			newY = (int)Math.round(pathEntity.posY + (world.rand.nextDouble() * 8 - (4)));
			newZ = (int)Math.round(pathEntity.posZ + (world.rand.nextDouble() * MaxPathDistance - (MaxPathDistance / 2)));

			BlockPos pos = new BlockPos(newX, newY, newZ);
			if (world.getBlockState(pos).getBlock() == Blocks.air && newY > 5){
				getEntityLocation(world);
				SetWaypoint(world, newX, newY, newZ, entity);
				GetPathToWaypoint(world, entity);
				break;
			}
		}
	}

	public void GenerateNewRandomWaypoint(World world, Entity entity, int minY, int maxY){
		int newX, newY, newZ;

		for (int i = 0; i < 5; ++i){
			newX = (int)Math.round(pathEntity.posX + (world.rand.nextDouble() * MaxPathDistance - (MaxPathDistance / 2)));
			newY = (int)Math.round(minY + (world.rand.nextDouble() * (maxY - minY)));
			newZ = (int)Math.round(pathEntity.posZ + (world.rand.nextDouble() * MaxPathDistance - (MaxPathDistance / 2)));
			BlockPos pos = new BlockPos(newX, newY, newZ);
			if (world.getBlockState(pos).getBlock() == Blocks.air && newY > 5){
				getEntityLocation(world);
				SetWaypoint(world, newX, newY, newZ, entity);
				GetPathToWaypoint(world, entity);
				break;
			}
		}
	}

	public void GenerateNewRandomWaypoint(World world){
		GenerateNewRandomWaypoint(world, null);
	}

	public void GenerateNewRandomWaypoint(World world, int minY, int maxY){
		GenerateNewRandomWaypoint(world, null, minY, maxY);
	}

	public boolean GenerateWaypointToEntity(Entity entityTarget, World world, Entity entity){
		if (pathEntity.getDistanceSqToEntity(entityTarget) > 225){
			return false;
		}
		getEntityLocation(world);
		SetWaypoint(world, (int)entityTarget.posX - 1, (int)entityTarget.posY, (int)entityTarget.posZ - 1, entity);
		return true;
	}

	public boolean GenerateWaypointToEntity(Entity entityTarget, World world){
		return GenerateWaypointToEntity(entityTarget, world, null);
	}

	public void SetWaypoint(World world, int x, int y, int z, Entity entity){

		getEntityLocation(world);

		this.currentWaypoint = new Point3D(x, y, z);
		this.longRangeWaypoint = new Point3D(x, y, z);

		Point3D estimatedLocation = Point3D.fromDoubleCoordinates(pathEntity.posX, pathEntity.posY, pathEntity.posZ);

		//is the path too far?  Do we need to set up a long range waypoint?
		if (estimatedLocation.GetDistanceSq(this.longRangeWaypoint) > 400){
			this.pathIsLongRange = true;
			double posX;
			double posZ;
			double posY = pathEntity.posY;
			double angle;

			float moveSpeed = 15;

			//calculate deltas
			double deltaZ = (longRangeWaypoint.z + 0.5f) - pathEntity.posZ;
			double deltaX = (longRangeWaypoint.x + 0.5f) - pathEntity.posX;
			double deltaY = posY - (longRangeWaypoint.y + 0.5f);
			double horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);

			float radiansYaw = (float)Math.atan2(deltaZ, deltaX);
			float radiansPitch = (float)(-Math.atan2(deltaY, horizontalDistance));

			posX = pathEntity.posX + MathHelper.cos(radiansYaw) * moveSpeed;
			posZ = pathEntity.posZ + MathHelper.sin(radiansYaw) * moveSpeed;
			posY = pathEntity.posY + MathHelper.sin(radiansPitch) * moveSpeed;

			while (!BlockIsAir(world, Point3D.fromDoubleCoordinates(posX, posY, posZ))){
				posY++;
			}
			currentWaypoint = Point3D.fromDoubleCoordinates(posX, posY, posZ);
		}
		GetPathToWaypoint(world, entity);
	}

	private void MoveEntityTowardBreadCrumbFlying(){
		double posX;
		double posZ;
		double posY = pathEntity.posY;

		float moveSpeed = pathEntity.getAIMoveSpeed() * 2;


		AMVector3 current = new AMVector3(pathEntity.posX, pathEntity.posY, pathEntity.posZ);
		AMVector3 target = new AMVector3(pathData.position.x + 0.5f, pathData.position.y + 0.5f, pathData.position.z + 0.5f);

		AMVector3 movement = target.sub(current).normalize();

		pathEntity.moveEntity(movement.x * moveSpeed, movement.y * moveSpeed, movement.z * moveSpeed);
	}

	private void checkDistance(World world, Entity entity){
		double distance = currentLocation.GetDistanceSq(pathData.position);
		if (distance < 0.8f){
			pathData = pathData.next;
			if (pathData != null){
				pathData.unshift();
			}else{
				//here we are at the end of the path sequence
				if (pathIsLongRange){
					SetWaypoint(world, this.longRangeWaypoint.x, this.longRangeWaypoint.y, this.longRangeWaypoint.z, entity);
					GetPathToWaypoint(world, entity);
				}
			}
		}
	}

	private void FaceEntityToBreadCrumb(){
		if (!HasPath() || !HasWaypoint()){
			return;
		}
		double distanceToTarget = currentLocation.GetDistanceSq(pathData.position);
		double deltaZ = currentWaypoint.z - currentLocation.z;
		double deltaX = currentWaypoint.x - currentLocation.x;
		double angle = Math.atan2(deltaZ, deltaX) * 180 / Math.PI;

		pathEntity.rotationYaw = (float)angle;
	}

	private void getEntityLocation(World world){

		currentLocation = new Point3D((int)Math.floor(pathEntity.posX), (int)Math.floor(pathEntity.posY), (int)Math.floor(pathEntity.posZ));
		if (!BlockIsAir(world, currentLocation)){
			currentLocation = new Point3D((int)Math.ceil(pathEntity.posX), (int)Math.floor(pathEntity.posY), (int)Math.floor(pathEntity.posZ));
			if (!BlockIsAir(world, currentLocation)){
				currentLocation = new Point3D((int)Math.floor(pathEntity.posX), (int)Math.ceil(pathEntity.posY), (int)Math.floor(pathEntity.posZ));
				if (!BlockIsAir(world, currentLocation)){
					currentLocation = new Point3D((int)Math.floor(pathEntity.posX), (int)Math.floor(pathEntity.posY), (int)Math.ceil(pathEntity.posZ));
					if (!BlockIsAir(world, currentLocation)){
						currentLocation = new Point3D((int)Math.ceil(pathEntity.posX), (int)Math.ceil(pathEntity.posY), (int)Math.floor(pathEntity.posZ));
						if (!BlockIsAir(world, currentLocation)){
							currentLocation = new Point3D((int)Math.ceil(pathEntity.posX), (int)Math.floor(pathEntity.posY), (int)Math.ceil(pathEntity.posZ));
							if (!BlockIsAir(world, currentLocation)){
								currentLocation = new Point3D((int)Math.floor(pathEntity.posX), (int)Math.ceil(pathEntity.posY), (int)Math.ceil(pathEntity.posZ));
								if (!BlockIsAir(world, currentLocation)){
									currentLocation = new Point3D((int)Math.ceil(pathEntity.posX), (int)Math.ceil(pathEntity.posY), (int)Math.ceil(pathEntity.posZ));
									if (!BlockIsAir(world, currentLocation)){
										//stuck!
										pathEntity.setPosition(pathEntity.posX, pathEntity.posY + 1, pathEntity.posZ);
									}
								}
							}
						}
					}
				}
			}
		}

		if (lastLocation != null && currentLocation.GetDistanceSq(lastLocation) < 1.0f && HasPath() && HasWaypoint()){
			ticksStuck++;
		}else{
			lastLocation = currentLocation;
			ticksStuck = 0;
		}

	}

	private boolean BlockIsAir(World world, Point3D point){
		BlockPos pos = new BlockPos(point.x, point.y, point.z);
		return world.isAirBlock(pos);
	}

	private void checkStuck(){
		if (ticksStuck > 40){
			currentWaypoint = null;
			longRangeWaypoint = null;
			pathData = null;
			ticksStuck = 0;
		}
	}

	private void GetPathToWaypoint(World world, Entity entity){
		try{
			if (currentLocation == null){
				return;
			}
			pathData = PathFinder.FindPath(world, currentLocation.Unshift(), currentWaypoint.Unshift(), entity);
			if (pathData != null){
				pathData.unshift();
			}
			if (currentWaypoint != null){
				currentWaypoint.Unshift();
			}
		}catch (Exception e){
			e.printStackTrace();
			currentWaypoint = null;
		}
	}
}
