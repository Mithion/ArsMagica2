package am2.entities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import am2.AMCore;
import am2.PlayerTracker;
import am2.bosses.EntityWinterGuardian;
import am2.buffs.BuffEffectFrostSlowed;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleMoveOnHeading;
import am2.playerextensions.ExtendedProperties;

public class EntityWinterGuardianArm extends EntityLiving{

	private final int maxTicksToExist;
	private EntityLivingBase throwingEntity;
	private Integer entityHit;
	private double projectileSpeed;
	private boolean takenArm = false;

	private static final int DW_THROWING_ENTITY = 20;
	private static final int DW_PROJECTILE_SPEED = 21;

	public EntityWinterGuardianArm(World par1World) {
		super(par1World);
		ticksExisted = 0;
		maxTicksToExist = 120;
		this.noClip = true;
		entityHit = null;
	}

	public EntityWinterGuardianArm(World world, EntityLivingBase entityLiving, double projectileSpeed)
	{
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

	public void setHeading(double movementX, double movementY, double movementZ, double projectileSpeed, double projectileSpeed2)
	{
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
	public void setDead() {
		if (getThrowingEntity() != null){
			if (getThrowingEntity() instanceof EntityWinterGuardian){
				((EntityWinterGuardian)getThrowingEntity()).returnOneArm();
			}else if (getThrowingEntity() instanceof EntityPlayer && !this.worldObj.isRemote){
				if (getThrowingEntity().getHealth() <= 0){
					PlayerTracker.storeSoulboundItemForRespawn((EntityPlayer)getThrowingEntity(), ItemsCommonProxy.winterArmEnchanted.copy());
				}else{
					if (!((EntityPlayer)getThrowingEntity()).inventory.addItemStackToInventory(ItemsCommonProxy.winterArmEnchanted.copy())){
						EntityItem item = new EntityItem(worldObj);
						item.setPosition(posX, posY, posZ);
						item.setEntityItemStack(ItemsCommonProxy.winterArmEnchanted.copy());
						worldObj.spawnEntityInWorld(item);
					}
				}
			}
		}
		if (entityHit != null){
			Entity entityhit = worldObj.getEntityByID(this.entityHit);
			if (entityhit != null){
				entityhit.motionX = 0;
				entityhit.motionY = 0;
				entityhit.motionZ = 0;
			}
		}
		super.setDead();
	}

	@Override
	public void onUpdate()
	{
		if (!worldObj.isRemote && (getThrowingEntity() == null || getThrowingEntity().isDead))
		{
			setDead();
			return;
		}
		else
		{
			ticksExisted++;
			if (ticksExisted >= maxTicksToExist && !worldObj.isRemote)
			{
				setDead();
				return;
			}
		}

		if (!takenArm && getThrowingEntity() != null && getThrowingEntity() instanceof EntityWinterGuardian){
			((EntityWinterGuardian)getThrowingEntity()).launchOneArm();
			takenArm = true;
		}

		Vec3 vec3d = Vec3.createVectorHelper(posX, posY, posZ);
		Vec3 vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec3d, vec3d1);
		vec3d = Vec3.createVectorHelper(posX, posY, posZ);
		vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		if (movingobjectposition != null)
		{
			vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}
		Entity entity = null;
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
		double d = 0.0D;
		for (int j = 0; j < list.size(); j++)
		{
			Entity entity1 = (Entity)list.get(j);
			if (!entity1.canBeCollidedWith() || entity1.isEntityEqual(getThrowingEntity()) && ticksExisted < 25)
			{
				continue;
			}
			float f2 = 0.3F;
			AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f2, f2, f2);
			MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);
			if (movingobjectposition1 == null)
			{
				continue;
			}
			double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
			if (d1 < d || d == 0.0D)
			{
				entity = entity1;
				d = d1;
			}
		}

		if (entity != null)
		{
			movingobjectposition = new MovingObjectPosition(entity);
		}
		if (movingobjectposition != null)
		{
			HitObject(movingobjectposition);
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float)((Math.atan2(motionX, motionZ) * 180D) / 3.1415927410125732D);
		for (rotationPitch = (float)((Math.atan2(motionY, f) * 180D) / 3.1415927410125732D); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
		for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
		for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
		for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.4F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.4F;
		float f1 = 0.95F;
		if (isInWater())
		{
			for (int k = 0; k < 4; k++)
			{
				float f3 = 0.25F;
				worldObj.spawnParticle("bubble", posX - motionX * f3, posY - motionY * f3, posZ - motionZ * f3, motionX, motionY, motionZ);
			}

			f1 = 0.8F;
		}else{
			for (int i = 0; i < 2; ++i){
				AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "ember", posX + rand.nextFloat() * 0.2 - 0.1, posY +1.2, posZ+ rand.nextFloat() * 0.2 - 0.1);
				if (particle != null){
					particle.setIgnoreMaxAge(false);
					particle.setMaxAge(15);
					particle.setParticleScale(0.35f);
					particle.setRGBColorF(0.5098f, 0.7843f, 0.7843f);
					particle.AddParticleController(new ParticleMoveOnHeading(particle, Math.toDegrees(this.rotationPitch), Math.toDegrees(this.rotationYaw), 0.2f, 1, false));
				}
			}
		}
		setPosition(posX, posY, posZ);

		int halflife = 80;

		if (this.ticksExisted > 30 && this.ticksExisted < halflife){
			this.motionX *= 0.8f;
			this.motionY *= 0.8f;
			this.motionZ *= 0.8f;
		}else if (this.ticksExisted > halflife && getThrowingEntity() != null){
			double deltaX = this.posX - getThrowingEntity().posX;
			double deltaZ = this.posZ - getThrowingEntity().posZ;
			double deltaY = this.posY - (getThrowingEntity().posY + getThrowingEntity().getEyeHeight());
			double angle = Math.atan2(deltaZ, deltaX);
			double speed = Math.min((this.ticksExisted - halflife) / 10f, this.getProjectileSpeed());

			double horizontalDistance = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
			float pitchRotation = (float)(-Math.atan2(deltaY, horizontalDistance));

			this.motionX = -Math.cos(angle) * speed;
			this.motionZ = -Math.sin(angle) * speed;
			this.motionY = Math.sin(pitchRotation) * speed;

			if (this.entityHit != null){
				Entity entityhit = worldObj.getEntityByID(this.entityHit);
				if (entityhit != null){
					entityhit.posX = this.posX;
					entityhit.posY = this.posY + 1.2;
					entityhit.posZ = this.posZ;

					entityhit.motionX = this.motionX;
					entityhit.motionY = this.motionY;
					entityhit.motionZ = this.motionZ;

					entityhit.lastTickPosX = this.lastTickPosX;
					entityhit.lastTickPosY = this.lastTickPosY + 1.2;
					entityhit.lastTickPosZ = this.lastTickPosZ;

					entityhit.fallDistance = 0;
				}
			}

			if (this.getDistanceSqToEntity(getThrowingEntity()) < 9 && !worldObj.isRemote){
				this.setDead();
			}
		}
	}

	protected void HitObject(MovingObjectPosition movingobjectposition)
	{
		if (movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityLivingBase)
		{
			if (movingobjectposition.entityHit == getThrowingEntity() || getThrowingEntity() == null) return;
			if (getThrowingEntity() != null && this.entityHit == null){
				movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(getThrowingEntity()), 3);
				this.entityHit = movingobjectposition.entityHit.getEntityId();
				this.ticksExisted = 80;
				if (movingobjectposition.entityHit instanceof EntityLivingBase){
					ExtendedProperties.For((EntityLivingBase) movingobjectposition.entityHit).deductMana(ExtendedProperties.For((EntityLivingBase) movingobjectposition.entityHit).getMaxMana() * 0.1f);
					((EntityLivingBase) movingobjectposition.entityHit).addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 2));
					((EntityLivingBase) movingobjectposition.entityHit).addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 60, 2));
					((EntityLivingBase) movingobjectposition.entityHit).addPotionEffect(new BuffEffectFrostSlowed(60, 2));
				}
			}
		}
	}

	@Override
	protected void entityInit() {
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
			throwingEntity = (EntityLivingBase) this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(DW_THROWING_ENTITY));
		}
		return throwingEntity;
	}

	@Override
	public ItemStack getHeldItem() {
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int i, ItemStack itemstack) {

	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return new ItemStack[0];
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		return false;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}
}
