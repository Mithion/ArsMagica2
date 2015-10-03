package am2.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotArmorPiece extends Slot{

	private int armorIndex;

	public SlotArmorPiece(IInventory par1iInventory, int par2, int par3, int par4){
		super(par1iInventory, par2, par3, par4);
	}

	public SlotArmorPiece(IInventory par1iInventory, int par2, int par3, int par4, int index){
		super(par1iInventory, par2, par3, par4);
		setArmorIndex(index);
	}

	public void setArmorIndex(int index){
		this.armorIndex = index;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack){
		return par1ItemStack.getItem().isValidArmor(null, armorIndex, null);
	}

}
