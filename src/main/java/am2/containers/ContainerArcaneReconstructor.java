package am2.containers;

import am2.blocks.tileentities.TileEntityArcaneReconstructor;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotChargeManaFociOnly;
import am2.containers.slots.SlotGhostRune;
import am2.containers.slots.SlotPickupOnly;
import am2.items.ItemFocusCharge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerArcaneReconstructor extends AM2Container{

	private TileEntityArcaneReconstructor reconstructor;

	private static final int PLAYER_INVENTORY_START = 19;
	private static final int PLAYER_ACTION_BAR_START = 46;
	private static final int PLAYER_ACTION_BAR_END = 55;


	public ContainerArcaneReconstructor(InventoryPlayer inventoryplayer, TileEntityArcaneReconstructor reconstructor){
		this.reconstructor = reconstructor;

		//foci
		this.addSlotToContainer(new SlotChargeManaFociOnly(reconstructor, 0, 62, 88));
		this.addSlotToContainer(new SlotChargeManaFociOnly(reconstructor, 1, 80, 88));
		this.addSlotToContainer(new SlotChargeManaFociOnly(reconstructor, 2, 98, 88));

		//working slot
		this.addSlotToContainer(new SlotPickupOnly(reconstructor, 3, 80, 48));

		int i;
		int j;
		int slot = 4;
		//inputs
		for (i = 0; i < 3; ++i){
			for (j = 0; j < 2; ++j){
				this.addSlotToContainer(new Slot(reconstructor, slot++, 8 + j * 18, 30 + i * 18));
			}
		}

		//outputs
		for (i = 0; i < 3; ++i){
			for (j = 0; j < 2; ++j){
				this.addSlotToContainer(new Slot(reconstructor, slot++, 134 + j * 18, 30 + i * 18));
			}
		}

		this.addSlotToContainer(new SlotGhostRune(reconstructor, 16, 62, 8));
		this.addSlotToContainer(new SlotGhostRune(reconstructor, 17, 80, 8));
		this.addSlotToContainer(new SlotGhostRune(reconstructor, 18, 98, 8));

		//display player inventory
		for (i = 0; i < 3; i++){
			for (j = 0; j < 9; j++){
				addSlotToContainer(new Slot(inventoryplayer, j + i * 9 + 9, 8 + j * 18, 118 + i * 18));
			}
		}

		//display player action bar
		for (j = 0; j < 9; j++){
			addSlotToContainer(new Slot(inventoryplayer, j, 8 + j * 18, 176));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return reconstructor.isUseableByPlayer(entityplayer);
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
				if (!mergeSpecialItems(itemstack1, slot)){
					if (!mergeItemStack(itemstack1, PLAYER_ACTION_BAR_START, PLAYER_ACTION_BAR_END, false)){
						return null;
					}
				}else{
					return null;
				}
			}else if (i >= PLAYER_ACTION_BAR_START && i < PLAYER_ACTION_BAR_END){
				if (!mergeSpecialItems(itemstack1, slot)){
					if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_START - 1, false)){
						return null;
					}
				}else{
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

	private boolean mergeSpecialItems(ItemStack stack, Slot slot){
		if (stack.getItem() instanceof ItemFocusCharge){
			for (int b = 0; b < 3; ++b){
				Slot focusSlot = (Slot)inventorySlots.get(b);
				if (focusSlot.getHasStack()) continue;

				focusSlot.putStack(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
				focusSlot.onSlotChanged();
				stack.stackSize--;
				if (stack.stackSize == 0){
					slot.putStack(null);
					slot.onSlotChanged();
				}
				return true;
			}
		}else if (stack.getItem().isRepairable() && stack.isItemDamaged()){
			for (int b = 4; b < 10; ++b){
				Slot repairSlot = (Slot)inventorySlots.get(b);
				if (repairSlot.getHasStack()) continue;

				ItemStack input = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
				if (stack.getTagCompound() != null){
					input.setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
				}
				repairSlot.putStack(input);
				repairSlot.onSlotChanged();
				stack.stackSize--;
				if (stack.stackSize == 0){
					slot.putStack(null);
					slot.onSlotChanged();
				}
				return true;
			}
		}
		return false;
	}
}
