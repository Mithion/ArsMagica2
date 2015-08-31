package am2.illeffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import am2.api.illeffect.IllEffectBase;
import am2.api.illeffect.IllEffectSeverity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class IllEffectExplode extends IllEffectBase{
	
	@Override
	public IllEffectSeverity GetSeverity() {
		return IllEffectSeverity.SEVERE;
	}

	@Override
	public Map<EntityPlayer, Object> ApplyIllEffect(World world, int x, int y, int z) {
		HashMap<EntityPlayer, Object> toReturn  = new HashMap<EntityPlayer, Object>();
		if (world.isRemote) 
			return new HashMap<EntityPlayer, Object>();
		
		world.newExplosion(null, x + 0.5, y + 0.5, z + 0.5, 7, true, true);
		
		return new HashMap<EntityPlayer, Object>();
	}
	
	@Override
	public String getDescription(EntityPlayer player, Object metadata) {
		return "An explosion happened!";
	}
}
