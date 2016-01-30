package am2.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenEssenceLakes extends WorldGenerator{
	private Block targetBlock;

	public WorldGenEssenceLakes(Block par1){
		this.targetBlock = par1;
	}

	@Override
	public boolean generate(World par1World, Random par2Random, BlockPos pos){
		pos = pos.west(8);

		for (pos = pos.north(8); pos.getY() > 5 && par1World.isAirBlock(pos); pos = pos.down()){
			;
		}

		if (pos.getY() <= 4){
			return false;
		}else{
			pos = pos.down(4);
			boolean[] aboolean = new boolean[2048];
			int l = par2Random.nextInt(4) + 4;
			int i1;

			for (i1 = 0; i1 < l; ++i1){
				double d0 = par2Random.nextDouble() * 6.0D + 3.0D;
				double d1 = par2Random.nextDouble() * 4.0D + 2.0D;
				double d2 = par2Random.nextDouble() * 6.0D + 3.0D;
				double d3 = par2Random.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
				double d4 = par2Random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
				double d5 = par2Random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

				for (int j1 = 1; j1 < 15; ++j1){
					for (int k1 = 1; k1 < 15; ++k1){
						for (int l1 = 1; l1 < 7; ++l1){
							double d6 = (j1 - d3) / (d0 / 2.0D);
							double d7 = (l1 - d4) / (d1 / 2.0D);
							double d8 = (k1 - d5) / (d2 / 2.0D);
							double d9 = d6 * d6 + d7 * d7 + d8 * d8;

							if (d9 < 1.0D){
								aboolean[(j1 * 16 + k1) * 8 + l1] = true;
							}
						}
					}
				}
			}

			int i2;
			int j2;
			boolean flag;

			for (i1 = 0; i1 < 16; ++i1){
				for (j2 = 0; j2 < 16; ++j2){
					for (i2 = 0; i2 < 8; ++i2){
						flag = !aboolean[(i1 * 16 + j2) * 8 + i2] && (i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + i2] || i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + i2] || j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + i2] || j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + i2] || i2 < 7 && aboolean[(i1 * 16 + j2) * 8 + i2 + 1] || i2 > 0 && aboolean[(i1 * 16 + j2) * 8 + (i2 - 1)]);

						if (flag){
							Material material = par1World.getBlockState(pos.add(i1, i2, j2)).getBlock().getMaterial();

							if (i2 >= 4 && material.isLiquid()){
								return false;
							}

							if (i2 < 4 && !material.isSolid() && par1World.getBlockState(pos.add(i1, i2, j2)).getBlock() != this.targetBlock){
								return false;
							}
						}
					}
				}
			}

			for (i1 = 0; i1 < 16; ++i1){
				for (j2 = 0; j2 < 16; ++j2){
					for (i2 = 0; i2 < 8; ++i2){
						if (aboolean[(i1 * 16 + j2) * 8 + i2]){
							par1World.setBlockState(
									pos.add(i1, i2, j2),
									i2 >= 4 ? Blocks.air.getDefaultState() : this.targetBlock.getDefaultState(), // todo 1.8 targetblock state
									2);
						}
					}
				}
			}

			return true;
		}
	}
}
