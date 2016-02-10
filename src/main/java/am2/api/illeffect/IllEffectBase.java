package am2.api.illeffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

/**
 * Base class for all ill effects.
 *
 * @author Mithion
 */
public abstract class IllEffectBase implements IIllEffect{

	@Override
	public final int compareTo(Object o){
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
	public abstract Map<EntityPlayer, Object> ApplyIllEffect(World world, BlockPos pos);

}
