package am2.api.illeffect;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Base class for all ill effects.
 * @author Mithion
 *
 */
public abstract class IllEffectBase implements IIllEffect{

	@Override
	public final int compareTo(Object o) {
		if (!(o instanceof IIllEffect)) return 0;
		IIllEffect right = (IIllEffect)o;
		int myOrdinal = this.GetSeverity().ordinal();
		int theirOrdinal = right.GetSeverity().ordinal();
		
		if (myOrdinal == theirOrdinal){
			return 0;
		}else if (myOrdinal < theirOrdinal){
			return -1;
		}else{
			return 1;
		}
	}

	@Override
	public abstract IllEffectSeverity GetSeverity();
	
	@Override
	public abstract String getDescription(EntityPlayer player, Object metadata);

	@Override
	public abstract Map<EntityPlayer, Object> ApplyIllEffect(World world, int x, int y, int z);

}
