package am2.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.api.spell.component.interfaces.ISpellComponent;
import am2.blocks.tileentities.TileEntitySpellSealedDoor;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
import am2.utility.KeystoneUtilities;


public class BlockSpellSealedDoor extends BlockDoor implements ITileEntityProvider{


	protected BlockSpellSealedDoor(){
		super(Material.wood);
		this.setHardness(2.5f);
		this.setResistance(2.0f);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing direction, float xOffset, float yOffset, float zOffset){

		if (world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(BlocksCommonProxy.keystoneDoor))
			pos = pos.add(0, -1, 0);

		TileEntity te = world.getTileEntity(pos);

		player.swingItem();

		if (!world.isRemote){

			if (KeystoneUtilities.HandleKeystoneRecovery(player, (IKeystoneLockable)te))
				return true;

			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)te, player, KeystoneAccessType.USE)){
				if (player.isSneaking()){
					player.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_SPELL_SEALED_DOOR, world, pos.getX(), pos.getY(), pos.getZ());
				}else{
					return false;
				}
			}
		}

		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		//intentionally left blank
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
		//intentionally left blank
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		TileEntitySpellSealedDoor door = (TileEntitySpellSealedDoor)world.getTileEntity(pos);
		if (door == null) return super.getDrops(world, pos, state, fortune);
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.addAll(super.getDrops(world, pos, state, fortune));
		drops.add(door.getStackInSlot(3));
		return drops;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.isRemote) {
			super.breakBlock(worldIn, pos, state);
			return;
		}
		worldIn.setBlockToAir(pos.add(0, 1, 0));
		super.breakBlock(worldIn, pos, state);;
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest){
		if (world.isRemote)
			return false;

		if (world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(BlocksCommonProxy.keystoneDoor))
			pos = pos.add(0, -1, 0);

		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);

		if (lockable == null)
			return false;

		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

		return super.removedByPlayer(world, pos, player, willHarvest);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player){
		if (world.isRemote)
			return;

		if (world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(BlocksCommonProxy.keystoneDoor))
			pos = pos.add(0, -1, 0);

		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);

		if (lockable == null)
			return;

		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK))
			return;
		super.onBlockHarvested(world, pos, state, player);
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world,
			BlockPos pos, EntityPlayer player) {
		return new ItemStack(ItemsCommonProxy.itemKeystoneDoor);
	}

	@Override
	public int getDamageValue(World worldIn, BlockPos pos) {
		return ItemsCommonProxy.itemKeystoneDoor.SPELL_SEALED_DOOR;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntitySpellSealedDoor();
	}

	public boolean applyComponentToDoor(IBlockAccess access, ISpellComponent component, BlockPos pos){
		BlockPos newPos = pos;
		
		if (access.getBlockState(pos.add(0, -1, 0)) == BlocksCommonProxy.spellSealedDoor)
			newPos = newPos.add(0, -1, 0);


		TileEntity te = access.getTileEntity(pos);
		if (te == null || te instanceof TileEntitySpellSealedDoor == false){
			return false;
		}

		((TileEntitySpellSealedDoor)te).addPartToCurrentKey(component);

		return true;
	}

	public void setDoorState(World world, BlockPos pos, EntityPlayer player, boolean open){
		int i1 = this.func_150012_g(world, pos);
		int j1 = i1 & 7;
		j1 ^= 4;

		if ((i1 & 8) == 0){
			world.setBlockMetadataWithNotify(x, y, z, j1, 2);
			world.markBlockRangeForRenderUpdate(pos, pos);
		}else{
			world.setBlockMetadataWithNotify(x, y - 1, z, j1, 2);
			world.markBlockRangeForRenderUpdate(pos.add(0, -1, 0), pos);
		}

		world.playAuxSFXAtEntity(player, 1003, pos, 0);
	}
}
