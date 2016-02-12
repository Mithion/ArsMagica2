package am2.blocks;

import am2.items.ItemsCommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockWizardsChalk extends AMSpecialRenderBlock{

	public BlockWizardsChalk(){
		super(Material.circuits);
		setBlockBounds(0, 0, 0, 1, 0.02f, 1);
	}

    @Override
    public int getLightOpacity() {
        return 0;
    }

    @Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
	public float getAmbientOcclusionLightValue(){
		return 1.0f;
	}

	@Override
	public int getBlockColor(){
		return 0xFFFFFF;
	}

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ItemsCommonProxy.wizardChalk);
    }
}
