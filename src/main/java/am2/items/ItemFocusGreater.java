package am2.items;

import net.minecraft.item.ItemStack;

public class ItemFocusGreater extends ItemFocus implements ISpellFocus{

	public ItemFocusGreater(){
		super();
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				"A A", "PFP", "A A",
				'A', new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ARCANEASH),
				'F', ItemsCommonProxy.standardFocus,
				'P', new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_PURIFIEDVINTEUM)
		};
	}

	@Override
	public String getInGameName(){
		return "Greater Focus";
	}

	@Override
	public int getFocusLevel(){
		return 2;
	}
}
