package am2.containers.slots;

import am2.api.spell.ItemSpellBase;
import am2.blocks.tileentities.TileEntityInscriptionTable;
import am2.items.ItemsCommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class SlotInscriptionTable extends Slot{

	public SlotInscriptionTable(TileEntityInscriptionTable par1iInventory, int par2, int par3, int par4){
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack){
		if (par1ItemStack == null || par1ItemStack.getItem() == null){
			return false;
		}
		Class clazz = par1ItemStack.getItem().getClass();
		if (par1ItemStack.getItem() == Items.written_book && (par1ItemStack.getTagCompound() == null || !par1ItemStack.getTagCompound().getBoolean("spellFinalized")))
			return true;
		else if (par1ItemStack.getItem() == Items.writable_book)
			return true;
		else if (par1ItemStack.getItem() == ItemsCommonProxy.spell)
			return true;
		return false;
	}

	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack){
		if (par2ItemStack.getItem() == Items.written_book)
			par2ItemStack = ((TileEntityInscriptionTable)this.inventory).writeRecipeAndDataToBook(par2ItemStack, par1EntityPlayer, "Spell Recipe");
		else
			((TileEntityInscriptionTable)this.inventory).clearCurrentRecipe();
		super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
	}

	@Override
	public void onSlotChanged(){
		if (this.getStack() != null){
			Class clazz = this.getStack().getItem().getClass();
			if (ItemSpellBase.class.isAssignableFrom(clazz)){
				((TileEntityInscriptionTable)this.inventory).reverseEngineerSpell(this.getStack());
			}
		}
		super.onSlotChanged();
	}

	@Override
	public void putStack(ItemStack stack){
		if (stack != null && stack.getItem() == Items.writable_book){
			stack.setItem(Items.written_book);
			stack.setStackDisplayName(StatCollector.translateToLocal("am2.tooltip.unfinishedSpellRecipe"));
		}
		super.putStack(stack);
	}
}
