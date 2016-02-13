package am2.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.blocks.tileentities.TileEntityBlackAurem;
import am2.blocks.tileentities.TileEntityCelestialPrism;
import am2.blocks.tileentities.TileEntityObelisk;
import am2.guis.ArsMagicaGuiIdList;

public class BlockEssenceGenerator extends AMSpecialRenderPoweredBlock{

	private int NexusType;

	public static final int NEXUS_STANDARD = 0;
	public static final int NEXUS_DARK = 1;
	public static final int NEXUS_LIGHT = 2;
	
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);

	public BlockEssenceGenerator(int nexusType){
		super(Material.cloth);
		setLightLevel(0.73f);
		setTickRandomly(true);
		setHardness(2f);
		setResistance(2f);
		this.NexusType = nexusType;
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		switch (this.NexusType){
		case NEXUS_STANDARD:
			setBlockBounds(0f, 0.0f, 0f, 1f, 2f, 1f);
			break;
		case NEXUS_LIGHT:
			setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 2f, 1.0f);
			break;
		case NEXUS_DARK:
			setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 2f, 1.0f);
			break;
		}
	}

	private TileEntityObelisk getTileEntity(IBlockAccess blockAccess, BlockPos pos){
		TileEntity te = blockAccess.getTileEntity(pos);
		if (te != null && te instanceof TileEntityObelisk){
			return (TileEntityObelisk)te;
		}
		return null;
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
	public boolean canProvidePower(){
		return true;
	}

	@Override
	public int quantityDropped(Random random){
		return 1;
	}
	


	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity){
		if (this == BlocksCommonProxy.blackAurem)
			return;
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World par1World, BlockPos pos, IBlockState state){
		if (this == BlocksCommonProxy.blackAurem)
			return null;
		return super.getCollisionBoundingBox(par1World, pos, state);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing face, float interactX, float interactY, float interactZ){

		if (handleSpecialItems(world, player, pos))
			return true;

		if (world.getBlockState(pos).getBlock() == BlocksCommonProxy.obelisk)
			player.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_OBELISK, world, pos.getX(), pos.getY(), pos.getZ());

		return super.onBlockActivated(world, pos, state, player, face, interactX, interactY, interactZ);
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		if (this.NexusType == NEXUS_DARK)
			return new TileEntityBlackAurem();
		else if (this.NexusType == NEXUS_LIGHT)
			return new TileEntityCelestialPrism();
		else
			return new TileEntityObelisk();
	}
	
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (this == BlocksCommonProxy.obelisk)
			drops.add(new ItemStack(BlocksCommonProxy.obelisk));
		else if (this == BlocksCommonProxy.blackAurem)
			drops.add(new ItemStack(BlocksCommonProxy.blackAurem));
		else if (this == BlocksCommonProxy.celestialPrism)
			drops.add(new ItemStack(BlocksCommonProxy.celestialPrism));
		TileEntityObelisk obelisk = getTileEntity(world, pos);
		if (obelisk == null) return drops;
		for (int l = 0; l < obelisk.getSizeInventory(); l++)
			drops.add(obelisk.getStackInSlot(l));
		return drops;
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
}
