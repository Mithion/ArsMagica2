package am2.blocks;

import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import java.util.List;
import java.util.Random;

public class WitchwoodSlabs extends BlockWoodSlab{
	private boolean isDouble;

	public WitchwoodSlabs(boolean par2){
		super();
		this.setHardness(2.0f);
		this.setResistance(2.0f);
		this.setHarvestLevel("axe", 2);
		this.isDouble = par2;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		Item drops = new ItemStack(BlocksCommonProxy.witchwoodSingleSlab).getItem();
		return drops;
	}

	@Override
	protected ItemStack createStackedBlock(IBlockState state) {
		return new ItemStack(BlocksCommonProxy.witchwoodSingleSlab, 2);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this));
	}

	@Override
	public boolean isDouble() {
		return isDouble;
	}
}
