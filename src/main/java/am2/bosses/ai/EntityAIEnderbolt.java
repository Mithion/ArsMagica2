package am2.bosses.ai;

import am2.bosses.BossActions;
import am2.bosses.EntityEnderGuardian;
import am2.bosses.IArsMagicaBoss;
import am2.spell.SpellHelper;
import am2.utility.NPCSpells;
import net.minecraft.entity.EntityLiving;
import thehippomaster.AnimationAPI.AIAnimation;
import thehippomaster.AnimationAPI.IAnimatedEntity;

//
//

public class EntityAIEnderbolt extends AIAnimation{
	private int cooldownTicks = 0;

	public EntityAIEnderbolt(IAnimatedEntity entity){
		super(entity);
	}

	@Override
	public int getAnimID(){
		return BossActions.STRIKE.ordinal();
	}

	@Override
	public boolean isAutomatic(){
		return false;
	}

	@Override
	public int getDuration(){
		return 30;
	}

	@Override
	public boolean shouldAnimate(){
		//accessor method in AIAnimation that gives access to the entity
		EntityLiving living = getEntity();

		//must have an attack target
		if (living.getAttackTarget() == null) return false;

		return cooldownTicks-- <= 0;
	}

	@Override
	public void resetTask(){
		cooldownTicks = 20;
	}

	@Override
	public void updateTask(){
		EntityEnderGuardian guardian = getEntity();
		if (guardian.getAttackTarget() != null){
			guardian.getLookHelper().setLookPositionWithEntity(guardian.getAttackTarget(), 30, 30);
			if (guardian.getTicksInCurrentAction() == 7){
				guardian.faceEntity(guardian.getAttackTarget(), 180, 180);
				guardian.worldObj.playSoundAtEntity(guardian, ((IArsMagicaBoss)guardian).getAttackSound(), 1.0f, (float)(0.5 + guardian.getRNG().nextDouble() * 0.5f));
				SpellHelper.instance.applyStackStage(NPCSpells.instance.enderGuardian_enderBolt, guardian, guardian, guardian.posX, guardian.posY + 0.5f, guardian.posZ, 0, guardian.worldObj, false, false, 0);
			}else{
				guardian.faceEntity(guardian.getAttackTarget(), 180, 180);
			}
		}
	}
}
