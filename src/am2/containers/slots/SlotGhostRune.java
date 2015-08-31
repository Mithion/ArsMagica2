package am2.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import am2.items.ItemsCommonProxy;

public class SlotGhostRune extends SlotGhostItem{

	public SlotGhostRune(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return par1ItemStack != null && par1ItemStack.getItem() == ItemsCommonProxy.rune && ItemsCommonProxy.rune.getKeyIndex(par1ItemStack) > 0;
	}

}
