package am2.utility;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockUtilities {
	
	public static Fluid getFluid(Block block) {
		if (block instanceof IFluidBlock) {
			return ((IFluidBlock) block).getFluid();
		} else if (block == Blocks.water || block == Blocks.water) {
			return FluidRegistry.WATER;
		} else if (block == Blocks.lava || block == Blocks.lava) {
			return FluidRegistry.LAVA;
		}
		return null;
	}
	
	public static FluidStack drainBlock(World world, int x, int y, int z, boolean doDrain) {
		return drainBlock(world.getBlock(x, y, z), world, x, y, z, doDrain);
	}
	
	public static FluidStack drainBlock(Block block, World world, int x, int y, int z, boolean doDrain) {
		if (block instanceof IFluidBlock) {
			IFluidBlock fluidBlock = (IFluidBlock) block;
			if (fluidBlock.canDrain(world, x, y, z))
				return fluidBlock.drain(world, x, y, z, doDrain);
		} else if (block == Blocks.water || block == Blocks.water) {
			int meta = world.getBlockMetadata(x, y, z);
			if (meta != 0)
				return null;
			if (doDrain)
				world.setBlockToAir(x, y, z);
			return new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME);
		} else if (block == Blocks.lava || block == Blocks.lava) {
			int meta = world.getBlockMetadata(x, y, z);
			if (meta != 0)
				return null;
			if (doDrain)
				world.setBlockToAir(x, y, z);
			return new FluidStack(FluidRegistry.LAVA, FluidContainerRegistry.BUCKET_VOLUME);
		}
		return null;
	}
}
