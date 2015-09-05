package am2.containers.slots;

import am2.containers.ContainerMagiciansWorkbench;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotMagiciansWorkbenchCrafting extends Slot{

	/**
	 * The craft matrix inventory linked to this result slot.
	 */
	private final IInventory craftMatrix;

	/**
	 * The player that is using the GUI where this slot resides.
	 */
	private final EntityPlayer thePlayer;

	private final ContainerMagiciansWorkbench workbench;

	/**
	 * The number of items that have been crafted so far. Gets passed to ItemStack.onCrafting before being reset.
	 */
	private int amountCrafted;

	public SlotMagiciansWorkbenchCrafting(EntityPlayer player, IInventory craftMatrix, IInventory craftResult, ContainerMagiciansWorkbench workbench, int index, int x, int y){
		super(craftResult, index, x, y);
		this.thePlayer = player;
		this.craftMatrix = craftMatrix;
		this.workbench = workbench;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack stack){
		return false;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
	 * stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1){
		if (this.getHasStack()){
			this.amountCrafted += Math.min(par1, this.getStack().stackSize);
		}

		return super.decrStackSize(par1);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
	 * internal count then calls onCrafting(item).
	 */
	@Override
	protected void onCrafting(ItemStack par1ItemStack, int par2){
		this.amountCrafted += par2;
		this.onCrafting(par1ItemStack);
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
	 */
	@Override
	protected void onCrafting(ItemStack itemCrafted){
		itemCrafted.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
		this.amountCrafted = 0;

		ItemStack[] components = new ItemStack[this.craftMatrix.getSizeInventory()];
		for (int i = 0; i < components.length; ++i){
			if (this.craftMatrix.getStackInSlot(i) != null)
				components[i] = this.craftMatrix.getStackInSlot(i).copy();
			else
				components[i] = null;
		}

		this.workbench.getWorkbench().rememberRecipe(itemCrafted, components, this.craftMatrix.getSizeInventory() == 4);
	}

	@Override
	public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack){
		if (par1ItemStack != null && par2ItemStack != null){
			if (par1ItemStack.getItem() == par2ItemStack.getItem()){
				int i = par2ItemStack.stackSize - par1ItemStack.stackSize;

				if (i > 0){
					this.onCrafting(par1ItemStack, i);
					doComponentDecrements();
				}
			}
		}
	}

	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack){
		this.onCrafting(par2ItemStack);
		ItemCraftedEvent event = new ItemCraftedEvent(par1EntityPlayer, par2ItemStack, craftMatrix);
		MinecraftForge.EVENT_BUS.post(event);
		doComponentDecrements();
	}

	private void doComponentDecrements(){
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); ++i){
			ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);

			if (itemstack1 != null){
				if (itemstack1.stackSize > 1 || !searchAndDecrement(itemstack1)){
					doStandardDecrement(this.craftMatrix, itemstack1, i);
				}else{
					this.workbench.onCraftMatrixChanged(craftMatrix);
				}
			}
		}
	}

	private boolean searchAndDecrement(ItemStack itemstack1){
		for (int n = this.workbench.getWorkbench().getStorageStart(); n < this.workbench.getWorkbench().getStorageStart() + this.workbench.getWorkbench().getStorageSize(); ++n){
			ItemStack wbStack = workbench.getWorkbench().getStackInSlot(n);
			if (wbStack != null && itemstack1.isItemEqual(wbStack)){
				doStandardDecrement(workbench.getWorkbench(), wbStack, n);
				return true;
			}
		}
		return false;
	}

	private void doStandardDecrement(IInventory inventory, ItemStack itemstack1, int i){

		if (itemstack1.getItem().hasContainerItem(itemstack1)){
			ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

			if (itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage()){
				MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
				itemstack2 = null;
			}

			if (itemstack2 != null && (itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1))){
				if (inventory.getStackInSlot(i) == null){
					inventory.setInventorySlotContents(i, itemstack2);
				}else{
					if (itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1)){
						if (!this.thePlayer.inventory.addItemStackToInventory(itemstack2))
							this.thePlayer.dropItem(itemstack2.getItem(), itemstack2.getItemDamage());
					}
				}
			}
		}else{
			inventory.decrStackSize(i, 1);
		}
	}

}
