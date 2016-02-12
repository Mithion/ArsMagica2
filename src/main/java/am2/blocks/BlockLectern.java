package am2.blocks;

import am2.blocks.tileentities.TileEntityLectern;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;

public class BlockLectern extends AMSpecialRenderBlockContainer{

	protected BlockLectern(){
		super(Material.wood);
		setHardness(2.0f);
		setResistance(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityLectern();
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);

        TileEntityLectern te = getTileEntity(world, pos);
        if (te == null){
            return true;
        }
        if (te.hasStack()){
            if (player.isSneaking()){
                if (!world.isRemote && ((player instanceof EntityPlayerMP) && ((EntityPlayerMP)player).theItemInWorldManager.getGameType() != GameType.ADVENTURE)){
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                    ItemStack newItem = new ItemStack(te.getStack().getItem(), 1, te.getStack().getItemDamage());
                    if (te.getStack().getTagCompound() != null)
                        newItem.setTagCompound((NBTTagCompound)te.getStack().getTagCompound().copy());
                    EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, newItem);
                    float f3 = 0.05F;
                    entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                    entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                    entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                    world.spawnEntityInWorld(entityitem);
                    te.setStack(null);
                }
            }else{
                te.getStack().getItem().onItemRightClick(te.getStack(), world, player);
            }
        }else{
            if (player.getCurrentEquippedItem() != null){
                if (te.setStack(player.getCurrentEquippedItem())){
                    player.getCurrentEquippedItem().stackSize--;
                    if (player.getCurrentEquippedItem().stackSize <= 0){
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityLectern te = getTileEntity(world, pos);
        if (te == null){
            return;
        }
        if (!world.isRemote){
            if (te.hasStack()){
                float f = world.rand.nextFloat() * 0.8F + 0.1F;
                float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                ItemStack newItem = new ItemStack(te.getStack().getItem(), 1, te.getStack().getItemDamage());
                if (te.getStack().getTagCompound() != null)
                    newItem.setTagCompound((NBTTagCompound)te.getStack().getTagCompound().copy());
                EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, newItem);
                float f3 = 0.05F;
                entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                world.spawnEntityInWorld(entityitem);
            }
        }
        super.breakBlock(world, pos, state);
    }

	private TileEntityLectern getTileEntity(World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityLectern){
			return (TileEntityLectern)te;
		}
		return null;
	}

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int p = MathHelper.floor_double((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;

        byte byte0 = 3;

        if (p == 0){
            byte0 = 4;
        }
        if (p == 1){
            byte0 = 3;
        }
        if (p == 2){
            byte0 = 2;
        }
        if (p == 3){
            byte0 = 1;
        }

        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(byte0), 2);
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }
}
