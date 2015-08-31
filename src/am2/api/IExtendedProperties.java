package am2.api;

import am2.api.math.AMVector3;

/**
 * Extended properties on EntityLiving used in Ars Magica.
 * @author Mithion
 *
 */
public interface IExtendedProperties {
	public float getCurrentMana();
	public float getMaxMana();

	public int getMarkDimension();
	public int getMagicLevel();
	public int getNumSummons();

	public AMVector3 getMarkLocation();

	public boolean getHasUnlockedAugmented();
	public boolean getMarkSet();

	public boolean setMagicLevelWithMana(int magicLevel);
	public void setCurrentMana(float currentMana);
}
