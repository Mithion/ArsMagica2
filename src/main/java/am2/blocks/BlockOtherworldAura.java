package am2.blocks;

import am2.blocks.tileentities.TileEntityOtherworldAura;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockOtherworldAura extends PoweredBlock{

	public BlockOtherworldAura(){
		super(Material.circuits);
		setHardness(2.0f);
		setResistance(2.0f);
		setBlockBounds(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f);
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
	public TileEntity createNewTileEntity(World var1, int var2){
		return new TileEntityOtherworldAura();
	}

    @Override
    public int getLightValue() {
        return 15;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (placer instanceof EntityPlayer) {
            TileEntityOtherworldAura te = (TileEntityOtherworldAura)world.getTileEntity(pos);
            te.setPlacedByUsername(((EntityPlayer)placer).getName());
        }
    }

    @Override
	public int getRenderType(){
		return BlocksCommonProxy.blockRenderID;
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}
}
