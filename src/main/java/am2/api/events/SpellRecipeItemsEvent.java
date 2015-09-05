package am2.api.events;

import cpw.mods.fml.common.eventhandler.Event;

public class SpellRecipeItemsEvent extends Event{
	public final String registeredName;                //The registered name of the spell part.  Used to identify what spell shape/component/modifier we are working with
	public final int ID;                            //The ID of the spell part.  Also can be used to identify what we are working with, if you don't like strings.

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
	 * </pre>
	 */
	public Object[] recipeItems;                    //The actual recipe items.  Change or verify them here.

	public SpellRecipeItemsEvent(String name, int ID, Object[] recipeItems){
		registeredName = name;
		this.ID = ID;
		this.recipeItems = recipeItems;
	}

}
