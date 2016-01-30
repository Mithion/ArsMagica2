package am2.illeffect;

import am2.api.illeffect.IllEffectBase;
import am2.api.illeffect.IllEffectSeverity;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class IllEffectDrainMana extends IllEffectBase{

	@Override
	public IllEffectSeverity GetSeverity(){
		return IllEffectSeverity.MINOR;
	}

	@Override
	public Map<EntityPlayer, Object> ApplyIllEffect(World world, int x, int y, int z){
		HashMap<EntityPlayer, Object> toReturn = new HashMap<EntityPlayer, Object>();
		if (world.isRemote) return toReturn;

		List<EntityPlayer> located_players = world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x - 3, y - 3, z - 3, x + 3, y + 3, z + 3));

		EntityPlayer[] players = located_players.toArray(new EntityPlayer[located_players.size()]);

		if (players.length == 0) return toReturn;

		for (EntityPlayer player : players){
			ExtendedProperties props = ExtendedProperties.For(player);
			props.setCurrentMana(0);
			props.forceSync();
			toReturn.put(player, null);
		}

		return toReturn;
	}

	@Override
	public String getDescription(EntityPlayer player, Object metadata){
		return String.format("%s had their mana drained.", player.getName());
	}
}
