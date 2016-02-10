package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityBrokenPowerLink;
import am2.items.ItemsCommonProxy;
import am2.texture.ResourceManager;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
		return null;
	}

	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z){
		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4){
		EntityPlayer player = AMCore.proxy.getLocalPlayer();
		if (player != null){
			if ((par2 == 0 && par3 == 0 && par4 == 0) || player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() == ItemsCommonProxy.magitechGoggles){
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
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
		if (!par1World.isRemote)
			return new AxisAlignedBB(0, 0, 0, 0, 0, 0);

		EntityPlayer localPlayer = AMCore.proxy.getLocalPlayer();
		if (localPlayer != null){
			if (localPlayer.getCurrentArmor(3) != null && localPlayer.getCurrentArmor(3).getItem() == ItemsCommonProxy.magitechGoggles){
				return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
			}
		}

		return new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand){
		int meta = world.getBlockMetadata(x, y, z);
		EntityPlayer closest = world.getClosestPlayer(x, y, z, 5.0);
		if (closest == null && meta != 0){
			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
			world.markBlockForUpdate(x, y, z);
		}else{
			if (meta != 1)
				world.setBlockMetadataWithNotify(x, y, z, 1, 2);
			world.markBlockForUpdate(x, y, z);
		}
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity){
	}

	@Override
	public boolean renderAsNormalBlock(){
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
	public void registerBlockIcons(IIconRegister IIconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("BrokenNodeBlock", IIconRegister);
	}

	@Override
	public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_){
		return this.blockIcon;
	}

	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_){
		return this.blockIcon;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return null;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5){
		return new ArrayList<ItemStack>();
	}
}
