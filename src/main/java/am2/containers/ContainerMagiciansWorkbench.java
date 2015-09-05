package am2.containers;

import am2.blocks.tileentities.TileEntityMagiciansWorkbench;
import am2.blocks.tileentities.TileEntityMagiciansWorkbench.RememberedRecipe;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotGhostRune;
import am2.containers.slots.SlotMagiciansWorkbenchCrafting;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;

public class ContainerMagiciansWorkbench extends AM2Container{

	private final TileEntityMagiciansWorkbench workbenchInventory;
	public InventoryCrafting firstCraftMatrix;
	public InventoryCrafting secondCraftMatrix;
	private boolean initializing;
	private final World world;

	private int INVENTORY_STORAGE_START = 20;
	private int PLAYER_INVENTORY_START = 47;
	private int PLAYER_ACTION_BAR_START = 74;
	private int PLAYER_ACTION_BAR_END = 83;

	public ContainerMagiciansWorkbench(InventoryPlayer playerInventory, TileEntityMagiciansWorkbench tileEntity){
		workbenchInventory = tileEntity;
		workbenchInventory.openInventory();

		world = playerInventory.player.worldObj;

		INVENTORY_STORAGE_START = tileEntity.getStorageStart() - 3;
		if (tileEntity.getUpgradeStatus(tileEntity.UPG_CRAFT))
			INVENTORY_STORAGE_START += 5;
		PLAYER_INVENTORY_START = INVENTORY_STORAGE_START + tileEntity.getStorageSize();
		PLAYER_ACTION_BAR_START = PLAYER_INVENTORY_START + 27;
		PLAYER_ACTION_BAR_END = PLAYER_ACTION_BAR_START + 9;

		firstCraftMatrix = new InventoryCrafting(this, 3, 3);
		secondCraftMatrix = tileEntity.getUpgradeStatus(tileEntity.UPG_CRAFT) ? new InventoryCrafting(this, 3, 3) : new InventoryCrafting(this, 2, 2);

		updateCraftingMatrices();

		int index = 0;
		//first crafting grid
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 3; k++){
				addSlotToContainer(new Slot(firstCraftMatrix, index++, 19 + k * 18, 29 + i * 18));
			}
		}
		//first output
		addSlotToContainer(new SlotMagiciansWorkbenchCrafting(playerInventory.player, firstCraftMatrix, tileEntity.firstCraftResult, this, 0, 37, 89));

		//second crafting grid
		index = 0;
		if (tileEntity.getUpgradeStatus(tileEntity.UPG_CRAFT)){
			for (int i = 0; i < 3; i++){
				for (int k = 0; k < 3; k++){
					addSlotToContainer(new Slot(secondCraftMatrix, index++, 93 + k * 18, 29 + i * 18));
				}
			}
		}else{
			for (int i = 0; i < 2; i++){
				for (int k = 0; k < 2; k++){
					addSlotToContainer(new Slot(secondCraftMatrix, index++, 102 + k * 18, 38 + i * 18));
				}
			}
		}

		//second output
		addSlotToContainer(new SlotMagiciansWorkbenchCrafting(playerInventory.player, secondCraftMatrix, tileEntity.secondCraftResult, this, 0, 111, 89));

		index = 18;
		//storage slots
		for (int i = 0; i < 9; ++i){
			for (int k = 0; k < 3; ++k){
				addSlotToContainer(new Slot(tileEntity, index++, 167 + k * 18, 1 + i * 18));
			}
		}

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(playerInventory, k + i * 9 + 9, 20 + k * 18, 168 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			addSlotToContainer(new Slot(playerInventory, j1, 20 + j1 * 18, 226));
		}

		//keystone lockable slots
		addSlotToContainer(new SlotGhostRune(tileEntity, 45, 194, 177));
		addSlotToContainer(new SlotGhostRune(tileEntity, 46, 194, 195));
		addSlotToContainer(new SlotGhostRune(tileEntity, 47, 194, 213));
	}

	public void updateCraftingMatrices(){

		initializing = true;

		for (int i = 0; i < 9; ++i){
			firstCraftMatrix.setInventorySlotContents(i, workbenchInventory.getStackInSlot(i));
		}

		if (workbenchInventory.getUpgradeStatus(workbenchInventory.UPG_CRAFT)){
			for (int i = 9; i < 18; ++i){
				secondCraftMatrix.setInventorySlotContents(i - 9, workbenchInventory.getStackInSlot(i));
			}
		}else{
			for (int i = 9; i < 13; ++i){
				secondCraftMatrix.setInventorySlotContents(i - 9, workbenchInventory.getStackInSlot(i));
			}
		}

		initializing = false;
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1iInventory){
		this.workbenchInventory.firstCraftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.firstCraftMatrix, world));
		if (!initializing){
			for (int i = 0; i < 9; ++i){
				workbenchInventory.setInventorySlotContents(i, firstCraftMatrix.getStackInSlot(i));
			}
		}

		this.workbenchInventory.secondCraftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.secondCraftMatrix, world));
		if (!initializing){
			for (int i = 0; i < 9; ++i){
				workbenchInventory.setInventorySlotContents(i + 9, secondCraftMatrix.getStackInSlot(i));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return workbenchInventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer){
		workbenchInventory.closeInventory();
		super.onContainerClosed(par1EntityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i){
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(i);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (slot instanceof SlotMagiciansWorkbenchCrafting){
				if (!mergeItemStack(itemstack1, INVENTORY_STORAGE_START, PLAYER_ACTION_BAR_END, true)){
					return null;
				}
			}else if (i < INVENTORY_STORAGE_START){
				if (!mergeItemStack(itemstack1, INVENTORY_STORAGE_START, PLAYER_ACTION_BAR_END, true)){
					return null;
				}
			}else if (i >= INVENTORY_STORAGE_START && i < PLAYER_INVENTORY_START) //from player inventory
			{
				if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)){
					return null;
				}
			}else if (i >= PLAYER_INVENTORY_START && i < PLAYER_ACTION_BAR_START) //from player inventory
			{
				if (!mergeItemStack(itemstack1, INVENTORY_STORAGE_START, PLAYER_INVENTORY_START, false)){
					if (!mergeItemStack(itemstack1, PLAYER_ACTION_BAR_START, PLAYER_ACTION_BAR_END, false)){
						return null;
					}
				}
			}else if (i >= PLAYER_ACTION_BAR_START && i < PLAYER_ACTION_BAR_END){
				if (!mergeItemStack(itemstack1, INVENTORY_STORAGE_START, PLAYER_ACTION_BAR_START - 1, false)){
					return null;
				}
			}else if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END, false)){
				return null;
			}

			if (itemstack1.stackSize == 0){
				slot.putStack((ItemStack)null);
			}else{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize){
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}
		return itemstack;
	}

	public TileEntityMagiciansWorkbench getWorkbench(){
		return this.workbenchInventory;
	}

	public HashMap<ImmutablePair<Item, Integer>, Integer> getComponentCount(int recipeIndex){
		HashMap<ImmutablePair<Item, Integer>, Integer> componentCount = new HashMap<ImmutablePair<Item, Integer>, Integer>();
		RememberedRecipe recipe = this.workbenchInventory.getRememberedRecipeItems().get(recipeIndex);
		for (ItemStack stack : recipe.components){
			if (stack == null) continue;
			ImmutablePair<Item, Integer> pair = new ImmutablePair<Item, Integer>(stack.getItem(), stack.getItemDamage());
			if (componentCount.containsKey(pair)){
				int amt = componentCount.get(pair);
				amt++;
				componentCount.put(pair, amt);
			}else{
				componentCount.put(pair, 1);
			}
		}
		return componentCount;
	}

	public boolean hasComponents(int recipeIndex){
		HashMap<ImmutablePair<Item, Integer>, Integer> componentCount = this.getComponentCount(recipeIndex);

		boolean allComponentsPresent = true;
		for (ImmutablePair<Item, Integer> pair : componentCount.keySet()){
			Integer qty = componentCount.get(pair);
			if (qty == null) return false;
			allComponentsPresent &= hasComponent(new ItemStack(pair.left, 1, pair.right), qty);
		}

		return allComponentsPresent;
	}

	private boolean hasComponent(ItemStack component, int qty){
		int matchedQty = 0;
		for (int i = getWorkbench().getStorageStart() - 3; i < getWorkbench().getStorageStart() - 3 + getWorkbench().getStorageSize(); ++i){
			ItemStack stack = getWorkbench().getStackInSlot(i);
			if (stack != null && stack.isItemEqual(component))
				matchedQty += stack.stackSize;
			if (matchedQty >= qty)
				return true;
		}
		return false;
	}

	private void decrementStoredComponents(int recipeIndex){
		HashMap<ImmutablePair<Item, Integer>, Integer> componentCount = this.getComponentCount(recipeIndex);
		for (ImmutablePair<Item, Integer> pair : componentCount.keySet()){
			Integer qty = componentCount.get(pair);
			if (qty == null) return;
			decrementStoredComponent(new ItemStack(pair.left, 1, pair.right), qty);
		}
	}

	private void decrementStoredComponent(ItemStack component, int qty){
		int qtyLeft = qty;
		for (int i = INVENTORY_STORAGE_START; i < PLAYER_INVENTORY_START - 1; ++i){
			Slot slot = ((Slot)this.inventorySlots.get(i));
			ItemStack stack = slot.getStack();
			if (stack != null && stack.isItemEqual(component)){
				if (stack.stackSize > qtyLeft){
					stack.stackSize -= qtyLeft;
					slot.putStack(stack);
					slot.onSlotChanged();
					return;
				}else{
					qtyLeft -= stack.stackSize;
					slot.putStack(null);
					slot.onSlotChanged();
				}
			}
		}
	}

	private void setRecipeItemsToGrid(int recipeIndex){
		RememberedRecipe recipe = this.workbenchInventory.getRememberedRecipeItems().get(recipeIndex);
		int count = 0;
		if ((recipe.is2x2 || getWorkbench().getUpgradeStatus(getWorkbench().UPG_CRAFT)) && craftingGridIsEmpty(true)){
			for (ItemStack stack : recipe.components){
				Slot slot = this.getSlot(10 + count);
				if (stack != null){
					slot.putStack(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
				}else{
					slot.putStack(null);
				}
				slot.onSlotChanged();
				count++;
				if (recipe.is2x2 && count == 2)
					count++;
			}
		}else if (craftingGridIsEmpty(false)){
			for (ItemStack stack : recipe.components){
				Slot slot = this.getSlot(count);
				if (stack != null){
					slot.putStack(new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
				}else{
					slot.putStack(null);
				}
				slot.onSlotChanged();
				count++;
				if (recipe.is2x2 && count == 2)
					count++;
			}
		}
	}

	public boolean gridIsFreeFor(int recipeIndex){
		RememberedRecipe recipe = this.workbenchInventory.getRememberedRecipeItems().get(recipeIndex);
		if (recipe.components.length > 4 && !this.workbenchInventory.getUpgradeStatus(this.workbenchInventory.UPG_CRAFT)){
			return craftingGridIsEmpty(false);
		}

		return craftingGridIsEmpty(false) || craftingGridIsEmpty(true);
	}

	private boolean craftingGridIsEmpty(boolean second){
		if (!second){
			for (int i = 0; i < 9; ++i){
				if (getWorkbench().getStackInSlot(i) != null)
					return false;
			}
		}else{
			for (int i = 0; i < 9; ++i){
				if (getWorkbench().getStackInSlot(i + 9) != null)
					return false;
			}
		}
		return true;
	}

	public void moveRecipeToCraftingGrid(int recipeIndex){
		if (!gridIsFreeFor(recipeIndex) || isRecipeAlreadyInGrid(recipeIndex))
			return;
		if (world.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(recipeIndex);
			AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.SET_MAG_WORK_REC, writer.generate());
			return;
		}
		if (hasComponents(recipeIndex)){
			decrementStoredComponents(recipeIndex);
			setRecipeItemsToGrid(recipeIndex);
			updateCraftingMatrices();
			onCraftMatrixChanged(workbenchInventory);
			detectAndSendChanges();
		}
	}

	public boolean isRecipeAlreadyInGrid(int recipeIndex){
		RememberedRecipe recipe = this.workbenchInventory.getRememberedRecipeItems().get(recipeIndex);
		if (getWorkbench().firstCraftResult.getStackInSlot(0) != null && getWorkbench().firstCraftResult.getStackInSlot(0).isItemEqual(recipe.output))
			return true;
		if (getWorkbench().secondCraftResult.getStackInSlot(0) != null && getWorkbench().secondCraftResult.getStackInSlot(0).isItemEqual(recipe.output))
			return true;
		return false;
	}
}
