package am2.bosses;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.math.AMVector3;
import am2.bosses.ai.EntityAIDispel;
import am2.bosses.ai.EntityAIHurricane;
import am2.bosses.ai.EntityAISpawnWhirlwind;
import am2.damage.DamageSourceLightning;
import am2.entities.ai.EntityAIGuardSpawnLocation;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.particles.AMParticle;
import am2.particles.ParticleApproachEntity;
import am2.particles.ParticleFloatUpward;

public class EntityAirGuardian  extends AM2Boss{
	private boolean useLeftArm = false;
	public float spinRotation = 0;
	private float orbitRotation;
	public int hitCount = 0;
	private boolean firstTick = true;

	public EntityAirGuardian(World par1World) {
		super(par1World);
		this.setSize(1.0f, 2.0f);
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(220D);
	}

	@Override
	public int getTotalArmorValue() {
		return 14;
	}

	@Override
	protected void initSpecificAI() {
		this.tasks.addTask(1, new EntityAIDispel(this));
		this.tasks.addTask(1, new EntityAISpawnWhirlwind(this));
		this.tasks.addTask(2, new EntityAIHurricane(this, 0.5f));
	}

	@Override
	public void setCurrentAction(BossActions action) {
		super.setCurrentAction(action);

		this.spinRotation = 0;
		this.hitCount = 0;

		if (action == BossActions.CASTING)
			this.useLeftArm = !this.useLeftArm;

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	public boolean useLeftArm(){
		return this.useLeftArm;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void onUpdate() {

		if (firstTick){
			this.tasks.addTask(0, new EntityAIGuardSpawnLocation(this, 0.5F, 5, 16, new AMVector3(this)));
			this.firstTick = false;
		}

		this.orbitRotation += 2f;
		this.orbitRotation %= 360;

		switch(currentAction){
		case IDLE:
			break;
		case SPINNING:
			this.spinRotation = (this.spinRotation - 40) % 360;
			if (this.worldObj.isRemote){
				for (int i = 0; i < AMCore.config.getGFXLevel(); ++i){
					AMParticle particle = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "wind", posX + worldObj.rand.nextDouble() * 4 - 2, posY, posZ + worldObj.rand.nextDouble() * 4 - 2);
					if (particle != null){
						if (ticksInCurrentAction < BossActions.SPINNING.getMaxActionTime() - 10){
							particle.AddParticleController(new ParticleApproachEntity(particle, this, 0.2f, 0.5f, 1, true));
							particle.AddParticleController(new ParticleFloatUpward(particle, 0.01f, 0.2f, 2, true));
						}else{
							particle.AddParticleController(new ParticleFloatUpward(particle, 0.1f, 1, 1, false));
						}
						particle.setMaxAge(30);
					}
				}
			}
			break;
		}

		if (this.motionY < 0){
			this.motionY *= 0.8999999f;
		}

		if (this.posY < 150){
			if (worldObj.isRemote){
				for (int i = 0; i < 25; ++i){
					AMParticle wind = (AMParticle) AMCore.proxy.particleManager.spawn(worldObj, "wind", posX, posY, posZ);
					if (wind != null){
						wind.setIgnoreMaxAge(false);
						wind.setMaxAge(10 + rand.nextInt(10));
						wind.setDontRequireControllers();
						wind.addRandomOffset(1, 1, 1);
						//wind.AddParticleController(new PaticleFleePoint(wind, new AMVector3(this), rand.nextInt() * 0.1f + 0.01f, 10, 1, false));
					}
				}
			}else{
				if (this.posY < 145)
					this.setDead();
			}
		}

		super.onUpdate();
	}

	public float getOrbitRotation(){
		return this.orbitRotation;
	}

	@Override
	public boolean canBePushed() {
		return this.getCurrentAction() != BossActions.SPINNING;
	}

	@Override
	protected void fall(float par1) {

	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_GREEN), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++)
		{
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_AIR), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemsCommonProxy.airSledEnchanted.copy(), 0.0f);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		hitCount++;
		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt) {
		if (source.isMagicDamage())
			damageAmt /= 2;
		else if (source instanceof DamageSourceLightning)
			damageAmt *= 2;
		return damageAmt;
	}

	@Override
	protected String getHurtSound() {
		return "arsmagica2:mob.airguardian.hit";
	}

	@Override
	protected String getDeathSound() {
		return "arsmagica2:mob.airguardian.death";
	}

	@Override
	protected String getLivingSound() {
		return "arsmagica2:mob.airguardian.idle";
	}

	@Override
	public String getAttackSound() {
		return "";
	}
}
