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
	public int onBlockPlaced(World par1World, int par2, int par3, int par4, int facing, float par6, float par7, float par8, int meta){
		int var10 = meta;
		var10 = -1;

		if (facing == 0){
			var10 = 1;
		}

		if (facing == 1){
			var10 = 2;
		}

		if (facing == 2){
			var10 = 3;
		}

		if (facing == 3){
			var10 = 4;
		}

		if (facing == 4){
			var10 = 5;
		}

		if (facing == 5){
			var10 = 6;
		}

		return var10;
	}

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityEssenceConduit();
	}
}
