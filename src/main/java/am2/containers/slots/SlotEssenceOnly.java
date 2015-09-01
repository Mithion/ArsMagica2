package am2.containers.slots;

import am2.items.ItemEssence;
import am2.items.ItemsCommonProxy;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotEssenceOnly extends Slot{
	
	public SlotEssenceOnly(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		if (par1ItemStack.getItem() instanceof ItemEssence) return true;
		return false;
	}

}
