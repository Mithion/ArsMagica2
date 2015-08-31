package am2.bosses.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import am2.bosses.BossActions;
import am2.bosses.IArsMagicaBoss;
import am2.entities.EntitySpellEffect;

public class EntityAIFireRain extends EntityAIBase{

	private final EntityLiving host;
	private int cooldownTicks = 0;
	private int boltTicks = 0;

	public EntityAIFireRain(IArsMagicaBoss host){
		this.host = (EntityLiving)host;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		boolean execute = ((IArsMagicaBoss)host).getCurrentAction() == BossActions.IDLE && host.getAttackTarget() != null && cooldownTicks-- <= 0;
		if (execute)
			((IArsMagicaBoss)host).setCurrentAction(BossActions.CASTING);
		return execute;
	}

	@Override
	public boolean continueExecuting() {
		return this.cooldownTicks <= 0;
	}

	@Override
	public void resetTask() {
		((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
		cooldownTicks = 150;
		boltTicks = 0;
	}

	@Override
	public void updateTask() {
		if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.CASTING)
			((IArsMagicaBoss)host).setCurrentAction(BossActions.CASTING);

		boltTicks++;
		if (boltTicks == 12){
			if (!host.worldObj.isRemote){
				EntitySpellEffect fire = new EntitySpellEffect(host.worldObj);
				fire.setPosition(host.posX, host.posY, host.posZ);
				fire.setTicksToExist(300);
				fire.setRainOfFire(true);
				fire.setRadius(10);
				fire.SetCasterAndStack(host, null);
				host.worldObj.spawnEntityInWorld(fire);
			}
		}
		if (boltTicks >= 23){
			resetTask();
		}
	}

}
