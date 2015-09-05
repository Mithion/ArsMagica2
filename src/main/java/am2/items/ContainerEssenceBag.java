package am2.items;

import am2.containers.slots.SlotEssenceOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerEssenceBag extends Container{
	private ItemStack bagStack;
	private InventoryEssenceBag essBagInventory;
	private int essenceBagSlot;
	public int specialSlotIndex;

	private static final int mainInventoryStart = 12;
	private static final int actionBarStart = 39;
	private static final int actionBarEnd = 47;

	public ContainerEssenceBag(InventoryPlayer inventoryplayer, ItemStack bagStack, InventoryEssenceBag inventoryBag){
		this.essBagInventory = inventoryBag;
		this.bagStack = bagStack;
		this.essenceBagSlot = inventoryplayer.currentItem;

		int slotIndex = 0;

		//rune slots
		for (int x = 0; x < 6; ++x){
			for (int y = 0; y < 2; ++y){
				addSlotToContainer(new SlotEssenceOnly(essBagInventory, slotIndex++, 26 + (x * 18), 8 + (y * 18)));
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
		ItemStack[] stack = new ItemStack[12];
		for (int i = 0; i < 12; ++i){
			stack[i] = ((Slot)inventorySlots.get(i)).getStack();
		}
		return stack;
	}

	@Override
	public void onContainerClosed(EntityPlayer entityplayer){
		World world = entityplayer.worldObj;

		if (!world.isRemote){
			ItemStack essenceBagItemStack = bagStack;
			ItemEssenceBag bag = (ItemEssenceBag)essenceBagItemStack.getItem();
			ItemStack[] items = GetFullInventory();
			bag.UpdateStackTagCompound(essenceBagItemStack, items);
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, essenceBagItemStack);
		}

		super.onContainerClosed(entityplayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return essBagInventory.isUseableByPlayer(entityplayer);
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
			}else if (i >= mainInventoryStart && i < actionBarStart) //range 27 - player inventory
			{
				if (!mergeItemStack(itemstack1, actionBarStart, actionBarEnd, false)){
					return null;
				}
			}else if (i >= actionBarStart && i < actionBarEnd) //range 9 - player action bar
			{
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
}
