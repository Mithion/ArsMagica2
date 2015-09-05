package am2.buffs;

import am2.utility.EntityUtilities;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class BuffEffectCharmed extends BuffEffect{

	private final int particleTicks;

	public static final int CHARM_TO_PLAYER = 1;
	public static final int CHARM_TO_MONSTER = 2;

	private EntityLivingBase charmer;

	public BuffEffectCharmed(int duration, int amplifier){
		super(BuffList.charmed.id, duration, amplifier);
		particleTicks = 0;
	}

	public void setCharmer(EntityLivingBase entity){
		charmer = entity;
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
		if (getAmplifier() + 1 == CHARM_TO_PLAYER && entityliving instanceof EntityCreature && charmer instanceof EntityPlayer){
			EntityUtilities.makeSummon_PlayerFaction((EntityCreature)entityliving, (EntityPlayer)charmer, true);
		}else if (getAmplifier() + 1 == CHARM_TO_MONSTER && entityliving instanceof EntityCreature){
			EntityUtilities.makeSummon_MonsterFaction((EntityCreature)entityliving, true);
		}
		EntityUtilities.setOwner(entityliving, charmer);
		EntityUtilities.setSummonDuration(entityliving, -1);
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
		if (entityliving instanceof EntityCreature){
			EntityUtilities.revertAI((EntityCreature)entityliving);
		}
	}

	@Override
	protected String spellBuffName(){
		return "Charmed";
	}

}
