package am2.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenFlowers;

public class AM2FlowerGen extends WorldGenFlowers{

	private Block plantBlock;
	private int plantBlockMeta;

	public AM2FlowerGen(Block block, int meta) {
		super(block);
		this.plantBlock = block;
		this.plantBlockMeta = meta;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
		for (int l = 0; l < 8; ++l)
		{
			int i1 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
			int j1 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
			int k1 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);

			if (par1World.isAirBlock(i1, j1, k1) && (!par1World.provider.hasNoSky || j1 < 127) && this.plantBlock.canBlockStay(par1World, i1, j1, k1))
			{
				par1World.setBlock(i1, j1, k1, this.plantBlock, this.plantBlockMeta, 2);
			}
		}

		return true;
	}
}
