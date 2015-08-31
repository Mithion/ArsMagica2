package am2.bosses.ai;

import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;


import am2.bosses.BossActions;
import am2.bosses.EntityEnderGuardian;
import am2.spell.SpellHelper;
import am2.utility.NPCSpells;

public class EntityAIProtect extends AIAnimation{

	private int cooldownTicks = 0;

	public EntityAIProtect(IAnimatedEntity entity) {
		super(entity);
	}

	@Override
	public int getAnimID() {
		return BossActions.SHIELD_BASH.ordinal();
	}

	@Override
	public boolean isAutomatic() {
		return false;
	}

	@Override
	public int getDuration() {
		return 35;
	}

	@Override
	public boolean shouldAnimate() {
		//accessor method in AIAnimation that gives access to the entity
		EntityEnderGuardian living = getEntity();

		//must have an attack target
		if(living.getAttackTarget() == null || living.getTicksSinceLastAttack() > 40)
			return false;

		return cooldownTicks-- <= 0;
	}

	@Override
	public void resetTask() {
		cooldownTicks = 20;
		EntityLivingBase ent = getEntity();
		ent.extinguish();
		SpellHelper.instance.applyStackStage(NPCSpells.instance.dispel, getEntity(), null, ent.posX, ent.posY, ent.posZ, 0, ent.worldObj, false, false, 0);
		super.resetTask();
	}

	@Override
	public void updateTask() {
		EntityEnderGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			guardian.getLookHelper().setLookPositionWithEntity(guardian.getAttackTarget(), 30, 30);
		}
	}

}
