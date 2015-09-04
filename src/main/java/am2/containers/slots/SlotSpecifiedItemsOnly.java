package am2.containers.slots;

import am2.CompoundKey;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class SlotSpecifiedItemsOnly extends Slot{

	private ArrayList<CompoundKey> acceptedItems;
	private int maxStackSize = 64;

	public SlotSpecifiedItemsOnly(IInventory par1iInventory, int par2, int par3, int par4, CompoundKey... validItemsAndMeta){
		super(par1iInventory, par2, par3, par4);

		this.acceptedItems = new ArrayList<CompoundKey>();

		for (CompoundKey c : validItemsAndMeta){
			this.acceptedItems.add(c);
		}
	}

	public SlotSpecifiedItemsOnly(IInventory par1iInventory, int par2, int par3, int par4, Item... validItems){
		super(par1iInventory, par2, par3, par4);

		this.acceptedItems = new ArrayList<CompoundKey>();

		for (Item i : validItems){
			this.acceptedItems.add(new CompoundKey(i, -1));
		}
	}

	public SlotSpecifiedItemsOnly(IInventory par1iInventory, int par2, int par3, int par4, ArrayList<Item> validItems){
		super(par1iInventory, par2, par3, par4);

		this.acceptedItems = new ArrayList<CompoundKey>();

		for (Item i : validItems){
			this.acceptedItems.add(new CompoundKey(i, -1));
		}
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack){
		for (CompoundKey c : this.acceptedItems){
			if (c.item == par1ItemStack.getItem() && (c.meta == -1 || c.meta == par1ItemStack.getItemDamage())){
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSlotStackLimit(){
		return this.maxStackSize;
	}

	public void setMaxStackSize(int amount){
		this.maxStackSize = amount;
	}

}
