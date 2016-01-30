package am2.utility;

import am2.AMCore;
import am2.entities.EntityBroomInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;

public class InventoryUtilities{
	public static int decrementStackQuantity(IInventory inventory, int slotIndex, int quantity){
		int deducted = 0;
		ItemStack stack = inventory.getStackInSlot(slotIndex);
		if (stack != null){
			if (stack.stackSize < quantity)
				quantity = stack.stackSize;
			stack.stackSize -= quantity;
			deducted = quantity;
			if (stack.stackSize <= 0){
				inventory.setInventorySlotContents(slotIndex, null);
			}
		}
		return deducted;
	}

	public static boolean mergeIntoInventory(IInventory inventory, ItemStack toMerge){
		return mergeIntoInventory(inventory, toMerge, toMerge.stackSize);
	}

	public static boolean mergeIntoInventory(IInventory inventory, ItemStack toMerge, int quantity){

		if (quantity > toMerge.stackSize)
			quantity = toMerge.stackSize;

		int qty = quantity;
		int emptySlot = -1;
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			if (!inventory.isItemValidForSlot(i, toMerge))
				continue;
			ItemStack inventoryStack = inventory.getStackInSlot(i);
			if (inventoryStack == null){
				if (emptySlot == -1)
					emptySlot = i;
				continue;
			}
			if (compareItemStacks(inventoryStack, toMerge, true, false, true, true)){
				if (inventoryStack.stackSize == inventoryStack.getMaxStackSize())
					continue;
				if (inventoryStack.getMaxStackSize() - inventoryStack.stackSize >= qty){
					inventoryStack.stackSize += qty;
					toMerge.stackSize -= qty;
					return true;
				}else{
					qty -= (inventoryStack.getMaxStackSize() - inventoryStack.stackSize);
					inventoryStack.stackSize = inventoryStack.getMaxStackSize();
				}
			}
		}

		if (qty > 0 && emptySlot > -1){
			ItemStack temp = toMerge.copy();
			temp.stackSize = qty;
			inventory.setInventorySlotContents(emptySlot, temp);
			toMerge.stackSize -= qty;
			return true;
		}

		toMerge.stackSize = qty;

		return false;
	}

	public static boolean mergeIntoInventory(IInventory inventory, ItemStack toMerge, int quantity, EnumFacing side){
		if (inventory instanceof ISidedInventory){
			ItemStack stack = toMerge.splitStack(Math.min(toMerge.stackSize, quantity));
			ISidedInventory sidedInventory = (ISidedInventory)inventory;
			int[] slots = sidedInventory.getSlotsForFace(side);
			boolean flag = false;

			for (int i = 0; i < slots.length && stack != null && stack.stackSize > 0; ++i){
				//For each slot that can be accessed from this side
				ItemStack prvStack = sidedInventory.getStackInSlot(slots[i]);
				if (InventoryUtilities.canInsertItemToInventory(sidedInventory, stack, slots[i], side)){
					//if the items can be inserted into the current slot
					if (prvStack == null){
						//if the stack in the slot is null then get the max value that can be moved and transfer the stack to the inventory
						int max = Math.min(stack.getMaxStackSize(), sidedInventory.getInventoryStackLimit());
						if (max >= stack.stackSize){
							sidedInventory.setInventorySlotContents(slots[i], stack.copy());
							stack.stackSize = 0;
							flag = true;
						}else{
							sidedInventory.setInventorySlotContents(slots[i], stack.splitStack(max));
							flag = true;
						}
					}else if (InventoryUtilities.canStacksMerge(prvStack, stack)){
						//if the stack in the slot can be merged with the stack we are trying to move get the max items that can exist in the slot
						//and insert as many as will fit from the stack we are trying to move
						int max = Math.min(stack.getMaxStackSize(), sidedInventory.getInventoryStackLimit());
						if (max > prvStack.stackSize){
							int qty = Math.min(stack.stackSize, max - prvStack.stackSize);
							prvStack.stackSize += qty;
							stack.stackSize -= qty;
							flag = qty > 0;
						}
					}
				}
			}

			toMerge.stackSize = toMerge.stackSize + stack.stackSize;
			return flag;
		}else{
			return mergeIntoInventory(inventory, toMerge, quantity);
		}
	}

	public static boolean deductFromInventory(IInventory inventory, ItemStack search, int quantity){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack inventoryStack = inventory.getStackInSlot(i);
			if (inventoryStack == null) continue;
			if (compareItemStacks(inventoryStack, search, true, false, true, true)){
				if (search.stackSize <= 0){
					inventory.setInventorySlotContents(i, null);
					return true;
				}else{
					quantity -= decrementStackQuantity(inventory, i, quantity);
					if (quantity <= 0){
						if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).stackSize <= 0){
							inventory.setInventorySlotContents(i, null);
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean inventoryHasItem(IInventory inventory, ItemStack search, int quantity){
		int qtyFound = 0;
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack inventoryStack = inventory.getStackInSlot(i);
			if (inventoryStack == null) continue;
			if (compareItemStacks(inventoryStack, search, true, false, true, true)){
				qtyFound += inventoryStack.stackSize;
				if (qtyFound >= quantity)
					return true;
			}
		}
		return false;
	}

	public static boolean inventoryHasItem(IInventory inventory, ItemStack search, int quantity, EnumFacing side){
		if (inventory instanceof ISidedInventory){
			ISidedInventory sidedInventory = (ISidedInventory)inventory;
			int qtyFound = 0;
			int[] slots = sidedInventory.getSlotsForFace(side);

			for (int i = 0; i < slots.length; i++){
				ItemStack inventoryStack = inventory.getStackInSlot(slots[i]);
				if (inventoryStack == null)
					continue;
				else if (compareItemStacks(inventoryStack, search, true, false, true, true)){
					qtyFound += inventoryStack.stackSize;
					if (qtyFound >= quantity)
						return true;
				}
			}

			return false;
		}else{
			return inventoryHasItem(inventory, search, quantity);
		}
	}

	public static int getFirstBlockInInventory(IInventory inventory){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack inventoryStack = inventory.getStackInSlot(i);
			if (inventoryStack == null) continue;
			if (inventoryStack.getItem() instanceof ItemBlock)
				return i;
		}
		return -1;
	}

	public static boolean isInventoryFull(IInventory inventory){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack inventoryStack = inventory.getStackInSlot(i);
			if (inventoryStack == null) return false;
		}
		return true;
	}

	/**
	 * Returns true if any items from source can be merged into dest.
	 */
	public static boolean canMergeHappen(IInventory source, IInventory dest){
		for (int i = 0; i < source.getSizeInventory(); ++i){
			if (source.getStackInSlot(i) == null) continue;
			if (inventoryHasRoomFor(dest, source.getStackInSlot(i))){
				return true;
			}
		}
		return false;
	}

	public static boolean isInventoryEmpty(IInventory inventory){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack inventoryStack = inventory.getStackInSlot(i);
			if (inventoryStack != null) return false;
		}
		return true;
	}

	public static boolean inventoryHasRoomFor(IInventory inventory, ItemStack stack){
		return inventoryHasRoomFor(inventory, stack, stack.stackSize);
	}

	public static boolean inventoryHasRoomFor(IInventory inventory, ItemStack stack, int qty){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack invStack = inventory.getStackInSlot(i);
			if (invStack == null)
				return true;
			if (compareItemStacks(invStack, stack, true, false, true, true) && invStack.getMaxStackSize() - invStack.stackSize >= qty)
				return true;
		}
		return false;
	}

	public static boolean inventoryHasRoomFor(IInventory inventory, ItemStack stack, int qty, EnumFacing side){
		if (inventory instanceof ISidedInventory){
			ISidedInventory sidedInventory = (ISidedInventory)inventory;
			int[] slots = sidedInventory.getSlotsForFace(side);

			for (int i = 0; i < slots.length; i++){
				ItemStack invStack = inventory.getStackInSlot(slots[i]);
				if (invStack == null)
					return true;
				if (compareItemStacks(invStack, stack, true, false, true, true) && invStack.getMaxStackSize() - invStack.stackSize >= qty)
					return true;
			}

			return false;
		}else{
			return inventoryHasRoomFor(inventory, stack, qty);
		}
	}

	public static ItemStack getFirstStackInInventory(EntityBroomInventory inventory){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack invStack = inventory.getStackInSlot(i);
			if (invStack != null){
				return invStack;
			}
		}
		return null;
	}

	public static int getInventorySlotIndexFor(IInventory inventory, Item item){
		return getInventorySlotIndexFor(inventory, item, Short.MAX_VALUE);
	}

	public static int getInventorySlotIndexFor(IInventory inventory, Item item, int metadata){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && stack.getItem() == item && (stack.getItemDamage() == metadata || metadata == Short.MAX_VALUE))
				return i;
		}
		return -1;
	}

	public static int getInventorySlotIndexFor(IInventory inventory, ItemStack search){
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack stack = inventory.getStackInSlot(i);
			if (stack != null && compareItemStacks(stack, search, false, false, true, true))
				return i;
		}
		return -1;
	}

	public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2){
		if (stack1 == null || stack2 == null)
			return false;

		if (!compareItemStacks(stack1, stack2, true, false, true, true))
			return false;

		return true;
	}

	public static int getLikeItemCount(IInventory inventory, ItemStack stack){
		int totalCount = 0;
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack invStack = inventory.getStackInSlot(i);
			if (invStack != null && compareItemStacks(invStack, stack, true, false, true, true))
				totalCount += invStack.stackSize;
		}

		return totalCount;
	}

	public static int getLikeItemCount(IInventory inventory, ItemStack stack, EnumFacing side){
		if (inventory instanceof ISidedInventory){
			int totalCount = 0;
			ISidedInventory sidedInventory = (ISidedInventory)inventory;
			int[] slots = sidedInventory.getSlotsForFace(side);

			for (int i = 0; i < slots.length; i++){
				ItemStack invStack = inventory.getStackInSlot(slots[i]);
				if (invStack != null && compareItemStacks(invStack, stack, true, false, true, true))
					totalCount += invStack.stackSize;
			}

			return totalCount;
		}else{
			return getLikeItemCount(inventory, stack);
		}
	}

	public static boolean canInsertItemToInventory(IInventory inventory, ItemStack itemStack, int slot, EnumFacing side){
		return !inventory.isItemValidForSlot(slot, itemStack) ? false : !(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).canInsertItem(slot, itemStack, side);
	}

	private static boolean canExtractItemFromInventory(IInventory inventory, ItemStack itemStack, int slot, EnumFacing side){
		return !(inventory instanceof ISidedInventory) || ((ISidedInventory)inventory).canExtractItem(slot, itemStack, side);
	}

	public static GetFirstStackStartingFromSlotResult getFirstStackStartingFromSlot(IInventory inventory, ItemStack itemStack, int slot){
		for (int i = slot; i < inventory.getSizeInventory(); i++){
			itemStack = inventory.getStackInSlot(i);
			if (itemStack != null){
				return new GetFirstStackStartingFromSlotResult(i, itemStack);
			}
		}

		return new GetFirstStackStartingFromSlotResult(-1, null);
	}

	public static GetFirstStackStartingFromSlotResult getFirstStackStartingFromSlot(IInventory inventory, ItemStack itemStack, int slot, EnumFacing side){
		if (inventory instanceof ISidedInventory){
			ISidedInventory sidededInventory = (ISidedInventory)inventory;
			int[] slots = sidededInventory.getSlotsForFace(side);

			for (int i = slot; i < slots.length; i++){
				itemStack = inventory.getStackInSlot(slots[i]);
				if (itemStack != null && canExtractItemFromInventory(sidededInventory, itemStack, slots[i], side)){
					return new GetFirstStackStartingFromSlotResult(i, itemStack);
				}
			}
		}else{
			return getFirstStackStartingFromSlot(inventory, itemStack, slot);
		}

		return new GetFirstStackStartingFromSlotResult(-1, null);
	}

	public static TileEntityChest getAdjacentChest(TileEntityChest chest){
		TileEntityChest adjacent = null;

		if (chest.adjacentChestXNeg != null)
			adjacent = chest.adjacentChestXNeg;
		else if (chest.adjacentChestXPos != null)
			adjacent = chest.adjacentChestXPos;
		else if (chest.adjacentChestZPos != null)
			adjacent = chest.adjacentChestZPos;
		else if (chest.adjacentChestZNeg != null)
			adjacent = chest.adjacentChestZNeg;

		return adjacent;
	}

	public static ItemStack replaceItem(ItemStack originalStack, Item newItem){
		ItemStack stack = new ItemStack(newItem, originalStack.stackSize, originalStack.getItemDamage());
		if (originalStack.hasTagCompound())
			stack.setTagCompound(originalStack.getTagCompound());
		return stack;
	}

	public static boolean compareItemStacks(ItemStack a, ItemStack b, boolean matchMeta, boolean matchStackSize, boolean matchNBT, boolean allowAnyMeta){
		if (a == null || b == null)
			return false;

		if (a.getItem() != b.getItem())
			return false;

		if (allowAnyMeta && !(a.getItemDamage() == b.getItemDamage() || a.getItemDamage() == AMCore.ANY_META || b.getItemDamage() == AMCore.ANY_META)){
			return false;
		}else if (matchMeta && a.getItemDamage() != b.getItemDamage()){
			return false;
		}

		if (matchStackSize && a.stackSize != b.stackSize)
			return false;

		if (matchNBT && !ItemStack.areItemStackTagsEqual(a, b))
			return false;

		return true;
	}
}
