package am2.api.illeffect;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

/**
 * Base interface for an ill effect during spell creation.  <br/>
 * Included for compatibility; do not directly inherit. <br/>
 * Use IllEffectBase instead.
 *
 * @author Mithion
 */
public interface IIllEffect extends Comparable{
	/**
	 * The severity level of the effect.  Controls at what instability level the effect could be applied.
	 *
	 * @return One of the values in the IllEffectSeverity enum.
	 */
	public IllEffectSeverity GetSeverity();

	/**
	 * Callback to actually apply the ill effect.
	 *
	 * @param world The world the effect is happening in
	 * @param pos     The position of the vinteum crystal
	 * @return A map of entity players affected, as well as any metadata relevant to the effect applied on that player.
	 */
	public Map<EntityPlayer, Object> ApplyIllEffect(World world, BlockPos pos);

	/**
	 * Gets the localized description of this ill effect as pertains to the affected entity player.
	 * This will only be called based on your own class, so you can assume the metadata matches your metadata output from ApplyIllEffect.
	 */
	public String getDescription(EntityPlayer player, Object metadata);
}
