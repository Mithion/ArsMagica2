package am2.utility;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

public class BlockUtilities{

	public static Fluid getFluid(Block block){
		if (block instanceof IFluidBlock){
			return ((IFluidBlock)block).getFluid();
		}else if (block == Blocks.water || block == Blocks.water){
			return FluidRegistry.WATER;
		}else if (block == Blocks.lava || block == Blocks.lava){
			return FluidRegistry.LAVA;
		}
		return null;
	}

	public static FluidStack drainBlock(World world, BlockPos pos, boolean doDrain){
		return drainBlock(world.getBlockState(pos).getBlock(), world, pos, doDrain);
	}

	public static FluidStack drainBlock(Block block, World world, BlockPos pos, boolean doDrain){
		if (block instanceof IFluidBlock){
			IFluidBlock fluidBlock = (IFluidBlock)block;
			if (fluidBlock.canDrain(world, pos))
				return fluidBlock.drain(world, pos, doDrain);
		}else if (block == Blocks.water || block == Blocks.water){
			int level = world.getBlockState(pos).getValue(BlockLiquid.LEVEL);
			if (level != 0)
				return null;
			if (doDrain)
				world.setBlockToAir(pos);
			return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
		}else if (block == Blocks.lava || block == Blocks.lava){
			int level = world.getBlockState(pos).getValue(BlockLiquid.LEVEL);
			if (level != 0)
				return null;
			if (doDrain)
				world.setBlockToAir(pos);
			return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
		}
		return null;
	}
}
