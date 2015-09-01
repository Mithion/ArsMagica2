/**
 *
 */
package am2.containers;

import net.minecraft.entity.player.EntityPlayer;
import am2.blocks.tileentities.TileEntityFlickerHabitat;
import am2.blocks.tileentities.TileEntityInertSpawner;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotSpecifiedItemsOnly;
import am2.items.ItemsCommonProxy;

/**
 * @author Mithion
 *
 */
public class ContainerInertSpawner extends AM2Container {

	private TileEntityInertSpawner spawner;

	public ContainerInertSpawner(EntityPlayer player, TileEntityInertSpawner habitat){
		this.spawner = habitat;
		SlotSpecifiedItemsOnly slot;

		slot = new SlotSpecifiedItemsOnly(habitat, 0, 79, 47, ItemsCommonProxy.crystalPhylactery);

		slot.setMaxStackSize(1);
		addSlotToContainer(slot);

		this.addPlayerInventory(player, 8, 84);
		this.addPlayerActionBar(player, 8, 143);

	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return this.spawner.isUseableByPlayer(entityplayer);
	}

}
