package am2.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.blocks.tileentities.TileEntityInertSpawner;
import am2.guis.ArsMagicaGuiIdList;

public class BlockInertSpawner extends PoweredBlock{

	protected BlockInertSpawner(){
		super(Material.iron);
		setHardness(3.0f);
		setResistance(3.0f);
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing meta, float hitX, float hitY, float hitZ){
		if (this.handleSpecialItems(world, player, pos)){
			return false;
		}
		player.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_INERT_SPAWNER, world, pos.getX(), pos.getY(), pos.getZ());
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_){
		return new TileEntityInertSpawner();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		TileEntityInertSpawner spawner = (TileEntityInertSpawner)world.getTileEntity(pos);
		if (spawner == null)
			return super.getDrops(world, pos, state, fortune);
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.addAll(super.getDrops(world, pos, state, fortune));
		if (spawner.getStackInSlot(0) != null)
			drops.add(spawner.getStackInSlot(0));
		return drops;
	}
}
