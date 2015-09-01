package am2.entities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityDarkling extends EntityMob {
	
	public EntityDarkling(World par1World) {
		super(par1World);
		initAI();
		this.setSize(0.5f, 0.5f);
	}		
	
	@Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(7D);
    }
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(22, (byte)0);
	}

	public boolean isAngry(){
		return this.dataWatcher.getWatchableObjectByte(22) == 1;
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}
	
	@Override
	public float getShadowSize() {
		return 0;
	}
	
	private void initAI(){
		this.targetTasks.taskEntries.clear();
		this.tasks.taskEntries.clear();

		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));

		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.6F));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 0.5f, true));
		this.tasks.addTask(7, new EntityAIWander(this, 0.4f));
		this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
	}

	@Override
	public void onUpdate() {
		if (this.getAttackTarget() != null){
			this.dataWatcher.updateObject(22, (byte)1);
		}else{
			this.dataWatcher.updateObject(22, (byte)0);
		}
		super.onUpdate();
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		return false;
	}
}
