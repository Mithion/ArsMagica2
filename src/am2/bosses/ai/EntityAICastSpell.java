package am2.bosses.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import am2.bosses.BossActions;
import am2.bosses.IArsMagicaBoss;
import am2.spell.SpellHelper;

public class EntityAICastSpell extends EntityAIBase {

	private final EntityLiving host;
	private int cooldownTicks = 0;
	private boolean hasCasted = false;
	private int castTicks = 0;

	private ItemStack stack;
	private int castPoint;
	private int duration;
	private int cooldown;
	private BossActions activeAction;
	private ISpellCastCallback callback;

	public EntityAICastSpell(IArsMagicaBoss host, ItemStack spell, int castPoint, int duration, int cooldown, BossActions activeAction){
		this.host = (EntityLiving)host;
		this.stack = spell;
		this.castPoint = castPoint;
		this.duration = duration;
		this.cooldown = cooldown;
		this.activeAction = activeAction;
		this.callback = null;
		this.setMutexBits(3);
	}

	public EntityAICastSpell(IArsMagicaBoss host, ItemStack spell, int castPoint, int duration, int cooldown, BossActions activeAction, ISpellCastCallback callback){
		this.host = (EntityLiving)host;
		this.stack = spell;
		this.castPoint = castPoint;
		this.duration = duration;
		this.cooldown = cooldown;
		this.activeAction = activeAction;
		this.callback = callback;
	}

	@Override
	public boolean shouldExecute() {
		cooldownTicks--;
		boolean execute = ((IArsMagicaBoss)host).getCurrentAction() == BossActions.IDLE && host.getAttackTarget() != null && cooldownTicks <= 0;
		if (execute){
			if (callback == null || callback.shouldCast(host, stack))
				hasCasted = false;
			else
				execute = false;
		}
		return execute;
	}

	@Override
	public boolean continueExecuting() {
		return !hasCasted && host.getAttackTarget() != null && !host.getAttackTarget().isDead;
	}

	@Override
	public void resetTask() {
		((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
		cooldownTicks = cooldown;
		hasCasted = true;
		castTicks = 0;
	}

	@Override
	public void updateTask() {
		host.getLookHelper().setLookPositionWithEntity(host.getAttackTarget(), 30, 30);
		if (host.getDistanceSqToEntity(host.getAttackTarget()) > 64){

			double deltaZ = host.getAttackTarget().posZ - host.posZ;
			double deltaX = host.getAttackTarget().posX - host.posX;

			double angle = -Math.atan2(deltaZ, deltaX);

			double newX = host.getAttackTarget().posX + (Math.cos(angle) * 6);
			double newZ = host.getAttackTarget().posZ + (Math.sin(angle) * 6);

			host.getNavigator().tryMoveToXYZ(newX, host.getAttackTarget().posY, newZ, 0.5f);
		}else if (!host.canEntityBeSeen(host.getAttackTarget())){
			host.getNavigator().tryMoveToEntityLiving(host.getAttackTarget(), 0.5f);
		}else{
			if (((IArsMagicaBoss)host).getCurrentAction() != activeAction)
				((IArsMagicaBoss)host).setCurrentAction(activeAction);

			castTicks++;
			if (castTicks == castPoint){
				if (!host.worldObj.isRemote)
					host.worldObj.playSoundAtEntity(host, ((IArsMagicaBoss)host).getAttackSound(), 1.0f, 1.0f);
				host.faceEntity(host.getAttackTarget(), 180, 180);
				SpellHelper.instance.applyStackStage(stack, host, host.getAttackTarget(), host.posX, host.posY, host.posZ, 0, host.worldObj, false, false, 0);
			}
		}
		if (castTicks >= duration){
			resetTask();
		}
	}
}
