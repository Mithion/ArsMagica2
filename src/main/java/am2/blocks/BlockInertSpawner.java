package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityInertSpawner;
import am2.guis.ArsMagicaGuiIdList;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockInertSpawner extends PoweredBlock{

	protected BlockInertSpawner(){
		super(Material.iron);
		setHardness(3.0f);
		setResistance(3.0f);
	}

	@Override
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
	}

	@Override
	public IIcon getIcon(int par1, int par2){
		return Blocks.mob_spawner.getIcon(par1, par2);
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ){
		if (this.handleSpecialItems(world, player, x, y, z)){
			return false;
		}
		FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_INERT_SPAWNER, world, x, y, z);
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_){
		return new TileEntityInertSpawner();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldMetadata){
		TileEntityInertSpawner spawner = (TileEntityInertSpawner)world.getTileEntity(x, y, z);

		//if there is no habitat at the location break out
		if (spawner == null)
			return;

		//if the habitat has a flicker throw it on the ground
		if (spawner.getStackInSlot(0) != null){
			ItemStack stack = spawner.getStackInSlot(0);

			float offsetX = world.rand.nextFloat() * 0.8F + 0.1F;
			float offsetY = world.rand.nextFloat() * 0.8F + 0.1F;
			float offsetZ = world.rand.nextFloat() * 0.8F + 0.1F;
			float force = 0.05F;

			EntityItem entityItem = new EntityItem(world, x + offsetX, y + offsetY, z + offsetZ, stack);
			entityItem.motionX = (float)world.rand.nextGaussian() * force;
			entityItem.motionY = (float)world.rand.nextGaussian() * force + 0.2F;
			entityItem.motionZ = (float)world.rand.nextGaussian() * force;
			world.spawnEntityInWorld(entityItem);
		}

		super.breakBlock(world, x, y, z, oldBlockID, oldMetadata);
	}
}
