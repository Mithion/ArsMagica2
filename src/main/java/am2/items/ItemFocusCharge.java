package am2.items;

import net.minecraft.init.Blocks;

public class ItemFocusCharge extends ItemFocus{

	public ItemFocusCharge(){
		super();
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				"CFC",
				'F', ItemsCommonProxy.standardFocus,
				'C', Blocks.glass
		};
	}

	@Override
	public String getInGameName(){
		return "Charge Focus";
	}
}
