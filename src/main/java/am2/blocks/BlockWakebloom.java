package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import java.util.ArrayList;
import java.util.List;

public class BlockWakebloom extends AMFlower{

	protected BlockWakebloom(){
		super();
	}

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Water;
    }

    @Override
	protected boolean canPlaceBlockOn(Block block){
		return block == Blocks.water;
	}

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return pos.getY() >= 0 && pos.getY() < 256 ? world.getBlockState(pos.down()).getBlock().getMaterial() == Material.water && world.getBlockState(pos.down()).getBlock().getMetaFromState(world.getBlockState(pos)) == 0 : false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(BlocksCommonProxy.wakebloom));
        return drops;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(BlocksCommonProxy.wakebloom);
    }
}
