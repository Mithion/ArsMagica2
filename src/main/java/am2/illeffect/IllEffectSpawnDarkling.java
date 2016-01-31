package am2.illeffect;

import am2.api.illeffect.IllEffectBase;
import am2.api.illeffect.IllEffectSeverity;
import am2.entities.EntityDarkling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;


public class IllEffectSpawnDarkling extends IllEffectBase{

	@Override
	public IllEffectSeverity GetSeverity(){
		return IllEffectSeverity.MINOR;
	}

	@Override
	public Map<EntityPlayer, Object> ApplyIllEffect(World world, BlockPos pos){
		if (!world.isRemote){
			EntityDarkling darkling = new EntityDarkling(world);
			darkling.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
			world.spawnEntityInWorld(darkling);
		}

		return new HashMap<EntityPlayer, Object>();
	}

	@Override
	public String getDescription(EntityPlayer player, Object meta){
		return "A Darkling has been spawned.";
	}

}
