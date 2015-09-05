package am2.containers;

import am2.blocks.tileentities.TileEntityEssenceRefiner;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotEssenceRefiner;
import am2.containers.slots.SlotGhostRune;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerEssenceRefiner extends AM2Container{
	//public EssenceExtractorCrafting craftMatrix;
	public IInventory craftResult;

	private TileEntityEssenceRefiner essenceRefiner;

	private static final int PLAYER_INVENTORY_START = 9;
	private static final int PLAYER_ACTION_BAR_START = 36;
	private static final int PLAYER_ACTION_BAR_END = 45;

	public ContainerEssenceRefiner(InventoryPlayer inventoryplayer, TileEntityEssenceRefiner tileEntityEssenceRefiner){
		//craftMatrix = new EssenceExtractorCrafting(this);
		craftResult = new InventoryCraftResult();
		essenceRefiner = tileEntityEssenceRefiner;
		//Crafting Grid
		addSlotToContainer(new Slot(essenceRefiner, 0, 80, 41)); //inventory, index, x, y
		addSlotToContainer(new Slot(essenceRefiner, 1, 48, 73));
		addSlotToContainer(new Slot(essenceRefiner, 2, 80, 73));
		addSlotToContainer(new Slot(essenceRefiner, 3, 112, 74));
		addSlotToContainer(new Slot(essenceRefiner, 4, 80, 106));
		addSlotToContainer(new SlotEssenceRefiner(inventoryplayer.player, tileEntityEssenceRefiner, 5, 143, 109));

		addSlotToContainer(new SlotGhostRune(tileEntityEssenceRefiner, 6, 62, 8));
		addSlotToContainer(new SlotGhostRune(tileEntityEssenceRefiner, 7, 80, 8));
		addSlotToContainer(new SlotGhostRune(tileEntityEssenceRefiner, 8, 98, 8));

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, 150 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			addSlotToContainer(new Slot(inventoryplayer, j1, 8 + j1 * 18, 208));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return essenceRefiner.isUseableByPlayer(entityplayer);
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
		return false;
	}
}
