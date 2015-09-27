package am2.blocks;

import am2.blocks.tileentities.TileEntityOtherworldAura;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockOtherworldAura extends PoweredBlock{

	public BlockOtherworldAura(){
		super(Material.circuits);
		setHardness(2.0f);
		setResistance(2.0f);
		setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune){
		//drop nothing
		return new ArrayList<ItemStack>();
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2){
		return new TileEntityOtherworldAura();
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		return 15;
	}


	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider){
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack){
		super.onBlockPlacedBy(world, x, y, z, elb, stack);
		if (elb instanceof EntityPlayer){
			TileEntityOtherworldAura te = (TileEntityOtherworldAura)world.getTileEntity(x, y, z);
			te.setPlacedByUsername(((EntityPlayer)elb).getCommandSenderName());
		}
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.blockRenderID;
	}

	@Override
	public void registerBlockIcons(IIconRegister reg){
		//intentionally do nothing
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}
}
