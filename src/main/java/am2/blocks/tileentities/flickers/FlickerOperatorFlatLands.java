package am2.blocks.tileentities.flickers;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.spell.enums.Affinity;
import am2.blocks.BlocksCommonProxy;
import am2.items.ItemsCommonProxy;
import am2.utility.DummyEntityPlayer;

public class FlickerOperatorFlatLands implements IFlickerFunctionality{

	@Override
	public boolean RequiresPower() {
		return false;
	}

	@Override
	public int PowerPerOperation() {
		return 10;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered) {
		int searchesPerLoop = 12;

		int radius = 6;
		int diameter = radius*2+1;

		if (!worldObj.isRemote){

			boolean actionPerformed = false;

			for (int i = 0; i < searchesPerLoop && !actionPerformed; ++i){
				int effectX = ((TileEntity)habitat).xCoord - radius + (worldObj.rand.nextInt(diameter));
				int effectZ = ((TileEntity)habitat).zCoord - radius + (worldObj.rand.nextInt(diameter));
				int effectY = ((TileEntity)habitat).yCoord + worldObj.rand.nextInt(radius);

				if (effectX == ((TileEntity)habitat).xCoord && effectY == ((TileEntity)habitat).yCoord && effectZ == ((TileEntity)habitat).zCoord)
					return false;

				Block block = worldObj.getBlock(effectX, effectY, effectZ);
				int meta = worldObj.getBlockMetadata(effectX, effectY, effectZ);

				if (block != null && !worldObj.isAirBlock(effectX, effectY, effectZ) && block.isOpaqueCube() && block != BlocksCommonProxy.invisibleUtility){
					if (ForgeEventFactory.doPlayerHarvestCheck(new DummyEntityPlayer(worldObj), block, true)){
						if (block.removedByPlayer(worldObj, new DummyEntityPlayer(worldObj), effectX, effectY, effectZ)){
							block.onBlockDestroyedByPlayer(worldObj, effectX, effectY, effectZ, meta);
							block.dropBlockAsItem(worldObj, effectX, effectY, effectZ, meta, 0);
							if (!worldObj.isRemote)
								worldObj.playAuxSFX(2001, effectX, effectY, effectZ, Block.getIdFromBlock(block) + (worldObj.getBlockMetadata(effectX, effectY, effectZ) << 12));
							worldObj.func_147478_e(effectX, effectY, effectZ, true);
							actionPerformed = true;
						}
					}
				}
			}

			return actionPerformed;
		}else{
			return false;
		}
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers) {
		return DoOperation(worldObj, habitat, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered) {
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers) {
		return powered ? 1 : 20;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController habitat, boolean powered, Affinity[] flickers) {
	}


	@Override
	public Object[] getRecipe() {
		return new Object[]{
				"S P",
				"ENI",
				" R ",
				Character.valueOf('S'), Items.iron_shovel,
				Character.valueOf('P'), Items.iron_pickaxe,
				Character.valueOf('E'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.EARTH.ordinal()),
				Character.valueOf('N'), new ItemStack(ItemsCommonProxy.essence, 1, ItemsCommonProxy.essence.META_EARTH),
				Character.valueOf('I'), new ItemStack(ItemsCommonProxy.flickerJar, 1, Affinity.ICE.ordinal()),
				Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK)
			};
	}
}
