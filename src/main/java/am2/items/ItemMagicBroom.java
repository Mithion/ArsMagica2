package am2.items;

import am2.api.math.AMVector3;
import am2.entities.EntityBroom;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemMagicBroom extends ArsMagicaItem{

	public ItemMagicBroom(){
		super();
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
		if (!world.isRemote){
			MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
			if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK){
				TileEntity te = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
				if (te instanceof IInventory){
					EntityBroom broom = new EntityBroom(world);
					broom.setPosition(player.posX, player.posY, player.posZ);
					broom.setChestLocation(new AMVector3(mop.blockX, mop.blockY, mop.blockZ));
					world.spawnEntityInWorld(broom);

					stack.stackSize--;

					if (stack.stackSize == 0){
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister){
	}
}
