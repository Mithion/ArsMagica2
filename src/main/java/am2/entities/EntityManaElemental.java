package am2.entities;

import am2.entities.ai.EntityAIManaDrainBolt;
import am2.items.ItemsCommonProxy;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityManaElemental extends EntityMob{

	private final float hostileSpeed;
	private double yaw;

	public EntityManaElemental(World par1World){
		super(par1World);
		this.setAIMoveSpeed(0.2f);
		this.hostileSpeed = 0.25F;
		//this.attackStrength = 4;
		this.setSize(0.8f, 2.5f);
		ExtendedProperties.For(this).setMagicLevelWithMana(15);
		initAI();
	}

	private void getYaw(){
		yaw = renderYawOffset;
		yaw %= 360.0F;

		/*if (yaw >= 180.0F)
		{
        	yaw -= 360.0F;
        }

        if (yaw < -180.0F)
        {
        	yaw += 360.0F;
        }*/
		yaw = Math.toRadians(yaw * -1);
	}

	public void setOnGroudFloat(float onGround){
	}

    @Override
    public boolean isAIDisabled() {
        return false;
    }

    @Override
	public int getTotalArmorValue(){
		return 12;
	}

	@Override
	public void onUpdate(){
		if (this.worldObj != null){
			if (this.worldObj.isRemote){
			}else{
				if (ExtendedProperties.For(this).getCurrentMana() <= 0){
					this.attackEntityFrom(DamageSource.generic, 500);
				}
			}
		}
		super.onUpdate();
	}

	@Override
	protected void dropRareDrop(int par1){
		this.entityDropItem(new ItemStack(ItemsCommonProxy.essence, 1, 0), 0.0f);
	}



	@Override
	protected Item getDropItem(){
		return ItemsCommonProxy.manaCake;
	}

	@Override
	protected String getLivingSound(){
		return "arsmagica2:mob.manaelemental.living";
	}

	@Override
	protected String getHurtSound(){
		return "arsmagica2:mob.manaelemental.hit";
	}

	@Override
	protected String getDeathSound(){
		return "arsmagica2:mob.manaelemental.death";
	}

	private void initAI(){
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(3, new EntityAIManaDrainBolt(this, this.hostileSpeed, 35, 1, 10));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, this.getAIMoveSpeed()));
		this.tasks.addTask(7, new EntityAIWander(this, this.getAIMoveSpeed()));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true, true));
	}

	@Override
	public boolean getCanSpawnHere(){
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		return super.getCanSpawnHere();
	}
}
