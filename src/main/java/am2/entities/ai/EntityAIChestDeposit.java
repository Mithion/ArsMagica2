package am2.entities.ai;

import am2.api.math.AMVector3;
import am2.entities.EntityBroom;
import am2.playerextensions.ExtendedProperties;
import am2.utility.InventoryUtilities;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class EntityAIChestDeposit extends EntityAIBase{

	private EntityBroom host;
	private boolean isDepositing = false;
	private int depositCounter = 0;

	public EntityAIChestDeposit(EntityBroom host){
		this.host = host;
		this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute(){
		AMVector3 iLoc = host.getChestLocation();
		if (iLoc == null)
			return false;

		if (InventoryUtilities.isInventoryEmpty(host.getInventory()))
			return false;
		return !host.isInventoryEmpty() && host.isInventoryFull() || ExtendedProperties.For(host).getInanimateTarget() == null;
	}

	@Override
	public boolean continueExecuting(){
		return isDepositing || super.continueExecuting();
	}

	@Override
	public void resetTask(){
		isDepositing = false;
		depositCounter = 0;
	}

	@Override
	public void updateTask(){
		AMVector3 iLoc = host.getChestLocation();

		if (iLoc == null)
			return;

		TileEntity te = host.worldObj.getTileEntity((int)iLoc.x, (int)iLoc.y, (int)iLoc.z);
		if (te == null || !(te instanceof IInventory)) return;

		if (new AMVector3(host).distanceSqTo(iLoc) > 256){
			host.setPosition(iLoc.x, iLoc.y, iLoc.z);
			return;
		}

		if (new AMVector3(host).distanceSqTo(iLoc) > 9){
			host.getNavigator().tryMoveToXYZ(iLoc.x + 0.5, iLoc.y, iLoc.z + 0.5, 0.5f);
		}else{
			IInventory inventory = (IInventory)te;
			if (!isDepositing)
				inventory.openInventory();

			isDepositing = true;
			depositCounter++;

			if (depositCounter > 10){
				ItemStack mergeStack = InventoryUtilities.getFirstStackInInventory(host.getInventory()).copy();
				int originalSize = mergeStack.stackSize;
				if (!InventoryUtilities.mergeIntoInventory(inventory, mergeStack, 1)){
					if (te instanceof TileEntityChest){
						TileEntityChest chest = (TileEntityChest)te;
						TileEntityChest adjacent = null;
						if (chest.adjacentChestXNeg != null)
							adjacent = chest.adjacentChestXNeg;
						else if (chest.adjacentChestXPos != null)
							adjacent = chest.adjacentChestXPos;
						else if (chest.adjacentChestZPos != null)
							adjacent = chest.adjacentChestZPos;
						else if (chest.adjacentChestZNeg != null)
							adjacent = chest.adjacentChestZNeg;

						if (adjacent != null){
							InventoryUtilities.mergeIntoInventory(adjacent, mergeStack, 1);
						}
					}
				}
				InventoryUtilities.deductFromInventory(host.getInventory(), mergeStack, originalSize - mergeStack.stackSize);
			}


			if (depositCounter > 10 && (InventoryUtilities.isInventoryEmpty(host.getInventory()) || !InventoryUtilities.canMergeHappen(host.getInventory(), inventory))){
				inventory.closeInventory();
				resetTask();
			}
		}
	}

}
