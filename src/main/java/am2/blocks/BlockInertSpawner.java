package am2.blocks;

import am2.AMCore;
import am2.blocks.tileentities.TileEntityInertSpawner;
import am2.guis.ArsMagicaGuiIdList;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockInertSpawner extends PoweredBlock{

	protected BlockInertSpawner(){
		super(Material.iron);
		setHardness(3.0f);
		setResistance(3.0f);
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.handleSpecialItems(world, player, pos)){
            return false;
        }
        FMLNetworkHandler.openGui(player, AMCore.instance, ArsMagicaGuiIdList.GUI_INERT_SPAWNER, world, pos.getX(), pos.getY(), pos.getZ());
        return false;
    }

    @Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_){
		return new TileEntityInertSpawner();
	}

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityInertSpawner spawner = (TileEntityInertSpawner)world.getTileEntity(pos);

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

            EntityItem entityItem = new EntityItem(world, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ, stack);
            entityItem.motionX = (float)world.rand.nextGaussian() * force;
            entityItem.motionY = (float)world.rand.nextGaussian() * force + 0.2F;
            entityItem.motionZ = (float)world.rand.nextGaussian() * force;
            world.spawnEntityInWorld(entityItem);
        }

        super.breakBlock(world, pos, state);
    }
}
