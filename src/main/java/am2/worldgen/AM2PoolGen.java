package am2.worldgen;

import am2.LogHelper;
import am2.blocks.BlocksCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.Random;

public class AM2PoolGen{
	public void generate(World world, Random rand, BlockPos pos){
		pos = correctYCoord(world, pos);
		if (pos.getY() == -1) return;
		if (validPoolLocation(world, pos)){
			world.setBlockState(pos, BlocksCommonProxy.liquidEssence.getDefaultState());
			world.setBlockState(pos.east(), BlocksCommonProxy.liquidEssence.getDefaultState());
			world.setBlockState(pos.north(), BlocksCommonProxy.liquidEssence.getDefaultState());
			world.setBlockState(pos.east().north(), BlocksCommonProxy.liquidEssence.getDefaultState());

			world.setBlockToAir(pos.up());
			world.setBlockToAir(pos.east().up());
			world.setBlockToAir(pos.north().up());
			world.setBlockToAir(pos.east().north().up());

			LogHelper.info("Generated pool at: %d %d %d", pos.getX(), pos.getY(), pos.getZ());
		}
	}

	private BlockPos correctYCoord(World world, BlockPos pos){
		while (pos.getY() > 0 && world.isAirBlock(pos)){
			pos = pos.down();
		}
		if (pos.getY() <= 0) return new BlockPos(pos.getX(), -1, pos.getZ());

		while (pos.getY() < world.provider.getActualHeight() && !world.isAirBlock(pos.up())){
			pos = pos.up();
		}
		if (pos.getY() > world.provider.getActualHeight()) return new BlockPos(pos.getX(), -1, pos.getZ());

		return pos;
	}

	private boolean validPoolLocation(World world, BlockPos pos){

		if (!biomeIsValid(world, pos)) return false;

		int radius = 2;

		IBlockState requiredBlock = world.getBiomeGenForCoords(pos).topBlock;
		IBlockState alternateBlock = world.getBiomeGenForCoords(pos).fillerBlock;

		if (requiredBlock == null || alternateBlock == null) return false;

		for (int i = -radius; i < radius; ++i){
			for (int k = -radius; k < radius; ++k){
				Block blockBelow = world.getBlockState(pos.add(i, -1, k)).getBlock();
				Block block = world.getBlockState(pos.add(i, 0, k)).getBlock();

				if (blockBelow == null || block == null) return false;
				if (!blockBelow.isOpaqueCube()) return false;
				if (block != requiredBlock && block != alternateBlock) return false;

				Block blockAbove = world.getBlockState(pos.up()).getBlock();
				if (blockAbove == null) continue;
				if (blockAbove.isOpaqueCube()) return false;
				if (blockAbove.getMaterial().blocksMovement()) return false;
			}
		}

		return true;
	}

	private boolean biomeIsValid(World world, BlockPos pos){
		BiomeGenBase biome = world.getBiomeGenForCoords(pos);
		Type[] types = BiomeDictionary.getTypesForBiome(biome);

		for (Type type : types){
			if (type == Type.END || type == Type.MUSHROOM || type == Type.NETHER || type == Type.WATER || type == Type.WASTELAND){
				return false;
			}
		}

		return true;
	}
}
