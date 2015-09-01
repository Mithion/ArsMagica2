package thehippomaster.AnimationExample;

import thehippomaster.AnimationAPI.AIAnimation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class AIHeadBang extends AIAnimation {
	
	private EntityTest entityTest;
	private EntityLivingBase attackTarget;
	
	public AIHeadBang(EntityTest test) {
		super(test);
		entityTest = test;
		attackTarget = null;
	}
	
	public int getAnimID() {
		return 1;
	}
	
	public boolean isAutomatic() {
		return true;
	}
	
	public int getDuration() {
		return 30;
	}
	
	public void startExecuting() {
		super.startExecuting();
		attackTarget = entityTest.getAttackTarget();
	}
	
	public void updateTask() {
		if(entityTest.getAnimTick() < 14)
			entityTest.getLookHelper().setLookPositionWithEntity(attackTarget, 30F, 30F);
		if(entityTest.getAnimTick() == 14 && attackTarget != null)
			attackTarget.attackEntityFrom(DamageSource.causeMobDamage(entityTest), 1);
	}
}
