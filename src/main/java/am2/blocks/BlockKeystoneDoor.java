package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.api.items.KeystoneAccessType;
import am2.blocks.tileentities.TileEntityKeystoneDoor;
import am2.guis.ArsMagicaGuiIdList;
import am2.items.ItemsCommonProxy;
import am2.lore.CompendiumUnlockHandler;
import am2.utility.KeystoneUtilities;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import java.util.Random;

public class BlockKeystoneDoor extends BlockDoor implements ITileEntityProvider{

	protected BlockKeystoneDoor(){
		super(Material.wood);
		this.setHardness(2.5f);
		this.setResistance(2.0f);
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.getBlockState(pos.down()) == BlocksCommonProxy.keystoneDoor)
            pos = pos.down();

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
                    activateNeighbors(world, pos, state, player, side, hitX, hitY, hitZ);
                    CompendiumUnlockHandler.unlockEntry(this.getUnlocalizedName().replace("arsmagica2:", "").replace("tile.", ""));
                    return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
                }
            }
        }

        return false;
    }

    private void activateNeighbors(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (world.getBlockState(pos.up()) == BlocksCommonProxy.keystoneDoor)
			super.onBlockActivated(world, pos.up(), state, player, side, hitX, hitY, hitZ);

		if (world.getBlockState(pos.west()) == BlocksCommonProxy.keystoneDoor)
			super.onBlockActivated(world, pos.west(), state, player, side, hitX, hitY, hitZ);

		if (world.getBlockState(pos.south()) == BlocksCommonProxy.keystoneDoor)
			super.onBlockActivated(world, pos.south(), state, player, side, hitX, hitY, hitZ);

		if (world.getBlockState(pos.north()) == BlocksCommonProxy.keystoneDoor)
			super.onBlockActivated(world, pos.north(), state, player, side, hitX, hitY, hitZ);
	}

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (world.isRemote)
            return false;

        if (world.getBlockState(pos.down()).getBlock() == BlocksCommonProxy.keystoneDoor)
            pos = pos.down();

        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);

        if (lockable == null)
            return false;

        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK)) return false;

        return super.removedByPlayer(world, pos, player, willHarvest);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (world.isRemote)
            return;

        if (world.getBlockState(pos.down()).getBlock() == BlocksCommonProxy.keystoneDoor)
            pos = pos.down();

        IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(pos);

        if (lockable == null)
            return;

        if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player, KeystoneAccessType.BREAK))
            return;
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemsCommonProxy.itemKeystoneDoor;
    }

    @Override
	public TileEntity createNewTileEntity(World world, int i){
		return new TileEntityKeystoneDoor();
	}
}
