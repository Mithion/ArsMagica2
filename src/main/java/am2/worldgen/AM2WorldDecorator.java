package am2.worldgen;

import am2.AMCore;
import am2.blocks.BlocksCommonProxy;
import am2.entities.SpawnBlacklists;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE;

public class AM2WorldDecorator implements IWorldGenerator{

	//ores
	private final WorldGenMinable vinteum;
	private final WorldGenMinable blueTopaz;
	private final WorldGenMinable chimerite;
	private final WorldGenMinable sunstone;

	//flowers
	private final AM2FlowerGen blueOrchid;
	private final AM2FlowerGen desertNova;
	private final AM2FlowerGen wakebloom;
	private final AM2FlowerGen aum;
	private final AM2FlowerGen tarmaRoot;

	private ArrayList<Integer> dimensionBlacklist = new ArrayList<Integer>();


	//trees
	private final WitchwoodTreeHuge witchwoodTree;

	//pools
	private final AM2PoolGen pools;
	private final WorldGenEssenceLakes lakes;

	public AM2WorldDecorator(){

		for (int i : AMCore.config.getWorldgenBlacklist()){
			if (i == -1) continue;
			dimensionBlacklist.add(i);
		}

		vinteum = new WorldGenMinable(BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_VINTEUM_ORE, 4, Blocks.stone);
		chimerite = new WorldGenMinable(BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_CHIMERITE_ORE, 6, Blocks.stone);
		blueTopaz = new WorldGenMinable(BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_BLUE_TOPAZ_ORE, 6, Blocks.stone);
		sunstone = new WorldGenMinable(BlocksCommonProxy.AMOres, BlocksCommonProxy.AMOres.META_SUNSTONE_ORE, 3, Blocks.lava);

		blueOrchid = new AM2FlowerGen(BlocksCommonProxy.cerublossom, 0);
		desertNova = new AM2FlowerGen(BlocksCommonProxy.desertNova, 0);
		wakebloom = new AM2FlowerGen(BlocksCommonProxy.wakebloom, 0);
		aum = new AM2FlowerGen(BlocksCommonProxy.aum, 0);
		tarmaRoot = new AM2FlowerGen(BlocksCommonProxy.tarmaRoot, 0);

		witchwoodTree = new WitchwoodTreeHuge(true);

		pools = new AM2PoolGen();

		lakes = new WorldGenEssenceLakes(BlocksCommonProxy.liquidEssence);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){

		if (!SpawnBlacklists.worldgenCanHappenInDimension(world.provider.dimensionId))
			return;

		if (world.provider.terrainType == WorldType.FLAT) return;
		if (dimensionBlacklist.contains(world.provider.dimensionId)) return;
		switch (world.provider.dimensionId){
		case -1:
			generateNether(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			break;
		case 1:
			break;
		default:
			generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}

	public void generateNether(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
		generateOre(sunstone, 20, world, random, 5, 120, chunkX, chunkZ);
	}

	public void generateOverworld(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider){
		generateOre(vinteum, 6, world, random, 10, 45, chunkX, chunkZ);
		generateOre(chimerite, 8, world, random, 10, 80, chunkX, chunkZ);
		generateOre(blueTopaz, 8, world, random, 10, 80, chunkX, chunkZ);
		generateOre(sunstone, 20, world, random, 5, 120, chunkX, chunkZ);

		generateFlowers(blueOrchid, world, random, chunkX, chunkZ);
		generateFlowers(desertNova, world, random, chunkX, chunkZ);
		generateFlowers(tarmaRoot, world, random, chunkX, chunkZ);

		BiomeGenBase biome = world.getBiomeGenForCoords(chunkX << 4, chunkZ << 4);
		Type[] biomeTypes = BiomeDictionary.getTypesForBiome(biome);
		boolean typeValid = false;
		for (Type type : biomeTypes){
			if (type == Type.BEACH || type == Type.SWAMP || type == Type.JUNGLE || type == Type.PLAINS || type == Type.WATER){
				typeValid = true;
			}else if (type == Type.FROZEN){
				typeValid = false;
				break;
			}
		}

		if (biome != BiomeGenBase.ocean && typeValid && random.nextInt(10) < 7){
			generateFlowers(wakebloom, world, random, chunkX, chunkZ);
		}

		if (random.nextInt(35) == 0){
			generateTree(witchwoodTree, world, random, chunkX, chunkZ);
		}

		if (random.nextInt(25) == 0){
			generatePools(world, random, chunkX, chunkZ);
		}

		if ((BiomeDictionary.isBiomeOfType(biome, Type.MAGICAL) || BiomeDictionary.isBiomeOfType(biome, Type.FOREST)) && random.nextInt(4) == 0 && TerrainGen.populate(chunkProvider, world, random, chunkX, chunkZ, true, LAKE)){
			int lakeGenX = (chunkX * 16) + random.nextInt(16) + 8;
			int lakeGenY = random.nextInt(128);
			int lakeGenZ = (chunkZ * 16) + random.nextInt(16) + 8;
			(new WorldGenEssenceLakes(BlocksCommonProxy.liquidEssence)).generate(world, random, lakeGenX, lakeGenY, lakeGenZ);
		}
	}

	private void generateFlowers(AM2FlowerGen flowers, World world, Random random, int chunkX, int chunkZ){
		int x = (chunkX << 4) + random.nextInt(16) + 8;
		int y = random.nextInt(128);
		int z = (chunkZ << 4) + random.nextInt(16) + 8;

		flowers.generate(world, random, x, y, z);
	}

	private void generateOre(WorldGenMinable mineable, int amount, World world, Random random, int minY, int maxY, int chunkX, int chunkZ){
		for (int i = 0; i < amount; ++i){
			int x = (chunkX << 4) + random.nextInt(16);
			int y = random.nextInt(maxY - minY) + minY;
			int z = (chunkZ << 4) + random.nextInt(16);

			mineable.generate(world, random, x, y, z);
		}
	}

	private void generateTree(WorldGenerator trees, World world, Random random, int chunkX, int chunkZ){
		int x = (chunkX * 16) + random.nextInt(16);
		int z = (chunkZ * 16) + random.nextInt(16);
		int y = world.getHeightValue(x, z);

		if (new WitchwoodTreeHuge(true).generate(world, random, x, y, z)){
			aum.generate(world, random, x, y, z);
		}
	}

	private void generatePools(World world, Random random, int chunkX, int chunkZ){
		int x = (chunkX * 16) + random.nextInt(16);
		int z = (chunkZ * 16) + random.nextInt(16);
		int y = world.getHeightValue(x, z);

		pools.generate(world, random, x, y, z);
	}
}
