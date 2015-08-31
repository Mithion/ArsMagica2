package am2.entities.ai;

import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;

public class EntityAITargetByTeam extends EntityAITarget{

	private EntityCreature host;
	private Team teamToTarget;
	private int radius;
	
	public EntityAITargetByTeam(EntityCreature host, Team teamToTarget, int radius){
		super(host, false);
		this.host = host;
		this.teamToTarget = teamToTarget;
		this.radius = radius;
	}
	
	@Override
	public boolean shouldExecute() {
		return host.getAttackTarget() == null;
	}
	
	@Override
	public void startExecuting() {
		tryGetTargetByTeam();
	}
	
	private boolean tryGetTargetByTeam(){
		List<EntityPlayer> players = host.worldObj.getEntitiesWithinAABB(EntityPlayer.class, host.boundingBox.expand(radius, radius, radius));
		
		for (EntityPlayer player : players){
			if (player.isOnTeam(teamToTarget)){
				if (host.getNavigator().tryMoveToEntityLiving(player, host.getAIMoveSpeed())){
					host.setAttackTarget(player);
					return true;
				}
			}
		}
		
		return false;
	}

}
