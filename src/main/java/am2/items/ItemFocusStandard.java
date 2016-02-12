package am2.items;

import net.minecraft.init.Items;

public class ItemFocusStandard extends ItemFocus implements ISpellFocus{

	public ItemFocusStandard(){
		super();
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				" R ", "RFR", " R ",
				'R', Items.redstone,
				'F', ItemsCommonProxy.lesserFocus
		};
	}

	@Override
	public String getInGameName(){
		return "Focus";
	}

	@Override
	public int getFocusLevel(){
		return 1;
	}
}
