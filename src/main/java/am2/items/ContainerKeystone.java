package am2.items;

import am2.containers.slots.SlotRuneOnly;
import am2.items.ItemKeystone.KeystoneCombination;
import am2.network.AMDataWriter;
import am2.network.AMNetHandler;
import am2.network.AMPacketIDs;
import am2.utility.InventoryUtilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashMap;

public class ContainerKeystone extends Container{
	private final ItemStack keystoneStack;
	private final ItemStack runeBagStack;

	private final InventoryKeyStone keyStoneInventory;
	private final InventoryRuneBag runeBag;
	private final int keystoneSlot;
	public final int runebagSlot;
	private final EntityPlayer player;
	public int specialSlotIndex;

	private int PLAYER_INVENTORY_START = 3;
	private int PLAYER_ACTION_BAR_START = 30;
	private int PLAYER_ACTION_BAR_END = 38;

	public ContainerKeystone(InventoryPlayer inventoryplayer, ItemStack bookStack, ItemStack runeBagStack, InventoryKeyStone inventoryKeystone, InventoryRuneBag runeBag, int runeBagSlot){
		//addSlot(new Slot(spellBook,0, 21, 36)); //inventory, index, x, y
		this.keyStoneInventory = inventoryKeystone;
		this.keystoneStack = bookStack;
		this.keystoneSlot = inventoryplayer.currentItem;
		this.runeBag = runeBag;
		this.runebagSlot = runeBagSlot;
		this.runeBagStack = runeBagStack;
		player = inventoryplayer.player;

		int slotIndex = 0;

		//rune slots (clockwise)

		addSlotToContainer(new SlotRuneOnly(keyStoneInventory, slotIndex++, 80, 18));
		addSlotToContainer(new SlotRuneOnly(keyStoneInventory, slotIndex++, 91, 36));
		addSlotToContainer(new SlotRuneOnly(keyStoneInventory, slotIndex++, 69, 36));

		//storage slots
		if (this.runeBag != null){
			int runeSlotIndex = 0;
			for (int i = 0; i < 8; i++){
				for (int j = 0; j < 2; j++){
					addSlotToContainer(new SlotRuneOnly(this.runeBag, runeSlotIndex++, 8 + i * 18, 109 + j * 17));
				}
			}

			PLAYER_INVENTORY_START += 16;
			PLAYER_ACTION_BAR_START += 16;
			PLAYER_ACTION_BAR_END += 16;
		}

		int playerInventoryCounter = 0;

		int y = runebagSlot > -1 ? 216 : 179;

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			if (playerInventoryCounter++ == runeBagSlot)
				continue;
			if (inventoryplayer.getStackInSlot(j1) == bookStack){
				specialSlotIndex = j1 + 32;
				continue;
			}
			addSlotToContainer(new Slot(inventoryplayer, j1, 8 + j1 * 18, y));
		}

		y = runebagSlot > -1 ? 158 : 121;

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				if (playerInventoryCounter++ == runeBagSlot)
					continue;
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 8 + k * 18, y + i * 18));
			}
		}


	}

	public ItemStack[] GetFullKeystoneInventory(){
		ItemStack[] stack = new ItemStack[keyStoneInventory.inventorySize];
		for (int i = 0; i < keyStoneInventory.inventorySize; ++i){
			stack[i] = ((Slot)inventorySlots.get(i)).getStack();
		}
		return stack;
	}

	public ItemStack[] GetFullRuneBagInventory(){
		ItemStack[] stack = new ItemStack[runeBag.inventorySize];
		for (int i = keyStoneInventory.inventorySize; i < keyStoneInventory.inventorySize + runeBag.inventorySize; ++i){
			stack[i - keyStoneInventory.inventorySize] = ((Slot)inventorySlots.get(i)).getStack();
		}
		return stack;
	}


	@Override
	public void onContainerClosed(EntityPlayer entityplayer){

		World world = entityplayer.worldObj;

		if (!world.isRemote){
			ItemStack keyStoneItemStack = keystoneStack;
			ItemStack[] items = GetFullKeystoneInventory();
			ItemsCommonProxy.keystone.UpdateStackTagCompound(keyStoneItemStack, items);
			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, keyStoneItemStack);

			if (runeBagStack != null){
				ItemStack runebagItemStack = runeBagStack;
				items = GetFullRuneBagInventory();
				ItemsCommonProxy.runeBag.UpdateStackTagCompound(runebagItemStack, items);
				entityplayer.inventory.setInventorySlotContents(InventoryUtilities.getInventorySlotIndexFor(entityplayer.inventory, ItemsCommonProxy.runeBag), runebagItemStack);
			}
		}

		super.onContainerClosed(entityplayer);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return keyStoneInventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i){
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(i);

		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < PLAYER_INVENTORY_START){
				if (PLAYER_INVENTORY_START > InventoryKeyStone.inventorySize){
					if (i > InventoryKeyStone.inventorySize){
						for (int n = 0; n < InventoryKeyStone.inventorySize; n++){
							Slot runeSlot = (Slot)inventorySlots.get(n);
							if (runeSlot.getHasStack()) continue;

							runeSlot.putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
							runeSlot.onSlotChanged();
							itemstack1.stackSize--;
							if (itemstack1.stackSize == 0){
								slot.putStack(null);
								slot.onSlotChanged();
							}
							return null;
						}
					}else{
						for (int n = InventoryKeyStone.inventorySize; n < PLAYER_INVENTORY_START; n++){
							Slot runeSlot = (Slot)inventorySlots.get(n);
							if (runeSlot.getHasStack()) continue;

							runeSlot.putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
							runeSlot.onSlotChanged();
							itemstack1.stackSize--;
							if (itemstack1.stackSize == 0){
								slot.putStack(null);
								slot.onSlotChanged();
							}
							return null;
						}
					}
				}
				if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_END - 1, true)){
					return null;
				}
			}else if (i >= PLAYER_INVENTORY_START && i < PLAYER_ACTION_BAR_START) //range 27 - player inventory
			{
				if (itemstack.getItem() instanceof ItemRune){
					for (int n = 0; n < PLAYER_INVENTORY_START; n++){
						Slot runeSlot = (Slot)inventorySlots.get(n);
						if (runeSlot.getHasStack()) continue;

						runeSlot.putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
						runeSlot.onSlotChanged();
						itemstack1.stackSize--;
						if (itemstack1.stackSize == 0){
							slot.putStack(null);
							slot.onSlotChanged();
						}
						return null;
					}
				}
				if (!mergeItemStack(itemstack1, PLAYER_ACTION_BAR_START, PLAYER_ACTION_BAR_END - 1, false)){
					return null;
				}
			}else if (i >= PLAYER_ACTION_BAR_START && i < PLAYER_ACTION_BAR_END) //range 9 - player action bar
			{
				if (itemstack.getItem() instanceof ItemRune){
					for (int n = 0; n < PLAYER_INVENTORY_START; n++){
						Slot runeSlot = (Slot)inventorySlots.get(n);
						if (runeSlot.getHasStack()) continue;

						runeSlot.putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
						runeSlot.onSlotChanged();
						itemstack1.stackSize--;
						if (itemstack1.stackSize == 0){
							slot.putStack(null);
							slot.onSlotChanged();
						}
						return null;
					}
				}
				if (!mergeItemStack(itemstack1, PLAYER_INVENTORY_START, PLAYER_ACTION_BAR_START, false)){
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

	public KeystoneCombination getCurrentMatchedCombination(){
		int savedCombos = ItemsCommonProxy.keystone.numCombinations(keystoneStack);
		int[] curMeta = new int[InventoryKeyStone.inventorySize];

		for (int c = 0; c < InventoryKeyStone.inventorySize; ++c){
			ItemStack stack = keyStoneInventory.getStackInSlot(c);
			curMeta[c] = stack != null ? stack.getMetadata() : -1;
		}

		for (int i = 0; i < savedCombos; ++i){
			KeystoneCombination currentCombo = ItemsCommonProxy.keystone.getCombinationAt(keystoneStack, i);
			if (currentCombo.metas.length < InventoryKeyStone.inventorySize) continue;
			boolean match = true;
			for (int c = 0; c < InventoryKeyStone.inventorySize; ++c){
				match &= curMeta[c] == currentCombo.metas[c];
			}
			if (match)
				return currentCombo;
		}

		return null;
	}

	public ItemStack getKeystoneStack(){
		return keystoneStack;
	}

	public boolean setInventoryToCombination(int comboIndex){

		KeystoneCombination combo = ItemsCommonProxy.keystone.getCombinationAt(keystoneStack, comboIndex);
		if (combo == null) return false;

		if (!inventoryContainsAllMetas(combo.metas)) return false;

		if (player.worldObj.isRemote){
			AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.SET_KEYSTONE_COMBO, new AMDataWriter().add(comboIndex).generate());
			return true;
		}

		int matchIndex = 0;
		int searchIndex = 0;

		while (matchIndex < combo.metas.length && searchIndex < InventoryKeyStone.inventorySize + (runeBag != null ? InventoryRuneBag.inventorySize : 0)){
			IInventory searchInventory = searchIndex >= InventoryKeyStone.inventorySize ? runeBag : keyStoneInventory;
			int inventoryIndex = searchIndex >= InventoryKeyStone.inventorySize ? searchIndex - InventoryKeyStone.inventorySize : searchIndex;

			ItemStack stack = searchInventory.getStackInSlot(inventoryIndex);

			if (stack != null && stack.getMetadata() == combo.metas[matchIndex]){
				swapInventorySlots(keyStoneInventory, searchInventory, matchIndex, inventoryIndex);
				matchIndex++;
				searchIndex = matchIndex;
				continue;
			}else if (stack == null && combo.metas[matchIndex] == -1){
				swapInventorySlots(keyStoneInventory, searchInventory, matchIndex, inventoryIndex);
				matchIndex++;
				searchIndex = matchIndex;
				continue;
			}
			searchIndex++;
		}

		this.detectAndSendChanges();
		return true;
	}

	private void swapInventorySlots(IInventory firstInventory, IInventory secondInventory, int slot1, int slot2){

		if (firstInventory == secondInventory && slot1 == slot2)
			return;

		ItemStack stack1 = firstInventory.getStackInSlot(slot1);
		ItemStack stack2 = secondInventory.getStackInSlot(slot2);

		firstInventory.setInventorySlotContents(slot1, stack2);
		secondInventory.setInventorySlotContents(slot2, stack1);
	}

	private boolean inventoryContainsAllMetas(int[] metas){
		HashMap<Integer, Integer> metaQuantities = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> invQuantities = new HashMap<Integer, Integer>();

		for (int i : metas){
			if (i == -1) continue;
			if (metaQuantities.containsKey(i)){
				int qty = metaQuantities.get(i);
				metaQuantities.put(i, ++qty);
			}else{
				metaQuantities.put(i, 1);
			}
		}

		for (int i = 0; i < keyStoneInventory.getSizeInventory(); ++i){
			ItemStack stack = keyStoneInventory.getStackInSlot(i);
			if (stack == null) continue;
			int meta = stack.getMetadata();
			if (invQuantities.containsKey(meta)){
				int qty = invQuantities.get(meta);
				invQuantities.put(meta, ++qty);
			}else{
				invQuantities.put(meta, 1);
			}
		}

		if (runeBag != null){
			for (int i = 0; i < runeBag.getSizeInventory(); ++i){
				ItemStack stack = runeBag.getStackInSlot(i);
				if (stack == null) continue;
				int meta = stack.getMetadata();
				if (invQuantities.containsKey(meta)){
					int qty = invQuantities.get(meta);
					invQuantities.put(meta, ++qty);
				}else{
					invQuantities.put(meta, 1);
				}
			}
		}

		for (int i : metaQuantities.keySet()){
			if (!invQuantities.containsKey(i)) return false;
			if (invQuantities.get(i) < metaQuantities.get(i)) return false;
		}

		return true;
	}

}
