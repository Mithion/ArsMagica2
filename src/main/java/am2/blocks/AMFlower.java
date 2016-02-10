package am2.blocks;

import java.util.List;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class AMFlower extends BlockBush{

	protected AMFlower(){
		super();
		setStepSound(soundTypeGrass);
	}

	public AMFlower setUnlocalizedNameAndID(String name){
		setUnlocalizedName(name);
		return this;
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list){
		list.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
	}

	public boolean canGrowOn(World worldIn, BlockPos pos, IBlockState state) {
		return canBlockStay(worldIn, pos, state);
	}

}
