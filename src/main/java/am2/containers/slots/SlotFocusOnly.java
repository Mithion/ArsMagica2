package am2.containers.slots;

import am2.items.ItemFocus;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFocusOnly extends Slot{

	public SlotFocusOnly(IInventory par1iInventory, int par2, int par3, int par4){
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack){
		if (itemstack.getItem() instanceof ItemFocus){
			return true;
		}
		return false;
	}

}
