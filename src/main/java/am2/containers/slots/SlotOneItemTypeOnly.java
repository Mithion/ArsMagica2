package am2.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotOneItemTypeOnly extends Slot{

	private Item item;
	private int meta;

	private int maxStackSize = 64;

	public SlotOneItemTypeOnly(IInventory par1iInventory, int slotIndex, int x, int y, Item item){
		super(par1iInventory, slotIndex, x, y);
		this.item = item;
		this.meta = -1;
	}

	public SlotOneItemTypeOnly setMaxStackSize(int size){
		this.maxStackSize = size;
		return this;
	}

	@Override
	public int getSlotStackLimit(){
		return maxStackSize;
	}

	public SlotOneItemTypeOnly(IInventory par1iInventory, int slotIndex, int x, int y, Item item, int meta){
		super(par1iInventory, slotIndex, x, y);
		this.item = item;
		this.meta = meta;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack){
		if (meta == -1)
			return par1ItemStack != null && par1ItemStack.getItem() != null && par1ItemStack.getItem() == this.item;
		return par1ItemStack != null && par1ItemStack.getItem() != null && par1ItemStack.getItem() == this.item && par1ItemStack.getItemDamage() == meta;
	}

}
