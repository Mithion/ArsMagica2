package am2.bosses;

import am2.AMCore;
import am2.bosses.ai.EntityAICastSpell;
import am2.bosses.ai.EntityAIDispel;
import am2.bosses.ai.EntityAISummonAllies;
import am2.bosses.ai.ISpellCastCallback;
import am2.entities.EntityDarkling;
import am2.entities.EntityEarthElemental;
import am2.entities.EntityFireElemental;
import am2.entities.EntityManaElemental;
import am2.items.ItemsCommonProxy;
import am2.utility.NPCSpells;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityLifeGuardian extends AM2Boss{

	private ArrayList<EntityLiving> minions;
	public ArrayList<EntityLiving> queued_minions;

	private static final int DATA_MINION_COUNT = 20;

	public EntityLifeGuardian(World par1World){
		super(par1World);
		this.setSize(1, 2);
		minions = new ArrayList<EntityLiving>();
		queued_minions = new ArrayList<EntityLiving>();
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataWatcher.addObject(DATA_MINION_COUNT, 0);
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(1, new EntityAIDispel(this));
		this.tasks.addTask(1, new EntityAICastSpell(this, NPCSpells.instance.healSelf, 16, 23, 100, BossActions.CASTING, new ISpellCastCallback<EntityLifeGuardian>(){
			@Override
			public boolean shouldCast(EntityLifeGuardian host, ItemStack spell){
				return host.getHealth() < host.getMaxHealth();
			}
		}));
		this.tasks.addTask(2, new EntityAICastSpell(this, NPCSpells.instance.nauseate, 16, 23, 20, BossActions.CASTING, new ISpellCastCallback<EntityLifeGuardian>(){
			@Override
			public boolean shouldCast(EntityLifeGuardian host, ItemStack spell){
				return minions.size() == 0;
			}
		}));
		this.tasks.addTask(3, new EntityAISummonAllies(this, EntityEarthElemental.class, EntityFireElemental.class, EntityManaElemental.class, EntityDarkling.class));
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		if (par1DamageSource.getSourceOfDamage() != null && par1DamageSource.getSourceOfDamage() instanceof EntityLivingBase){
			for (EntityLivingBase minion : minions.toArray(new EntityLivingBase[minions.size()])){
				((EntityLiving)minion).setAttackTarget((EntityLivingBase)par1DamageSource.getSourceOfDamage());
			}
		}
		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (minions.size() > 0){
			damageAmt = 0;
			minions.get(getRNG().nextInt(minions.size())).attackEntityFrom(source, damageAmt);
		}
		return damageAmt;
	}

	public int getNumMinions(){
		return this.dataWatcher.getWatchableObjectInt(DATA_MINION_COUNT);
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200D);
	}

	@Override
	public void onUpdate(){
		//Minion management - add any queued minions to the minion list and prune out any fallen or nonexistant ones
		if (!worldObj.isRemote){
			minions.addAll(queued_minions);
			queued_minions.clear();
			Iterator<EntityLiving> it = minions.iterator();
			while (it.hasNext()){
				EntityLiving minion = it.next();
				if (minion == null || minion.isDead)
					it.remove();
			}

			this.dataWatcher.updateObject(DATA_MINION_COUNT, minions.size());

			if (this.ticksExisted % 100 == 0){
				for (EntityLivingBase e : minions)
					AMCore.proxy.particleManager.spawn(worldObj, "textures/blocks/oreblocksunstone.png", this, e);
			}
		}

		if (this.ticksExisted % 40 == 0)
			this.heal(2f);

		super.onUpdate();
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.lifeguardian.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.lifeguardian.death";
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.lifeguardian.idle";
	}

	@Override
	public String getAttackSound(){
		return "arsmagica2:mob.lifeguardian.heal";
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_INF_ORB_GREEN), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_LIFE), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemsCommonProxy.lifeWardEnchanted.copy(), 0.0f);
		}
	}

	@Override
	public float getEyeHeight(){
		return 1.5f;
	}
}
