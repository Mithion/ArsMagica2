/**
 *
 */
package am2.containers;

import am2.blocks.BlockCrystalMarker;
import am2.blocks.tileentities.TileEntityCrystalMarker;
import am2.containers.slots.AM2Container;
import am2.containers.slots.SlotGhostItem;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Zero
 */
public class ContainerCrystalMarker extends AM2Container{

	private TileEntityCrystalMarker crystalMarker;

	public ContainerCrystalMarker(EntityPlayer player, TileEntityCrystalMarker crystalMarker){
		this.crystalMarker = crystalMarker;

		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				SlotGhostItem slot = new SlotGhostItem(crystalMarker, i + j * 3, 62 + i * 18, 19 + j * 18);

				if (this.crystalMarker.getMarkerType() == BlockCrystalMarker.META_REGULATE_EXPORT || this.crystalMarker.getMarkerType() == BlockCrystalMarker.META_REGULATE_MULTI){
					slot.setSlotStackLimit(128);
				}

				addSlotToContainer(slot);
			}
		}

		this.addPlayerInventory(player, 8, 84);
		this.addPlayerActionBar(player, 8, 142);

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer){
		return this.crystalMarker.isUseableByPlayer(entityplayer);
	}


}
