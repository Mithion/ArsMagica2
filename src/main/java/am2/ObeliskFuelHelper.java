package am2;

import am2.api.power.IObeliskFuelHelper;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class ObeliskFuelHelper implements IObeliskFuelHelper{
	private HashMap<ItemStack, Integer> validFuels;

	public static final ObeliskFuelHelper instance = new ObeliskFuelHelper();

	private ObeliskFuelHelper(){
		validFuels = new HashMap<ItemStack, Integer>();
	}

	@Override
	public void registerFuelType(ItemStack stack, int burnTime){
		stack.stackSize = 0;
		validFuels.put(stack.copy(), burnTime);
	}

	@Override
	public int getFuelBurnTime(ItemStack stack){
		if (stack == null)
			return 0;

		for (ItemStack possibleFuel : validFuels.keySet()){
			if (stack.getItem() == possibleFuel.getItem() && (possibleFuel.getItemDamage() == Short.MAX_VALUE || possibleFuel.getItemDamage() == stack.getItemDamage()))
				return validFuels.get(possibleFuel);
		}
		return 0;
	}
}
