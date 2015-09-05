package am2.entities;

import am2.AMCore;
import am2.entities.ai.EntityAIWaterElementalAttack;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityWaterElemental extends EntityMob{

	private float hostileSpeed;

	public EntityWaterElemental(World par1World){
		super(par1World);
		this.hostileSpeed = 0.46F;
		initAI();
		ExtendedProperties.For(this).setMagicLevel(5);
		ExtendedProperties.For(this).setMaxMana(300);
		ExtendedProperties.For(this).setCurrentMana(300);
	}

	@Override
	public boolean isAIEnabled(){
		return true;
	}

	private void initAI(){

		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAIWaterElementalAttack(this, EntityPlayer.class, this.hostileSpeed, 4, false));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	@Override
	public void onUpdate(){
		if (this.worldObj != null){
			if (this.worldObj.isRemote){
				spawnLivingParticles();
			}
		}
		super.onUpdate();
	}

	@Override
	protected void dropRareDrop(int par1){
		this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, 4), 0.0f);
	}

	private void spawnLivingParticles(){
		if (rand.nextBoolean()){
			double yPos = this.posY + 1.1;
			AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "water_ball",
					this.posX + ((rand.nextFloat() * 0.2) - 0.1f),
					yPos,
					this.posZ + ((rand.nextFloat() * 0.4) - 0.2f));
			if (effect != null){
				effect.AddParticleController(new ParticleFloatUpward(effect, 0.1f, -0.06f, 1, false));
				effect.AddParticleController(new ParticleFadeOut(effect, 2, false).setFadeSpeed(0.04f));
				effect.setMaxAge(25);
				effect.setIgnoreMaxAge(false);
				effect.setParticleScale(0.1f);
			}
		}
	}

	/* Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
	 * true)
	 */
	@Override
	public boolean isInWater(){
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
	}

	@Override
	public boolean getCanSpawnHere(){
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		return super.getCanSpawnHere();
	}
}
