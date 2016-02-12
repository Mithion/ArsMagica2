package am2.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemPlayerFocus extends ItemFilterFocus{

	protected ItemPlayerFocus(){
		super();
	}

	@Override
	public Class getFilterClass(){
		return EntityPlayer.class;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				"L",
				"F",
				Character.valueOf('L'), new ItemStack(ItemsCommonProxy.essence, 1, 8),
				Character.valueOf('F'), ItemsCommonProxy.standardFocus
		};
	}

	@Override
	public String getInGameName(){
		return "Player Focus";
	}
}
