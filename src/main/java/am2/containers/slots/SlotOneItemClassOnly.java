package am2.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOneItemClassOnly extends Slot{

	private Class itemClass;	
	private int maxStackSize;
	
	public SlotOneItemClassOnly(IInventory par1iInventory, int slotIndex, int x, int y, Class itemClass) {
		super(par1iInventory, slotIndex, x, y);
		this.itemClass = itemClass;
		this.maxStackSize = 64;
	}
	
	public SlotOneItemClassOnly(IInventory par1iInventory, int slotIndex, int x, int y, Class itemClass, int maxStackSize) {
		super(par1iInventory, slotIndex, x, y);
		this.itemClass = itemClass;
		this.maxStackSize = maxStackSize;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return itemClass.isAssignableFrom(par1ItemStack.getItem().getClass());
	}
}
