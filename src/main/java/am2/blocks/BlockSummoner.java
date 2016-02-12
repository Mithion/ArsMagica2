package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntitySummoner;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;


public class BlockSummoner extends AMSpecialRenderPoweredBlock{

	public BlockSummoner(){
		super(Material.wood);
		setHardness(2.0f);
		setResistance(2.0f);
		setBlockBounds(0.25f, 0.0f, 0.25f, 0.75f, 1.2f, 0.75f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntitySummoner();
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);

        if (handleSpecialItems(world, player, pos)){
            return true;
        }

        if (!world.isRemote){
            if (KeystoneUtilities.HandleKeystoneRecovery(player, (IKeystoneLockable)world.getTileEntity(pos)))
                return true;
            if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)world.getTileEntity(pos), player, KeystoneAccessType.USE)){
                super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
                FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_SUMMONER, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int y = MathHelper.floor_double((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;
        Vec3 look = placer.getLook(1.0f);
        int p = (int)(Math.round(look.yCoord * 0.6) + 1) & 3;

        byte byte0 = 3;

        if (y == 0){
            byte0 = 0;
        }else if (y == 1){
            byte0 = 3;
        }else if (y == 2){
            byte0 = 2;
        }else if (y == 3){
            byte0 = 1;
        }

        if (p == 0){
            byte0 |= 0x4;
        }else if (p == 2){
            byte0 |= 0x8;
        }

        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(byte0), 2); // TODO test this when the mod compiles
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote){
            super.breakBlock(world, pos, state);
            return;
        }
        TileEntitySummoner summoner = (TileEntitySummoner)world.getTileEntity(pos);
        if (summoner == null) return;
        for (int l = 0; l < summoner.getSizeInventory() - 3; l++){
            ItemStack itemstack = summoner.getStackInSlot(l);
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
                ItemStack newStack = new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage());
                if (itemstack.hasTagCompound()){
                    newStack.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }
                EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, newStack);
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
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);
        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

        return super.removedByPlayer(world, pos, player, willHarvest);
    }
}
