package am2.entities;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.damage.DamageSources;
import am2.particles.*;
import am2.playerextensions.ExtendedProperties;
import am2.utility.MathUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class EntityManaVortex extends Entity{

	private float rotation;
	private float scale = 0.0f;

	private final int DW_MANA_STOLEN = 21;
	private final int DW_TICKS_TO_EXIST = 20;
	private boolean hasGoneBoom = false;

	public EntityManaVortex(World par1World){
		super(par1World);
	}

	@Override
	protected void entityInit(){
		this.dataWatcher.addObject(DW_TICKS_TO_EXIST, 50 + rand.nextInt(250));
		this.dataWatcher.addObject(DW_MANA_STOLEN, 0.0f);
	}

	public int getTicksToExist(){
		try{
			return this.dataWatcher.getWatchableObjectInt(DW_TICKS_TO_EXIST);
		}catch (Throwable t){
			return -1;
		}
	}

	@Override
	public void onUpdate(){
		this.ticksExisted++;
		this.rotation += 5;
		if (!this.worldObj.isRemote && (this.isDead || this.ticksExisted >= getTicksToExist())){
			this.setDead();
			return;
		}

		if (this.getTicksToExist() - this.ticksExisted <= 20){
			this.scale -= 1f / 20f;
		}else if (this.scale < 0.99f){
			this.scale = (float)(Math.sin((float)this.ticksExisted / 50));
		}

		if (getTicksToExist() - this.ticksExisted <= 5 && !hasGoneBoom){
			hasGoneBoom = true;
			if (!worldObj.isRemote){
				List players = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(3 + Math.floor(this.ticksExisted / 50), 2, 3 + Math.floor(this.ticksExisted / 50)));
				float damage = this.dataWatcher.getWatchableObjectFloat(DW_MANA_STOLEN) * 0.005f;
				if (damage > 100)
					damage = 100;

				Object[] playerArray = players.toArray();
				for (Object o : playerArray){
					EntityLivingBase e = (EntityLivingBase)o;
					MovingObjectPosition mop = this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY, this.posZ), new Vec3(e.posX, e.posY + e.getEyeHeight(), e.posZ), false);
					if (mop == null)
						e.attackEntityFrom(DamageSources.causeEntityPhysicalDamage(this), damage);
				}
			}else{
				for (int i = 0; i < 360; i += AMCore.config.FullGFX() ? 5 : AMCore.config.LowGFX() ? 10 : 20){
					AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "ember", this.posX, this.posY, this.posZ);
					if (effect != null){
						effect.setIgnoreMaxAge(true);
						effect.AddParticleController(new ParticleMoveOnHeading(effect, i, 0, 0.7f, 1, false));
						effect.setRGBColorF(0.24f, 0.24f, 0.8f);
						effect.noClip = false;
						effect.AddParticleController(new ParticleFadeOut(effect, 1, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
						effect.AddParticleController(
								new ParticleLeaveParticleTrail(effect, "ember", false, 5, 1, false)
										.addControllerToParticleList(new ParticleMoveOnHeading(effect, i, 0, 0.1f, 1, false))
										.addControllerToParticleList(new ParticleFadeOut(effect, 1, false).setFadeSpeed(0.1f).setKillParticleOnFinish(true))
										.setParticleRGB_F(0.24f, 0.24f, 0.8f)
										.addRandomOffset(0.2f, 0.2f, 0.2f)
						);
					}
				}
			}
		}

		if (getTicksToExist() - this.ticksExisted > 30){
			//get all players within 5 blocks
			List<EntityLivingBase> players = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(3 + Math.floor(this.ticksExisted / 50), 2, 3 + Math.floor(this.ticksExisted / 50)));
			Object[] playerArray = players.toArray();

			Vec3 thisPos = new Vec3(this.posX, this.posY, this.posZ);

			for (Object o : playerArray){
				EntityLivingBase e = (EntityLivingBase)o;

				MovingObjectPosition mop = this.worldObj.rayTraceBlocks(new Vec3(this.posX, this.posY, this.posZ), new Vec3(e.posX, e.posY + e.getEyeHeight(), e.posZ), false);
				if (mop != null)
					continue;

				Vec3 playerPos = new Vec3(e.posX, e.posY + e.getEyeHeight(), e.posZ);
				if (worldObj.isRemote){
					if (AMCore.config.NoGFX()){
						break;
					}
					if (AMCore.config.LowGFX() && (this.ticksExisted % 4) != 0){
						break;
					}
					AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "ember", e.posX, e.posY + (e.getEyeHeight() / 2), e.posZ);
					if (effect != null){
						effect.AddParticleController(new ParticleArcToEntity(effect, 1, this, false).generateControlPoints().setKillParticleOnFinish(true));
						effect.setRGBColorF(0.24f, 0.24f, 0.8f);
						effect.setIgnoreMaxAge(true);
					}
				}
				float manaStolen = ExtendedProperties.For(e).getMaxMana() * 0.01f;
				float curMana = ExtendedProperties.For(e).getCurrentMana();

				if (manaStolen > curMana)
					manaStolen = curMana;

				this.dataWatcher.updateObject(DW_MANA_STOLEN, this.dataWatcher.getWatchableObjectFloat(DW_MANA_STOLEN) + manaStolen);
				ExtendedProperties.For(e).setCurrentMana(ExtendedProperties.For(e).getCurrentMana() - manaStolen);

				AMVector3 movement = MathUtilities.GetMovementVectorBetweenEntities(e, this);
				float speed = -0.075f;

				e.addVelocity(movement.x * speed, movement.y * speed, movement.z * speed);
			}
		}
	}

	public float getManaStolenPercent(){
		float damage = this.dataWatcher.getWatchableObjectFloat(DW_MANA_STOLEN) * 0.005f;
		if (damage > 100)
			damage = 100;
		return damage / 100f;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1){
		dataWatcher.updateObject(DW_TICKS_TO_EXIST, var1.getInteger("ticksToExist"));
		dataWatcher.updateObject(DW_MANA_STOLEN, var1.getFloat("manaStolen"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1){
		var1.setInteger("ticksToExist", this.getTicksToExist());
		var1.setFloat("manaStolen", this.dataWatcher.getWatchableObjectFloat(DW_MANA_STOLEN));
	}

	public float getRotation(){
		return this.rotation;
	}

	public float getScale(){
		return this.scale;
	}

	public float getManaStolen(){
		return this.dataWatcher.getWatchableObjectFloat(DW_MANA_STOLEN);
	}
}
