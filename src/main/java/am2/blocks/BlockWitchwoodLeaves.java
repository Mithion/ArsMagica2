package am2.blocks;

import am2.AMCore;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticlePendulum;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockWitchwoodLeaves extends BlockLeaves{
	protected BlockWitchwoodLeaves(){
		super();
		setHardness(0.2F);
		setLightOpacity(1);
		this.setTickRandomly(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor(){
		return 0xFFFFFF;
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	public int getRenderColor(int par1){
		return 0xFFFFFF;
	}

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return 0xFFFFFF;
    }

    @Override
	public int quantityDropped(Random par1Random){
		return par1Random.nextInt(300) == 0 ? 1 : 0;
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        return drops;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!world.isRemote){
            int j1 = 300;

            if (world.rand.nextInt(j1) == 0){
                dropBlockAsItem(world, pos, BlocksCommonProxy.witchwoodLeaves.getDefaultState(), fortune); // might be broken not sure
            }
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this));
	}

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(this, 1, state.getBlock().getMetaFromState(state));
    }

    @Override
	public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!AMCore.config.witchwoodLeafPFX())
            return;

        if (random.nextInt(300) == 0 && world.isAirBlock(pos.down())){
            AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "leaf", pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble());
            if (particle != null){
                particle.AddParticleController(new ParticleFloatUpward(particle, 0, -0.05f, 1, false));
                particle.setMaxAge(120);
                particle.noClip = false;
                particle.setParticleScale(0.1f + random.nextFloat() * 0.1f);
                particle.AddParticleController(new ParticlePendulum(particle, 0.2f, 0.15f + random.nextFloat() * 0.2f, 2, false));
            }
        }
    }

	/*@Override
	public String[] func_150125_e(){
		return new String[]{"Witchwood"};
	}*/

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return null;
    }
}
