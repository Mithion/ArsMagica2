package am2.entities;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.BlocksCommonProxy;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;
import am2.lore.CompendiumUnlockHandler;
import am2.network.AMNetHandler;
import am2.particles.AMParticle;
import am2.particles.ParticleChangeSize;
import am2.particles.ParticleColorShift;
import am2.particles.ParticleHoldPosition;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.spell.modifiers.Colour;
import am2.utility.MathUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import java.util.List;

public class EntityThrownRock extends EntityLiving{

	private EntityLivingBase throwingEntity;
	private int maxTicksToExist;
	private AMVector3 target = null;
	private float damage;

	private static final int IS_MOONSTONE_METEOR = 20;
	private static final int IS_SHOOTING_STAR = 21;
	private static final int SPELL_STACK = 22;

	public EntityThrownRock(World par1World){
		super(par1World);
		ticksExisted = 0;
		maxTicksToExist = 120;
		this.noClip = true;
	}

	public void setMoonstoneMeteor(){
		this.dataWatcher.updateObject(IS_MOONSTONE_METEOR, (byte)1);
	}

	public void setShootingStar(float damage){
		this.dataWatcher.updateObject(IS_SHOOTING_STAR, (byte)1);
		this.damage = damage;
	}

	public void setMoonstoneMeteorTarget(AMVector3 target){
		this.target = target;
	}

	public boolean getIsMoonstoneMeteor(){
		return dataWatcher.getWatchableObjectByte(IS_MOONSTONE_METEOR) == 1;
	}

	public boolean getIsShootingStar(){
		return dataWatcher.getWatchableObjectByte(IS_SHOOTING_STAR) == 1;
	}

	public EntityThrownRock(World world, EntityLivingBase entityLiving, double projectileSpeed){
		super(world);
		this.noClip = true;
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
		maxTicksToExist = 100;
		setHeading(motionX, motionY, motionZ, projectileSpeed, projectileSpeed);
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

	public void setThrowingEntity(EntityLivingBase thrower){
		this.throwingEntity = thrower;
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataWatcher.addObject(IS_MOONSTONE_METEOR, (byte)0);
		this.dataWatcher.addObject(IS_SHOOTING_STAR, (byte)0);
		this.dataWatcher.addObject(SPELL_STACK, new ItemStack(ItemsCommonProxy.spell));
	}

	private ItemStack getSpellStack(){
		return this.dataWatcher.getWatchableObjectItemStack(SPELL_STACK);
	}

	public void setSpellStack(ItemStack spellStack){
		this.dataWatcher.updateObject(SPELL_STACK, spellStack);
	}

	@Override
	protected boolean canDespawn(){
		return !getIsMoonstoneMeteor() && !getIsShootingStar();
	}

	@Override
	public void onUpdate(){
		super.onUpdate();

		if (this.target != null && this.posY > this.target.y){
			double deltaX = this.posX - target.x;
			double deltaY = this.posY - target.y;
			double deltaZ = this.posZ - target.z;

			double angle = Math.atan2(deltaZ, deltaX);

			double hDist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

			double vAngle = Math.atan2(deltaY, hDist);

			motionX = -Math.cos(angle) * 0.2;
			motionZ = -Math.sin(angle) * 0.2;
			motionY = -Math.sin(vAngle) * 2.5;
		}

		if (!getIsMoonstoneMeteor() && !getIsShootingStar()){
			if (!worldObj.isRemote && (throwingEntity == null || throwingEntity.isDead)){
				setDead();
			}else{
				ticksExisted++;
				int maxTicksToLive = maxTicksToExist > -1 ? maxTicksToExist : 100;
				if (ticksExisted >= maxTicksToLive && !worldObj.isRemote){
					setDead();
					return;
				}
			}
		}

		if (getIsShootingStar()){
			motionY -= 0.1f;
			if (motionY < -2f)
				motionY = -2f;
		}

		if (worldObj.isRemote){
			if (getIsMoonstoneMeteor()){
				AMParticle fire = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "explosion_2", posX, posY, posZ);
				if (fire != null){
					fire.setMaxAge(20);
					fire.setRGBColorF(1, 1, 1);
					fire.setParticleScale(2.0f);
					fire.AddParticleController(new ParticleHoldPosition(fire, 20, 1, false));
					fire.AddParticleController(new ParticleColorShift(fire, 1, false).SetShiftSpeed(0.1f).SetColorTarget(0.01f, 0.01f, 0.01f).SetEndOnReachingTargetColor().setKillParticleOnFinish(false));
				}
			}else if (getIsShootingStar()){

				int color = -1;
				if (getSpellStack() != null){
					if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, getSpellStack(), 0)){
						ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(getSpellStack(), 0);
						int ordinalCount = 0;
						for (ISpellModifier mod : mods){
							if (mod instanceof Colour){
								byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(getSpellStack(), mod, 0, ordinalCount++);
								color = (int)mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
							}
						}
					}
				}

				for (float i = 0; i < Math.abs(motionY); i += 0.1f){
					AMParticle star = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "ember", posX + motionX * i, posY + motionY * i, posZ + motionZ * i);
					if (star != null){
						star.setMaxAge(22);
						float clr = rand.nextFloat();
						float clrMod = Minecraft.getMinecraft().theWorld.rand.nextFloat();
						int finalColor = -1;
						if (color == -1)
							finalColor = MathUtilities.colorFloatsToInt(0.24f * clrMod, 0.58f * clrMod, 0.71f * clrMod);
						else{
							float[] colors = MathUtilities.colorIntToFloats(color);
							for (int c = 0; c < colors.length; ++c)
								colors[c] = colors[c] * clrMod;
							finalColor = MathUtilities.colorFloatsToInt(colors[0], colors[1], colors[2]);
						}
						star.setRGBColorI(finalColor);
						star.AddParticleController(new ParticleHoldPosition(star, 20, 1, false));
						star.AddParticleController(new ParticleChangeSize(star, 0.5f, 0.05f, 20, 1, false));
					}
				}
			}
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
			if (!entity1.canBeCollidedWith() || entity1.isEntityEqual(throwingEntity) && ticksExisted < 25){
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
		setPosition(posX, posY, posZ);
	}

	protected void HitObject(MovingObjectPosition movingobjectposition){
		if (worldObj.isRemote){
			return;
		}

		if (getIsShootingStar()){
			AMNetHandler.INSTANCE.sendStarImpactToClients(posX, posY + ((movingobjectposition.typeOfHit == MovingObjectType.ENTITY) ? -movingobjectposition.entityHit.getEyeHeight() : 1.5f), posZ, worldObj, this.getSpellStack());
			List<EntityLivingBase> ents = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundingBox.expand(5, 5, 5));
			for (EntityLivingBase e : ents){
				if (e == throwingEntity) continue;
				if (this.getEntitySenses().canSee(e))
					SpellHelper.instance.attackTargetSpecial(null, e, DamageSources.causeEntityMagicDamage(throwingEntity), damage);
			}
		}else{

			if (movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityLivingBase){
				if (movingobjectposition.entityHit == throwingEntity || throwingEntity == null) return;
				if (throwingEntity != null){
					movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(throwingEntity), 10);
				}
			}else if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK){
				if (this.getIsMoonstoneMeteor()){

					if (this.target == null){
						this.target = new AMVector3(movingobjectposition.hitVec);
					}
					this.worldObj.newExplosion(this, this.target.x, this.target.y, this.target.z, 0.8f, false, AMCore.config.moonstoneMeteorsDestroyTerrain());

					int numOres = rand.nextInt(6) + 1;

					for (int i = 0; i < numOres; ++i){
						generateSurfaceOreAtOffset(worldObj, (int)Math.floor(this.target.x), (int)Math.floor(this.target.y), (int)Math.floor(this.target.z), i == 0);
					}

					if (this.worldObj.isRemote){
						for (Object player : worldObj.playerEntities)
							if (((EntityPlayer)player).getDistanceSqToEntity(this) < 4096)
								CompendiumUnlockHandler.unlockEntry("moonstone_meteors");
					}
				}
			}
		}

		setDead();
	}

	private void generateSurfaceOreAtOffset(World world, int x, int y, int z, boolean force){
		x = x + rand.nextInt(4) - 2;
		z = z + rand.nextInt(4) - 2;

		while (!world.isAirBlock(x, y, z) && y < world.getActualHeight())
			y++;

		if (rand.nextInt(4) < 2 || force)
			world.setBlock(x, y, z, BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_MOONSTONE_ORE, 2);
		else
			world.setBlock(x, y, z, Blocks.stone);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound){
		super.readEntityFromNBT(par1nbtTagCompound);

		this.damage = par1nbtTagCompound.getFloat("star_damage");
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound){
		super.writeEntityToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setFloat("star_damage", damage);
	}
}
