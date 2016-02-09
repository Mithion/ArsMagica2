package am2.entities;

import am2.PlayerTracker;
import am2.bosses.EntityNatureGuardian;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;
import am2.utility.DummyEntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.List;

public class EntityThrownSickle extends EntityLiving{

	private final int maxTicksToExist;
	private EntityLivingBase throwingEntity;
	private final ArrayList<Integer> entityHits;
	private double projectileSpeed;

	private static final int DW_THROWING_ENTITY = 20;
	private static final int DW_PROJECTILE_SPEED = 21;

	public EntityThrownSickle(World par1World){
		super(par1World);
		ticksExisted = 0;
		maxTicksToExist = 120;
		this.noClip = true;
		entityHits = new ArrayList<Integer>();
		this.setSize(0.5f, 2);
	}

	public EntityThrownSickle(World world, EntityLivingBase entityLiving, double projectileSpeed){
		this(world);
		throwingEntity = entityLiving;
		setSize(0.25F, 0.25F);
		setLocationAndAngles(entityLiving.posX, entityLiving.posY + entityLiving.getEyeHeight(), entityLiving.posZ, entityLiving.rotationYaw, entityLiving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		float f = 0.05F;
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f;
		setHeading(motionX, motionY, motionZ, projectileSpeed, projectileSpeed);
		this.projectileSpeed = projectileSpeed;
	}

	public void setHeading(double movementX, double movementY, double movementZ, double projectileSpeed, double projectileSpeed2){
		float f = MathHelper.sqrt_double(movementX * movementX + movementY * movementY + movementZ * movementZ);
		movementX /= f;
		movementY /= f;
		movementZ /= f;
		movementX += rand.nextGaussian() * 0.0074999998323619366D * projectileSpeed2;
		movementY += rand.nextGaussian() * 0.0074999998323619366D * projectileSpeed2;
		movementZ += rand.nextGaussian() * 0.0074999998323619366D * projectileSpeed2;
		movementX *= projectileSpeed;
		movementY *= projectileSpeed;
		movementZ *= projectileSpeed;
		motionX = movementX;
		motionY = movementY;
		motionZ = movementZ;
		float f1 = MathHelper.sqrt_double(movementX * movementX + movementZ * movementZ);
		prevRotationYaw = rotationYaw = (float)((Math.atan2(movementX, movementZ) * 180D) / Math.PI);
		prevRotationPitch = rotationPitch = (float)((Math.atan2(movementY, f1) * 180D) / Math.PI);
	}

	@Override
	public void setDead(){
		if (getThrowingEntity() != null && getThrowingEntity() instanceof EntityNatureGuardian){
			((EntityNatureGuardian)getThrowingEntity()).hasSickle = true;
		}else if (getThrowingEntity() != null && getThrowingEntity() instanceof EntityPlayer){
			if (!worldObj.isRemote)
				if (getThrowingEntity().getHealth() <= 0){
					PlayerTracker.storeSoulboundItemForRespawn((EntityPlayer)getThrowingEntity(), ItemsCommonProxy.natureScytheEnchanted.copy());
				}else{
					if (!((EntityPlayer)getThrowingEntity()).inventory.addItemStackToInventory(ItemsCommonProxy.natureScytheEnchanted.copy())){
						EntityItem item = new EntityItem(worldObj);
						item.setPosition(posX, posY, posZ);
						item.setEntityItemStack(ItemsCommonProxy.natureScytheEnchanted.copy());
						worldObj.spawnEntityInWorld(item);
					}
				}
		}
		super.setDead();
	}

	@Override
	public void onUpdate(){
		if (!worldObj.isRemote && (getThrowingEntity() == null || getThrowingEntity().isDead)){
			setDead();
			return;
		}else{
			ticksExisted++;
			if (ticksExisted >= maxTicksToExist && !worldObj.isRemote){
				setDead();
				return;
			}
		}

		if (getThrowingEntity() != null && getThrowingEntity() instanceof EntityNatureGuardian){
			((EntityNatureGuardian)getThrowingEntity()).hasSickle = false;
		}

		Vec3 vec3d = new Vec3(posX, posY, posZ);
		Vec3 vec3d1 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3d, vec3d1);
		vec3d = new Vec3(posX, posY, posZ);
		vec3d1 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
		if (movingobjectposition != null){
			vec3d1 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}
		Entity entity = null;
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
		double d = 0.0D;
		for (int j = 0; j < list.size(); j++){
			Entity entity1 = (Entity)list.get(j);
			if (!entity1.canBeCollidedWith() || entity1.isEntityEqual(getThrowingEntity()) && ticksExisted < 25){
				continue;
			}
			float f2 = 0.3F;
			AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f2, f2, f2);
			MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
			if (movingobjectposition1 == null){
				continue;
			}
			double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
			if (d1 < d || d == 0.0D){
				entity = entity1;
				d = d1;
			}
		}

		if (entity != null){
			movingobjectposition = new MovingObjectPosition(entity);
		}else{
			movingobjectposition = new MovingObjectPosition((int)Math.floor(posX), (int)Math.floor(posY), (int)Math.floor(posZ), 0, new Vec3(posX, posY, posZ));
		}
		if (movingobjectposition != null){
			HitObject(movingobjectposition);
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
		for (rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F){
		}
		for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F){
		}
		for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F){
		}
		for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F){
		}
		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		float f1 = 0.95F;
		if (isInWater()){
			for (int k = 0; k < 4; k++){
				float f3 = 0.25F;
				worldObj.spawnParticle("bubble", posX - motionX * f3, posY - motionY * f3, posZ - motionZ * f3, motionX, motionY, motionZ);
			}

			f1 = 0.8F;
		}
		setPosition(posX, posY, posZ);

		if (this.ticksExisted > 30 && this.ticksExisted < 40){
			this.motionX *= 0.8f;
			this.motionY *= 0.8f;
			this.motionZ *= 0.8f;
		}else if (this.ticksExisted > 40 && getThrowingEntity() != null){
			double deltaX = this.posX - getThrowingEntity().posX;
			double deltaZ = this.posZ - getThrowingEntity().posZ;
			double deltaY = this.posY - getThrowingEntity().posY;
			double angle = Math.atan2(deltaZ, deltaX);
			double speed = Math.min((this.ticksExisted - 40f) / 10f, this.getProjectileSpeed());

			double horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
			float pitchRotation = (float)(-Math.atan2(deltaY, horizontalDistance));

			this.motionX = -Math.cos(angle) * speed;
			this.motionZ = -Math.sin(angle) * speed;
			this.motionY = Math.sin(pitchRotation) * speed;

			if (this.getDistanceSqToEntity(getThrowingEntity()) < 1.2 && !worldObj.isRemote){
				this.setDead();
			}
		}
	}

	protected void HitObject(MovingObjectPosition movingobjectposition){
		if (movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityLivingBase){
			if (movingobjectposition.entityHit == getThrowingEntity() || getThrowingEntity() == null) return;
			if (getThrowingEntity() != null && !this.entityHits.contains(movingobjectposition.entityHit.getEntityId())){
				this.entityHits.add(movingobjectposition.entityHit.getEntityId());
				if (getThrowingEntity() instanceof EntityPlayer){
					if (movingobjectposition.entityHit instanceof EntityPlayer && (getThrowingEntity().worldObj.isRemote || !MinecraftServer.getServer().isPVPEnabled()))
						return;
					movingobjectposition.entityHit.attackEntityFrom(DamageSources.causeEntityCactusDamage(getThrowingEntity(), true), 10);
				}else{
					movingobjectposition.entityHit.attackEntityFrom(DamageSources.causeEntityCactusDamage(getThrowingEntity(), true), 12);
				}
			}
		}else if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK && this.getThrowingEntity() != null && this.getThrowingEntity() instanceof EntityPlayer){
			int radius = 1;
			for (int i = -radius; i <= radius; ++i){
				for (int j = -radius; j <= radius; ++j){
					for (int k = -radius; k <= radius; ++k){
						Block nextBlock = worldObj.getBlock(movingobjectposition.blockX + i, movingobjectposition.blockY + j, movingobjectposition.blockZ + k);
						if (nextBlock == null) continue;
						if (nextBlock instanceof BlockLeaves || nextBlock instanceof BlockFlower || nextBlock instanceof BlockCrops){
							if (ForgeEventFactory.doPlayerHarvestCheck(DummyEntityPlayer.fromEntityLiving(getThrowingEntity()), nextBlock, true))
								if (!worldObj.isRemote)
									worldObj.func_147478_e(movingobjectposition.blockX + i, movingobjectposition.blockY + j, movingobjectposition.blockZ + k, true);
						}
					}
				}
			}
		}
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataWatcher.addObject(DW_THROWING_ENTITY, 0);
		this.dataWatcher.addObject(DW_PROJECTILE_SPEED, 20);
	}

	public void setThrowingEntity(EntityLivingBase entity){
		this.throwingEntity = entity;
		this.dataWatcher.updateObject(DW_THROWING_ENTITY, entity.getEntityId());
	}

	public void setProjectileSpeed(double speed){
		this.projectileSpeed = speed;
		this.dataWatcher.updateObject(DW_PROJECTILE_SPEED, (int)(speed * 10));
	}

	private double getProjectileSpeed(){
		return this.dataWatcher.getWatchableObjectInt(DW_PROJECTILE_SPEED) / 10;
	}

	private EntityLivingBase getThrowingEntity(){
		if (throwingEntity == null){
			Entity e = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(DW_THROWING_ENTITY));
			if (e instanceof EntityLivingBase)
				throwingEntity = (EntityLivingBase)e;
		}
		return throwingEntity;
	}

	@Override
	public ItemStack getHeldItem(){
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int i, ItemStack itemstack){

	}

	@Override
	public ItemStack[] getLastActiveItems(){
		return new ItemStack[0];
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		return false;
	}

	@Override
	protected boolean canDespawn(){
		return false;
	}
}
