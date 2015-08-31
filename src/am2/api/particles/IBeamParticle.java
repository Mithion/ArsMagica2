package am2.api.particles;

public interface IBeamParticle {
	/**
	 * Sets the beam type.  Determines the texture of the beam.  Valid types are 0-2.
	 */
	public void setType(int type);
	/**
	 * Sets the color from a single integer.  Good for hex colors.
	 */
	public void setRGBColor(int color);
	/**
	 * Sets the beam color from 3 int values (0-255).
	 */
	public void setRGBColorI(int r, int g, int b);
	/**
	 * Sets the beam color from 3 floating point values (0.0f - 1.0f)
	 */
	public void setRGBColorF(float r, float g, float b);
	/**
	 * Indicates that this beam is being cast by a player that is in first person.  Should only be called when creating a beam from the local player, if the player is in first person.
	 * <br />
	 * This will offset the beam to make it look like it is coming from the player's active item and not their face.
	 */
	public void setFirstPersonPlayerCast();
}
