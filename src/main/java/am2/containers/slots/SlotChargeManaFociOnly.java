package am2.containers.slots;

import am2.items.ItemFocusCharge;
import am2.items.ItemFocusMana;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotChargeManaFociOnly extends Slot{

	public SlotChargeManaFociOnly(IInventory par1iInventory, int par2, int par3, int par4){
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack){
		return par1ItemStack.getItem() instanceof ItemFocusCharge || par1ItemStack.getItem() instanceof ItemFocusMana;
	}

}
