package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityKeystoneDoor;
import am2.guis.ArsMagicaGuiIdList;
import am2.utility.KeystoneUtilities;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockKeystoneTrapdoor extends BlockTrapDoor implements ITileEntityProvider{

	protected BlockKeystoneTrapdoor(){
		super(Material.wood);
		this.setHardness(2.5f);
		this.setResistance(2.0f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityKeystoneDoor();
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);

        player.swingItem();

        if (!world.isRemote){
            if (KeystoneUtilities.HandleKeystoneRecovery(player, (IKeystoneLockable)te))
                return true;
            if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)te, player, KeystoneAccessType.USE)){
                if (player.isSneaking()){
                    FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_KEYSTONE_LOCKABLE, world, pos.getX(), pos.getY(), pos.getZ());
                }else{
                    world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "random.door_open", 1.0f, 1.0f);
                    return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
                }
            }
        }

        return false;
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);
        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

        return super.removedByPlayer(world, pos, player, willHarvest);
    }
}
