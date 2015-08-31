package am2.bosses;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.IBossDisplayData;

public interface IArsMagicaBoss<T extends EntityLiving> extends IBossDisplayData{
	/**
	 * Should simply return the current action
	 */
	public BossActions getCurrentAction();
	/**
	 * Should set the current action as well as reset the number of ticks in the current action.
	 * If world is not remote should also send a packet to all nearby clients advising action change. 
	 */
	public void setCurrentAction(BossActions action);
	/**
	 * Should return the number of ticks in the current action
	 */
	public int getTicksInCurrentAction();
	/**
	 * Is the passed in action valid based on any internal states?  (Ex, plant guardian has thrown sickle) 
	 */
	public boolean isActionValid(BossActions action);
	
	/**
	 * Gets the sound played when the entity attacks
	 */
	public String getAttackSound();
}
