package am2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import am2.blocks.tileentities.TileEntityEssenceConduit;

public class BlockEssenceConduit extends AMSpecialRenderPoweredBlock{
	
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
	
	public BlockEssenceConduit(){
		super(Material.cloth);
		setHardness(3.0f);
		setBlockBounds(0.125f, 0.0f, 0.125f, 0.875f, 1.0f, 0.875f);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing.getOpposite());
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityEssenceConduit();
	}
}
