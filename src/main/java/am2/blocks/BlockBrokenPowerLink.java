package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityBrokenPowerLink;
import am2.items.ItemsCommonProxy;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockBrokenPowerLink extends BlockContainer{

	protected BlockBrokenPowerLink(){
		super(Material.circuits);
		setBlockUnbreakable(); // can't be broken, but can be directly replaced, and has no collisions
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityBrokenPowerLink();
	}

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        super.setBlockBoundsBasedOnState(worldIn, pos);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        if (!worldIn.isRemote)
            return new AxisAlignedBB(0, 0, 0, 0, 0, 0);

        EntityPlayer localPlayer = AMCore.proxy.getLocalPlayer();
        if (localPlayer != null){
            if (localPlayer.getCurrentArmor(3) != null && localPlayer.getCurrentArmor(3).getItem() == ItemsCommonProxy.magitechGoggles){
                return super.getSelectedBoundingBox(worldIn, pos);
            }
        }

        return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    }

    @Override
	public void setBlockBoundsForItemRender(){
		this.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

    @Override
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) { // TODO: more metadata when blockstates come later
        /*int meta = world.getBlockMetadata(x, y, z);
        EntityPlayer closest = world.getClosestPlayer(x, y, z, 5.0);
        if (closest == null && meta != 0){
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            world.markBlockForUpdate(x, y, z);
        }else{
            if (meta != 1)
                world.setBlockMetadataWithNotify(x, y, z, 1, 2);
            world.markBlockForUpdate(x, y, z);
        }*/
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {

    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return null;
    }
}
