package am2.illeffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import am2.api.illeffect.IllEffectBase;
import am2.api.illeffect.IllEffectSeverity;
import am2.entities.EntityDarkling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;


public class IllEffectSpawnDarkling extends IllEffectBase{

	@Override
	public IllEffectSeverity GetSeverity() {
		return IllEffectSeverity.MINOR;
	}

	@Override
	public Map<EntityPlayer, Object> ApplyIllEffect(World world, int x, int y, int z) {
		if (!world.isRemote){
			EntityDarkling darkling = new EntityDarkling(world);
			darkling.setPosition(x, y+1, z);
			world.spawnEntityInWorld(darkling);
		}
		
		return new HashMap<EntityPlayer, Object>();
	}

	@Override
	public String getDescription(EntityPlayer player, Object meta) {
		return "A Darkling has been spawned.";
	}

}
