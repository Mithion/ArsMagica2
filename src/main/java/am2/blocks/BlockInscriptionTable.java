package am2.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.blocks.tileentities.TileEntityInscriptionTable;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;

public class BlockInscriptionTable extends AMSpecialRenderBlockContainer{
	
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.HORIZONTALS);
	public static final PropertyBool LEFT = PropertyBool.create("left");
	
	public BlockInscriptionTable(){
		super(Material.wood);

		//setTextureFile(AMCore.proxy.getOverrideBlockTexturePath());
		setHardness(2.0f);
		setResistance(2.0f);
		setLightLevel(0.8f);
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.3f, 1.0f);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LEFT, false));
	}
	
	@Override
	public BlockState getBlockState() {
		return new BlockState(this, FACING, LEFT);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal() - 2 + (state.getValue(LEFT) ? 4 : 0);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta % 4 + 2]).withProperty(LEFT, meta >= 4);
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().rotateAround(Axis.Y));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (worldIn.getBlockState(pos.add(state.getValue(FACING).getDirectionVec())).getBlock().isAir(worldIn, pos.add(state.getValue(FACING).getDirectionVec()))) {
			worldIn.setBlockState(pos.add(state.getValue(FACING).getDirectionVec()), state.withProperty(LEFT, true));
		} else {
			worldIn.setBlockToAir(pos);
			if (placer instanceof EntityPlayer) {
				((EntityPlayer)placer).inventory.addItemStackToInventory(new ItemStack(BlocksCommonProxy.inscriptionTable));
			}
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public int getLightValue(){
		return 12;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9){
		super.onBlockActivated(par1World, pos, state, par5EntityPlayer, par6, par7, par8, par9);

		if (par1World.isRemote){
			return true;
		}

		TileEntityInscriptionTable te = (TileEntityInscriptionTable)par1World.getTileEntity(pos);
		TileEntityInscriptionTable tealt = te;

		EnumFacing facing = state.getValue(FACING);
		boolean isLeft = state.getValue(LEFT);
		if (te != null){
			if (!isLeft){
				te = (TileEntityInscriptionTable)par1World.getTileEntity(pos.add(facing.getDirectionVec()));
			}else{
				tealt = (TileEntityInscriptionTable)par1World.getTileEntity(pos.add(-facing.getDirectionVec().getX(), -facing.getDirectionVec().getY(), -facing.getDirectionVec().getZ()));
			}
		}

		if (te == null)
			return true;

		if (te.isInUse(par5EntityPlayer)){
			par5EntityPlayer.addChatMessage(new ChatComponentText("Someone else is using this."));
			return true;
		}

		ItemStack curItem = par5EntityPlayer.getCurrentEquippedItem();
		if (curItem != null && curItem.getItem() == ItemsCommonProxy.inscriptionUpgrade){
			if (te.getUpgradeState() == curItem.getItemDamage()){
				par5EntityPlayer.inventory.setInventorySlotContents(par5EntityPlayer.inventory.currentItem, null);
				te.incrementUpgradeState();
				tealt.incrementUpgradeState();
				return true;
			}
		}

		par5EntityPlayer.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_INSCRIPTION_TABLE, par1World, pos.getX(), pos.getY(), pos.getZ());

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityInscriptionTable();
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		TileEntityInscriptionTable insc = (TileEntityInscriptionTable)world.getTileEntity(pos);
		List<ItemStack> drops = super.getDrops(world, pos, state, fortune);
		if (insc != null) {
			for (int i = 0; i < insc.getSizeInventory();i++) {
				drops.add(insc.getStackInSlot(i));
			}
		}
		return drops;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
		EnumFacing facing = state.getValue(FACING);
		if (state.getValue(LEFT))
			facing = facing.getOpposite();
		
		
		if (world.getBlockState(pos.add(facing.getDirectionVec())).getBlock().equals(this))
			world.setBlockToAir(pos.add(facing.getDirectionVec()));

		super.breakBlock(world, pos, state);
	}
}
