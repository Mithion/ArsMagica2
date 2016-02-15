package am2.blocks;

import am2.texture.ResourceManager;
import am2.worldgen.AM2FlowerGen;
import am2.worldgen.WitchwoodTreeHuge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class WitchwoodSapling extends BlockFlower{

	protected WitchwoodSapling(){
		super();
	}

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote){
            super.updateTick(world, pos, state, rand);

            int nearbyEssence = countNearbyEssencePools(world, pos, rand);
            updateOrGrowTree(world, pos, rand, nearbyEssence);
        }
    }

    private void updateOrGrowTree(World world, int x, int y, int z, Random rand, int numNearbyPools){
		if (rand.nextInt(7) == 0){
			int meta = world.getBlockMetadata(x, y, z) + numNearbyPools;
			if (meta > 15)
				growTree(world, x, y, z, rand);
			else
				world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		}
	}

	private void growTree(World world, int x, int y, int z, Random rand){
		WitchwoodTreeHuge generator = new WitchwoodTreeHuge(true);

		world.setBlock(x, y, z, Blocks.air, 0, 4);
		if (!generator.generate(world, rand, x, y, z))
			world.setBlock(x, y, z, this, 15, 4);
		else
			new AM2FlowerGen(BlocksCommonProxy.aum, 0).generate(world, rand, x, y, z);
	}

	private int countNearbyEssencePools(World world, int x, int y, int z, Random rand){
		int essenceNearby = 0;

		for (int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++){
				Block block = world.getBlock(x + i, y - 1, z + j);
				int blockMeta = world.getBlockMetadata(x + i, y - 1, z + j);
				if (block == BlocksCommonProxy.liquidEssence && blockMeta == 0)
					essenceNearby++;
			}
		}

		return essenceNearby;
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list){
		list.add(new ItemStack(this));
	}
}
