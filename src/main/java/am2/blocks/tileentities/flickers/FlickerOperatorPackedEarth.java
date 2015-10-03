package am2.blocks.tileentities.flickers;

import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.items.ItemsCommonProxy;
import am2.utility.InventoryUtilities;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FlickerOperatorPackedEarth implements IFlickerFunctionality{

	@Override
	public boolean RequiresPower(){
		return false;
	}

	@Override
	public int PowerPerOperation(){
		return 10;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered){
		int searchesPerLoop = 12;

		int radius = 6;
		int diameter = radius * 2 + 1;

		if (!worldObj.isRemote){

			boolean actionPerformed = false;
			for (int i = 0; i < searchesPerLoop && !actionPerformed; ++i){
				TileEntity te = worldObj.getTileEntity(((TileEntity)habitat).xCoord, ((TileEntity)habitat).yCoord - 1, ((TileEntity)habitat).zCoord);
				if (te == null || !(te instanceof IInventory)){
					return false;
				}

				int effectX = ((TileEntity)habitat).xCoord - radius + (worldObj.rand.nextInt(diameter));
				int effectZ = ((TileEntity)habitat).zCoord - radius + (worldObj.rand.nextInt(diameter));
				int effectY = ((TileEntity)habitat).yCoord - 1 - worldObj.rand.nextInt(radius);

				if (effectY < 3)
					effectY = 3;

				Block block = worldObj.getBlock(effectX, effectY, effectZ);

				if (worldObj.isAirBlock(effectX, effectY, effectZ) || block.isReplaceable(worldObj, effectX, effectY, effectZ)){
					int inventoryIndex = InventoryUtilities.getFirstBlockInInventory((IInventory)te);
					if (inventoryIndex > -1){
						ItemStack stack = ((IInventory)te).getStackInSlot(inventoryIndex);
						InventoryUtilities.decrementStackQuantity((IInventory)te, inventoryIndex, 1);
						worldObj.setBlock(effectX, effectY, effectZ, Block.getBlockFromItem(stack.getItem()), stack.getMetadata(), 2);
						actionPerformed = true;
					}
				}
			}
		}

		return true;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers){
		return DoOperation(worldObj, controller, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController controller, boolean powered){
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		return powered ? 1 : 20;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController controller, boolean powered, Affinity[] flickers){
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"DDD",
				"RFR",
				" E ",
				Character.valueOf('D'), Blocks.dirt,
				Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
				Character.valueOf('E'), new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_EARTH),
				Character.valueOf('F'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.EARTH.ordinal())

		};
	}

}
