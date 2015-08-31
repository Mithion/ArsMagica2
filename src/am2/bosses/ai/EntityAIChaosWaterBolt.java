package am2.bosses.ai;

import java.util.Random;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import am2.bosses.BossActions;
import am2.bosses.EntityWaterGuardian;
import am2.items.ItemsCommonProxy;
import am2.spell.SpellHelper;
import am2.spell.SpellUtils;

public class EntityAIChaosWaterBolt extends EntityAIBase{
	private final EntityWaterGuardian host;
	private int cooldownTicks = 0;
	private final Random rand;

	private static final ItemStack castStack = createDummyStack();

	private static ItemStack createDummyStack(){
		ItemStack stack = new ItemStack(ItemsCommonProxy.spell);
		SpellUtils.instance.addSpellStageToScroll(stack, "Projectile", new String[]{ "WateryGrave", "MagicDamage", "Knockback" }, new String[0]);
		return stack;
	}

	public EntityAIChaosWaterBolt(EntityWaterGuardian host){
		this.host =  host;
		this.setMutexBits(1);
		rand = new Random();
	}

	@Override
	public boolean shouldExecute() {
		if (host.getCurrentAction() == BossActions.IDLE && host.isActionValid(BossActions.CASTING)) return true;
		return false;
	}

	@Override
	public boolean continueExecuting() {
		if (host.getCurrentAction() == BossActions.CASTING && host.getTicksInCurrentAction() > 100){
			host.setCurrentAction(BossActions.IDLE);
			cooldownTicks = 0;
			return false;
		}
		return true;
	}

	@Override
	public void updateTask() {
		if (host.getCurrentAction() != BossActions.CASTING)
			host.setCurrentAction(BossActions.CASTING);

		if (!host.worldObj.isRemote && host.getCurrentAction() == BossActions.CASTING){
			float yaw = rand.nextFloat() * 360;
			host.rotationYaw = yaw;
			host.prevRotationYaw = yaw;
			SpellHelper.instance.applyStackStage(castStack, host, host, host.posX, host.posY, host.posZ, 0, host.worldObj, false, false, 0);
		}
	}
}
