package am2.containers;

import am2.blocks.tileentities.TileEntityKeystoneChest;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotGhostRune;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerKeystoneChest extends AM2Container{

	private TileEntityKeystoneChest chest;

	private static final int CHEST_INVENTORY_END = 27;
	private static final int PLAYER_INVENTORY_START = 30;
	private static final int PLAYER_ACTION_BAR_START = 57;
	private static final int PLAYER_ACTION_BAR_END = 66;

	public ContainerKeystoneChest(InventoryPlayer inventoryplayer, TileEntityKeystoneChest tileEntityChest){
		chest = tileEntityChest;
		chest.openChest();
		//chest inventory slots
		for (int j = 0; j < 3; ++j){
			for (int i = 0; i < 9; ++i){
				addSlotToContainer(new Slot(tileEntityChest, i + j * 9, 8 + (18 * i), 30 + (j * 18)));
			}
		}

		addSlotToContainer(new SlotGhostRune(tileEntityChest, 27, 62, 8)); //inventory, index, x, y
		addSlotToContainer(new SlotGhostRune(tileEntityChest, 28, 80, 8));
		addSlotToContainer(new SlotGhostRune(tileEntityChest, 29, 98, 8));

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 98 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			addSlotToContainer(new Slot(inventoryplayer, j1, 8 + j1 * 18, 156));
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
				if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, true)){
					return null;
				}
			}else if (i >= PLAYER_INVENTORY_START && i < PLAYER_ACTION_BAR_START) //from player inventory
			{
				if (!mergeItemStack(itemstack1, 0, CHEST_INVENTORY_END, false)){
					if (!mergeItemStack(itemstack1, PLAYER_ACTION_BAR_START, PLAYER_ACTION_BAR_END, false)){
						return null;
					}
				}
			}else if (i >= PLAYER_ACTION_BAR_START && i < PLAYER_ACTION_BAR_END){
				if (!mergeItemStack(itemstack1, 0, CHEST_INVENTORY_END, false)){
					if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_START - 1, false)){
						return null;
					}
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

	@Override
	public boolean canInteractWith(EntityPlayer var1){
		return chest.isUseableByPlayer(var1);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer){
		chest.closeChest();
		super.onContainerClosed(par1EntityPlayer);
	}

}
