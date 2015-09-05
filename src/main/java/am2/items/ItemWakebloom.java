package am2.items;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemWakebloom extends ItemBlock{

	public ItemWakebloom(){
		super(BlocksCommonProxy.wakebloom);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

		if (movingobjectposition == null){
			return par1ItemStack;
		}else{
			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK){
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if (!par2World.canMineBlock(par3EntityPlayer, i, j, k)){
					return par1ItemStack;
				}

				if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack)){
					return par1ItemStack;
				}

				if (par2World.getBlock(i, j, k).getMaterial() == Material.water && par2World.getBlockMetadata(i, j, k) == 0 && par2World.isAirBlock(i, j + 1, k)){
					par2World.setBlock(i, j + 1, k, BlocksCommonProxy.wakebloom);

					if (!par3EntityPlayer.capabilities.isCreativeMode){
						--par1ItemStack.stackSize;
					}
				}
			}

			return par1ItemStack;
		}
	}

}
