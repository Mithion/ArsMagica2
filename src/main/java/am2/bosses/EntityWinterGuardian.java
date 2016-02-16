package am2.bosses;

import am2.AMCore;
import am2.bosses.ai.*;
import am2.buffs.BuffEffectFrostSlowed;
import am2.damage.DamageSourceFire;
import am2.damage.DamageSourceFrost;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.particles.*;
import am2.utility.NPCSpells;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

public class EntityWinterGuardian extends AM2Boss{

	private boolean hasRightArm;
	private boolean hasLeftArm;
	private float orbitRotation;

	public EntityWinterGuardian(World par1World){
		super(par1World);
		this.setSize(1.25f, 3.25f);
		hasRightArm = true;
		hasLeftArm = true;
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(1, new EntityAICastSpell(this, NPCSpells.instance.dispel, 16, 23, 50, BossActions.CASTING, new ISpellCastCallback<EntityWinterGuardian>(){
			@Override
			public boolean shouldCast(EntityWinterGuardian host, ItemStack spell){
				return host.getActivePotionEffects().size() > 0;
			}
		}));
		this.tasks.addTask(2, new EntityAISmash(this, 0.5f, DamageSources.DamageSourceTypes.FROST));
		this.tasks.addTask(3, new EntityAIStrikeAttack(this, 0.5f, 6f, DamageSources.DamageSourceTypes.FROST));
		this.tasks.addTask(4, new EntityWinterGuardianLaunchArm(this, 0.5f));
	}

	public void returnOneArm(){
		if (!hasLeftArm) hasLeftArm = true;
		else if (!hasRightArm) hasRightArm = true;
	}

	public void launchOneArm(){
		if (hasLeftArm) hasLeftArm = false;
		else if (hasRightArm) hasRightArm = false;
	}

	public boolean hasLeftArm(){
		return hasLeftArm;
	}

	public boolean hasRightArm(){
		return hasRightArm;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(290D);
	}

	@Override
	public int getTotalArmorValue(){
		return 23;
	}

	@Override
	public void onUpdate(){
		if (worldObj.getBiomeGenForCoords(getPosition()).getEnableSnow() && worldObj.getWorldInfo().isRaining()){
			if (worldObj.isRemote){
				AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "ember", posX + (rand.nextFloat() * 6 - 3), posY + 2 + (rand.nextFloat() * 2 - 1), posZ + (rand.nextFloat() * 6 - 3));
				if (particle != null){
					particle.AddParticleController(new ParticleApproachEntity(particle, this, 0.15f, 0.1, 1, false));
					particle.setIgnoreMaxAge(false);
					particle.setMaxAge(30);
					particle.setParticleScale(0.35f);
					particle.setRGBColorF(0.7843f, 0.5098f, 0.5098f);
				}
			}else{
				this.heal(0.1f);
			}
		}

		if (worldObj.isRemote){
			updateRotations();
			spawnParticles();
		}else{
			if (this.ticksExisted % 100 == 0){
				List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(2, 2, 2));
				for (EntityLivingBase entity : entities){
					if (entity == this)
						continue;
					entity.addPotionEffect(new BuffEffectFrostSlowed(220, 1));
					entity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 220, 3));
				}
			}
		}

		super.onUpdate();
	}

	@Override
	public void setCurrentAction(BossActions action){
		super.setCurrentAction(action);

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	private void updateRotations(){
		this.orbitRotation += 2f;
		this.orbitRotation %= 360;
	}

	private void spawnParticles(){
		for (int i = 0; i < AMCore.config.getGFXLevel() * 4; ++i){
			int rnd = rand.nextInt(10);
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, rnd < 5 ? "snowflakes" : "ember", posX + (rand.nextFloat() * 0.4 - 0.2), posY + 2, posZ + (rand.nextFloat() * 0.4 - 0.2));
			if (particle != null){
				if (rnd < 2 || rnd > 8){
					particle.AddParticleController(new ParticleOrbitEntity(particle, this, 0.2f, 1, false));
				}else{
					particle.AddParticleController(new ParticleFloatUpward(particle, 0.5f, -0.2f, 1, false));
					particle.AddParticleController(new ParticleFleeEntity(particle, this, 0.06f, 2, 2, false).setKillParticleOnFinish(true));
				}
				particle.setIgnoreMaxAge(false);
				particle.setMaxAge(30);
				particle.setParticleScale(rnd < 5 ? 0.15f : 0.35f);
				particle.setRGBColorF(0.5098f, 0.7843f, 0.7843f);
			}
		}
	}

	public float getOrbitRotation(){
		return this.orbitRotation;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_RED), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_ICE), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3){
			this.entityDropItem(ItemsCommonProxy.winterArmEnchanted.copy(), 0.0f);
		}
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source instanceof DamageSourceFrost)
			damageAmt = 0;
		if (source.isFireDamage() || source instanceof DamageSourceFire)
			damageAmt *= 2;
		return damageAmt;
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.winterguardian.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.winterguardian.death";
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.winterguardian.idle";
	}

	@Override
	public String getAttackSound(){
		return "arsmagica2:mob.winterguardian.attack";
	}

    @Override
    public World getWorld() {
        return worldObj;
    }
}
