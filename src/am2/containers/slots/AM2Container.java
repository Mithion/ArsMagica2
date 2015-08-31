package am2.containers.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import am2.utility.InventoryUtilities;

public abstract class AM2Container extends Container {

	public AM2Container() {
	}

	@Override
	public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player) {
		Slot slot = slotNum < 0 ? null : (Slot) this.inventorySlots.get(slotNum);
		if(slot instanceof SlotGhostItem){
			return slotClickGhost(slot, mouseButton, modifier, player);
		}
		return super.slotClick(slotNum, mouseButton, modifier, player);
	}

	public ItemStack slotClickGhost(Slot slot, int mouseButton, int modifier, EntityPlayer player){
		ItemStack stack = null;

		if(mouseButton == 2){
			if(((IGhostSlot)slot).canAdjust()){
				slot.putStack(null);
			}
		} else if(mouseButton == 0 || mouseButton == 1){
			InventoryPlayer playerInv = player.inventory;
			slot.onSlotChanged();
			ItemStack stackSlot = slot.getStack();
			ItemStack stackHeld = playerInv.getItemStack();

			if(stackSlot != null){
				stack = stackSlot.copy();
			}

			if(stackSlot == null){
				if(stackHeld != null && slot.isItemValid(stackHeld)){
					fillGhostSlot(slot, stackHeld, mouseButton, modifier);
				}
			} else if(stackHeld == null){
				adjustGhostSlot(slot, mouseButton, modifier);
				slot.onPickupFromSlot(player, playerInv.getItemStack());
			} else if(slot.isItemValid(stackHeld)){
				if(InventoryUtilities.canStacksMerge(stackSlot, stackHeld)){
					adjustGhostSlot(slot, mouseButton, modifier);
				} else {
					fillGhostSlot(slot, stackHeld, mouseButton, modifier);
				}
			}
		}

		return stack;
	}

	private void adjustGhostSlot(Slot slot, int mouseButton, int modifier) {
		if(!((IGhostSlot)slot).canAdjust()){
			return;
		}

		ItemStack stackSlot = slot.getStack();
		int stackSize;
		if(modifier == 1){
			stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize + 2;
		} else {
			stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
		}

		if(stackSize > slot.getSlotStackLimit()){
			stackSize = slot.getSlotStackLimit();
		}

		stackSlot.stackSize = stackSize;

		if(stackSlot.stackSize <= 0){
			slot.putStack((ItemStack) null);
		}
	}

	private void fillGhostSlot(Slot slot, ItemStack stackHeld, int mouseButton,
			int modifier) {
		if(!((IGhostSlot)slot).canAdjust()){
			return;
		}

		int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
		if(stackSize > slot.getSlotStackLimit()){
			stackSize = slot.getSlotStackLimit();
		}
		ItemStack ghostStack = stackHeld.copy();
		ghostStack.stackSize = stackSize;

		slot.putStack(ghostStack);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack originalStack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		int numSlots = inventorySlots.size();
		if(slot != null && slot.getHasStack()){
			ItemStack stackInSlot = slot.getStack();
			originalStack = stackInSlot.copy();
			if(slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots)){

			} else if(slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9){
				if(!shiftItemStack(stackInSlot, numSlots - 9, numSlots)){
					return null;
				}
			} else if(slotIndex >= numSlots - 9 && slotIndex < numSlots){
				if(!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9)){
					return null;
				}
			} else if(!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots)){
				return null;
			}
			slot.onSlotChange(stackInSlot, originalStack);
			if(stackInSlot.stackSize <= 0){
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}

			if(stackInSlot.stackSize == originalStack.stackSize){
				return null;
			}

			slot.onPickupFromSlot(player, stackInSlot);
		}
		return originalStack;
	}

	private boolean shiftItemStack(ItemStack stackToShift, int start, int end) {
		boolean changed = false;
		if(stackToShift.isStackable()){
			for(int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++){
				Slot slot = (Slot) inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if(stackInSlot != null && InventoryUtilities.canStacksMerge(stackInSlot, stackToShift)){
					int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
					int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					if(resultingStackSize <= max){
						stackToShift.stackSize = 0;
						stackInSlot.stackSize = resultingStackSize;
						slot.onSlotChanged();
						changed = true;
					} else if(stackInSlot.stackSize < max){
						stackToShift.stackSize -= max - stackInSlot.stackSize;
						stackInSlot.stackSize = max;
						slot.onSlotChanged();
						changed = true;
					}
				}
			}
		}

		if(stackToShift.stackSize > 0){
			for(int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++){
				Slot slot = (Slot) inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if(stackInSlot == null){
					int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					stackInSlot = stackToShift.copy();
					stackInSlot.stackSize = Math.min(stackToShift.stackSize, max);
					stackToShift.stackSize -= stackInSlot.stackSize;
					slot.putStack(stackInSlot);
					slot.onSlotChanged();
					changed = true;
				}
			}
		}

		return changed;
	}

	private boolean tryShiftItem(ItemStack stackToShift, int numSlots) {
		for(int machineIndex =0; machineIndex < numSlots - 9 * 4; machineIndex++){
			Slot slot = (Slot)inventorySlots.get(machineIndex);
			if(slot instanceof IGhostSlot){
				continue;
			}
			if(shiftItemStack(stackToShift, machineIndex, machineIndex + 1)){
				return true;
			}
		}

		return false;
	}

	protected void addPlayerInventory(EntityPlayer player, int left, int top){
		//display player inventory
		for (int i = 0; i < 3; i++)
		{
			for (int k = 0; k < 9; k++)
			{
				addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, left + k * 18, top + i * 18));
			}
		}
	}

	protected void addPlayerActionBar(EntityPlayer player, int left, int top){
		//display player action bar
		for (int j1 = 0; j1 < 9; j1++)
		{
			addSlotToContainer(new Slot(player.inventory, j1, left + j1 * 18, top));
		}
	}
}
