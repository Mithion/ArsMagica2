package am2.blocks;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class WitchwoodStairs extends BlockStairs{

	protected WitchwoodStairs(IBlockState state){
		super(state);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
		this.setHarvestLevel("axe", 2);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this));
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face){
		return 0;
	}
}
