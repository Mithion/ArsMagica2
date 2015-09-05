package am2.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityEarthElemental extends EntityMob{

	public EntityEarthElemental(World world){
		super(world);
		setSize(1F, 2F);
		initAI();
	}

	private void initAI(){
		this.getNavigator().setBreakDoors(true);
		this.getNavigator().setAvoidSun(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIBreakDoor(this));
		this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.5f, false));
		this.tasks.addTask(7, new EntityAIWander(this, 0.5f));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
	}

	@Override
	protected boolean isAIEnabled(){
		return true;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12);
	}

	@Override
	public int getTotalArmorValue(){
		return 14;
	}

	protected float MovementSpeed(){
		return 0.4f;
	}

	@Override
	protected String getLivingSound(){
		return "golem_living";
	}

	@Override
	protected String getHurtSound(){
		return "golem_hurt";
	}

	@Override
	protected String getDeathSound(){
		return "golem_death";
	}

	@Override
	protected void attackEntity(Entity entity, float f){
		if (f < 1.5f && entity.boundingBox.maxY >= boundingBox.minY && entity.boundingBox.minY <= boundingBox.maxY){
			if (onGround){
				entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1);
			}
		}
	}

	@Override
	public boolean getCanSpawnHere(){
		if (!SpawnBlacklists.entityCanSpawnHere(this.posX, this.posZ, worldObj, this))
			return false;
		return super.getCanSpawnHere();
	}
}
