package am2.containers.slots;

import am2.items.ISpellFocus;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;

public class SlotSpellFocusOnly extends Slot{

	public SlotSpellFocusOnly(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
    public boolean isItemValid(ItemStack itemstack)
    {
    	if (itemstack.getItem() instanceof ISpellFocus){
    		return true;
    	}
        return false;
    }

}
