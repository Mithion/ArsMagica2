package am2.blocks;

import am2.blocks.tileentities.TileEntityEssenceConduit;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockEssenceConduit extends AMSpecialRenderPoweredBlock{

	public BlockEssenceConduit(){
		super(Material.cloth);
		setHardness(3.0f);
		setBlockBounds(0.125f, 0.0f, 0.125f, 0.875f, 1.0f, 0.875f);
	}

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        int var10 = meta;
        var10 = -1;

        if (facing == EnumFacing.DOWN){
            var10 = 1;
        }

        if (facing == EnumFacing.UP){
            var10 = 2;
        }

        if (facing == EnumFacing.NORTH){
            var10 = 3;
        }

        if (facing == EnumFacing.SOUTH){
            var10 = 4;
        }

        if (facing == EnumFacing.WEST){
            var10 = 5;
        }

        if (facing == EnumFacing.EAST){
            var10 = 6;
        }

        return getStateFromMeta(var10);
    }

    @Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityEssenceConduit();
	}
}
