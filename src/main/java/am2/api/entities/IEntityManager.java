package am2.api.entities;

public interface IEntityManager{
	/**
	 * Adds the specified entities to the butchery blacklist (won't be affected by sigil of butchery)
	 */
	public void addButcheryBlacklist(Class... clazz);

	/**
	 * Adds the specified entities to the progeny blacklist (won't be affected by sigil of progeny)
	 */
	public void addProgenyBlacklist(Class... clazz);
}
