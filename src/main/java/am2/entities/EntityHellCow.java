package am2.entities;

import am2.entities.ai.EntityAIManaDrainBolt;
import am2.items.ItemsCommonProxy;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityHellCow extends EntityMob{

	private final float hostileSpeed;

	public EntityHellCow(World par1World){
		super(par1World);
		this.setSize(1.0f, 2.5f);
		this.hostileSpeed = 0.4F;
		initAI();
	}

	@Override
	protected boolean isAIEnabled(){
		return true;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(10D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(50D);
	}

	@Override
	public int getTotalArmorValue(){
		return 15;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		int i = rand.nextInt(100);
		if (i == 1 && par1){
			this.entityDropItem(ItemsCommonProxy.cowHorn.createItemStack(), 0.0f);
		}
	}

	@Override
	protected void dropRareDrop(int par1){
		if (this.rand.nextBoolean())
			this.entityDropItem(new ItemStack(Items.diamond), 0.0f);
		else
			this.entityDropItem(new ItemStack(Items.emerald), 0.0f);
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.moo.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.moo.death";
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.moo.idle";
	}

	private void initAI(){
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 0.6F, false));
		this.tasks.addTask(4, new EntityAIManaDrainBolt(this, 0.5f, 35, 2, 0));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0F));
		this.tasks.addTask(7, new EntityAIWander(this, 0.3F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		if (par2 > 10) par2 = 10;
		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	public boolean getCanSpawnHere(){
		return true;
	}


	@Override
	public int getTalkInterval(){
		return 160;
	}

	@Override
	protected float getSoundVolume(){
		return 0.4f;
	}
}
