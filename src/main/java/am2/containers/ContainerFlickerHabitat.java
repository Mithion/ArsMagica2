/**
 *
 */
package am2.containers;

import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotSpecifiedItemsOnly;
import am2.items.ItemsCommonProxy;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Zero
 */
public class ContainerFlickerHabitat extends AM2Container{

	private TileEntityFlickerHabitat habitat;

	public ContainerFlickerHabitat(EntityPlayer player, TileEntityFlickerHabitat habitat){
		this.habitat = habitat;
		SlotSpecifiedItemsOnly slot;

		if (habitat.isUpgrade()){
			slot = new SlotSpecifiedItemsOnly(habitat, 0, 79, 47, ItemsCommonProxy.flickerJar);
		}else{
			slot = new SlotSpecifiedItemsOnly(habitat, 0, 79, 47, ItemsCommonProxy.flickerJar, ItemsCommonProxy.flickerFocus);
		}

		slot.setMaxStackSize(1);
		addSlotToContainer(slot);

		this.addPlayerInventory(player, 8, 84);
		this.addPlayerActionBar(player, 8, 143);

	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.Container#canInteractWith(net.minecraft.entity.player.EntityPlayer)
	 */
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return this.habitat.isUseableByPlayer(entityplayer);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer){
		super.onContainerClosed(par1EntityPlayer);
		this.habitat.closeInventory(par1EntityPlayer);
		this.habitat.updateOperator(habitat.getStackInSlot(0));
	}

}
