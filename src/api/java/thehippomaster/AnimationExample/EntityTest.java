package thehippomaster.AnimationExample;

import thehippomaster.AnimationAPI.AnimationAPI;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityTest extends EntityCreature implements IAnimals, IAnimatedEntity {
	
	private int animID;
	private int animTick;
	
	public EntityTest(World world) {
		super(world);
		animID = 0;
		animTick = 0;
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new AIHeadBang(this));
		tasks.addTask(2, new AIShakeHead(this));
		tasks.addTask(3, new EntityAIAttackOnCollide(this, 1D, false));
		tasks.addTask(4, new EntityAIWander(this, 0.8D));
		tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
		tasks.addTask(5, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16D); //max health
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.24D); //move speed
	}
	
	public boolean isAIEnabled() {
		return true;
	}
	
	/*
	 * Implemented method from IAnimatedEntity.
	 * Set the animID field to the id in the parameter.
	 */
	public void setAnimID(int id) {
		animID = id;
	}
	
	/*
	 * Implemented method from IAnimatedEntity.
	 * Set the animTick field to the tick in the parameter.
	 */
	public void setAnimTick(int tick) {
		animTick = tick;
	}
	
	/*
	 * Implemented method from IAnimatedEntity.
	 * Return the animID.
	 */
	public int getAnimID() {
		return animID;
	}
	
	/*
	 * Implemented method from IAnimatedEntity.
	 * Return the animTick.
	 */
	public int getAnimTick() {
		return animTick;
	}
	
	public void onUpdate() {
		super.onUpdate();
		//increment the animTick if there is an animation playing
		if(animID != 0) animTick++;
	}
	
	public boolean attackEntityAsMob(Entity entity) {
		if(animID == 0) AnimationAPI.sendAnimPacket(this, 1);
		return true;
	}
}
