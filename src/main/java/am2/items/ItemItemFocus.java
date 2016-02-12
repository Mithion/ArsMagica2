package am2.items;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class ItemItemFocus extends ItemFilterFocus{

	protected ItemItemFocus(){
		super();
	}

	@Override
	public Class getFilterClass(){
		return EntityItem.class;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				" C ",
				"PFS",
				" W ",
				Character.valueOf('C'), Blocks.cobblestone,
				Character.valueOf('B'), Items.stone_pickaxe,
				Character.valueOf('F'), ItemsCommonProxy.standardFocus,
				Character.valueOf('T'), Items.iron_shovel,
				Character.valueOf('W'), Blocks.crafting_table
		};
	}

	@Override
	public String getInGameName(){
		return "Item Focus";
	}
}
