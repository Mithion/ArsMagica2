package am2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityCalefactor;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;

public class BlockCalefactor extends AMSpecialRenderPoweredBlock{

	public BlockCalefactor(){
		super(Material.rock);
		this.setHardness(3.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntityCalefactor();
	}
		
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (handleSpecialItems(world, player, pos)){
			return true;
		}
		if (!world.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(player, ((IKeystoneLockable)world.getTileEntity(pos))))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)world.getTileEntity(pos), player, KeystoneAccessType.USE)){
				super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
				player.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_CALEFACTOR, world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return true;
	}
	
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state){
		if (world.isRemote){
			super.breakBlock(world, pos, state);
			return;
		}
		TileEntityCalefactor calefactor = (TileEntityCalefactor)world.getTileEntity(pos);
		if (calefactor == null) return;
		for (int l = 0; l < calefactor.getSizeInventory() - 3; l++){
			ItemStack itemstack = calefactor.getStackInSlot(l);
			if (itemstack == null){
				continue;
			}
			float f = world.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
			do{
				if (itemstack.stackSize <= 0){
					break;
				}
				int i1 = world.rand.nextInt(21) + 10;
				if (i1 > itemstack.stackSize){
					i1 = itemstack.stackSize;
				}
				itemstack.stackSize -= i1;
				ItemStack newItem = new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage());
				newItem.setTagCompound(itemstack.getTagCompound());
				EntityItem entityitem = new EntityItem(world, pos.getZ() + f, pos.getY() + f1, pos.getX() + f2, newItem);
				float f3 = 0.05F;
				entityitem.motionX = (float)world.rand.nextGaussian() * f3;
				entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
				world.spawnEntityInWorld(entityitem);
			}while (true);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

		return super.removedByPlayer(world, pos, player, willHarvest);
	}
}
