package am2.bosses.ai;

import java.lang.reflect.Constructor;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import am2.bosses.BossActions;
import am2.bosses.EntityLifeGuardian;
import am2.bosses.IArsMagicaBoss;
import am2.buffs.BuffEffectMagicShield;
import am2.buffs.BuffEffectShrink;
import am2.utility.EntityUtilities;

public class EntityAISummonAllies extends EntityAIBase{
	private final EntityLifeGuardian host;
	private int cooldownTicks = 0;
	private boolean hasCasted = false;
	private int actionTicks = 0;
	private Class[] mobs;

	public EntityAISummonAllies(EntityLifeGuardian host, Class<? extends EntityCreature>...summons){
		this.host = host;
		this.setMutexBits(1);
		mobs = summons;
	}

	@Override
	public boolean shouldExecute() {
		cooldownTicks--;
		boolean execute = ((IArsMagicaBoss)host).getCurrentAction() != BossActions.CASTING && cooldownTicks <= 0;
		if (execute) hasCasted = false;
		return execute;
	}

	@Override
	public boolean continueExecuting() {
		return !hasCasted;
	}

	@Override
	public void resetTask() {
		((IArsMagicaBoss)host).setCurrentAction(BossActions.IDLE);
		cooldownTicks = 200;
		hasCasted = true;
		actionTicks = 0;
	}

	@Override
	public void updateTask() {
		if (((IArsMagicaBoss)host).getCurrentAction() != BossActions.CASTING)
			((IArsMagicaBoss)host).setCurrentAction(BossActions.CASTING);

		actionTicks++;
		if (actionTicks == 16){
			if (!host.worldObj.isRemote)
				host.worldObj.playSoundAtEntity(host, "arsmagica2:mob.lifeguardian.summon", 1.0f, host.getRNG().nextFloat() * 0.5f + 0.5f);
			int numAllies = 3;
			for (int i = 0; i < numAllies; ++i){
				Class summon = mobs[host.worldObj.rand.nextInt(mobs.length)];
				try {
					Constructor ctor = summon.getConstructor(World.class);
					EntityCreature mob = (EntityCreature)ctor.newInstance(host.worldObj);
					mob.setPosition(host.posX + host.worldObj.rand.nextDouble()*2-1, host.posY, host.posZ + host.worldObj.rand.nextDouble()*2-1);
					mob.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 99999, 1));
					mob.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 99999, 1));
					mob.addPotionEffect(new PotionEffect(Potion.regeneration.id, 99999, 1));
					mob.addPotionEffect(new BuffEffectMagicShield(99999, 1));
					if (host.getHealth() < host.getMaxHealth() / 2){
						mob.addPotionEffect(new BuffEffectShrink(99999, 1));
					}
					EntityUtilities.makeSummon_MonsterFaction(mob, false);
					EntityUtilities.setOwner(mob, host);
					EntityUtilities.setSummonDuration(mob, 1800);
					host.worldObj.spawnEntityInWorld(mob);

					host.queued_minions.add(mob);
				} catch (Throwable e) {
					e.printStackTrace();
					return;
				}
			}
		}
		if (actionTicks >= 23){
			resetTask();
		}
	}

}
