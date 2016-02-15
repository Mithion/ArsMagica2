package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityArcaneDeconstructor;
import am2.guis.ArsMagicaGuiIdList;
import am2.texture.ResourceManager;
import am2.utility.KeystoneUtilities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockArcaneDeconstructor extends PoweredBlock{

	public BlockArcaneDeconstructor(){
		super(Material.iron);
		setHardness(2.0f);
		setResistance(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityArcaneDeconstructor();
	}

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int l = MathHelper.floor_double((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;
        if (l == 0){
            world.setBlockMetadataWithNotify(i, j, k, 2, 2);
        }
        if (l == 1){
            world.setBlockMetadataWithNotify(i, j, k, 5, 2);
        }
        if (l == 2){
            world.setBlockMetadataWithNotify(i, j, k, 3, 2);
        }
        if (l == 3){
            world.setBlockMetadataWithNotify(i, j, k, 4, 2);
        }
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);
        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;
        return super.removedByPlayer(world, pos, player, willHarvest);
    }

    @Override
	public void breakBlock(World world, int i, int j, int k, Block par5, int metadata){
		TileEntityArcaneDeconstructor deconstructor = (TileEntityArcaneDeconstructor)world.getTileEntity(i, j, k);
		if (deconstructor == null) return;
		for (int l = 0; l < deconstructor.getSizeInventory() - 3; l++){
			ItemStack itemstack = deconstructor.getStackInSlot(l);
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
				EntityItem entityitem = new EntityItem(world, i + f, j + f1, k + f2, newItem);
				float f3 = 0.05F;
				entityitem.motionX = (float)world.rand.nextGaussian() * f3;
				entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
				world.spawnEntityInWorld(entityitem);
			}while (true);

		}
		super.breakBlock(world, i, j, k, par5, metadata);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9){
		if (handleSpecialItems(par1World, par5EntityPlayer, par2, par3, par4)){
			return true;
		}
		if (!par1World.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(par5EntityPlayer, ((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4))))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)par1World.getTileEntity(par2, par3, par4), par5EntityPlayer, KeystoneAccessType.USE)){
				FMLNetworkHandler.openGui(par5EntityPlayer, AMCore.instance, ArsMagicaGuiIdList.GUI_ARCANE_DECONSTRUCTOR, par1World, par2, par3, par4);
				super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
			}
		}
		return true;
	}
}
