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
import am2.blocks.tileentities.TileEntityEssenceRefiner;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;

public class BlockEssenceRefiner extends PoweredBlock{
	
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);
	
	public BlockEssenceRefiner(){
		super(Material.wood);
		setHardness(2.0f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9){
		if (this.handleSpecialItems(par1World, par5EntityPlayer, pos)){
			return true;
		}
		if (!par1World.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, ((IKeystoneLockable<TileEntityEssenceRefiner>)par1World.getTileEntity(pos))))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable<TileEntityEssenceRefiner>)par1World.getTileEntity(pos), par5EntityPlayer, KeystoneAccessType.USE)){
				super.onBlockActivated(par1World, pos, state, par5EntityPlayer, par6, par7, par8, par9);
				par5EntityPlayer.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_ESSENCE_REFINER, par1World, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), placer, stack);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> stack = new ArrayList<ItemStack>();
		stack.addAll(super.getDrops(world, pos, state, fortune));
		TileEntityEssenceRefiner refiner = (TileEntityEssenceRefiner)world.getTileEntity(pos);
		for (int i = 0; i < refiner.getSizeInventory() - 3; i++) 
			stack.add(refiner.getStackInSlot(i));
		return stack;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest){
		IKeystoneLockable<TileEntityEssenceRefiner> lockable = (IKeystoneLockable<TileEntityEssenceRefiner>)world.getTileEntity(pos);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

		return super.removedByPlayer(world, pos, player, willHarvest);
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityEssenceRefiner();
	}
}
