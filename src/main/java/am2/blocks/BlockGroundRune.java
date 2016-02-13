package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public abstract class BlockGroundRune extends AMBlockContainer{
	private EntityPlayer placedBy;
	private Block blockBelow;

	protected BlockGroundRune(){
		super(Material.wood);
		setTickRandomly(true);
		float f = 0.0625F;
		setBlockBounds(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
		setHardness(10);
		setResistance(0.5f);
		//setBlockBounds(f, 0.0F, f, 1.0F - f, 0.0625F, 1.0F - f);
	}

	protected boolean triggerOnCaster(){
		return false;
	}

	@Override
	public abstract TileEntity createNewTileEntity(World var1, int i);

	protected int getCastingMode(World world, BlockPos pos){
		return world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
	}

	public int quantityDropped(Random random){
		return 0;
	}

	public void SetPlacedBy(EntityPlayer placedBy){
		this.placedBy = placedBy;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP || side == EnumFacing.DOWN;
    }

    public int tickRate(){
		return 20;
	}

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock().isNormalCube(worldIn, pos.down()) && worldIn.isAirBlock(pos);
    }

	public boolean placeAt(World world, BlockPos pos, int meta){
		if (!canPlaceBlockAt(world, pos)) return false;
		world.setBlockState(pos, this.getStateFromMeta(meta), 2);
		return true;
	}

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        blockBelow = worldIn.getBlockState(pos).getBlock();
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.isRemote){
            return;
        }
        if (world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)) == 0){
            return;
        }else{
            setStateIfMobInteractsWithPlate(world, pos);
            return;
        }
    }

    public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity){
		if (world.isRemote){
			return;
		}
		setStateIfMobInteractsWithPlate(world, pos);
		return;
	}

	private void setStateIfMobInteractsWithPlate(World world, BlockPos pos){
		float f = 0.125F;
		List list = null;
		list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB((float)pos.getX() - f, pos.getY(), (float)pos.getZ() - f, (float)(pos.getX() + 1 + f), (double)pos.getY() + 2D, (float)(pos.getZ() + 1 + f)));
		if (!triggerOnCaster() && list.contains(placedBy)){
			list.remove(placedBy);
		}
		if (list.size() > 0){
			if (ActivateRune(world, list, pos)){
				int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
				if (!isPermanent(world, pos, meta)){
					int numTriggers = getNumTriggers(world, pos, meta);
					if (--numTriggers <= 0)
						world.setBlockToAir(pos);
					else
						setNumTriggers(world, pos, meta, numTriggers);
				}

			}
		}
	}

	protected abstract boolean ActivateRune(World world, List<Entity> entitiesInRange, BlockPos pos);

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        int l = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        Block m = world.getBlockState(pos).getBlock();
        if (l > 0){
            world.notifyNeighborsOfStateChange(pos, m);
            world.notifyNeighborsOfStateChange(pos.down(), m);
        }
        super.breakBlock(world, pos, state);
    }

    public void setBlockBoundsForItemRender(){
		float f = 0.5F;
		float f1 = 0.125F;
		float f2 = 0.5F;
		setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
	}

	public int getMobilityFlag(){
		return 1;
	}

	protected abstract boolean isPermanent(World world, BlockPos pos, int metadata);

	protected abstract int getNumTriggers(World world, BlockPos pos, int metadata);

	public abstract void setNumTriggers(World world, BlockPos pos, int meta, int numTriggers);
}
