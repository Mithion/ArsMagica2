package am2.containers.slots;

import am2.api.spell.ItemSpellBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSpellCustomization extends Slot{
	public SlotSpellCustomization(IInventory par1iInventory, int par2, int par3, int par4){
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer){
		return false;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack){
		return (par1ItemStack != null) ? par1ItemStack.getItem() instanceof ItemSpellBase : false;
	}
}
