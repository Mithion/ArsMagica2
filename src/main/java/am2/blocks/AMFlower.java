package am2.blocks;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class AMFlower extends BlockFlower{

	protected AMFlower(){
		super();
		setStepSound(soundTypeGrass);
	}

    @Override
    public EnumFlowerColor getBlockType() {
        return null;
    }

    public AMFlower setUnlocalizedNameAndID(String name){
		setRegistryName(name);
        setUnlocalizedName(name);
		return this;
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list){
		list.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
	}

	public boolean canGrowOn(World worldIn, BlockPos pos, IBlockState state) {
		return canBlockStay(worldIn, pos, state);
	}
}
