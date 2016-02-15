package am2.containers;

import am2.api.spell.ItemSpellBase;
import am2.blocks.tileentities.TileEntitySummoner;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotFocusOnly;
import am2.containers.slots.SlotGhostRune;
import am2.containers.slots.SlotOneItemClassOnly;
import am2.items.ItemFocus;
import am2.items.ItemFocusCharge;
import am2.items.ItemFocusMana;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerSummoner extends AM2Container{
	private TileEntitySummoner summoner;

	private static final int PLAYER_INVENTORY_START = 7;
	private static final int PLAYER_ACTION_BAR_START = 34;
	private static final int PLAYER_ACTION_BAR_END = 43;

	public ContainerSummoner(InventoryPlayer inventoryplayer, TileEntitySummoner summoner){
		this.summoner = summoner;
		//Crafting Grid
		addSlotToContainer(new SlotFocusOnly(summoner, 0, 62, 28)); //inventory, index, x, y
		addSlotToContainer(new SlotFocusOnly(summoner, 1, 80, 28));
		addSlotToContainer(new SlotFocusOnly(summoner, 2, 98, 28));
		addSlotToContainer(new SlotOneItemClassOnly(summoner, 3, 80, 68, ItemSpellBase.class, 1));

		addSlotToContainer(new SlotGhostRune(summoner, 4, 62, 8));
		addSlotToContainer(new SlotGhostRune(summoner, 5, 80, 8));
		addSlotToContainer(new SlotGhostRune(summoner, 6, 98, 8));

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 163 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			addSlotToContainer(new Slot(inventoryplayer, j1, 8 + j1 * 18, 221));
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
		if (stack.getItem() instanceof ItemFocus){
			if (stack.getItem() instanceof ItemFocusCharge || stack.getItem() instanceof ItemFocusMana){

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
			}
		}else if (stack.getItem() instanceof ItemSpellBase){
			Slot scrollSlot = (Slot)inventorySlots.get(3);
			if (scrollSlot.getHasStack()) return false;

			ItemStack castStack = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
			if (stack.hasTagCompound()){
				castStack.setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
			}
			scrollSlot.putStack(castStack);
			scrollSlot.onSlotChanged();
			stack.stackSize--;
			if (stack.stackSize == 0){
				slot.putStack(null);
				slot.onSlotChanged();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return summoner.isUseableByPlayer(entityplayer);
	}
}
