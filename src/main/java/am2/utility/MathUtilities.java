package am2.utility;

import am2.api.math.AMVector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class MathUtilities{
	public static AMVector3 bezier(AMVector3 s, AMVector3 c1, AMVector3 c2, AMVector3 e, float t){
		if (t < 0 || t > 1.0f){
			throw new InvalidParameterException("t is out of range, with a value of :" + t);
		}
		float one_minus_t = 1 - t;

		AMVector3 retValue = new AMVector3(0, 0, 0);
		AMVector3[] terms = new AMVector3[4];
		terms[0] = calcNewVector(one_minus_t * one_minus_t * one_minus_t, s);
		terms[1] = calcNewVector(3 * one_minus_t * one_minus_t * t, c1);
		terms[2] = calcNewVector(3 * one_minus_t * t * t, c2);
		terms[3] = calcNewVector(t * t * t, e);

		for (int i = 0; i < 4; ++i){
			retValue.add(terms[i]);
		}

		return retValue;
	}

	private static AMVector3 calcNewVector(float scaler, AMVector3 base){
		AMVector3 retValue = new AMVector3(base.x, base.y, base.z);
		retValue.scale(scaler);
		return retValue;
	}

	public static AMVector3[] GetHorizontalBlocksInFrontOfCharacter(EntityLivingBase entity, int numBlocks, int x, int y, int z){
		float speed = 0.1F;

		float factor = (float)(Math.PI / 180.0f);

		float sinYawRadians = MathHelper.sin(entity.rotationYaw * factor);
		float cosYawRadians = MathHelper.cos(entity.rotationYaw * factor);

		float cosPitchRadians = MathHelper.cos(entity.rotationPitch * factor);

		double motionZ = cosYawRadians * speed;
		double motionX = -sinYawRadians * speed;

		double curX = x;
		double curY = y;
		double curZ = z;

		float minimum = 0.01f;

		if (Math.abs(motionX) < minimum){
			motionX = 0;
		}
		if (Math.abs(motionZ) < minimum){
			motionZ = 0;
		}

		int lastX = x;
		int lastY = y;
		int lastZ = z;


		AMVector3[] list = new AMVector3[numBlocks];
		list[0] = new AMVector3(x, y, z);
		int count = 1;
		while (count < numBlocks){
			curX += motionX;
			curZ += motionZ;

			//check for deltas
			if ((int)Math.round(curX) != lastX || (int)Math.round(curY) != lastY || (int)Math.round(curZ) != lastZ){
				lastX = (int)Math.round(curX);
				lastY = (int)Math.round(curY);
				lastZ = (int)Math.round(curZ);
				list[count++] = new AMVector3(lastX, lastY, lastZ);
			}
		}
		return list;
	}

	public static AMVector3[] GetBlocksInFrontOfCharacter(EntityLivingBase entity, int numBlocks, int x, int y, int z){
		float speed = 0.1F;

		float factor = (float)(Math.PI / 180.0f);

		float sinYawRadians = MathHelper.sin(entity.rotationYaw * factor);
		float cosYawRadians = MathHelper.cos(entity.rotationYaw * factor);

		float sinPitchRadians = MathHelper.sin(entity.rotationPitch * factor);
		float cosPitchRadians = MathHelper.cos(entity.rotationPitch * factor);

		double motionZ = cosYawRadians * cosPitchRadians * speed;
		double motionX = -sinYawRadians * cosPitchRadians * speed;
		double motionY = -sinPitchRadians * speed;

		double curX = x;
		double curY = y;
		double curZ = z;

		float minimum = 0.01f;

		if (Math.abs(motionX) < minimum){
			motionX = 0;
		}
		if (Math.abs(motionY) < minimum){
			motionY = 0;
		}
		if (Math.abs(motionZ) < minimum){
			motionZ = 0;
		}

		int lastX = x;
		int lastY = y;
		int lastZ = z;


		AMVector3[] list = new AMVector3[numBlocks];
		list[0] = new AMVector3(x, y, z);
		int count = 1;
		while (count < numBlocks){
			curX += motionX;
			curY += motionY;
			curZ += motionZ;

			//check for deltas
			if ((int)Math.round(curX) != lastX || (int)Math.round(curY) != lastY || (int)Math.round(curZ) != lastZ){
				lastX = (int)Math.round(curX);
				lastY = (int)Math.round(curY);
				lastZ = (int)Math.round(curZ);
				list[count++] = new AMVector3(lastX, lastY, lastZ);
			}
		}
		return list;
	}

	public static Entity[] GetEntitiesInAngleNearEntity(World world, EntityLivingBase source, int degrees, int radius, Class filterClass, boolean includeSource){

		//argument verification
		if (degrees > 360){
			degrees = 360;
		}
		if (degrees < 5){
			return new Entity[0];
		}

		//get all entities within distance
		List distanceFilter = world.getEntitiesWithinAABB(filterClass, new AxisAlignedBB((float)source.posX - radius, source.posY - radius, (float)source.posZ - radius, source.posX + radius, source.posY + radius, source.posZ + radius));
		if (!includeSource){
			for (int x = 0; x < distanceFilter.size(); ++x){
				if (distanceFilter.get(x) == source){
					distanceFilter.remove(x);
					x--;
				}
			}
		}

		ArrayList angleFilter = new ArrayList();

		//get the angle that the player is facing and normalize it
		double playerAngle = NormalizeRotation(source.rotationYaw);

		//calculate the min and max angle that the mob can be within the player's view
		float maxAngle = degrees;

		//filter by angle
		for (int i = 0; i < distanceFilter.size(); ++i){
			//get the current entity for the loop
			Entity curEntity = (Entity)distanceFilter.get(i);
			if (isInFieldOfVision(curEntity, source, maxAngle, maxAngle) && source.canEntityBeSeen(curEntity)){
				angleFilter.add(curEntity);
			}
		}

		Entity[] array = new Entity[angleFilter.size()];

		for (int t = 0; t < angleFilter.size(); t++){
			array[t] = (Entity)angleFilter.get(t);
		}

		return array;
	}

	public static boolean isInFieldOfVision(Entity e1, EntityLivingBase e2, float angleX, float angleY){
		//save Entity 2's original rotation variables
		float rotationYawPrime = e2.rotationYaw;
		float rotationPitchPrime = e2.rotationPitch;
		//make Entity 2 directly face Entity 1
		if (e2 instanceof EntityLiving)
			((EntityLiving)e2).faceEntity(e1, 360F, 360F);
		//switch values of prime rotation variables with current rotation variables
		float f = e2.rotationYaw;
		float f2 = e2.rotationPitch;
		e2.rotationYaw = rotationYawPrime;
		e2.rotationPitch = rotationPitchPrime;
		rotationYawPrime = f;
		rotationPitchPrime = f2;
		//assuming field of vision consists of everything within X degrees from rotationYaw and Y degrees from rotationPitch, check if entity 2's current rotationYaw and rotationPitch within this X and Y range
		float X = angleX;
		float Y = angleY;
		float yawFOVMin = e2.rotationYaw - X;
		float yawFOVMax = e2.rotationYaw + X;
		float pitchFOVMin = e2.rotationPitch - Y;
		float pitchFOVMax = e2.rotationPitch + Y;
		boolean flag1 = (yawFOVMin < 0F && (rotationYawPrime >= yawFOVMin + 360F || rotationYawPrime <= yawFOVMax)) || (yawFOVMax >= 360F && (rotationYawPrime <= yawFOVMax - 360F || rotationYawPrime >= yawFOVMin)) || (yawFOVMax < 360F && yawFOVMin >= 0F && rotationYawPrime <= yawFOVMax && rotationYawPrime >= yawFOVMin);
		boolean flag2 = (pitchFOVMin <= -180F && (rotationPitchPrime >= pitchFOVMin + 360F || rotationPitchPrime <= pitchFOVMax)) || (pitchFOVMax > 180F && (rotationPitchPrime <= pitchFOVMax - 360F || rotationPitchPrime >= pitchFOVMin)) || (pitchFOVMax < 180F && pitchFOVMin >= -180F && rotationPitchPrime <= pitchFOVMax && rotationPitchPrime >= pitchFOVMin);
		if (flag1 && flag2 && e2.canEntityBeSeen(e1))
			return true;
		else return false;
	}

	public static double NormalizeRotation(double yawValue){
		if (yawValue < 0){
			while (yawValue < 0){
				yawValue += 360;
			}
		}else if (yawValue > 359){
			while (yawValue > 359){
				yawValue -= 360;
			}
		}
		return yawValue;
	}

	public static AMVector3 GetMovementVectorBetweenEntities(Entity from, Entity to){
		AMVector3 fromPosition = new AMVector3(from.posX, from.posY, from.posZ);
		AMVector3 toPosition = new AMVector3(to.posX, to.posY, to.posZ);
		AMVector3 delta = fromPosition.sub(toPosition);
		delta.normalize();
		return delta;
	}

	public static AMVector3 GetMovementVectorBetweenPoints(AMVector3 from, AMVector3 to){
		AMVector3 delta = from.sub(to);
		delta.normalize();
		return delta;
	}

	public static Entity getPointedEntity(World world, EntityLivingBase entityplayer, double range, double collideRadius){
		return getPointedEntity(world, entityplayer, range, collideRadius, false);
	}

	public static Entity getPointedEntity(World world, EntityLivingBase entityplayer, double range, double collideRadius, boolean nonCollide){
		Entity pointedEntity = null;
		double d = range;
		Vec3 vec3d = new Vec3(entityplayer.posX, entityplayer.posY + entityplayer.getEyeHeight(), entityplayer.posZ);
		Vec3 vec3d1 = entityplayer.getLookVec();
		Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
		double f1 = collideRadius;
		List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.boundingBox.addCoord(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d).expand(f1, f1, f1));

		double d2 = 0.0D;
		for (int i = 0; i < list.size(); i++){
			Entity entity = (Entity)list.get(i);
			MovingObjectPosition mop = world.rayTraceBlocks(
					new Vec3(entityplayer.posX, entityplayer.posY + entityplayer.getEyeHeight(), entityplayer.posZ),
					new Vec3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
					false);
			if (((entity.canBeCollidedWith()) || (nonCollide)) && mop == null){
				float f2 = Math.max(0.8F, entity.getCollisionBorderSize());
				AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
				MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
				if (axisalignedbb.isVecInside(vec3d)){
					if ((0.0D < d2) || (d2 == 0.0D)){
						pointedEntity = entity;
						d2 = 0.0D;
					}

				}else if (movingobjectposition != null){
					double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
					if ((d3 < d2) || (d2 == 0.0D)){
						pointedEntity = entity;
						d2 = d3;
					}
				}
			}
		}
		return pointedEntity;
	}

	public static Vec3 extrapolateEntityLook(World par1World, EntityLivingBase entity, double range){
		float var4 = 1.0F;
		float var5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * var4;
		float var6 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * var4;
		double var7 = entity.prevPosX + (entity.posX - entity.prevPosX) * var4;
		double var9 = entity.prevPosY + (entity.posY - entity.prevPosY) * var4 + 1.6D - entity.yOffset;
		double var11 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * var4;
		Vec3 var13 = new Vec3(var7, var9, var11);
		float var14 = MathHelper.cos(-var6 * 0.017453292F - (float)Math.PI);
		float var15 = MathHelper.sin(-var6 * 0.017453292F - (float)Math.PI);
		float var16 = -MathHelper.cos(-var5 * 0.017453292F);
		float var17 = MathHelper.sin(-var5 * 0.017453292F);
		float var18 = var15 * var16;
		float var20 = var14 * var16;
		double var21 = range;
		Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);

		return var23;
	}

	public static AMVector3 getLook(Entity source, float f){
		float var2;
		float var3;
		float var4;
		float var5;

		if (f == 1.0F){
			var2 = MathHelper.cos(-source.rotationYaw * 0.017453292F - (float)Math.PI);
			var3 = MathHelper.sin(-source.rotationYaw * 0.017453292F - (float)Math.PI);
			var4 = -MathHelper.cos(-source.rotationPitch * 0.017453292F);
			var5 = MathHelper.sin(-source.rotationPitch * 0.017453292F);
			return new AMVector3(var3 * var4, var5, var2 * var4);
		}else{
			var2 = source.prevRotationPitch + (source.rotationPitch - source.prevRotationPitch) * f;
			var3 = source.prevRotationYaw + (source.rotationYaw - source.prevRotationYaw) * f;
			var4 = MathHelper.cos(-var3 * 0.017453292F - (float)Math.PI);
			var5 = MathHelper.sin(-var3 * 0.017453292F - (float)Math.PI);
			float var6 = -MathHelper.cos(-var2 * 0.017453292F);
			float var7 = MathHelper.sin(-var2 * 0.017453292F);
			return new AMVector3(var5 * var6, var7, var4 * var6);
		}
	}

	public static int getDistanceToGround(EntityLivingBase ent, World world){
		int yCoord = (int)(ent.posY);
		int distance = 0;

		while (distance < 20){
			if (world.isAirBlock((int)Math.floor(ent.posX), yCoord, (int)Math.floor(ent.posZ))){
				break;
			}
			if (world.isAirBlock((int)Math.ceil(ent.posX), yCoord, (int)Math.floor(ent.posZ))){
				break;
			}
			if (world.isAirBlock((int)Math.floor(ent.posX), yCoord, (int)Math.ceil(ent.posZ))){
				break;
			}
			if (world.isAirBlock((int)Math.ceil(ent.posX), yCoord, (int)Math.ceil(ent.posZ))){
				break;
			}
			distance++;
			yCoord--;
		}

		return distance;
	}

	public static float[] colorIntToFloats(int color){
		float[] colors = new float[3];
		colors[0] = ((color >> 16) & 0xFF) / 255.0F;
		colors[1] = ((color >> 8) & 0xFF) / 255.0F;
		colors[2] = ((color) & 0xFF) / 255.0F;

		return colors;
	}

	public static int colorFloatsToInt(float r, float g, float b){
		return ((int)(r * 255) << 16) + ((int)(g * 255) << 8) + ((int)(b * 255));
	}

	public static int[] push(int[] original, int value){
		int[] newArr = new int[original.length + 1];
		for (int i = 0; i < original.length; ++i)
			newArr[i] = original[i];
		newArr[newArr.length - 1] = value;
		return newArr;
	}

	public static int[] splice(int[] arr, int index){
		if (arr.length <= 1)
			return arr;
		int[] newArr = new int[arr.length - 1];
		int count = 0;
		for (int i = 0; i < arr.length; ++i){
			if (i == index)
				continue;
			newArr[count++] = arr[i];
		}
		return newArr;
	}
}
