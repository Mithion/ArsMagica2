package am2.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import am2.blocks.tileentities.TileEntityArmorImbuer;
import am2.containers.slots.SlotArmorPiece;

public class ContainerArmorInfuser extends Container{

	private TileEntityArmorImbuer tileEntity;

	public ContainerArmorInfuser(EntityPlayer player, TileEntityArmorImbuer infuser)
	{
		this.tileEntity = infuser;
		//armor infusion slot
		addSlotToContainer(new Slot(infuser,0, 113, 197)); //inventory, index, x, y
		//player armor
		addSlotToContainer(new SlotArmorPiece(player.inventory,39, 86, 221, 0));
		addSlotToContainer(new SlotArmorPiece(player.inventory,38, 104, 221, 1));
		addSlotToContainer(new SlotArmorPiece(player.inventory,37, 122, 221, 2));
		addSlotToContainer(new SlotArmorPiece(player.inventory,36, 140, 221, 3));

		infuser.setCreativeModeAllowed(player.capabilities.isCreativeMode);
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileEntity.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		Slot slot = (Slot) this.inventorySlots.get(index);
		ItemStack stack = slot.getStack();
		if (stack == null)
			return null;
		if (stack.getItem() instanceof ItemArmor){
			ItemArmor armor = (ItemArmor)stack.getItem();
			if (index == 0){
				Slot playerSlot = (Slot) inventorySlots.get(armor.armorType + 1);
				if (!playerSlot.getHasStack()){
					ItemStack clone = stack.copy();
					playerSlot.putStack(clone);
					slot.putStack(null);
					playerSlot.onSlotChanged();
					slot.onSlotChanged();
					return clone;
				}
			}else{
				Slot armorSlot = (Slot) this.inventorySlots.get(0);
				if (!armorSlot.getHasStack()){
					ItemStack clone = stack.copy();
					armorSlot.putStack(clone);
					slot.putStack(null);
					armorSlot.onSlotChanged();
					slot.onSlotChanged();
					return clone;
				}
			}
		}

		return null;
	}

}
