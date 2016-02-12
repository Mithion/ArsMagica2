package am2.items;

import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Items;

public class ItemMobFocus extends ItemFilterFocus{

	protected ItemMobFocus(){
		super();
	}

	@Override
	public Class getFilterClass(){
		return IMob.class;
	}

	@Override
	public Object[] getRecipeItems(){
		return new Object[]{
				"S",
				"F",
				"S",
				Character.valueOf('S'), Items.iron_sword,
				Character.valueOf('F'), ItemsCommonProxy.standardFocus
		};
	}

	@Override
	public String getInGameName() {
        return "Monster Focus";
    }
}
