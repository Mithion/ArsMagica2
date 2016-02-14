package am2.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityArcaneDeconstructor;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;

public class BlockArcaneDeconstructor extends PoweredBlock{
	
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);
	
	public BlockArcaneDeconstructor(){
		super(Material.iron);
		setHardness(2.0f);
		setResistance(2.0f);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityArcaneDeconstructor();
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal() - 2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta + 2]);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos,
			EnumFacing side) {
		return true;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityliving, ItemStack stack){
		super.onBlockPlacedBy(world, pos, state.withProperty(FACING, entityliving.getHorizontalFacing().getOpposite()), entityliving, stack);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest){
		IKeystoneLockable<TileEntityArcaneDeconstructor> lockable = (IKeystoneLockable<TileEntityArcaneDeconstructor>)world.getTileEntity(pos);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

		return super.removedByPlayer(world, pos, player, willHarvest);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		TileEntityArcaneDeconstructor deconstructor = (TileEntityArcaneDeconstructor)world.getTileEntity(pos);
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.addAll(super.getDrops(world, pos, state, fortune));
		if (deconstructor == null) return drops;
		for (int i = 0; i < deconstructor.getSizeInventory(); i++) {
			if (deconstructor.getStackInSlot(i) != null)
				drops.add(deconstructor.getStackInSlot(i));
		}
		return drops;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9){
		if (handleSpecialItems(par1World, par5EntityPlayer, pos)){
			return true;
		}
		if (!par1World.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, ((IKeystoneLockable<TileEntityArcaneDeconstructor>)par1World.getTileEntity(pos))))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable<TileEntityArcaneDeconstructor>)par1World.getTileEntity(pos), par5EntityPlayer, KeystoneAccessType.USE)){
				par5EntityPlayer.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_ARCANE_DECONSTRUCTOR, par1World, pos.getX(), pos.getY(), pos.getZ());
				super.onBlockActivated(par1World, pos, state, par5EntityPlayer, par6, par7, par8, par9);
			}
		}
		return true;
	}
}
