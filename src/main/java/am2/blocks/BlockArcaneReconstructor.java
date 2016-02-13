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
import am2.blocks.tileentities.TileEntityArcaneReconstructor;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;


public class BlockArcaneReconstructor extends AMSpecialRenderPoweredBlock{

	protected BlockArcaneReconstructor(){
		super(Material.rock);
		setHardness(3.0f);
		setResistance(3.0f);
		setBlockBounds(0, 0, 0, 1, 0.52f, 1);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityArcaneReconstructor();
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9){
		if (handleSpecialItems(par1World, par5EntityPlayer, pos)){
			return true;
		}
		if (!par1World.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, ((IKeystoneLockable)par1World.getTileEntity(pos))))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)par1World.getTileEntity(pos), par5EntityPlayer, KeystoneAccessType.USE)){
				par5EntityPlayer.openGui(AMCore.instance, ArsMagicaGuiIdList.GUI_ARCANE_RECONSTRUCTOR, par1World, pos.getX(), pos.getY(), pos.getZ());
				super.onBlockActivated(par1World, pos, state, par5EntityPlayer, par6, par7, par8, par9);
			}
		}
		return true;
	}
		
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!world.isRemote){
			TileEntityArcaneReconstructor reconstructor = (TileEntityArcaneReconstructor)world.getTileEntity(pos);
			if (reconstructor == null) return;
			for (int l = 0; l < reconstructor.getSizeInventory() - 3; l++){
				ItemStack itemstack = reconstructor.getStackInSlot(l);
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
					EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, newItem);
					float f3 = 0.05F;
					entityitem.motionX = (float)world.rand.nextGaussian() * f3;
					entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				}while (true);
			}
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
