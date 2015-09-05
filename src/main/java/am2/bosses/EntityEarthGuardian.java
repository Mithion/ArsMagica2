package am2.bosses;

import am2.bosses.ai.EntityAIDispel;
import am2.bosses.ai.EntityAISmash;
import am2.bosses.ai.EntityAIStrikeAttack;
import am2.bosses.ai.EntityAIThrowRock;
import am2.damage.DamageSourceFrost;
import am2.damage.DamageSourceLightning;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityEarthGuardian extends AM2Boss{

	private float rodRotation = 0;
	public boolean leftArm = false;

	public EntityEarthGuardian(World par1World){
		super(par1World);
		this.setSize(1.0f, 2.0f);
		this.stepHeight = 1.02f;
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(1, new EntityAIDispel(this));
		this.tasks.addTask(1, new EntityAIThrowRock(this, 0.5f));
		this.tasks.addTask(2, new EntityAISmash(this, 0.5f, DamageSources.DamageSourceTypes.PHYSICAL));
		this.tasks.addTask(2, new EntityAIStrikeAttack(this, 0.5f, 4.0f, DamageSources.DamageSourceTypes.PHYSICAL));
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(140D);
	}

	public boolean shouldRenderRock(){
		return this.currentAction == BossActions.THROWING_ROCK && ticksInCurrentAction > 5 && ticksInCurrentAction < 27;
	}

	@Override
	public void setCurrentAction(BossActions action){
		super.setCurrentAction(action);

		if (currentAction != action && action == BossActions.STRIKE && worldObj.isRemote)
			this.leftArm = !this.leftArm;

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	public float getRodRotations(){
		return this.rodRotation;
	}

	@Override
	public void onUpdate(){
		if (ticksInCurrentAction > 40 && !worldObj.isRemote){
			setCurrentAction(BossActions.IDLE);
		}

		if (worldObj.isRemote){
			updateRotations();
		}

		super.onUpdate();
	}

	private void updateRotations(){
		this.rodRotation += 0.02f;
		this.rodRotation %= 360;
	}

	@Override
	public int getTotalArmorValue(){
		return 23;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_BLUE), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_EARTH), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3){
			this.entityDropItem(ItemsCommonProxy.earthArmorEnchanted.copy(), 0.0f);
		}
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source instanceof DamageSourceFrost){
			return damageAmt * 2;
		}else if (source instanceof DamageSourceLightning){
			return damageAmt / 4;
		}
		return damageAmt;
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.earthguardian.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.earthguardian.death";
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.earthguardian.idle";
	}

	@Override
	public String getAttackSound(){
		return "arsmagica2:mob.earthguardian.attack";
	}
}
