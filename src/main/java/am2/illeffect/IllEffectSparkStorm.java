package am2.illeffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.illeffect.IllEffectBase;
import am2.api.illeffect.IllEffectSeverity;

public class IllEffectSparkStorm extends IllEffectBase{

	@Override
	public IllEffectSeverity GetSeverity() {
		return IllEffectSeverity.SEVERE;
	}

	@Override
	public Map<EntityPlayer, Object> ApplyIllEffect(World world, int x, int y, int z) {
		HashMap<EntityPlayer, Object> toReturn  = new HashMap<EntityPlayer, Object>();
		if (world.isRemote) return toReturn;
		Random rand = new Random();
		List<EntityPlayer> located_players = world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x - 3, y - 3, z - 3, x + 3, y + 3, z + 3));
		EntityPlayer[] players = located_players.toArray(new EntityPlayer[located_players.size()]);
		if (players.length == 0) return toReturn;

		for (EntityPlayer unlucky : players){
			AMCore.instance.proxy.particleManager.BoltFromPointToPoint(world, x, y, z, unlucky.posX, unlucky.posY, unlucky.posZ, 4, -1);
			unlucky.attackEntityFrom(DamageSource.generic, 1);
			toReturn.put(unlucky, null);
		}

		return toReturn;
	}

	@Override
	public String getDescription(EntityPlayer player, Object meta) {
		return player.getCommandSenderName() + " got caught in a sparkstorm!";
	}

}
