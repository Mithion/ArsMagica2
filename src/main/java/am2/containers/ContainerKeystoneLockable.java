package am2.containers;

import am2.api.blocks.IKeystoneLockable;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotGhostRune;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerKeystoneLockable extends AM2Container{
	private IKeystoneLockable lockable;

	private static final int CHEST_INVENTORY_END = 2;
	private static final int PLAYER_INVENTORY_START = 3;
	private static final int PLAYER_ACTION_BAR_START = 30;
	private static final int PLAYER_ACTION_BAR_END = 38;

	public ContainerKeystoneLockable(InventoryPlayer inventoryplayer, IKeystoneLockable lockable){
		this.lockable = lockable;
		addSlotToContainer(new SlotGhostRune((IInventory)lockable, 0, 62, 8)); //inventory, index, x, y
		addSlotToContainer(new SlotGhostRune((IInventory)lockable, 1, 80, 8));
		addSlotToContainer(new SlotGhostRune((IInventory)lockable, 2, 98, 8));

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 40 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			addSlotToContainer(new Slot(inventoryplayer, j1, 8 + j1 * 18, 98));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return ((IInventory)lockable).isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i){
		ItemStack itemstack = null;

		Slot slot = (Slot)inventorySlots.get(i);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < PLAYER_INVENTORY_START){
				return null; //ghost slots don't merge!
			}else if (i >= PLAYER_INVENTORY_START && i < PLAYER_ACTION_BAR_START) //from player inventory
			{
				if (!mergeItemStack(itemstack1, PLAYER_ACTION_BAR_START, PLAYER_ACTION_BAR_END, false)){
					return null;
				}
			}else if (i >= PLAYER_ACTION_BAR_START && i < PLAYER_ACTION_BAR_END){
				if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_START - 1, false)){
					return null;
				}
			}else if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)){
				return null;
			}

			if (itemstack1.stackSize == 0){
				slot.putStack(null);
			}else{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize != itemstack.stackSize){
				slot.onSlotChange(itemstack1, itemstack);
			}else{
				return null;
			}
		}
		return itemstack;
	}
}
