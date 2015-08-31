package am2.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import am2.ObeliskFuelHelper;

public class SlotObelisk extends Slot{

	public SlotObelisk(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return ObeliskFuelHelper.instance.getFuelBurnTime(par1ItemStack) > 0;
	}

}
