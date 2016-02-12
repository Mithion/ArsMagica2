package am2.items;

import net.minecraft.entity.EntityCreature;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class ItemCreatureFocus extends ItemFilterFocus{

	protected ItemCreatureFocus(){
		super();
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				" P ",
				"LFT",
				" W ",
				Character.valueOf('P'), Items.porkchop,
				Character.valueOf('B'), Items.leather,
				Character.valueOf('F'), ItemsCommonProxy.standardFocus,
				Character.valueOf('T'), Items.feather,
				Character.valueOf('W'), Blocks.wool,
		};
	}

	@Override
	public String getInGameName(){
		return "Creature Focus";
	}

	@Override
	public Class getFilterClass(){
		return EntityCreature.class;
	}
}
