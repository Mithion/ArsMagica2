package am2.entities;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import am2.AMCore;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleMoveOnHeading;
import am2.playerextensions.ExtendedProperties;
import am2.texture.ResourceManager;

public class EntityHecate extends EntityZombie {

	private double leftArmAnimTicks;
	private double rightArmAnimTicks;

	private double leftArmRotationOffset;
	private double rightArmRotationOffset;

	private final float hostileSpeed;
	private static final float forwardThreshold =  1.22f;
	private static float rotationAtThreshold;
	private float currentForwardRotation = 0f;

	private int invisibilityCooldown = 0;
	private int invisibilityCounter = 0;
	private boolean hasSpawnedInvisParticles = false;
	private final String localTexture;

	public EntityHecate(World par1World) {
		super(par1World);
		leftArmAnimTicks = 0;
		rightArmAnimTicks = 12;
		leftArmRotationOffset= 0;
		rightArmRotationOffset = 0;
		localTexture = ResourceManager.getMobTexturePath("mobHecate.png");
		this.hostileSpeed = 1.7F;
		this.setSize(1f, 1.5f);

		ExtendedProperties.For(this).setMagicLevel(7);
		ExtendedProperties.For(this).setMaxMana(600);
		ExtendedProperties.For(this).setCurrentMana(600);

		this.tasks.taskEntries.clear();
		this.targetTasks.taskEntries.clear();
		initAI();
		this.stepHeight = 1.02f;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	private void initAI(){
		this.getNavigator().setBreakDoors(true);
		this.getNavigator().setAvoidSun(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIBreakDoor(this));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.hostileSpeed, false));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityVillager.class, this.hostileSpeed, true));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityGolem.class, this.hostileSpeed, true));
		this.tasks.addTask(1, new EntityAIFleeSun(this, this.hostileSpeed));
		this.tasks.addTask(7, new EntityAIWander(this, 0.5f));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityGolem.class,  0, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
	}

	@Override
	public boolean isAIEnabled(){
		return true;
	}

	@Override
	public int getTotalArmorValue()
	{
		return 5;
	}

	public float getHorizontalAverageVelocity(){
		return (float) ((this.motionX + this.motionZ) / 2);
	}

	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
	}

	private boolean isMoving(){
		return (this.prevPosX != this.posX) || (this.prevPosZ != this.posZ);
	}

	private void updateForwardRotation(){
		if (isMoving() && currentForwardRotation < forwardThreshold){
			currentForwardRotation += 0.12f;
		}else if (!isMoving() && currentForwardRotation > 0){
			currentForwardRotation -= 0.12f;
		}
	}

	public float getForwardRotation(){
		return currentForwardRotation;
	}

	private void checkAttackTarget(){
		if (this.getAttackTarget() != null){
			this.setAttackTarget(null);
		}
	}

	@Override
	public void onUpdate(){

		if (invisibilityCooldown > 0){
			invisibilityCooldown--;
		}
		if (invisibilityCooldown == 0) hasSpawnedInvisParticles = false;

		if (this.motionY < 0)
			this.motionY *= 0.79999f;

		if (this.worldObj != null){
			if (this.worldObj.isRemote){
				if (!this.getFlag(5) && this.ticksExisted % 3 == 0){
					spawnLivingParticles();
				}else if (!hasSpawnedInvisParticles){
					spawnInvisibilityParticles();
				}

				if (invisibilityCounter > 0) invisibilityCounter--;

				updateArmRotations();
				updateForwardRotation();
			}
			if (this.worldObj.difficultySetting == EnumDifficulty.HARD && this.getAttackTarget() != null && this.invisibilityCooldown == 0){
				this.addPotionEffect(new PotionEffect(Potion.invisibility.id, 60, 2));
				this.invisibilityCooldown = 600;
			}
		}
		super.onUpdate();
	}

	@Override
	public void onLivingUpdate() {
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isDead)
		{
			float f = this.getBrightness(1.0F);

			if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
			{
				AMNetHandler.INSTANCE.sendHecateDeathToAllAround(this);
				this.attackEntityFrom(DamageSource.onFire, 5000);
			}
		}
		super.onLivingUpdate();
	}

	private void spawnInvisibilityParticles(){
		/*for (int i = 0; i < 50; ++i){
			ArsMagicaParticle effect = ParticleManager.spawn(this.worldObj, "hr_smoke", this.posX + rand.nextDouble(), this.posY + 1, this.posZ);
			if (effect != null){
				effect.setMaxAge(20);
				effect.setIgnoreMaxAge(false);
				effect.AddParticleController(new ParticleFleeEntity(effect, this, 0.1, 3, 1, false));
			}
		}*/
		hasSpawnedInvisParticles = true;
		this.invisibilityCooldown = 600;
	}

	public double getLeftArmOffset(){
		return this.leftArmRotationOffset;
	}

	public double getRightArmOffset(){
		return this.rightArmRotationOffset;
	}

	private void spawnLivingParticles(){

		if (rand.nextInt(3) == 0){
			double yPos = this.posY + 1.1;
			if (this.currentForwardRotation >= 0.24){
				yPos += 0.3;
			}

			AMParticle effect = (AMParticle) AMCore.instance.proxy.particleManager.spawn(worldObj, "smoke",
					this.posX + ((rand.nextFloat() * 0.2) - 0.1f),
					yPos,
					this.posZ + ((rand.nextFloat() * 0.4) - 0.2f));
			if (effect != null){
				if (this.currentForwardRotation < 0.24){
					effect.AddParticleController(new ParticleFloatUpward(effect, 0.1f, -0.06f, 1, false));
				}else{
					effect.AddParticleController(new ParticleMoveOnHeading(effect, this.rotationYaw - 90, this.rotationPitch, 0.01f, 1, false));
				}
				effect.AddParticleController(new ParticleFadeOut(effect, 2, false).setFadeSpeed(0.04f));
				effect.setMaxAge(25);
				effect.setIgnoreMaxAge(false);
				effect.setRGBColorF(0.3f, 0.3f, 0.3f);
			}
		}
	}

	private void updateArmRotations(){
		leftArmAnimTicks += 0.05;
		leftArmAnimTicks %= 90;
		rightArmAnimTicks += 0.05;
		rightArmAnimTicks %= 90;

		//double lpct = ((double)leftArmAnimTicks - 90) / 180.0d;
		//double rpct = ((double)rightArmAnimTicks - 90) / 180.0d;

		leftArmRotationOffset = Math.sin(leftArmAnimTicks) * .3;
		rightArmRotationOffset = Math.cos(rightArmAnimTicks) * .3;
	}

	@Override
	protected void dropRareDrop(int par1)
	{
		this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, 9), 0.0f);
	}

	@Override
	protected Item getDropItem() {
		return null;
	}

	@Override
	protected String getHurtSound() {
		return "arsmagica2:mob.hecate.hit";
	}

	@Override
	protected String getDeathSound() {
		return "arsmagica2:mob.hecate.death";
	}

	@Override
	protected String getLivingSound() {
		return "arsmagica2:mob.hecate.idle";
	}

	@Override
	public float getShadowSize() {
		return 0;
	}

	private int getAverageNearbyPlayerMagicLevel(){
		if (this.worldObj == null) return 0;
		List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBox.expand(250, 250, 250));
		if (players.size() == 0) return 0;
		int avgLvl = 0;
		for (EntityPlayer player : players){
			avgLvl += ExtendedProperties.For(player).getMagicLevel();
		}
		return (int)Math.ceil(avgLvl/players.size());
	}

	@Override
	public boolean getCanSpawnHere() {
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		if (getAverageNearbyPlayerMagicLevel() < 20){
			return false;
		}
		return super.getCanSpawnHere();
	}
}
