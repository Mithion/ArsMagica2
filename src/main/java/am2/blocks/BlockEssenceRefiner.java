package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityEssenceRefiner;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEssenceRefiner extends PoweredBlock{

	private static boolean keepRefinerInventory = false;

	/*@SideOnly(Side.CLIENT)
	private IIcon[] icons;*/
	private String[] textureNames = {"essence_refiner_side", "essence_refiner_top", "essence_refiner_front_idle", "essence_refiner_front_active"};

	public BlockEssenceRefiner(){
		super(Material.wood);
		setHardness(2.0f);
	}

	/*private void setDefaultDirection(World world, BlockPos pos){
		if (world.isRemote){
			return;
		}
		Block l = world.getBlockState(pos.north()).getBlock();
		Block i1 = world.getBlockState(pos.south()).getBlock();
		Block j1 = world.getBlockState(pos.west()).getBlock();
		Block k1 = world.getBlockState(pos.east()).getBlock();
		byte byte0 = 3;
		if (l.isOpaqueCube() && !i1.isOpaqueCube()){
			byte0 = 3;
		}
		if (i1.isOpaqueCube() && !l.isOpaqueCube()){
			byte0 = 2;
		}
		if (j1.isOpaqueCube() && !k1.isOpaqueCube()){
			byte0 = 5;
		}
		if (k1.isOpaqueCube() && !j1.isOpaqueCube()){
			byte0 = 4;
		}
		//world.setBlockMetadataWithNotify(i, j, k, byte0, 0);
	}*/

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.handleSpecialItems(world, player, pos)){
            return true;
        }
        if (!world.isRemote){
            if (KeystoneUtilities.HandleKeystoneRecovery(player, ((IKeystoneLockable)world.getTileEntity(pos))))
                return true;
            if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)world.getTileEntity(pos), player, KeystoneAccessType.USE)){
                super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
                FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_ESSENCE_REFINER, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) { // TODO metadata stuff
        /*int l = MathHelper.floor_double((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;
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
        }*/
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!keepRefinerInventory){
            TileEntityEssenceRefiner refiner = (TileEntityEssenceRefiner)world.getTileEntity(pos);
            if (refiner == null) return;
            for (int l = 0; l < refiner.getSizeInventory() - 3; l++){
                ItemStack itemstack = refiner.getStackInSlot(l);
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
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);
        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;
        return super.removedByPlayer(world, pos, player, willHarvest);
    }

	@Override
	public TileEntity createNewTileEntity(World par1World, int i){
		return new TileEntityEssenceRefiner();
	}
}
