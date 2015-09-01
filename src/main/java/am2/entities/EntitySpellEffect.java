package am2.entities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.math.AMLineSegment;
import am2.api.math.AMVector3;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.enums.SpellModifiers;
import am2.buffs.BuffEffectFrostSlowed;
import am2.damage.DamageSources;
import am2.particles.AMParticle;
import am2.particles.AMParticleIcons;
import am2.particles.ParticleFleePoint;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleOrbitPoint;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;
import am2.spell.modifiers.Colour;
import am2.utility.DummyEntityPlayer;

public class EntitySpellEffect extends Entity {

	private float rotation;
	private final float rotationSpeed;

	private int ticksToEffect = 20;
	private int maxTicksToEffect = 20;
	private int maxTicksToEffect_wall = 5;

	private int ticksToExist = 100;

	private ItemStack spellStack;
	private EntityPlayer dummycaster;
	private int casterEntityID;
	private float moveSpeed;	//used by waves only

	private static final int WATCHER_STACK = 22;
	private static final int WATCHER_RADIUS = 23;
	private static final int WATCHER_GRAVITY = 24;
	private static final int WATCHER_TYPE = 25; //0 == zone, 1 == rain of fire, 2 == blizzard, 3 == wall, 4 == wave
	private static final int WATCHER_ROF_IGNITE = 26;
	private static final int WATCHER_DAMAGEBONUS = 27;

	private static final int TYPE_ZONE = 0;
	private static final int TYPE_ROF = 1;
	private static final int TYPE_BLIZ = 2;
	private static final int TYPE_WALL = 3;
	private static final int TYPE_WAVE = 4;

	private boolean firstApply = true;

	public EntitySpellEffect(World par1World) {
		super(par1World);
		this.rotation = 0;
		this.rotationSpeed = 10f;
		this.setSize(0.25f, 0.25f);
	}

	public void SetCasterAndStack(EntityLivingBase caster, ItemStack spellScroll){
		this.spellStack = spellScroll;
		this.dummycaster = DummyEntityPlayer.fromEntityLiving(caster);
		casterEntityID = caster.getEntityId();
		if (spellStack != null)
			this.dataWatcher.updateObject(WATCHER_STACK, spellStack);
	}

	public void setRadius(float newRadius){
		this.dataWatcher.updateObject(WATCHER_RADIUS, newRadius);
	}

	public void setTickRate(int newTickRate){
		this.maxTicksToEffect = newTickRate;
	}

	public void setTicksToExist(int ticks){
		this.ticksToExist = ticks;
	}

	public void setGravity(double gravity){
		dataWatcher.updateObject(WATCHER_GRAVITY, (int)Math.floor(gravity * 100));
	}

	public void setDamageBonus(float damageBonus){
		this.dataWatcher.updateObject(WATCHER_DAMAGEBONUS, damageBonus);
	}

	public float getRotation(){
		return this.rotation;
	}

	public void setWall(float rotation){
		this.setRotation(rotation, 0);
		this.dataWatcher.updateObject(WATCHER_TYPE, TYPE_WALL);
	}

	public void setWave(float rotation, float speed){
		this.setRotation(rotation, 0);
		this.dataWatcher.updateObject(WATCHER_TYPE, TYPE_WAVE);
		this.moveSpeed = speed;
		this.stepHeight = 0.5f;
		maxTicksToEffect_wall = 1;
	}

	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(WATCHER_RADIUS, 3f);
		this.dataWatcher.addObject(WATCHER_STACK, new ItemStack(Items.golden_apple));
		this.dataWatcher.addObject(WATCHER_GRAVITY, 0);
		this.dataWatcher.addObject(WATCHER_TYPE, 0);
		this.dataWatcher.addObject(WATCHER_ROF_IGNITE, (byte)0);
		this.dataWatcher.addObject(WATCHER_DAMAGEBONUS, 1.0f);
	}

	@Override
	public void onUpdate() {

		if (dummycaster != null && dummycaster instanceof DummyEntityPlayer)
			dummycaster.onUpdate();

		switch(this.dataWatcher.getWatchableObjectInt(WATCHER_TYPE)){
		case TYPE_ZONE:
			zoneUpdate();
			break;
		case TYPE_ROF:
			rainOfFireUpdate();
			break;
		case TYPE_BLIZ:
			blizzardUpdate();
			break;
		case TYPE_WALL:
			wallUpdate();
			break;
		case TYPE_WAVE:
			waveUpdate();
			break;
		}

		if (!worldObj.isRemote && this.ticksExisted >= this.ticksToExist){
			this.setDead();
		}
	}

	@Override
	public void setDead() {
		if (dummycaster instanceof DummyEntityPlayer)
			dummycaster.setDead();
		super.setDead();
	}

	private void zoneUpdate(){
		if (this.worldObj.isRemote){
			if (!AMCore.config.NoGFX()){
				this.rotation += this.rotationSpeed;
				this.rotation %= 360;

				double dist = this.dataWatcher.getWatchableObjectFloat(WATCHER_RADIUS);
				double _rotation = rotation;

				if (spellStack == null){
					spellStack = this.dataWatcher.getWatchableObjectItemStack(22);
					if (spellStack == null){
						return;
					}
				}

				int color = 0xFFFFFF;
				if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, spellStack, 0)){
					ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(spellStack, 0);
					int ordinalCount = 0;
					for (ISpellModifier mod : mods){
						if (mod instanceof Colour){
							byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(spellStack, mod, 0, ordinalCount++);
							color = (int) mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
						}
					}
				}

				if ((AMCore.config.FullGFX() && this.ticksExisted % 2 == 0) || this.ticksExisted % 8 == 0){
					for (int i = 0; i < 4; ++i){
						_rotation = (rotation + (90 * i)) % 360;
						double x = this.posX - Math.cos(3.141 / 180 * (_rotation)) * dist;
						double z = this.posZ - Math.sin(3.141 / 180 * (_rotation)) * dist;

						AMParticle effect = (AMParticle) AMCore.instance.proxy.particleManager.spawn(worldObj,AMParticleIcons.instance.getParticleForAffinity(SpellUtils.instance.mainAffinityFor(spellStack)), x, posY, z);
						if (effect != null){
							effect.setIgnoreMaxAge(false);
							effect.setMaxAge(20);
							effect.setParticleScale(0.15f);
							effect.setRGBColorI(color);
							effect.AddParticleController(new ParticleFloatUpward(effect, 0, 0.07f, 1, false));
							if (AMCore.config.LowGFX()){
								effect.AddParticleController(new ParticleOrbitPoint(effect, posX, posY, posZ, 2, false).setIgnoreYCoordinate(true).SetOrbitSpeed(0.05f).SetTargetDistance(dist).setRotateDirection(true));
							}
						}
					}
				}

			}
		}

		this.moveEntity(0, this.dataWatcher.getWatchableObjectInt(WATCHER_GRAVITY) / 100.0f, 0);

		ticksToEffect--;
		if (spellStack == null){
			if (!worldObj.isRemote){
				this.setDead();
			}
			return;
		}
		if (dummycaster == null){
			dummycaster = DummyEntityPlayer.fromEntityLiving(new EntityDummyCaster(worldObj));
		}
		if (ticksToEffect <= 0){
			ticksToEffect = maxTicksToEffect;
			float radius = this.dataWatcher.getWatchableObjectFloat(WATCHER_RADIUS);
			List<Entity> possibleTargets = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(posX - radius, posY - 1, posZ - radius, posX + radius, posY + 3, posZ + radius));
			for (Entity e : possibleTargets){
				if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
					e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;

				if (e instanceof EntityLivingBase)
					SpellHelper.instance.applyStageToEntity(spellStack, dummycaster, worldObj, e, 0, false);
			}
			if (this.dataWatcher.getWatchableObjectInt(WATCHER_GRAVITY) < 0 && !firstApply)
				SpellHelper.instance.applyStackStage(spellStack, dummycaster, null, posX, posY-1, posZ, 0, worldObj, false, false, this.ticksExisted);
			else
				SpellHelper.instance.applyStackStage(spellStack, dummycaster, null, posX, posY, posZ, 0, worldObj, false, false, this.ticksExisted);
			firstApply = false;
		}
	}

	private void rainOfFireUpdate(){
		float radius = this.dataWatcher.getWatchableObjectFloat(WATCHER_RADIUS);
		if (worldObj.isRemote){

			if (spellStack == null){
				spellStack = this.dataWatcher.getWatchableObjectItemStack(22);
				if (spellStack == null){
					return;
				}
			}

			int color = 0xFFFFFF;
			if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, spellStack, 0)){
				ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(spellStack, 0);
				int ordinalCount = 0;
				for (ISpellModifier mod : mods){
					if (mod instanceof Colour){
						byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(spellStack, mod, 0, ordinalCount++);
						color = (int) mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
					}
				}
			}

			for (int i = 0; i < 10; ++i){
				double x = this.posX - radius + (rand.nextDouble() * radius * 2);
				double z = this.posZ - radius + (rand.nextDouble() * radius * 2);
				double y = this.posY + 10;

				AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "explosion_2", x, y, z);
				if (particle != null){
					particle.setMaxAge(20);
					particle.addVelocity(rand.nextDouble() * 0.2f, 0, rand.nextDouble() * 0.2f);
					particle.setAffectedByGravity();
					particle.setDontRequireControllers();
					particle.setRGBColorI(color);
					particle.noClip = false;
				}
			}

			//TODO: SoundHelper.instance.loopSound(worldObj, (float)posX, (float)posY, (float)posZ, "arsmagica2:spell.loop.fire", 1.0f);
		}else{
			List<Entity> possibleTargets = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(posX - radius, posY - 1, posZ - radius, posX + radius, posY + 3, posZ + radius));
			for (Entity e : possibleTargets){
				if (e != dummycaster){
					if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
						e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;

					double lastVelX = e.motionX;
					double lastVelY = e.motionY;
					double lastVelZ = e.motionZ;

					float damage = 0.75f * this.dataWatcher.getWatchableObjectFloat(WATCHER_DAMAGEBONUS);

					if (SpellHelper.instance.attackTargetSpecial(null, e, DamageSources.causeEntityFireDamage(dummycaster), damage) && !(e instanceof EntityPlayer))
						e.hurtResistantTime = 10;
					e.addVelocity(-(e.motionX - lastVelX), -(e.motionY - lastVelY), -(e.motionZ - lastVelZ));
				}
			}
			if (this.dataWatcher.getWatchableObjectByte(WATCHER_ROF_IGNITE) == 1 && rand.nextInt(10) < 2){
				int pX = (int)(posX - radius + rand.nextInt((int)Math.ceil(radius) * 2));
				int pY = (int)posY;
				int pZ = (int)(posZ - radius + rand.nextInt((int)Math.ceil(radius) * 2));
				if (worldObj.isAirBlock(pX, pY, pZ))
					worldObj.setBlock(pX, pY, pZ, Blocks.fire);
			}
		}
	}

	private void blizzardUpdate(){
		float radius = this.dataWatcher.getWatchableObjectFloat(WATCHER_RADIUS);
		if (worldObj.isRemote){

			if (spellStack == null){
				spellStack = this.dataWatcher.getWatchableObjectItemStack(22);
				if (spellStack == null){
					return;
				}
			}

			int color = 0xFFFFFF;
			if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, spellStack, 0)){
				ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(spellStack, 0);
				int ordinalCount = 0;
				for (ISpellModifier mod : mods){
					if (mod instanceof Colour){
						byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(spellStack, mod, 0, ordinalCount++);
						color = (int) mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
					}
				}
			}

			for (int i = 0; i < 20; ++i){
				double x = this.posX - radius + (rand.nextDouble() * radius * 2);
				double z = this.posZ - radius + (rand.nextDouble() * radius * 2);
				double y = this.posY + 10;

				AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "snowflakes", x, y, z);
				if (particle != null){
					particle.setMaxAge(20);
					particle.setParticleScale(0.1f);
					particle.addVelocity(rand.nextDouble() * 0.2f - 0.1f, 0, rand.nextDouble() * 0.2f - 0.1f);
					particle.setAffectedByGravity();
					particle.setRGBColorI(color);
					particle.setDontRequireControllers();
					particle.noClip = false;
				}
			}

			double x = this.posX - radius + (rand.nextDouble() * radius * 2);
			double z = this.posZ - radius + (rand.nextDouble() * radius * 2);
			double y = this.posY + rand.nextDouble();
			AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "smoke", x, y, z);
			if (particle != null){
				particle.setParticleScale(2.0f);
				particle.setMaxAge(20);
				//particle.setRGBColorF(0.5f, 0.92f, 0.92f);
				particle.setRGBColorF(0.5098f, 0.7843f, 0.7843f);
				particle.SetParticleAlpha(0.6f);
				particle.AddParticleController(new ParticleFleePoint(particle, new AMVector3(x,y,z), 0.1f, 3f, 1, false));
			}

			//TODO: SoundHelper.instance.loopSound(worldObj, (float)posX, (float)posY, (float)posZ, "arsmagica2:spell.loop.air", 1.0f);
		}else{
			List<Entity> possibleTargets = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(posX - radius, posY - 1, posZ - radius, posX + radius, posY + 3, posZ + radius));
			for (Entity e : possibleTargets){
				if (e != dummycaster){
					if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
						e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;

					if (e instanceof EntityLivingBase)
						((EntityLivingBase)e).addPotionEffect(new BuffEffectFrostSlowed(80, 3));

					float damage = 1 * this.dataWatcher.getWatchableObjectFloat(WATCHER_DAMAGEBONUS);

					double lastVelX = e.motionX;
					double lastVelY = e.motionY;
					double lastVelZ = e.motionZ;
					if (SpellHelper.instance.attackTargetSpecial(null, e, DamageSources.causeEntityFrostDamage(dummycaster), damage)&& !(e instanceof EntityPlayer))
						e.hurtResistantTime = 15;
					e.addVelocity(-(e.motionX - lastVelX), -(e.motionY - lastVelY), -(e.motionZ - lastVelZ));
				}
			}

			if (rand.nextInt(10) < 2){
				int pX = (int)(posX - radius + rand.nextInt((int)Math.ceil(radius) * 2));
				int pY = (int)posY + rand.nextInt(2);
				int pZ = (int)(posZ - radius + rand.nextInt((int)Math.ceil(radius) * 2));
				if (worldObj.isAirBlock(pX, pY, pZ) && !worldObj.isAirBlock(pX, pY-1, pZ) && worldObj.getBlock(pX, pY, pZ).isOpaqueCube())
					worldObj.setBlock(pX, pY, pZ, Blocks.snow);
			}
		}
	}

	private void wallUpdate(){
		if (worldObj.isRemote){
			if (spellStack == null){
				spellStack = this.dataWatcher.getWatchableObjectItemStack(22);
				if (spellStack == null){
					return;
				}
			}

			double dist = this.dataWatcher.getWatchableObjectFloat(WATCHER_RADIUS);

			int color = 0xFFFFFF;
			if (SpellUtils.instance.modifierIsPresent(SpellModifiers.COLOR, spellStack, 0)){
				ISpellModifier[] mods = SpellUtils.instance.getModifiersForStage(spellStack, 0);
				int ordinalCount = 0;
				for (ISpellModifier mod : mods){
					if (mod instanceof Colour){
						byte[] meta = SpellUtils.instance.getModifierMetadataFromStack(spellStack, mod, 0, ordinalCount++);
						color = (int) mod.getModifier(SpellModifiers.COLOR, null, null, null, meta);
					}
				}
			}

			double px = Math.cos(3.141 / 180 * (rotationYaw + 90)) * 0.1f;
			double pz = Math.sin(3.141 / 180 * (rotationYaw + 90)) * 0.1f;
			double py = 0.1f;

			for (float i = 0; i < dist; i += 0.5f){
				double x = this.posX - Math.cos(3.141 / 180 * (rotationYaw)) * i;
				double z = this.posZ - Math.sin(3.141 / 180 * (rotationYaw)) * i;

				AMParticle effect = (AMParticle) AMCore.instance.proxy.particleManager.spawn(worldObj,AMParticleIcons.instance.getParticleForAffinity(SpellUtils.instance.mainAffinityFor(spellStack)), x, posY, z);
				if (effect != null){
					effect.setIgnoreMaxAge(false);
					effect.setMaxAge(20);
					effect.addRandomOffset(1, 1, 1);
					effect.setParticleScale(0.15f);
					effect.setRGBColorI(color);
					if (dataWatcher.getWatchableObjectInt(WATCHER_TYPE) == TYPE_WALL){
						effect.AddParticleController(new ParticleFloatUpward(effect, 0, 0.07f, 1, false));
					}else{
						effect.setAffectedByGravity();
						effect.setDontRequireControllers();
						effect.addVelocity(px, py, pz);
					}
				}

				x = this.posX - Math.cos(Math.toRadians(rotationYaw)) * -i;
				z = this.posZ - Math.sin(Math.toRadians(rotationYaw)) * -i;

				effect = (AMParticle) AMCore.instance.proxy.particleManager.spawn(worldObj,AMParticleIcons.instance.getParticleForAffinity(SpellUtils.instance.mainAffinityFor(spellStack)), x, posY, z);
				if (effect != null){
					effect.setIgnoreMaxAge(false);
					effect.addRandomOffset(1, 1, 1);
					effect.setMaxAge(20);
					effect.setParticleScale(0.15f);
					effect.setRGBColorI(color);
					if (dataWatcher.getWatchableObjectInt(WATCHER_TYPE) == TYPE_WALL){
						effect.AddParticleController(new ParticleFloatUpward(effect, 0, 0.07f, 1, false));
					}else{
						effect.setAffectedByGravity();
						effect.setDontRequireControllers();
						effect.addVelocity(px, py, pz);
					}
				}
			}

		}else{

			ticksToEffect--;
			if (spellStack == null){
				if (!worldObj.isRemote){
					this.setDead();
				}
				return;
			}

			if (dummycaster == null){
				dummycaster = DummyEntityPlayer.fromEntityLiving(new EntityDummyCaster(worldObj));
			}
			if (ticksToEffect <= 0){
				ticksToEffect = maxTicksToEffect_wall;
				float radius = this.dataWatcher.getWatchableObjectFloat(WATCHER_RADIUS);
				List<Entity> possibleTargets = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(posX - radius, posY - 1, posZ - radius, posX + radius, posY + 3, posZ + radius));
				ItemStack newStack = SpellUtils.instance.popStackStage(spellStack);
				for (Entity e : possibleTargets){
					if (e == this || e == dummycaster || e.getEntityId() == casterEntityID) continue;

					if (e instanceof EntityDragonPart && ((EntityDragonPart)e).entityDragonObj instanceof EntityLivingBase)
						e = (EntityLivingBase)((EntityDragonPart)e).entityDragonObj;

					AMVector3 target = new AMVector3(e);

					double dirX = Math.cos(3.141 / 180 * (rotationYaw));
					double dirZ = Math.sin(3.141 / 180 * (rotationYaw));

					AMVector3 a = new AMVector3(this.posX - dirX * radius, this.posY, this.posZ - dirZ * radius);
					AMVector3 b = new AMVector3(this.posX - dirX * -radius, this.posY, this.posZ - dirZ * -radius);

					AMVector3 closest = new AMLineSegment(a, b).closestPointOnLine(target);

					closest.y = 0;
					target.y = 0;

					double hDistance = closest.distanceTo(target);
					double vDistance = Math.abs(this.posY - e.posY);

					if (e instanceof EntityLivingBase && hDistance < 0.75f && vDistance < 2){
						//commented out in favor of line below so as to apply subsequent shapes as well
						//uncomment and comment out below line to revert to direct target only, but mark wave/wall as terminus
						//SpellHelper.instance.applyStageToEntity(spellStack, dummycaster, worldObj, e, 0, false);
						SpellHelper.instance.applyStackStage(spellStack, dummycaster, (EntityLivingBase) e, this.posX, this.posY, this.posZ, 0, worldObj, false, false, 0);
					}
				}
			}
		}
	}

	private void waveUpdate(){
		ticksToEffect = 0;
		wallUpdate();
		double dx = Math.cos(Math.toRadians(this.rotationYaw + 90));
		double dz = Math.sin(Math.toRadians(this.rotationYaw + 90));

		this.moveEntity(dx * moveSpeed, 0, dz * moveSpeed);

		double dxH = Math.cos(Math.toRadians(this.rotationYaw));
		double dzH = Math.sin(Math.toRadians(this.rotationYaw));

		float radius = this.dataWatcher.getWatchableObjectFloat(WATCHER_RADIUS);

		AMVector3 a = new AMVector3( (this.posX + dx) - dxH * radius, this.posY, (this.posZ + dz) - dzH * radius);
		AMVector3 b = new AMVector3((this.posX + dx) - dxH * -radius, this.posY, (this.posZ + dz) - dzH * -radius);

		if (dummycaster == null){
			dummycaster = DummyEntityPlayer.fromEntityLiving(new EntityDummyCaster(worldObj));
		}

		AMVector3[] vecs = getAllBlockLocationsBetween(a, b);
		for (AMVector3 vec : vecs){
			SpellHelper.instance.applyStageToGround(spellStack, dummycaster, worldObj, (int)vec.x, (int)vec.y, (int)vec.z, 0, vec.x+0.5, vec.y+0.5, vec.z+0.5, 0, false);
		}

	}

	private AMVector3[] getAllBlockLocationsBetween(AMVector3 a, AMVector3 b){
		a.floorToI();
		b.floorToI();

		double stepX = a.x < b.x ? 0.2f : -0.2f;
		double stepZ = a.z < b.z ? 0.2f : -0.2f;
		ArrayList<AMVector3> vecList = new ArrayList<AMVector3>();
		AMVector3 curPos = new AMVector3(a.x, a.y, a.z);
		for (int i = 0; i < this.height; ++i){
			vecList.add(new AMVector3(curPos.x, curPos.y + i, curPos.z));
		}

		while (stepX != 0 || stepZ != 0){
			if ( (stepX < 0 && curPos.x <= b.x) || (stepX > 0 && curPos.x >= b.x) )
				stepX = 0;
			if ( (stepZ < 0 && curPos.z <= b.z) || (stepZ > 0 && curPos.z >= b.z) )
				stepZ = 0;
			curPos = new AMVector3(curPos.x + stepX, curPos.y, curPos.z + stepZ);
			AMVector3 tempPos = curPos.copy();
			tempPos.roundToI();
			if (!vecList.contains(tempPos)){
				for (int i = 0; i < this.height; ++i){
					vecList.add(new AMVector3(tempPos.x, tempPos.y + i, tempPos.z));
				}
			}
		}

		return vecList.toArray(new AMVector3[vecList.size()]);
	}

	@Override
	protected void fall(float par1) {
	}

	@Override
	public void onEntityUpdate() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1) {
	}

	public void setRainOfFire(boolean ignite){
		this.dataWatcher.updateObject(WATCHER_TYPE, TYPE_ROF);
		if (ignite)
			this.dataWatcher.updateObject(WATCHER_ROF_IGNITE, (byte)1);
	}

	public void setBlizzard(){
		this.dataWatcher.updateObject(WATCHER_TYPE, TYPE_BLIZ);
	}

	public boolean isBlizzard(){
		return this.dataWatcher.getWatchableObjectInt(WATCHER_TYPE) == TYPE_BLIZ;
	}

	public boolean isRainOfFire(){
		return this.dataWatcher.getWatchableObjectInt(WATCHER_TYPE) == TYPE_ROF;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}
}
