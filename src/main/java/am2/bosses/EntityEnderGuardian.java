package am2.bosses;

import thehippomaster.AnimationAPI.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.math.AMVector3;
import am2.bosses.ai.EntityAIEnderRush;
import am2.bosses.ai.EntityAIEnderbolt;
import am2.bosses.ai.EntityAIEndertorrent;
import am2.bosses.ai.EntityAIEnderwave;
import am2.bosses.ai.EntityAIOtherworldlyRoar;
import am2.bosses.ai.EntityAIProtect;
import am2.bosses.ai.EntityAIShadowstep;
import am2.buffs.BuffList;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;

public class EntityEnderGuardian extends AM2Boss implements IAnimatedEntity{

	private int wingFlapTime = 0;
	private int ticksSinceLastAttack = 0;
	private String lastDamageType = "";
	private int hitCount = 0;
	private AMVector3 spawn;

	private static final int ATTACK_TARGET = 20;

	public EntityEnderGuardian(World par1World) {
		super(par1World);
		setSize(1, 3);
	}

	@Override
	protected void initSpecificAI() {
		//tasks.addTask(2, new EntityAIHandsOfTheDead(this));
		tasks.addTask(2, new EntityAIShadowstep(this));
		tasks.addTask(2, new EntityAIEnderwave(this));
		tasks.addTask(2, new EntityAIOtherworldlyRoar(this));
		tasks.addTask(2, new EntityAIProtect(this));
		tasks.addTask(2, new EntityAIEnderRush(this));
		tasks.addTask(2, new EntityAIEndertorrent(this));
		tasks.addTask(2, new EntityAIEnderbolt(this));
	}

	@Override
	public int getTotalArmorValue() {
		return 16;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(ATTACK_TARGET, -1);
	}

	@Override
	public float getEyeHeight() {
		return 2.5f;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(490D);
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt) {
		if (source.isMagicDamage()){
			damageAmt *= 2f;
		}
		return damageAmt;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (spawn == null)
			spawn = new AMVector3(this);

		wingFlapTime++;
		ticksSinceLastAttack++;

		if (this.motionY < 0)
			this.motionY *= 0.7999999f;

		switch(getCurrentAction()){
		case LONG_CASTING: //roar
			if (this.getTicksInCurrentAction() == 32)
				worldObj.playSoundAtEntity(this, "arsmagica2:mob.enderguardian.roar", 1.0f, 1.0f);
			break;
		case CHARGE:
			if (this.getTicksInCurrentAction() == 0)
				this.addVelocity(0, 1.5f, 0);
			break;
		default:
		}

		if (shouldFlapWings() && wingFlapTime % (50 * this.getWingFlapSpeed()) == 0){
			worldObj.playSoundAtEntity(this, "arsmagica2:mob.enderguardian.flap", 1.0f, 1.0f);
		}
	}

	public int getTicksSinceLastAttack(){
		return ticksSinceLastAttack;
	}

	@Override
	public void setAttackTarget(EntityLivingBase par1EntityLivingBase) {
		super.setAttackTarget(par1EntityLivingBase);
		if (!worldObj.isRemote){
			if (par1EntityLivingBase != null)
				this.dataWatcher.updateObject(ATTACK_TARGET, par1EntityLivingBase.getEntityId());
			else
				this.dataWatcher.updateObject(ATTACK_TARGET, -1);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {

		int you = 0;
		int should = 0;
		int not = 0;
		int be = 0;
		int looking = 0;
		int here = 0;
		int cheater = 0;

		if (par1DamageSource.getSourceOfDamage() instanceof EntityEnderman){
			((EntityEnderman)par1DamageSource.getSourceOfDamage()).attackEntityFrom(DamageSources.wtfBoom, 5000);
			this.heal(10);
			return false;
		}

		if (par1DamageSource.damageType.equals("outOfWorld")){
			if (spawn != null){
				this.setPosition(spawn.x, spawn.y, spawn.z);
				this.setCurrentAction(BossActions.IDLE);
				if (!this.worldObj.isRemote)
					AMCore.proxy.addDeferredTargetSet(this, null);
			}else{
				this.setDead();
			}
			return false;
		}

		ticksSinceLastAttack = 0;

		if (!worldObj.isRemote && par1DamageSource.getSourceOfDamage() != null && par1DamageSource.getSourceOfDamage() instanceof EntityPlayer){
			if (par1DamageSource.damageType == this.lastDamageType){
				hitCount++;
				if (hitCount > 5)
					this.heal(par2 / 4);
				return false;
			}else{
				this.lastDamageType = par1DamageSource.damageType;
				hitCount = 1;
			}
		}

		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	public EntityLivingBase getAttackTarget() {
		if (!worldObj.isRemote)
			return super.getAttackTarget();
		else
			return (EntityLivingBase) worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(ATTACK_TARGET));
	}
	@Override
	public void setCurrentAction(BossActions action) {
		this.currentAction = action;
		if (action == BossActions.LONG_CASTING)
			wingFlapTime = 0;
	}

	public int getWingFlapTime(){
		return wingFlapTime;
	}

	public float getWingFlapSpeed(){
		switch(this.currentAction){
		case CASTING:
			return 0.5f;
		case STRIKE:
			return 0.4f;
		case CHARGE:
			if (ticksInCurrentAction < 15)
				return 0.25f;
			return 0.75f;
		default:
			return 0.25f;
		}
	}

	public boolean shouldFlapWings(){
		return currentAction != BossActions.LONG_CASTING && currentAction != BossActions.SHIELD_BASH;
	}

	@Override
	public boolean isPotionActive(int par1) {
		if (par1 == BuffList.spellReflect.id && (currentAction == BossActions.SHIELD_BASH || currentAction == BossActions.LONG_CASTING))
			return true;
		if (par1 == BuffList.magicShield.id && (currentAction == BossActions.SHIELD_BASH || currentAction == BossActions.LONG_CASTING))
			return true;
		return super.isPotionActive(par1);
	}

	@Override
	public boolean isPotionActive(Potion par1Potion) {
		if (par1Potion == BuffList.spellReflect && (currentAction == BossActions.SHIELD_BASH || currentAction == BossActions.LONG_CASTING))
			return true;
		if (par1Potion == BuffList.magicShield && (currentAction == BossActions.SHIELD_BASH || currentAction == BossActions.LONG_CASTING))
			return true;
		return super.isPotionActive(par1Potion);
	}

	@Override
	protected String getHurtSound() {
		return "arsmagica2:mob.enderguardian.hit";
	}

	@Override
	protected String getDeathSound() {
		return "arsmagica2:mob.enderguardian.death";
	}

	@Override
	protected String getLivingSound() {
		return "arsmagica2:mob.enderguardian.idle";
	}

	@Override
	public String getAttackSound() {
		return "arsmagica2:mob.enderguardian.attack";
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_RED), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++)
		{
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ENDER), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemsCommonProxy.enderBootsEnchanted.copy(), 0.0f);
		}
	}

	@Override
	public void setAnimID(int id) {
		setCurrentAction(BossActions.values()[id]);
		ticksInCurrentAction = 0;
	}

	@Override
	public void setAnimTick(int tick) {
		this.ticksInCurrentAction = tick;
	}

	@Override
	public int getAnimID() {
		return currentAction.ordinal();
	}

	@Override
	public int getAnimTick() {
		return ticksInCurrentAction;
	}

	@Override
	protected void fall(float par1) {

	}

}
