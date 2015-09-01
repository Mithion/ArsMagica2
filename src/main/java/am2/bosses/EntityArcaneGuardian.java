package am2.bosses;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import am2.AMCore;
import am2.bosses.ai.EntityAICastSpell;
import am2.bosses.ai.EntityAIDispel;
import am2.bosses.ai.ISpellCastCallback;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.utility.NPCSpells;

public class EntityArcaneGuardian extends AM2Boss{

	private float runeRotationZ = 0;
	private float runeRotationY = 0;

	private static final int DW_TARGET_ID = 20;

	public EntityArcaneGuardian(World par1World) {
		super(par1World);
		this.setSize(1.0f, 2.0f);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(DW_TARGET_ID, -1);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(115D);
	}

	@Override
	public void onUpdate() {

		if (this.motionY < 0){
			this.motionY *= 0.7999999f;
		}

		updateRotations();

		if (!worldObj.isRemote){
			int eid = this.dataWatcher.getWatchableObjectInt(DW_TARGET_ID);
			int tid = -1;
			if (this.getAttackTarget() != null){
				tid = this.getAttackTarget().getEntityId();
			}
			if (eid != tid){
				this.dataWatcher.updateObject(DW_TARGET_ID, tid);
			}

		}

		super.onUpdate();
	}

	private void updateRotations(){
		runeRotationZ = 0;
		float targetRuneRotationY = 0;
		float runeRotationSpeed = 0.3f;
		if (this.getTarget() != null){
			double deltaX = this.getTarget().posX - this.posX;
			double deltaZ = this.getTarget().posZ - this.posZ;

			double angle = Math.atan2(deltaZ, deltaX);

			angle -= Math.toRadians(MathHelper.wrapAngleTo180_float(this.rotationYaw + 90) + 180);

			targetRuneRotationY = (float) angle;
			runeRotationSpeed = 0.085f;
		}

		if (targetRuneRotationY > runeRotationY)
			runeRotationY += runeRotationSpeed;
		else if (targetRuneRotationY < runeRotationY)
			runeRotationY -= runeRotationSpeed;

		if (isWithin(runeRotationY, targetRuneRotationY, 0.25f)){
			runeRotationY = targetRuneRotationY;
		}

	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (par1DamageSource.getSourceOfDamage() == null){
			return super.attackEntityFrom(par1DamageSource, par2);
		}

		if (checkRuneRetaliation(par1DamageSource))
			return super.attackEntityFrom(par1DamageSource, par2);
		return false;
	}

	private boolean checkRuneRetaliation(DamageSource damagesource){
		Entity source = damagesource.getSourceOfDamage();

		double deltaX = source.posX - this.posX;
		double deltaZ = source.posZ - this.posZ;

		double angle = Math.atan2(deltaZ, deltaX);

		angle -= Math.toRadians(MathHelper.wrapAngleTo180_float(this.rotationYaw + 90) + 180);

		float targetRuneRotationY = (float) angle;

		if (isWithin(runeRotationY, targetRuneRotationY, 0.5f)){
			if (this.getDistanceSqToEntity(source) < 9){
				double speed = 2.5;
				double vertSpeed = 0.325;

				deltaZ = source.posZ - this.posZ;
				deltaX = source.posX - this.posX;
				angle = Math.atan2(deltaZ, deltaX);

				double radians = angle;

				if (source instanceof EntityPlayer){
					AMNetHandler.INSTANCE.sendVelocityAddPacket(source.worldObj, (EntityLivingBase) source, speed * Math.cos(radians), vertSpeed, speed * Math.sin(radians));
				}
				source.motionX = (speed * Math.cos(radians));
				source.motionZ = (speed * Math.sin(radians));
				source.motionY = vertSpeed;

				source.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
			}
		}
		return true;
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt) {
		return damageAmt;
	}

	private boolean isWithin(float source, float target, float tolerance){
		return source + tolerance > target && source - tolerance < target;
	}

	private Entity getTarget(){
		int eid = this.dataWatcher.getWatchableObjectInt(DW_TARGET_ID);
		if (eid == -1) return null;
		return this.worldObj.getEntityByID(eid);
	}

	public float getRuneRotationZ(){
		return runeRotationZ;
	}

	public float getRuneRotationY(){
		return runeRotationY;
	}

	@Override
	protected void initSpecificAI() {
		this.tasks.addTask(1, new EntityAIDispel(this));
		this.tasks.addTask(1, new EntityAICastSpell(this, NPCSpells.instance.healSelf, 16, 23, 60, BossActions.CASTING, new ISpellCastCallback<EntityArcaneGuardian>() {
			@Override
			public boolean shouldCast(EntityArcaneGuardian host, ItemStack spell) {
				return host.getHealth() < host.getMaxHealth();
			}
		}));
		this.tasks.addTask(2, new EntityAICastSpell(this, NPCSpells.instance.blink, 16, 23, 20, BossActions.CASTING));
		this.tasks.addTask(3, new EntityAICastSpell(this, NPCSpells.instance.arcaneBolt, 12, 23, 5, BossActions.CASTING));
	}

	@Override
	public void setCurrentAction(BossActions action) {
		super.setCurrentAction(action);
		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	@Override
	public int getTotalArmorValue() {
		return 9;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_GREEN), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++)
		{
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ARCANE), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemsCommonProxy.arcaneSpellBookEnchanted.copy(), 0.0f);
		}
	}

	@Override
	protected void fall(float par1) {

	}

	@Override
	protected String getHurtSound() {
		return "arsmagica2:mob.arcaneguardian.hit";
	}

	@Override
	protected String getDeathSound() {
		return "arsmagica2:mob.arcaneguardian.death";
	}

	@Override
	protected String getLivingSound() {
		return "arsmagica2:mob.arcaneguardian.idle";
	}

	@Override
	public String getAttackSound() {
		return "arsmagica2:mob.arcaneguardian.spell";
	}
}
