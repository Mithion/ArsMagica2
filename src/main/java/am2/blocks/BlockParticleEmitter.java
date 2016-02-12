package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityParticleEmitter;
import am2.items.ItemCrystalWrench;
import am2.items.ItemsCommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockParticleEmitter extends AMBlockContainer{

	protected BlockParticleEmitter(){
		super(Material.glass);
	}

    @Override
    public boolean isAir(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
	public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        TileEntityParticleEmitter tile = (TileEntityParticleEmitter)world.getTileEntity(pos);
        if(tile != null && !tile.getShow())
            return false;

        return super.removedByPlayer(world, pos, player, willHarvest);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        TileEntity te = world.getTileEntity(pos);
        TileEntityParticleEmitter te2 = null;
        if (te instanceof TileEntityParticleEmitter)
            te2 = (TileEntityParticleEmitter)te;
        if (te2 == null)
            super.onBlockExploded(world, pos, explosion);
        if (te2 != null && te2.getShow())
            super.onBlockExploded(world, pos, explosion);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int p = MathHelper.floor_double((placer.rotationYaw * 4F) / 360F + 0.5D) & 3;

        byte byte0 = 3;

        if (p == 0){
            byte0 = 1;
        }
        if (p == 1){
            byte0 = 0;
        }
        if (p == 2){
            byte0 = 3;
        }
        if (p == 3){
            byte0 = 2;
        }
        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(byte0), 2);

        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityParticleEmitter();
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        int meta = access.getBlockState(pos).getBlock().getMetaFromState(access.getBlockState(pos));
        if ((meta & 0x8) == 0x8){
            this.setBlockBounds(0, 0, 0, 0.01f, 0.01f, 0.01f);
        }else{
            this.setBlockBounds(0, 0, 0, 1, 1, 1);
        }
    }

    @Override
	public void setBlockBoundsForItemRender(){
		this.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote){
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te instanceof TileEntityParticleEmitter){
                if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() == ItemsCommonProxy.crystalWrench){
                    if (ItemCrystalWrench.getMode(player.inventory.getCurrentItem()) == 0){
                        AMCore.proxy.openParticleBlockGUI(world, player, (TileEntityParticleEmitter)te);
                    }
                    else{
                        if (AMCore.proxy.cwCopyLoc == null){
                            player.addChatMessage(new ChatComponentText("Settings Copied."));
                            AMCore.proxy.cwCopyLoc = new NBTTagCompound();
                            ((TileEntityParticleEmitter)te).writeSettingsToNBT(AMCore.proxy.cwCopyLoc);
                        }else{
                            player.addChatMessage(new ChatComponentText("Settings Applied."));
                            ((TileEntityParticleEmitter)te).readSettingsFromNBT(AMCore.proxy.cwCopyLoc);
                            ((TileEntityParticleEmitter)te).syncWithServer();
                            AMCore.proxy.cwCopyLoc = null;
                        }
                    }
                    return true;
                }
                else{
                    AMCore.proxy.openParticleBlockGUI(world, player, (TileEntityParticleEmitter)te);
                    return true;
                }
            }
        }
        return false;
    }
}
