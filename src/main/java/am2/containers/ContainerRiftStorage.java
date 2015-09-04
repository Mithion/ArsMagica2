package am2.containers;

import am2.playerextensions.RiftStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRiftStorage extends Container{

	private final RiftStorage inventory;
	private final InventoryPlayer playerInventory;

	private int PLAYER_INVENTORY_START = 0;
	private int PLAYER_ACTIONBAR_START = 0;
	private int PLAYER_ACTIONBAR_END = 0;

	public ContainerRiftStorage(InventoryPlayer playerInventory, RiftStorage inventory){
		this.inventory = inventory;
		this.playerInventory = playerInventory;

		int rows = 1;

		switch (inventory.getAccessLevel()){
		case 1:
			rows = 1;
			break;
		case 2:
			rows = 3;
			break;
		case 3:
			rows = 6;
			break;
		}

		PLAYER_INVENTORY_START = rows * 9;
		PLAYER_ACTIONBAR_START = PLAYER_INVENTORY_START + 27;
		PLAYER_ACTIONBAR_END = PLAYER_ACTIONBAR_START + 9;


		//chest inventory slots
		for (int j = 0; j < rows; ++j){
			for (int i = 0; i < 9; ++i){
				addSlotToContainer(new Slot(inventory, i + j * 9, 8 + (18 * i), 13 + (j * 18)));
			}
		}

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(playerInventory, k + i * 9 + 9, 8 + k * 18, 130 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			addSlotToContainer(new Slot(playerInventory, j1, 8 + j1 * 18, 188));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i){
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(i);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < PLAYER_INVENTORY_START){
				if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTIONBAR_END, true)){
					return null;
				}
			}else if (i >= PLAYER_INVENTORY_START && i < PLAYER_ACTIONBAR_START){
				if (!mergeItemStack(itemstack1, 0, PLAYER_INVENTORY_START - 1, false)){
					return null;
				}
			}else if (i >= PLAYER_ACTIONBAR_START && i < PLAYER_ACTIONBAR_END){
				if (!mergeItemStack(itemstack1, 0, PLAYER_INVENTORY_START - 1, false)){
					return null;
				}
			}else if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTIONBAR_END, false)){
				return null;
			}
			if (itemstack1.stackSize == 0){
				slot.putStack(null);
				slot.onSlotChange(itemstack, itemstack1);
			}else{
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize){
				return null;
			}
		}
		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return inventory.isUseableByPlayer(entityplayer);
	}

}
