package am2.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import am2.AMCore;
import am2.blocks.tileentities.TileEntityBrokenPowerLink;
import am2.items.ItemsCommonProxy;
import am2.texture.ResourceManager;

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
	public AxisAlignedBB getCollisionBoundingBox(World par1World, BlockPos pos, IBlockState state){
		return null;
	}

	@Override
	public boolean isReplaceable(World worldIn, BlockPos pos) {
		return true;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, BlockPos pos){
		EntityPlayer player = AMCore.proxy.getLocalPlayer();
		if (player != null){
			if ((pos.equals(new BlockPos(0, 0, 0))) || player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() == ItemsCommonProxy.magitechGoggles){
				this.setBlockBounds(0, 0, 0, 1, 1, 1);
				return;
			}
		}
		this.setBlockBounds(0, 0, 0, 0, 0, 0);
	}

	@Override
	public void setBlockBoundsForItemRender(){
		this.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(World par1World, BlockPos pos){
		if (!par1World.isRemote)
			return new AxisAlignedBB(0, 0, 0, 0, 0, 0);

		EntityPlayer localPlayer = AMCore.proxy.getLocalPlayer();
		if (localPlayer != null){
			if (localPlayer.getCurrentArmor(3) != null && localPlayer.getCurrentArmor(3).getItem() == ItemsCommonProxy.magitechGoggles){
				return super.getCollisionBoundingBox(par1World, pos, par1World.getBlockState(pos));
			}
		}

		return new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	}
	
	@Override
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
		//TODO Fix this, I don't know what meta stands for in this block...
		int meta = world.getBlockMetadata(pos);
		EntityPlayer closest = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5.0);
		if (closest == null && meta != 0){
			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
			world.markBlockForUpdate(pos);
		}else{
			if (meta != 1)
				world.setBlockMetadataWithNotify(x, y, z, 1, 2);
			world.markBlockForUpdate(pos);
		}
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {}
	
	@Override
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public int getRenderType(){
		return -1;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos){
		return null;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ArrayList<ItemStack>();
	}
}
