package am2.bosses.ai;

import am2.bosses.BossActions;
import am2.bosses.IArsMagicaBoss;
import am2.spell.SpellHelper;
import am2.utility.NPCSpells;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIDispel extends EntityAIBase{
	private final EntityLiving host;
	private int cooldownTicks = 0;
	private boolean hasCasted = false;
	private int boltTicks = 0;

	public EntityAIDispel(IArsMagicaBoss host){
		this.host = (EntityLiving)host;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute(){
		cooldownTicks--;
		boolean execute = ((IArsMagicaBoss)host).getCurrentAction() == BossActions.IDLE && host.getActivePotionEffects().size() > 0 && cooldownTicks <= 0;
		if (execute) hasCasted = false;
		return execute;
	}

	@Override
	public boolean continueExecuting(){
		return !hasCasted && host.getAttackTarget() != null && !host.getAttackTarget().isDead;
	}

	@Override
	public void resetTask(){
		((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
		cooldownTicks = 50;
		hasCasted = true;
		boltTicks = 0;
	}

	@Override
	public void updateTask(){
		if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.CASTING)
			((IArsMagicaBoss)host).setCurrentAction(BossActions.CASTING);

		boltTicks++;
		if (boltTicks == 16){
			if (!host.worldObj.isRemote)
				host.worldObj.playSoundAtEntity(host, ((IArsMagicaBoss)host).getAttackSound(), 1.0f, 1.0f);
			SpellHelper.instance.applyStackStage(NPCSpells.instance.dispel, host, host, host.posX, host.posY, host.posZ, 0, host.worldObj, false, false, 0);
		}
		if (boltTicks >= 23){
			resetTask();
		}
	}
}
