package am2.api.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.entity.EntityLivingBase;

/**
 * Raised when a player's magic level changes.
 *
 * @author Mithion
 */
public class PlayerMagicLevelChangeEvent extends Event{
	public final int level;
	public final EntityLivingBase entity;

	public PlayerMagicLevelChangeEvent(EntityLivingBase entity, int level){
		this.entity = entity;
		this.level = level;
	}
}
