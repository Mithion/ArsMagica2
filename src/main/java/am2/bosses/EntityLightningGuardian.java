package am2.bosses;

import am2.AMCore;
import am2.bosses.ai.*;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleHoldPosition;
import am2.playerextensions.ExtendedProperties;
import am2.utility.NPCSpells;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import thehippomaster.AnimationAPI.IAnimatedEntity;

//

public class EntityLightningGuardian extends AM2Boss implements IAnimatedEntity{

	public EntityLightningGuardian(World par1World){
		super(par1World);
		this.setSize(1.75f, 3);
	}

	@Override
	public float getEyeHeight(){
		return 2f;
	}


	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(1, new EntityAIDispel(this));
		this.tasks.addTask(2, new EntityAILightningRod(this));
		this.tasks.addTask(3, new EntityAIStatic(this));
		this.tasks.addTask(3, new EntityAICastSpell(this, NPCSpells.instance.lightningRune, 22, 27, 200, BossActions.CASTING));
		this.tasks.addTask(3, new EntityAICastSpell(this, NPCSpells.instance.scrambleSynapses, 45, 60, 300, BossActions.SMASH));
		this.tasks.addTask(5, new EntityAILightningBolt(this));
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(250D);
	}

	@Override
	public void onDeath(DamageSource par1DamageSource){
		if (this.getAttackTarget() != null)
			ExtendedProperties.For(this.getAttackTarget()).setDisableGravity(false);
		super.onDeath(par1DamageSource);
	}

	@Override
	public int getTotalArmorValue(){
		return 18;
	}

	@Override
	public void onUpdate(){
		super.onUpdate();

		if (this.getAttackTarget() != null){
			if (this.getCurrentAction() != BossActions.LONG_CASTING){
				ExtendedProperties.For(getAttackTarget()).setDisableGravity(false);
			}

			if (!this.worldObj.isRemote && this.getDistanceSqToEntity(getAttackTarget()) > 64D && this.getCurrentAction() == BossActions.IDLE){
				this.getNavigator().tryMoveToEntityLiving(getAttackTarget(), 0.5f);
			}
		}

		if (worldObj.isRemote){
			int halfDist = 8;
			int dist = 16;
			if (this.getCurrentAction() == BossActions.CHARGE){
				if (ticksInCurrentAction > 50){
					for (int i = 0; i < 2 * AMCore.config.getGFXLevel(); ++i){
						AMParticle smoke = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "smoke", posX, posY + 4, posZ);
						if (smoke != null){
							smoke.addRandomOffset(halfDist, 1, halfDist);
							smoke.SetParticleAlpha(1f);
							smoke.setParticleScale(1f);
							smoke.setMaxAge(20);
							smoke.AddParticleController(new ParticleHoldPosition(smoke, 10, 1, false));

						}
					}
				}
				if (ticksInCurrentAction > 66){
					AMCore.proxy.particleManager.BoltFromPointToPoint(
							worldObj,
							posX + rand.nextDouble() - 0.5,
							posY + rand.nextDouble() - 0.5 + 2,
							posZ + rand.nextDouble() - 0.5,
							posX + rand.nextDouble() * dist - halfDist,
							posY + rand.nextDouble() * dist - halfDist,
							posZ + rand.nextDouble() * dist - halfDist);
				}
			}else if (this.getCurrentAction() == BossActions.LONG_CASTING){
				if (ticksInCurrentAction > 25 && ticksInCurrentAction < 150){
					for (int i = 0; i < 2 * AMCore.config.getGFXLevel(); ++i){
						AMParticle smoke = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "smoke", posX, posY + 4, posZ);
						if (smoke != null){
							smoke.addRandomOffset(halfDist, 1, halfDist);
							smoke.SetParticleAlpha(1f);
							smoke.setParticleScale(1f);
							smoke.setRGBColorI(ticksInCurrentAction < 85 ? 0xFFFFFF - 0x111111 * ((ticksInCurrentAction - 25) / 4) : 0x222222);
							smoke.setMaxAge(20);
							smoke.AddParticleController(new ParticleHoldPosition(smoke, 10, 1, false));

						}
					}
				}
			}
		}
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source.isMagicDamage() || source.damageType.equals("magic"))
			damageAmt *= 2;
		if (source.damageType.equals("drown"))
			damageAmt *= 4;
		if (source.damageType.equals("DamageAMLightning"))
			damageAmt *= -1;
		return damageAmt;
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.lightningguardian.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.lightningguardian.death";
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.lightningguardian.idle";
	}

	@Override
	public String getAttackSound(){
		return "arsmagica2:mob.lightningguardian.attack_static";
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_GREEN), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIGHTNING), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemsCommonProxy.lightningCharmEnchanted.copy(), 0.0f);
		}
	}


	@Override
	public void setAnimID(int id){
		setCurrentAction(BossActions.values()[id]);
	}


	@Override
	public void setAnimTick(int tick){
		this.ticksInCurrentAction = tick;
	}


	@Override
	public int getAnimID(){
		return this.currentAction.ordinal();
	}


	@Override
	public int getAnimTick(){
		return this.ticksInCurrentAction;
	}

}
