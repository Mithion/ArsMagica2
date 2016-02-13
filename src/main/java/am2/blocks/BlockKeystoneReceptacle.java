package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityKeystoneRecepticle;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemKeystone;
import am2.utility.KeystoneUtilities;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockKeystoneReceptacle extends AMSpecialRenderPoweredBlock{
	public BlockKeystoneReceptacle(){
		super(Material.rock);
		setHardness(4.5f);
		setResistance(10f);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntityKeystoneRecepticle();
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);

        if (handleSpecialItems(world, player, pos)){
            return true;
        }


        TileEntity myTE = world.getTileEntity(pos);
        if (myTE == null || !(myTE instanceof TileEntityKeystoneRecepticle)){
            return true;
        }
        TileEntityKeystoneRecepticle receptacle = (TileEntityKeystoneRecepticle)myTE;

        if (KeystoneUtilities.HandleKeystoneRecovery(player, receptacle)){
            return true;
        }


        if (player.isSneaking()){
            if (!world.isRemote && KeystoneUtilities.instance.canPlayerAccess(receptacle, player, KeystoneAccessType.USE)){
                FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_KEYSTONE_LOCKABLE, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }else{
            if (receptacle.canActivate()){
                long key = 0;
                ItemStack rightClickItem = player.getCurrentEquippedItem();
                if (rightClickItem != null && rightClickItem.getItem() instanceof ItemKeystone){
                    key = ((ItemKeystone)rightClickItem.getItem()).getKey(rightClickItem);
                }
                receptacle.setActive(key);
            }else if (receptacle.isActive()){
                receptacle.deactivate();
            }
        }

        return true;
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

        AMCore.instance.proxy.blocks.registerKeystonePortal(pos, world.provider.getDimensionId());

        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(byte0), 2);

        TileEntityKeystoneRecepticle receptacle = (TileEntityKeystoneRecepticle)world.getTileEntity(pos);
        receptacle.onPlaced();

        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);
        if (KeystoneUtilities.instance.getKeyFromRunes(lockable.getRunesInKey()) != 0){
            if (!world.isRemote)
                player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("am2.tooltip.clearKey")));
            return false;
        }

        return super.removedByPlayer(world, pos, player, willHarvest);
    }
}
