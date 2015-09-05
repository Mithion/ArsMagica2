package am2.containers.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotGhostItem extends Slot implements IGhostSlot{

	int maxStackSize = 1;

	public SlotGhostItem(IInventory par1iInventory, int par2, int par3, int par4){
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean canTakeStack(EntityPlayer par1EntityPlayer){
		return false;
	}

	@Override
	public boolean canAdjust(){
		return true;
	}

	@Override
	public int getSlotStackLimit(){
		return maxStackSize;
	}

	public void setSlotStackLimit(int stackSize){
		maxStackSize = stackSize;
	}

}
