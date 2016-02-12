package am2.items;

import am2.api.math.AMVector3;
import am2.entities.EntityBroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemMagicBroom extends ArsMagicaItem{

	public ItemMagicBroom(){
		super();
	}

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote){
            MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK){
                TileEntity te = world.getTileEntity(mop.getBlockPos());
                if (te instanceof IInventory){
                    EntityBroom broom = new EntityBroom(world);
                    broom.setPosition(player.posX, player.posY, player.posZ);
                    broom.setChestLocation(new AMVector3(mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ()));
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
}
