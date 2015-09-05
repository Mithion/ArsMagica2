package am2.bosses;

import am2.bosses.ai.EntityAICastSpell;
import am2.bosses.ai.EntityAIChaosWaterBolt;
import am2.bosses.ai.EntityAICloneSelf;
import am2.bosses.ai.EntityAISpinAttack;
import am2.damage.DamageSourceFrost;
import am2.damage.DamageSourceLightning;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.playerextensions.ExtendedProperties;
import am2.utility.NPCSpells;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityWaterGuardian extends AM2Boss{

	private EntityWaterGuardian master;
	private final EntityWaterGuardian[] clones;
	private float orbitRotation;
	private boolean uberSpinAvailable = false;

	private static final int IS_CLONE = 21;

	public float spinRotation = 0;

	public EntityWaterGuardian(World par1World){
		super(par1World);
		currentAction = BossActions.IDLE;
		master = null;
		clones = new EntityWaterGuardian[2];
		this.setSize(1.0f, 2.0f);
		ExtendedProperties.For(this).setMagicLevelWithMana(10);
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(75D);
	}

	public void setClones(EntityWaterGuardian clone1, EntityWaterGuardian clone2){
		clones[0] = clone1;
		clones[1] = clone2;
	}

	private boolean hasClones(){
		return clones[0] != null || clones[1] != null;
	}

	public void clearClones(){
		if (clones[0] != null){
			clones[0].setDead();
		}
		if (clones[1] != null){
			clones[1].setDead();
		}
		clones[0] = null;
		clones[1] = null;
	}

	private void enableUberAttack(){
		uberSpinAvailable = true;
	}

	public void setMaster(EntityWaterGuardian master){
		dataWatcher.updateObject(IS_CLONE, (byte)1);
		this.master = master;
	}

	public boolean isClone(){
		return dataWatcher.getWatchableObjectByte(IS_CLONE) == 1;
	}

	public void clearMaster(){
		this.master = null;
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(2, new EntityAIChaosWaterBolt(this));
		this.tasks.addTask(3, new EntityAICloneSelf(this));
		this.tasks.addTask(4, new EntityAICastSpell(this, NPCSpells.instance.waterBolt, 12, 23, 5, BossActions.CASTING));
		this.tasks.addTask(3, new EntityAISpinAttack(this, 0.5f, 4));
	}

	@Override
	public void onUpdate(){

		if (currentAction == BossActions.CASTING){
			uberSpinAvailable = false;
		}

		if (!worldObj.isRemote && uberSpinAvailable && currentAction != BossActions.CASTING && currentAction != BossActions.IDLE){
			setCurrentAction(BossActions.IDLE);
		}

		if (!worldObj.isRemote && isClone() && (master == null || ticksExisted > 400)){
			setDead();
		}

		if (worldObj.isRemote){
			updateRotations();
		}
		super.onUpdate();
	}

	@Override
	protected void entityInit(){
		super.entityInit();

		this.dataWatcher.addObject(IS_CLONE, (byte)0);
	}

	private void updateRotations(){
		if (!isClone())
			orbitRotation += 2f;
		else
			orbitRotation -= 2f;
		orbitRotation %= 360;

		if (this.getCurrentAction() == BossActions.SPINNING || this.getCurrentAction() == BossActions.CASTING){
			this.spinRotation = (this.spinRotation - 30) % 360;
		}
	}

	public float getOrbitRotation(){
		return orbitRotation;
	}

	@Override
	public void setCurrentAction(BossActions action){
		super.setCurrentAction(action);
		this.spinRotation = 0;

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		if (par1DamageSource.getSourceOfDamage() instanceof EntityWaterGuardian)
			return false;
		if (isClone() && master != null){
			master.enableUberAttack();
			master.clearClones();
		}else if (hasClones()){
			clearClones();
		}

		if (!isClone() && rand.nextInt(10) < 6){
			worldObj.playSoundAtEntity(this, getLivingSound(), 1.0f, 0.4f + rand.nextFloat() * 0.6f);
			return false;
		}

		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source instanceof DamageSourceLightning)
			damageAmt *= 2.0f;
		if (source.getSourceOfDamage() != null && source.getSourceOfDamage() instanceof EntityWaterGuardian)
			damageAmt = 0;
		if (source instanceof DamageSourceFrost)
			damageAmt = 0;

		return damageAmt;
	}

	@Override
	public boolean isActionValid(BossActions action){
		if (uberSpinAvailable && action != BossActions.CASTING) return false;
		if (action == BossActions.CASTING){
			return uberSpinAvailable;
		}
		if (action == BossActions.CLONE){
			return !isClone();
		}
		return true;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound){
		super.writeEntityToNBT(par1nbtTagCompound);

		par1nbtTagCompound.setBoolean("isClone", isClone());
	}

	@Override
	public int getTotalArmorValue(){
		return 10;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound){
		super.readEntityFromNBT(par1nbtTagCompound);

		dataWatcher.updateObject(IS_CLONE, par1nbtTagCompound.getBoolean("isClone") ? (byte)1 : (byte)0);
	}


	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_BLUE), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_WATER), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3){
			this.entityDropItem(ItemsCommonProxy.waterOrbsEnchanted.copy(), 0.0f);
		}
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.waterguardian.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.waterguardian.death";
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.waterguardian.idle";
	}

	@Override
	public String getAttackSound(){
		return "arsmagica2:mob.waterguardian.attack";
	}
}
