package am2.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventorySpellBook implements IInventory{
	public static int inventorySize = 40;
	public static int activeInventorySize = 8;
	private ItemStack[] inventoryItems;

	public InventorySpellBook(){
		inventoryItems = new ItemStack[inventorySize];
	}

	public void SetInventoryContents(ItemStack[] inventoryContents){
		int loops = (int)Math.min(inventorySize, inventoryContents.length);
		for (int i = 0; i < loops; ++i){
			inventoryItems[i] = inventoryContents[i];
		}
	}

	@Override
	public int getSizeInventory(){
		return inventorySize;
	}

	@Override
	public ItemStack getStackInSlot(int i){
		if (i < 0 || i > inventoryItems.length - 1){
			return null;
		}
		return inventoryItems[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j){

		if (inventoryItems[i] != null){
			if (inventoryItems[i].stackSize <= j){
				ItemStack itemstack = inventoryItems[i];
				inventoryItems[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = inventoryItems[i].splitStack(j);
			if (inventoryItems[i].stackSize == 0){
				inventoryItems[i] = null;
			}
			return itemstack1;
		}else{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack){
		inventoryItems[i] = itemstack;
	}

	@Override
	public String getInventoryName(){
		return "Spell Book";
	}

	@Override
	public int getInventoryStackLimit(){
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer){
		return true;
	}

	@Override
	public void openInventory(){
	}

	@Override
	public void closeInventory(){
	}

	public ItemStack[] GetInventoryContents(){
		return inventoryItems;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i){
		if (inventoryItems[i] != null){
			ItemStack itemstack = inventoryItems[i];
			inventoryItems[i] = null;
			return itemstack;
		}else{
			return null;
		}
	}

	@Override
	public boolean hasCustomInventoryName(){
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack){
		return false;
	}

	@Override
	public void markDirty(){
	}


}









