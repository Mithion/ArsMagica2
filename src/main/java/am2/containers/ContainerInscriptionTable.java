package am2.containers;

import am2.api.spell.component.interfaces.ISkillTreeEntry;
import am2.api.spell.component.interfaces.ISpellModifier;
import am2.api.spell.component.interfaces.ISpellPart;
import am2.api.spell.enums.SpellModifiers;
import am2.blocks.tileentities.TileEntityInscriptionTable;
import am2.containers.slots.SlotInscriptionTable;
import am2.spell.SpellValidator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;

import java.util.EnumSet;

public class ContainerInscriptionTable extends Container{

	private final TileEntityInscriptionTable table;
	private final InventoryPlayer inventoryPlayer;

	private static final int PLAYER_INVENTORY_START = 1;
	private static final int PLAYER_ACTION_BAR_START = 28;
	private static final int PLAYER_ACTION_BAR_END = 37;

	public ContainerInscriptionTable(TileEntityInscriptionTable table, InventoryPlayer inventoryplayer){
		this.table = table;
		this.inventoryPlayer = inventoryplayer;
		addSlotToContainer(new SlotInscriptionTable(table, 0, 102, 74));

		//display player inventory
		for (int i = 0; i < 3; i++){
			for (int k = 0; k < 9; k++){
				addSlotToContainer(new Slot(inventoryplayer, k + i * 9 + 9, 30 + k * 18, 170 + i * 18));
			}
		}

		//display player action bar
		for (int j1 = 0; j1 < 9; j1++){
			addSlotToContainer(new Slot(inventoryplayer, j1, 30 + j1 * 18, 228));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return table.isUseableByPlayer(entityplayer);
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
		if (stack.getItem() instanceof ItemWritableBook){
			Slot bookSlot = (Slot)inventorySlots.get(0);
			if (bookSlot.getHasStack()) return false;

			ItemStack newStack = stack.copy();
			newStack.stackSize = 1;
			bookSlot.putStack(newStack);
			bookSlot.onSlotChanged();

			stack.stackSize--;
			if (stack.stackSize == 0){
				slot.putStack(null);
				slot.onSlotChanged();
			}
			return true;
		}
		return false;
	}

	public int getCurrentRecipeSize(){
		return table.getCurrentRecipe().size();
	}

	public boolean currentRecipeContains(ISkillTreeEntry part){
		return table.getCurrentRecipe().contains(part);
	}

	public ISpellPart getRecipeItemAt(int index){
		return table.getCurrentRecipe().get(index);
	}

	public void removeMultipleRecipeParts(int startIndex, int length){
		table.removeMultipleSpellParts(startIndex, length);
	}

	public void removeSingleRecipePart(int index){
		table.removeSpellPart(index);
	}

	public void addRecipePart(ISpellPart part){
		table.addSpellPart(part);
	}

	public void addRecipePartToGroup(int groupIndex, ISpellPart part){
		table.addSpellPartToStageGroup(groupIndex, part);
	}

	public void removeSingleRecipePartFromGroup(int groupIndex, int index){
		table.removeSpellPartFromStageGroup(index, groupIndex);
	}

	public void removeMultipleRecipePartsFromGroup(int groupIndex, int startIndex, int length){
		table.removeMultipleSpellPartsFromStageGroup(startIndex, length, groupIndex);
	}

	public int getNumStageGroups(){
		return table.getNumStageGroups();
	}

	public int getShapeGroupSize(int groupIndex){
		return table.getShapeGroupSize(groupIndex);
	}

	public ISpellPart getShapeGroupPartAt(int groupIndex, int index){
		return table.getShapeGroupPartAt(groupIndex, index);
	}

	public void setSpellName(String name){
		table.setSpellName(name);
	}

	public String getSpellName(){
		return table.getSpellName();
	}

	public void giveSpellToPlayer(EntityPlayer player){
		table.createSpellForPlayer(player);
	}

	public boolean slotHasStack(int slot){
		return ((Slot)this.inventorySlots.get(slot)).getHasStack();
	}

	public boolean slotIsBook(int slot){
		return slotHasStack(slot) &&
				((Slot)this.inventorySlots.get(slot)).getStack().getItem() == Items.written_book &&
				((Slot)this.inventorySlots.get(slot)).getStack().getTagCompound() != null &&
				!((Slot)this.inventorySlots.get(slot)).getStack().getTagCompound().getBoolean("spellFinalized");
	}

	public SpellValidator.ValidationResult validateCurrentDefinition(){
		return table.currentRecipeIsValid();
	}

	public boolean modifierCanBeAdded(ISpellModifier modifier){
		EnumSet<SpellModifiers> modifiers = modifier.getAspectsModified();
		for (SpellModifiers mod : modifiers){
			if (table.getModifierCount(mod) > 2)
				return false;
		}
		return true;
	}

	public boolean currentSpellDefIsReadOnly(){
		return table.currentSpellDefIsReadOnly();
	}

	public void resetSpellNameAndIcon(){
		ItemStack stack = ((Slot)this.inventorySlots.get(0)).getStack();
		if (stack != null)
			table.resetSpellNameAndIcon(stack, inventoryPlayer.player);
		((Slot)(this.inventorySlots.get(0))).onSlotChanged();
		detectAndSendChanges();
	}
}
