package am2.bosses;

import am2.AMCore;
import am2.bosses.ai.*;
import am2.damage.DamageSourceFrost;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.particles.*;
import am2.utility.NPCSpells;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class EntityFireGuardian extends AM2Boss{

	private boolean isUnderground = false;
	private int hitCount = 0;

	public EntityFireGuardian(World par1World){
		super(par1World);
		this.setSize(1.0f, 4.0f);
		this.isImmuneToFire = true;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(250);
	}

	@Override
	public int getTotalArmorValue(){
		return 17;
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(1, new EntityAIFireRain(this));
		this.tasks.addTask(1, new EntityAIDispel(this));
		this.tasks.addTask(2, new EntityAIDive(this));
		this.tasks.addTask(2, new EntityAICastSpell(this, NPCSpells.instance.meltArmor, 12, 23, 40, BossActions.CASTING));
		this.tasks.addTask(3, new EntityAIFlamethrower(this));
		this.tasks.addTask(4, new EntityAICastSpell(this, NPCSpells.instance.fireBolt, 12, 23, 5, BossActions.CASTING));
	}

	public boolean getIsUnderground(){
		return this.isUnderground;
	}


	@Override
	public void setCurrentAction(BossActions action){
		super.setCurrentAction(action);

		if (action == BossActions.SPINNING){
			this.addVelocity(0, 1.5, 0);
		}else{
			hitCount = 0;
			isUnderground = false;
		}

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	private void nova(){
		if (this.worldObj.isRemote){
			for (int i = 0; i < 36; ++i){
				AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "explosion_2", posX, posY - 3, posZ);
				if (particle != null){
					particle.AddParticleController(new ParticleMoveOnHeading(particle, i * 10, rand.nextInt(20) - 10, 0.2f, 1, false));
					particle.AddParticleController(new ParticleLeaveParticleTrail(particle, "explosion_2", false, 10, 1, false)
							.setTicksBetweenSpawns(1)
							.setParticleRGB_F(1, 1, 1)
							.addControllerToParticleList(new ParticleHoldPosition(particle, 10, 1, false)));
					particle.setMaxAge(20);
					particle.setParticleScale(0.5f);
					particle.setIgnoreMaxAge(false);
				}
			}
		}else{
			List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(2.5, 2.5, 2.5).addCoord(0, -3, 0));
			for (EntityLivingBase ent : entities){
				if (ent == this)
					continue;
				ent.attackEntityFrom(DamageSources.causeEntityFireDamage(this), 5);
			}
		}
	}

	private void flamethrower(){
		Vec3 look = this.getLook(1.0f);
		if (worldObj.isRemote){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "explosion_2", posX + (Math.cos(Math.toRadians(this.renderYawOffset + 90)) * 2), posY + 3, posZ + (Math.sin(Math.toRadians(this.renderYawOffset + 90)) * 2));
			if (particle != null){
				particle.AddParticleController(new ParticleMoveOnHeading(particle, this.rotationYaw + 90 + rand.nextInt(20) - 10, rand.nextInt(20) - 10, 0.2f, 1, false));
				particle.setMaxAge(40);
				particle.setParticleScale(0.5f);
				particle.setIgnoreMaxAge(false);
			}
		}else{
			List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.boundingBox.expand(2.5, 2.5, 2.5).addCoord(look.xCoord * 3, 0, look.zCoord * 3));
			for (EntityLivingBase ent : entities){
				if (ent == this)
					continue;
				ent.attackEntityFrom(DamageSources.causeEntityFireDamage(this), 5);
			}
		}
	}

	private void doFlameShield(){
		if (worldObj.isRemote){
			return;
		}else{
			for (Object p : worldObj.playerEntities){
				EntityPlayer player = (EntityPlayer)p;
				if (this.getDistanceSqToEntity(player) < 9){
					player.attackEntityFrom(DamageSources.causeEntityFireDamage(this), 2);
				}
			}
		}
	}

	@Override
	public boolean isBurning(){
		return !isUnderground;
	}

	@Override
	public void onUpdate(){

		if (ticksInCurrentAction == 30 && currentAction == BossActions.SPINNING){
			nova();
		}

		if (ticksInCurrentAction > 13 && currentAction == BossActions.LONG_CASTING){
			if (this.getAttackTarget() != null)
				this.faceEntity(this.getAttackTarget(), 10, 10);
			flamethrower();
		}

		doFlameShield();

		super.onUpdate();
	}

	@Override
	protected void fall(float par1){
		if (this.getCurrentAction() == BossActions.SPINNING){
			this.isUnderground = true;
			return;
		}
		super.fall(par1);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){

		if (par1DamageSource.isFireDamage()){
			this.heal(par2);
			if (this.worldObj.isRemote){
				AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "sparkle", posX, posY - 1, posZ);
				if (particle != null){
					particle.addRandomOffset(1, 1, 1);
					particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.1f, 1, false));
					particle.AddParticleController(new ParticleOrbitEntity(particle, this, 0.5f, 2, false).setIgnoreYCoordinate(true).SetTargetDistance(0.3f + rand.nextDouble() * 0.3));
					particle.setMaxAge(20);
					particle.setParticleScale(0.2f);
					particle.setRGBColorF(0.1f, 1f, 0.1f);
				}
			}
			return false;
		}

		if (this.isUnderground && this.getCurrentAction() != BossActions.SPINNING){
			return false;
		}else{
			if (this.getCurrentAction() == BossActions.SPINNING)
				++hitCount;
		}

		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source == DamageSource.drown)
			damageAmt *= 2;
		else if (source instanceof DamageSourceFrost)
			damageAmt /= 3;
		return damageAmt;
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.fireguardian.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.fireguardian.death";
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.fireguardian.idle";
	}

	@Override
	public String getAttackSound(){
		return "arsmagica2:mob.fireguardian.attack";
	}

	public int getNumHits(){
		return this.hitCount;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_RED), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_FIRE), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemsCommonProxy.fireEarsEnchanted.copy(), 0.0f);
		}
	}
}
