package am2.bosses;

import am2.AMCore;
import am2.bosses.ai.*;
import am2.damage.DamageSourceFire;
import am2.damage.DamageSourceFrost;
import am2.damage.DamageSources;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleOrbitEntity;
import am2.utility.NPCSpells;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityNatureGuardian extends AM2Boss{

	public float tendrilRotation;
	public boolean hasSickle;

	public float last_rotation_z_main = 0;
	public float last_rotation_z_shield = 0;

	public float last_rotation_x_main = 0;
	public float last_rotation_x_shield = 0;

	public float last_rotation_y_main = 0;
	public float last_rotation_y_shield = 0;

	public float spinRotation = 0;

	public EntityNatureGuardian(World par1World){
		super(par1World);
		this.setSize(2.0f, 5.0f);
		hasSickle = true;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500D);
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(1, new EntityAICastSpell(this, NPCSpells.instance.dispel, 16, 23, 50, BossActions.CASTING, new ISpellCastCallback<EntityNatureGuardian>(){
			@Override
			public boolean shouldCast(EntityNatureGuardian host, ItemStack spell){
				return host.getActivePotionEffects().size() > 0;
			}
		}));
		this.tasks.addTask(3, new EntityAIPlantGuardianThrowSickle(this, 0.75f));
		this.tasks.addTask(3, new EntityAISpinAttack(this, 0.5f, 8));
		this.tasks.addTask(4, new EntityAIStrikeAttack(this, 0.75f, 12.0f, DamageSources.DamageSourceTypes.CACTUS));
		this.tasks.addTask(5, new EntityAIShieldBash(this, 0.75f));
	}

	@Override
	public void onUpdate(){
		if (worldObj.isRemote){
			updateMovementAngles();
			spawnParticles();
		}
		super.onUpdate();
	}

	@Override
	public int getTotalArmorValue(){
		return 20;
	}

	private void spawnParticles(){
		AMParticle leaf = (AMParticle)AMCore.proxy.particleManager.spawn(worldObj, "leaf", posX + (rand.nextDouble() * 3) - 1.5f, posY + (rand.nextDouble() * 5f), posZ + (rand.nextDouble() * 3) - 1.5f);
		if (leaf != null){
			leaf.setMaxAge(20);
			leaf.setIgnoreMaxAge(false);
			leaf.AddParticleController(new ParticleFloatUpward(leaf, 0.05f, -0.02f, 1, false));
			if (getCurrentAction() == BossActions.SPINNING){
				leaf.AddParticleController(new ParticleOrbitEntity(leaf, this, 0.6f, 1, false));
			}
		}
	}

	@Override
	public void setCurrentAction(BossActions action){
		super.setCurrentAction(action);
		this.spinRotation = 0;

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	private void updateMovementAngles(){
		tendrilRotation += 0.2f;
		tendrilRotation %= 360;

		switch (currentAction){
		case IDLE:
			break;
		case SPINNING:
			this.spinRotation = (this.spinRotation - 40) % 360;
			break;
		case STRIKE:
			break;
		case THROWING_SICKLE:
			break;
		default:
			break;

		}
	}

	@Override
	public ItemStack getHeldItem(){
		return null;
	}

	@Override
	public void setCurrentItemOrArmor(int i, ItemStack itemstack){
	}

	@Override
	public ItemStack[] getLastActiveItems(){
		return new ItemStack[0];
	}

	@Override
	public boolean isActionValid(BossActions action){
		if (action == BossActions.STRIKE || action == BossActions.SPINNING || action == BossActions.THROWING_SICKLE){
			return hasSickle;
		}
		return true;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_RED), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_NATURE), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemsCommonProxy.natureScytheEnchanted.copy(), 0.0f);
		}
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source instanceof DamageSourceFire || source.isFireDamage()){
			damageAmt *= 2f;
		}else if (source instanceof DamageSourceFrost){
			damageAmt *= 1.5f;
		}
		return damageAmt;
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.natureguardian.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.natureguardian.death";
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.natureguardian.idle";
	}

	@Override
	public String getAttackSound(){
		return "arsmagica2:mob.natureguardian.attack";
	}
}
