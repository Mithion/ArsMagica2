package am2.worldgen;

import am2.blocks.AMFlower;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenFlowers;

import java.util.Random;

public class AM2FlowerGen extends WorldGenFlowers{

	private AMFlower plantBlock;
	private int plantBlockMeta;

	public AM2FlowerGen(AMFlower block, int meta){
		super(block, BlockFlower.EnumFlowerType.POPPY);
		this.plantBlock = block;
		this.plantBlockMeta = meta;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, BlockPos pos){ // TODO test this when the mod compiles

		for (int l = 0; l < 8; ++l){
			BlockPos pos_ = pos.add(
					par2Random.nextInt(8) - par2Random.nextInt(8),
					par2Random.nextInt(4) - par2Random.nextInt(4),
					par2Random.nextInt(8) - par2Random.nextInt(8)
			);

			if (par1World.isAirBlock(pos_) && (!par1World.provider.getHasNoSky() || pos_.getY() < 255) && this.plantBlock.canGrowOn(par1World, pos_, par1World.getBlockState(pos_))){
				par1World.setBlockState(pos_, par1World.getBlockState(pos_).getBlock().getStateFromMeta(this.plantBlockMeta), 2);
			}
		}

		return true;
	}
}
