package am2.items;

import am2.containers.slots.SlotRuneOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerRuneBag extends Container{
	private ItemStack bagStack;
	private InventoryRuneBag runeBagInventory;
	private int runeBagSlot;
	public int specialSlotIndex;

	private static final int mainInventoryStart = 16;
	private static final int actionBarStart = 43;
	private static final int actionBarEnd = 51;

	public ContainerRuneBag(InventoryPlayer inventoryplayer, ItemStack bagStack, InventoryRuneBag inventoryBag){
		this.runeBagInventory = inventoryBag;
		this.bagStack = bagStack;
		this.runeBagSlot = inventoryplayer.currentItem;

		int slotIndex = 0;

		//rune slots

		for (int x = 0; x < 8; ++x){
			for (int y = 0; y < 2; ++y){
				addSlotToContainer(new SlotRuneOnly(runeBagInventory, slotIndex++, 8 + (x * 18), 8 + (y * 18)));
			}
		}

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 58 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			if (inventoryplayer.getStackInSlot(j1) == bagStack){
				specialSlotIndex = j1 + 32;
				continue;
			}
			addSlotToContainer(new Slot(inventoryplayer, j1, 8 + j1 * 18, 116));
		}

	}

	public ItemStack[] GetFullInventory(){
		ItemStack[] stack = new ItemStack[InventoryRuneBag.inventorySize];
		for (int i = 0; i < InventoryRuneBag.inventorySize; ++i){
			stack[i] = ((Slot)inventorySlots.get(i)).getStack();
		}
		return stack;
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer){
		World world = entityplayer.worldObj;

		if (!world.isRemote){
			ItemStack runeBagItemStack = bagStack;
			ItemRuneBag bag = (ItemRuneBag)runeBagItemStack.getItem();
			ItemStack[] items = GetFullInventory();
			bag.UpdateStackTagCompound(runeBagItemStack, items);
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, runeBagItemStack);
		}

		super.onContainerClosed(entityplayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return runeBagInventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i){
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(i);

		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < mainInventoryStart){
				if (!mergeItemStack(itemstack1, mainInventoryStart, actionBarEnd, true)){
					return null;
				}
			}else if (i >= mainInventoryStart && i < actionBarStart) //player inventory
			{
				if (!mergeRunes(itemstack1, slot))
					return null;
				if (!mergeItemStack(itemstack1, actionBarStart, actionBarEnd, false)){
					return null;
				}
			}else if (i >= actionBarStart && i < actionBarEnd) //player action bar
			{
				if (!mergeRunes(itemstack1, slot))
					return null;
				if (!mergeItemStack(itemstack1, mainInventoryStart, actionBarStart - 1, false)){
					return null;
				}
			}else if (!mergeItemStack(itemstack1, mainInventoryStart, actionBarEnd, false)){
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

	private boolean mergeRunes(ItemStack itemstack1, Slot slot){
		if (itemstack1.getItem() == ItemsCommonProxy.rune){
			for (int j = 0; j < InventoryRuneBag.inventorySize; ++j){
				Slot runeSlot = ((Slot)inventorySlots.get(j));
				if (runeSlot.getHasStack()) continue;

				ItemStack rune = new ItemStack(ItemsCommonProxy.rune, 1, itemstack1.getMetadata());
				runeSlot.putStack(rune);

				itemstack1.stackSize--;

				if (itemstack1.stackSize <= 0){
					slot.putStack(null);
					slot.onSlotChanged();
				}
				return false;
			}
		}
		return false;
	}
}
