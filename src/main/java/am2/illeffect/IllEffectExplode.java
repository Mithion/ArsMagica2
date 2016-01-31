package am2.illeffect;

import am2.api.illeffect.IllEffectBase;
import am2.api.illeffect.IllEffectSeverity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class IllEffectExplode extends IllEffectBase{

	@Override
	public IllEffectSeverity GetSeverity(){
		return IllEffectSeverity.SEVERE;
	}

	@Override
	public Map<EntityPlayer, Object> ApplyIllEffect(World world, BlockPos pos){
		HashMap<EntityPlayer, Object> toReturn = new HashMap<EntityPlayer, Object>();
		if (world.isRemote)
			return new HashMap<EntityPlayer, Object>();

		world.newExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 7, true, true);

		return new HashMap<EntityPlayer, Object>();
	}

	@Override
	public String getDescription(EntityPlayer player, Object metadata){
		return "An explosion happened!";
	}
}
