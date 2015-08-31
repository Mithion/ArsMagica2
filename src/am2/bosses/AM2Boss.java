package am2.bosses;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import am2.AMCore;
import am2.buffs.BuffList;
import am2.entities.EntityLightMage;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;

public abstract class AM2Boss extends EntityMob implements IArsMagicaBoss {

	protected BossActions currentAction = BossActions.IDLE;
	protected int ticksInCurrentAction;

	public boolean playerCanSee = false;;

	public AM2Boss(World par1World) {
		super(par1World);
		this.stepHeight = 1.02f;
		ExtendedProperties.For(this).setMagicLevelWithMana(50);
		initAI();
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48);
	}

	/**
	 * This contains the default AI tasks.  To add new ones, override {@link #initSpecificAI()}
	 */
	protected void initAI(){
		this.getNavigator().setBreakDoors(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLightMage.class, 0, true));

		initSpecificAI();
	}

	/**
	 * Initializer for class-specific AI
	 */
	protected abstract void initSpecificAI();

	@Override
	public BossActions getCurrentAction() {
		return currentAction;
	}

	@Override
	public void setCurrentAction(BossActions action) {
		currentAction = action;
		ticksInCurrentAction = 0;
	}

	@Override
	public int getTicksInCurrentAction() {
		return ticksInCurrentAction;
	}

	@Override
	public boolean isActionValid(BossActions action){
		return true;
	}

	@Override
	public abstract String getAttackSound();

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {

		if (par1DamageSource == DamageSource.inWall){
			if (!worldObj.isRemote){
				for (int i = -1; i <= 1; ++i){
					for (int j = 0; j < 3; ++j){
						for (int k = -1; k <= 1; ++k){
							worldObj.func_147478_e(i, j, k, true);
						}
					}
				}
			}
			return false;
		}

		if (par1DamageSource.getSourceOfDamage() != null){

			if (par1DamageSource.getSourceOfDamage() instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer)par1DamageSource.getSourceOfDamage();
				if (player.capabilities.isCreativeMode && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ItemsCommonProxy.woodenLeg){
					if (!worldObj.isRemote)
						this.setDead();
					return false;
				}
			}else if (par1DamageSource.getSourceOfDamage() instanceof EntityArrow){
				Entity shooter = ((EntityArrow)par1DamageSource.getSourceOfDamage()).shootingEntity;
				if (shooter != null && this.getDistanceSqToEntity(shooter) > 900){
					this.setPositionAndUpdate(shooter.posX, shooter.posY, shooter.posZ);
				}
				return false;
			}else if (this.getDistanceSqToEntity(par1DamageSource.getSourceOfDamage()) > 900){
				Entity shooter = (par1DamageSource.getSourceOfDamage());
				if (shooter != null){
					this.setPositionAndUpdate(shooter.posX, shooter.posY, shooter.posZ);
				}
			}
		}

		if (par2 > 7) par2 = 7;

		par2 = modifyDamageAmount(par1DamageSource, par2);

		if (par2 <= 0){
			heal(-par2);
			return false;
		}

		if (super.attackEntityFrom(par1DamageSource, par2)){
			this.hurtResistantTime = 40;
			return true;
		}
		return false;
	}

	protected abstract float modifyDamageAmount(DamageSource source, float damageAmt);

	@Override
	public void onUpdate() {

		this.ticksInCurrentAction++;

		if (ticksInCurrentAction > 200){
			setCurrentAction(BossActions.IDLE);
		}

		if (worldObj.isRemote)
			playerCanSee = AMCore.proxy.getLocalPlayer().canEntityBeSeen(this);

		super.onUpdate();
	}

	@Override
	public boolean allowLeashing() {
		return false;
	}

	@Override
	public void addPotionEffect(PotionEffect effect) {
		if (effect.getPotionID() == BuffList.silence.id)
			return;
		super.addPotionEffect(effect);
	}
}
