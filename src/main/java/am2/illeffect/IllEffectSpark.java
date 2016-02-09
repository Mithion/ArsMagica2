package am2.illeffect;

import am2.AMCore;
import am2.api.illeffect.IllEffectBase;
import am2.api.illeffect.IllEffectSeverity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class IllEffectSpark extends IllEffectBase{

	@Override
	public IllEffectSeverity GetSeverity(){
		return IllEffectSeverity.MODERATE;
	}

	@Override
	public Map<EntityPlayer, Object> ApplyIllEffect(World world, BlockPos pos){
		HashMap<EntityPlayer, Object> toReturn = new HashMap<EntityPlayer, Object>();
		if (world.isRemote) return toReturn;
		List<EntityPlayer> located_players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-3, -3, -3), pos.add(3, 3, 3)));
		EntityPlayer[] players = located_players.toArray(new EntityPlayer[located_players.size()]);
		if (players.length == 0) return toReturn;
		EntityPlayer unlucky = players[world.rand.nextInt(players.length)];

		AMCore.instance.proxy.particleManager.BoltFromPointToPoint(world, pos.getX(), pos.getY(), pos.getZ(), unlucky.posX, unlucky.posY, unlucky.posZ, 4, -1);
		unlucky.attackEntityFrom(DamageSource.generic, 1);

		toReturn.put(unlucky, null);
		return toReturn;
	}

	@Override
	public String getDescription(EntityPlayer player, Object meta){
		return player.getName() + " got hit by a stray tendril of energy!";
	}

}
