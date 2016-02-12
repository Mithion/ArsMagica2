package am2.items;

import net.minecraft.item.ItemStack;

public class ItemFocusMana extends ItemFocus{

	public ItemFocusMana(){
		super();
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				"P", "F", "P",
				'P', new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_VINTEUMDUST),
				'F', ItemsCommonProxy.standardFocus
		};
	}

	@Override
	public String getInGameName(){
		return "Mana Focus";
	}
}
