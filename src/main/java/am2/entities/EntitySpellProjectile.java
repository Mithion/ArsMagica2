package am2.entities;

import am2.AMCore;
import am2.LogHelper;
import am2.api.math.AMVector3;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.SpellModifiers;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.AMParticleIcons;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleHoldPosition;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.spell.modifiers.Colour;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntitySpellProjectile extends Entity{

	private final int maxTicksToExist;
	private int ticksExisted;
	private int originalBounceCount; //used for resetting bounces

	private final double friction_coefficient = AMCore.config.getFrictionCoefficient();

	private ArrayList<AMVector3> blockhits;
	private ArrayList<Integer> entityHits;

	private static final int DW_BOUNCE_COUNTER = 21;
	private static final int DW_GRAVITY = 22;
	private static final int DW_EFFECT = 23;
	private static final int DW_ICON_NAME = 24;
	private static final int DW_PIERCE_COUNT = 25;
	private static final int DW_COLOR = 26;
	private static final int DW_SHOOTER = 27;
	private static final int DW_TARGETGRASS = 28;
	private static final int DW_HOMING = 29;
	private static final int DW_HOMING_TARGET = 30;

	private static final float GRAVITY_TERMINAL_VELOCITY = -2.0f;

	private String particleType = "";

	//=========================================================================
	//Constructors
	//=========================================================================
	public EntitySpellProjectile(World world){
		super(world);
		ticksExisted = 0;
		maxTicksToExist = -1;
		this.noClip = true;
	}

	public EntitySpellProjectile(World world, EntityLivingBase entityLiving, double projectileSpeed){
		super(world);
		this.noClip = true;
		setSize(0.25F, 0.25F);
		setLocationAndAngles(entityLiving.posX, entityLiving.posY + entityLiving.getEyeHeight(), entityLiving.posZ, entityLiving.rotationYaw, entityLiving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		float f = 0.01F;
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f;
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f;
		maxTicksToExist = -1;
		setSpellProjectileHeading(motionX, motionY, motionZ, projectileSpeed, projectileSpeed);
	}
	//=========================================================================

	@Override
	protected void entityInit(){
		this.dataWatcher.addObject(DW_BOUNCE_COUNTER, 0);
		this.dataWatcher.addObject(DW_GRAVITY, 0);
		this.dataWatcher.addObject(DW_EFFECT, new ItemStack(ItemsCommonProxy.spell));
		this.dataWatcher.addObject(DW_ICON_NAME, "arcane");
		this.dataWatcher.addObject(DW_PIERCE_COUNT, 0);
		this.dataWatcher.addObject(DW_COLOR, 0xFFFFFF);
		this.dataWatcher.addObject(DW_SHOOTER, 0);
		this.dataWatcher.addObject(DW_TARGETGRASS, (byte)0);
		this.dataWatcher.addObject(DW_HOMING, (byte)0);
		this.dataWatcher.addObject(DW_HOMING_TARGET, -1);
		blockhits = new ArrayList<AMVector3>();
		entityHits = new ArrayList<Integer>();
	}

	public void setSpellProjectileHeading(double movementX, double movementY, double movementZ, double projectileSpeed, double projectileSpeed2){
		float f = MathHelper.sqrt_double(movementX * movementX + movementY * movementY + movementZ * movementZ);
		movementX /= f;
		movementY /= f;
		movementZ /= f;
		//movementX += rand.nextGaussian() * 0.0074999998323619366D * projectileSpeed2;
		//movementY += rand.nextGaussian() * 0.0074999998323619366D * projectileSpeed2;
		//movementZ += rand.nextGaussian() * 0.0074999998323619366D * projectileSpeed2;
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

	public void setHomingTarget(EntityLivingBase entity){
		if (!this.worldObj.isRemote){
			this.dataWatcher.updateObject(DW_HOMING_TARGET, entity.getEntityId());
		}
	}

	private void findHomingTarget(){
		List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABB(
				EntityLivingBase.class,
				AxisAlignedBB.getBoundingBox(
						this.posX - 15,
						this.posY - 15,
						this.posZ - 15,
						this.posX + 15,
						this.posY + 15,
						this.posZ + 15));

		EntityLivingBase closest = null;
		double curShortestDistance = 900;
		AMVector3 me = new AMVector3(this);

		for (EntityLivingBase e : entities){
			if (e == this.getShootingEntity())
				continue;
			double distance = new AMVector3(e).distanceSqTo(me);
			if (distance < curShortestDistance){
				curShortestDistance = distance;
				closest = e;
			}
		}

		if (closest != null){
			setHomingTarget(closest);
		}
	}

	public void setShootingEntity(EntityLivingBase caster){
		if (!this.worldObj.isRemote){
			this.dataWatcher.updateObject(DW_SHOOTER, caster.getEntityId());
		}
	}

	public void setTargetWater(){
		if (!this.worldObj.isRemote)
			this.dataWatcher.updateObject(DW_TARGETGRASS, (byte)1);
	}

	private boolean targetWater(){
		return this.dataWatcher.getWatchableObjectByte(DW_TARGETGRASS) == (byte)1;
	}

	private EntityLivingBase getShootingEntity(){
		int entityID = this.dataWatcher.getWatchableObjectInt(DW_SHOOTER);
		Entity e = this.worldObj.getEntityByID(entityID);
		if (e != null && e instanceof EntityLivingBase)
			return (EntityLivingBase)e;
		return null;
	}

	private EntityLivingBase getHomingEntity(){
		int entityID = this.dataWatcher.getWatchableObjectInt(DW_HOMING_TARGET);
		Entity e = this.worldObj.getEntityByID(entityID);
		if (e != null && e instanceof EntityLivingBase)
			return (EntityLivingBase)e;
		return null;
	}

	public void moveTowards(Entity entity, float maxYaw, float maxPitch){
		double deltaX = entity.posX - this.posX;
		double deltaZ = entity.posZ - this.posZ;
		double deltaY;

		if (entity instanceof EntityLivingBase){
			EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
			deltaY = entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() - (this.posY + (double)this.getEyeHeight());
		}else{
			deltaY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
		}

		double hypotenuse = (double)MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		float f2 = (float)(Math.atan2(deltaZ, deltaX));// * 180.0D / Math.PI) - 90.0F;
		float f3 = (float)(Math.atan2(deltaY, hypotenuse));// * 180.0D / Math.PI);

		this.rotationPitch = this.updateRotation(this.rotationPitch, f3, maxPitch);
		this.rotationYaw = this.updateRotation(this.rotationYaw, f2, maxYaw);

		this.motionX = Math.cos(rotationYaw) * 0.4;
		this.motionZ = Math.sin(rotationYaw) * 0.4;
		this.motionY = Math.sin(this.rotationPitch);
	}

	private float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_){
		float f3 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);

		if (f3 > p_70663_3_){
			f3 = p_70663_3_;
		}

		if (f3 < -p_70663_3_){
			f3 = -p_70663_3_;
		}

		return p_70663_1_ + f3;
	}


	@Override
	public void onUpdate(){
		//super.onUpdate();
		if (!worldObj.isRemote && (getShootingEntity() == null || getShootingEntity().isDead)){
			setDead();
		}else{
			ticksExisted++;
			int maxTicksToLive = maxTicksToExist > -1 ? maxTicksToExist : 100;
			if (ticksExisted >= maxTicksToLive && !worldObj.isRemote){
				setDead();
				return;
			}
		}

		//TODO Fix homing
		if (this.dataWatcher.getWatchableObjectByte(DW_HOMING) != (byte)0 && this.ticksExisted > 10){
			if (this.dataWatcher.getWatchableObjectInt(DW_HOMING_TARGET) == -1){
				findHomingTarget();

			}else{
				EntityLivingBase homingTarget = getHomingEntity();
				//AMCore.log.info("%.2f, %.2f, %.2f", posX, posY, posZ);
				LogHelper.trace("Homing Target: " + getHomingEntity());
				if (homingTarget != null && new AMVector3(this).distanceSqTo(new AMVector3(homingTarget)) > 2){
					this.moveTowards(homingTarget, 60, 60);
				}
			}
		}


		Vec3 vec3d = Vec3.createVectorHelper(posX, posY, posZ);
		Vec3 vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition movingobjectposition = null;
		movingobjectposition = worldObj.rayTraceBlocks(vec3d, vec3d1, true);
		vec3d = Vec3.createVectorHelper(posX, posY, posZ);
		vec3d1 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		if (movingobjectposition != null){
			vec3d1 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
		}
		Entity entity = null;
		List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
		double d = 0.0D;
		for (int j = 0; j < list.size(); j++){
			Entity entity1 = (Entity)list.get(j);
			if (!entity1.canBeCollidedWith() || entity1.isEntityEqual(getShootingEntity())){
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
			if (entity instanceof EntityDragonPart && ((EntityDragonPart)entity).entityDragonObj != null && ((EntityDragonPart)entity).entityDragonObj instanceof EntityLivingBase) {
				entity = (EntityLivingBase)((EntityDragonPart)entity).entityDragonObj;
			}
			movingobjectposition = new MovingObjectPosition(entity);
		}
		if (movingobjectposition != null){
			boolean doHit = true;
			boolean pierce = this.getNumPierces() > 0;
			if (movingobjectposition.typeOfHit == MovingObjectType.ENTITY && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityLivingBase){
				EntityLivingBase ent = (EntityLivingBase)movingobjectposition.entityHit;
				if (ent.isPotionActive(BuffList.spellReflect.id) && !pierce){
					doHit = false;
					this.setShootingEntity(ent);
					this.motionX = -this.motionX;
					this.motionY = -this.motionY;
					this.motionZ = -this.motionZ;

					if (!worldObj.isRemote)
						ent.removePotionEffect(BuffList.spellReflect.id);

					setBounces(originalBounceCount);

					if (worldObj.isRemote){
						for (int i = 0; i < 13; ++i){
							AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "hr_lensflare", ent.posX + rand.nextDouble() - 0.5, ent.posY + ent.getEyeHeight() + rand.nextDouble() - 0.5, ent.posZ + rand.nextDouble() - 0.5);
							if (effect != null){
								EntityPlayer player = AMCore.instance.proxy.getLocalPlayer();
								effect.setIgnoreMaxAge(true);
								if (player != null && ent != player){
									effect.setParticleScale(1.5f);
								}
								effect.setRGBColorF(0.5f + rand.nextFloat() * 0.5f, 0.2f, 0.5f + rand.nextFloat() * 0.5f);
								effect.AddParticleController(new ParticleHoldPosition(effect, 100, 1, false));
							}
						}
					}
				}
			}else if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK && getBounces() > 0){
				doHit = false;
				switch (movingobjectposition.sideHit){
				case 0:
				case 1:
					motionY = motionY * friction_coefficient * -1;
					break;
				case 2:
				case 3:
					motionZ = motionZ * friction_coefficient * -1;
					break;
				case 4:
				case 5:
					motionX = motionX * friction_coefficient * -1;
					break;
				}
				this.setBounces(getBounces() - 1);
			}

			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK){
				Block block = worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
				AxisAlignedBB bb = block.getCollisionBoundingBoxFromPool(worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
				if (bb == null && !SpellUtils.instance.modifierIsPresent(SpellModifiers.TARGET_NONSOLID_BLOCKS, getEffectStack(), 0))
					doHit = false;
			}

			if (doHit)
				HitObject(movingobjectposition, pierce);
		}

		if (getGravity() < 0 && motionY > GRAVITY_TERMINAL_VELOCITY){
			this.motionY += getGravity();
		}else if (getGravity() > 0 && motionY < -GRAVITY_TERMINAL_VELOCITY){
			this.motionY -= getGravity();
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float)(Math.atan2(motionX, motionZ));
		for (rotationPitch = (float)(Math.atan2(motionY, f)); rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
		for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
		for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
		for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
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
		if (!AMCore.config.NoGFX() && worldObj.isRemote && this.getShootingEntity() instanceof EntityPlayer){
			if (this.particleType == null || this.particleType.isEmpty())
				particleType = AMParticleIcons.instance.getSecondaryParticleForAffinity(SpellUtils.instance.mainAffinityFor(getEffectStack()));
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, particleType, this.posX, this.posY, this.posZ);
			if (particle != null){
				particle.addRandomOffset(0.3f, 0.3f, 0.3f);
				particle.AddParticleController(new ParticleFloatUpward(particle, 0.1f, 0, 1, false));
				particle.setMaxAge(10);
				particle.setParticleScale(0.05f);
			}
		}
		setPosition(posX, posY, posZ);
	}

	protected void HitObject(MovingObjectPosition movingobjectposition, boolean pierce){

		if (movingobjectposition.entityHit != null){
			if (movingobjectposition.entityHit == getShootingEntity() || getShootingEntity() == null) return;

			Entity e = movingobjectposition.entityHit;
			if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
				e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;

			if (e instanceof EntityLivingBase && getShootingEntity() != null && !this.entityHits.contains(movingobjectposition.entityHit.getEntityId())){
				SpellHelper.instance.applyStageToEntity(this.getEffectStack(), getShootingEntity(), this.worldObj, e, 0, true);
				SpellHelper.instance.applyStackStage(SpellUtils.instance.popStackStage(getEffectStack()), getShootingEntity(), (EntityLivingBase)e, movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord, 0, worldObj, false, true, 0);
				this.entityHits.add(movingobjectposition.entityHit.getEntityId());
			}
		}else{
			AMVector3 blockLoc = new AMVector3(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ);
			if (getShootingEntity() != null && !this.blockhits.contains(blockLoc)){
				SpellHelper.instance.applyStageToGround(getEffectStack(), getShootingEntity(), worldObj, movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, movingobjectposition.sideHit, movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord, 0, true);
				SpellHelper.instance.applyStackStage(SpellUtils.instance.popStackStage(getEffectStack()), getShootingEntity(), getShootingEntity(), movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ, movingobjectposition.sideHit, worldObj, false, true, 0);
				this.blockhits.add(blockLoc);
			}
		}
		this.setNumPierces(this.getNumPierces() - 1);
		if (!worldObj.isRemote && !pierce){
			this.setDead();
		}
	}

	//=========================================================================
	//Data Watcher Stuff
	//=========================================================================
	public int getBounces(){
		return this.dataWatcher.getWatchableObjectInt(DW_BOUNCE_COUNTER);
	}

	public void setBounces(int bounces){
		this.dataWatcher.updateObject(DW_BOUNCE_COUNTER, bounces);
	}

	public double getGravity(){
		return this.dataWatcher.getWatchableObjectInt(DW_GRAVITY) / 1000D;
	}

	public void setGravity(double gravity){
		this.dataWatcher.updateObject(DW_GRAVITY, (int)(gravity * 1000));
	}

	public ItemStack getEffectStack(){
		return this.dataWatcher.getWatchableObjectItemStack(DW_EFFECT);
	}

	public void setEffectStack(ItemStack stack){
		this.dataWatcher.updateObject(DW_EFFECT, stack);

		if (!this.worldObj.isRemote){
			Affinity aff = SpellUtils.instance.mainAffinityFor(stack);
			switch (aff){
			case AIR:
				setIcon("wind");
				break;
			case ARCANE:
				setIcon("arcane");
				break;
			case EARTH:
				setIcon("rock");
				break;
			case ENDER:
				setIcon("pulse");
				setColor(0x550055);
				break;
			case FIRE:
				setIcon("explosion_2");
				break;
			case ICE:
				setIcon("ember");
				setColor(0x2299FF);
				break;
			case LIFE:
				setIcon("sparkle");
				setColor(0x22FF44);
				break;
			case LIGHTNING:
				setIcon("lightning_hand");
				break;
			case NATURE:
				setIcon("plant");
				break;
			case WATER:
				setIcon("water_ball");
				break;
			case NONE:
			default:
				setIcon("lens_flare");
				break;

			}
		}
		if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, stack, 0)){
			ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(stack, 0);
			int ordinalCount = 0;
			for (ISpellModifier mod : mods){
				if (mod instanceof Colour){
					byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(stack, mod, 0, ordinalCount++);
					setColor((int)mod.getModifier(SpellModifiers.COLOR, null, null, null, meta));
				}
			}
		}
	}

	public int getNumPierces(){
		return this.dataWatcher.getWatchableObjectInt(DW_PIERCE_COUNT);
	}

	public void setNumPierces(int pierces){
		this.dataWatcher.updateObject(DW_PIERCE_COUNT, pierces);
	}

	public boolean isHoming(){
		return this.dataWatcher.getWatchableObjectByte(DW_HOMING) != 0;
	}

	public void setHoming(int homing){
		this.dataWatcher.updateObject(DW_HOMING, (byte)homing);
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(){
		String name = this.dataWatcher.getWatchableObjectString(DW_ICON_NAME);
		return AMParticleIcons.instance.getIconByName(name);
	}

	public void setIcon(String name){
		this.dataWatcher.updateObject(DW_ICON_NAME, name);
	}

	public int getColor(){
		return this.dataWatcher.getWatchableObjectInt(DW_COLOR);
	}

	private void setColor(int color){
		this.dataWatcher.updateObject(DW_COLOR, color);
	}

	//=========================================================================

	//=========================================================================
	//Utility overrides
	//=========================================================================
	@Override
	public boolean isInRangeToRenderDist(double d){
		double d1 = boundingBox.getAverageEdgeLength() * 4D;
		d1 *= 64D;
		return d < d1 * d1;
	}

	@Override
	public boolean canBeCollidedWith(){
		return false;
	}

	@Override
	public float getCollisionBorderSize(){
		return 0.0F;
	}

	@Override
	public float getShadowSize(){
		return 0.0F;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound){
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound){
	}
	//=========================================================================
}
