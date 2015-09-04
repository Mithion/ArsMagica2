package am2.entities;

import am2.AMCore;
import am2.entities.ai.EntityAIFireballAttack;
import am2.particles.AMParticle;
import am2.particles.ParticleApproachPoint;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EntityFireElemental extends EntityMob{

	private static final ItemStack defaultHeldItem;
	private static final int cookRadius = 10;
	private int burnTimer;

	private byte burning;

	public EntityFireElemental(World world){
		super(world);
		setSize(1F, 2F);
		isImmuneToFire = true;

		initAI();
	}

	private void initAI(){
		this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIBreakDoor(this));
		this.tasks.addTask(2, new EntityAIFireballAttack(this, 0.5f, 1, 20));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.5f, false));
		this.tasks.addTask(7, new EntityAIWander(this, 0.5f));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	@Override
	protected boolean isAIEnabled(){
		return true;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15D);
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		dataWatcher.addObject(18, 0);
		dataWatcher.addObject(19, 0);
	}

	@Override
	public int getTotalArmorValue(){
		return 5;
	}

	@Override
	public boolean isBurning(){
		return this.getAttackTarget() != null;
	}

	@Override
	protected boolean isValidLightLevel(){
		return true;
	}

	@Override
	protected String getLivingSound(){
		return "fire_elem_living";
	}

	@Override
	protected String getHurtSound(){
		return "fire_elem_hurt";
	}

	@Override
	protected String getDeathSound(){
		return "fire_elem_death";
	}

	public int getEntityBrightnessForRender(float f){
		return 0xf000f0;
	}

	public float getEntityBrightness(float f){
		return 1.0F;
	}

	@Override
	public void onLivingUpdate(){
		if (isWet()){
			this.attackEntityFrom(DamageSource.drown, 1);
		}
		if (!this.worldObj.isRemote){
			if (this.getAttackTarget() != null && !this.getAttackTarget().isDead){
				if (this.dataWatcher.getWatchableObjectByte(0) == (byte)0){
					this.dataWatcher.updateObject(0, (byte)1);
					burnTimer = 20;
				}
			}else{
				if (burnTimer > 0){
					burnTimer--;
				}else if (this.dataWatcher.getWatchableObjectByte(0) == (byte)1){
					this.dataWatcher.updateObject(0, (byte)0);
				}
			}
		}
		super.onLivingUpdate();
	}

	@Override
	public void onUpdate(){
		int cookTargetID = dataWatcher.getWatchableObjectInt(19);
		if (cookTargetID != 0){
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(cookRadius, cookRadius, cookRadius));
			EntityItem inanimate = null;
			for (EntityItem item : items){
				if (item.getEntityId() == cookTargetID){
					inanimate = item;
				}
			}

			if (inanimate != null && worldObj.isRemote){
				AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(worldObj, "fire", posX, posY + getEyeHeight(), posZ);
				if (effect != null){
					effect.setIgnoreMaxAge(true);
					effect.AddParticleController(new ParticleApproachPoint(effect, inanimate.posX + (rand.nextFloat() - 0.5), inanimate.posY + (rand.nextFloat() - 0.5), inanimate.posZ + (rand.nextFloat() - 0.5), 0.1f, 0.1f, 1, false).setKillParticleOnFinish(true));
				}
			}
		}

		if (worldObj.isRemote && rand.nextInt(100) > 75 && !isBurning())
			for (int i = 0; i < AMCore.config.getGFXLevel(); i++)
				worldObj.spawnParticle("largesmoke", posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
		super.onUpdate();
	}

	@Override
	public ItemStack getHeldItem(){
		return defaultHeldItem;
	}

	static{
		defaultHeldItem = new ItemStack(Blocks.fire, 1);
	}

	@Override
	public boolean getCanSpawnHere(){
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		return super.getCanSpawnHere();
	}
}
