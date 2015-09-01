package am2.api.spell.component.interfaces;

/**
 * Identifies that this object is either a spell component, a spell modifier, or a spell shape.
 * Do not inherit this directly unless you are adding talents and such, rather inherit ISpellComponent, ISpellModifier, or ISpellShape for spell parts.
 * @author Mithion
 *
 */
public interface ISpellPart extends ISkillTreeEntry {

	/**
	 * recipe items, in order, that need to be thrown into the crafting altar in order to create the item.
	 * <br/>
	 * Use Items for items, Blocks for blocks, Strings for OreDictionary items, and E:[type flag] followed by an integer for essence amounts.
	 * <br/>
	 * Use itemstacks for items/blocks/oredict when meta is required.  By default it is meta 0.  Quantity (stack size) is ignored.
	 * <br/>
	 * Integer pairs represent the type (*=any, 1=neutral, 2=light, 4=dark, etc.), and the quantity of essence required.  The type can be used as flags,
	 * <br/>
	 * For example:
	 * <pre>
	 *     new Object[]{ "E:1|2", 1500 } //require 1500 of neutral or 1500 of light power.
	 *     new Object[]{ "E:*", 1500 } //require 1500 of any kind of power
	 *     etc.
	 *</pre>
	 */
	public Object[] getRecipeItems();
}
