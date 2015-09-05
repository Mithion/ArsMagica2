package am2.api;

import am2.api.spell.enums.Affinity;

public interface IAffinityData{

	/**
	 * Gets the depth of the specified affinity as a percentage.
	 */
	float getAffinityDepth(Affinity affinity);

	/**
	 * Gets the factor at which affinity gains are currently reduced.
	 */
	float getDiminishingReturnsFactor();

	/**
	 * Directly sets an affinity.  Does not take into account oppositions.  Use sparingly.
	 *
	 * @param affinity The affinity to set
	 * @param depth    The depth to set
	 */
	void setAffinityAndDepth(Affinity affinity, float depth);

	/**
	 * Increments the affinity by the specified amount, and decrements other affinities by an amount related to
	 * how much in opposition they are to the specified affinity.
	 *
	 * @param affinity The affinity to increase
	 * @param amt      The amount to increase the affinity by
	 */
	void incrementAffinity(Affinity affinity, float amt);
}
