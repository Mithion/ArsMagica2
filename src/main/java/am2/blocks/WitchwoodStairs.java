package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class WitchwoodStairs extends BlockStairs{

	protected WitchwoodStairs(Block par2Block, int par3){
		super(par2Block, par3);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this));
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face){
		return 0;
	}

	@Override
	public int getHarvestLevel(int metadata){
		return 2;
	}

	@Override
	public String getHarvestTool(int metadata){
		return "axe";
	}
}
