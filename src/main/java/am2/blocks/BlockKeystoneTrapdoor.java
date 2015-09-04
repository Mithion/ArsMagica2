package am2.blocks;

import am2.AMCore;
import am2.api.blocks.IKeystoneLockable;
import am2.blocks.tileentities.TileEntityKeystoneDoor;
import am2.guis.ArsMagicaGuiIdList;
import am2.texture.ResourceManager;
import am2.utility.KeystoneUtilities;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int direction, float xOffset, float yOffset, float zOffset){
		TileEntity te = world.getTileEntity(x, y, z);

		player.swingItem();

		if (!world.isRemote){
			if (KeystoneUtilities.HandleKeystoneRecovery(player, (IKeystoneLockable)te))
				return true;
			if (KeystoneUtilities.instance.canPlayerAccess((IKeystoneLockable)te, player)){
				if (player.isSneaking()){
					FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_KEYSTONE_LOCKABLE, world, x, y, z);
				}else{
					world.playSoundEffect(x, y, z, "random.door_open", 1.0f, 1.0f);
					return super.onBlockActivated(world, x, y, z, player, direction, xOffset, yOffset, zOffset);
				}
			}
		}

		return false;
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z){
		IKeystoneLockable lockable = (IKeystoneLockable)world.getTileEntity(x, y, z);
		if (!KeystoneUtilities.instance.canPlayerAccess(lockable, player)) return false;

		return super.removedByPlayer(world, player, x, y, z);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
		this.blockIcon = ResourceManager.RegisterTexture("keystone_trapdoor", par1IconRegister);
	}

}
