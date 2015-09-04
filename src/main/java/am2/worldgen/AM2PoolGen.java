package am2.worldgen;

import am2.blocks.BlocksCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import java.util.Random;

public class AM2PoolGen{
	public void generate(World world, Random rand, int x, int y, int z){
		y = correctYCoord(world, x, y, z);
		if (y == -1) return;
		if (validPoolLocation(world, x, y, z)){
			world.setBlock(x, y, z, BlocksCommonProxy.liquidEssence);
			world.setBlock(x - 1, y, z, BlocksCommonProxy.liquidEssence);
			world.setBlock(x, y, z - 1, BlocksCommonProxy.liquidEssence);
			world.setBlock(x - 1, y, z - 1, BlocksCommonProxy.liquidEssence);

			world.setBlockToAir(x, y + 1, z);
			world.setBlockToAir(x - 1, y + 1, z);
			world.setBlockToAir(x, y + 1, z - 1);
			world.setBlockToAir(x - 1, y + 1, z - 1);

			//FMLLog.info("Generated pool at: %d %d %d", x, y ,z);
		}
	}

	private int correctYCoord(World world, int x, int y, int z){
		while (y > 0 && world.isAirBlock(x, y, z)){
			y--;
		}
		if (y <= 0) return -1;

		while (y < world.provider.getActualHeight() && !world.isAirBlock(x, y + 1, z)){
			y++;
		}
		if (y > world.provider.getActualHeight()) return -1;

		return y;
	}

	private boolean validPoolLocation(World world, int x, int y, int z){

		if (!biomeIsValid(world, x, y, z)) return false;

		int radius = 2;

		Block requiredBlock = world.getBiomeGenForCoords(x, z).topBlock;
		Block alternateBlock = world.getBiomeGenForCoords(x, z).fillerBlock;

		if (requiredBlock == null || alternateBlock == null) return false;

		for (int i = -radius; i < radius; ++i){
			for (int k = -radius; k < radius; ++k){
				Block blockBelow = world.getBlock(x + i, y - 1, z + k);
				Block block = world.getBlock(x + i, y, z + k);

				if (blockBelow == null || block == null) return false;
				if (!blockBelow.isOpaqueCube()) return false;
				if (block != requiredBlock && block != alternateBlock) return false;

				Block blockAbove = world.getBlock(x, y + 1, z);
				if (blockAbove == null) continue;
				if (blockAbove.isOpaqueCube()) return false;
				if (blockAbove.getMaterial().blocksMovement()) return false;
			}
		}

		return true;
	}

	private boolean biomeIsValid(World world, int x, int y, int z){
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		Type[] types = BiomeDictionary.getTypesForBiome(biome);

		for (Type type : types){
			if (type == Type.END || type == Type.MUSHROOM || type == Type.NETHER || type == Type.WATER || type == Type.WASTELAND){
				return false;
			}
		}

		return true;
	}
}
