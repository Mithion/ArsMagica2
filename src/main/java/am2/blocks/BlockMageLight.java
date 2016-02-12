package am2.blocks;

import am2.AMCore;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.particles.ParticleGrow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockMageLight extends AMSpecialRenderBlock{

	protected BlockMageLight(){
		super(Material.circuits);
		setBlockBounds(0.35f, 0.35f, 0.35f, 0.65f, 0.65f, 0.65f);
		this.setTickRandomly(true);
	}

	@Override
	public int tickRate(World par1World){
		return 20 - 5 * AMCore.config.getGFXLevel();
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        int color = ItemDye.dyeColors[meta];

        AMParticle particle = (AMParticle)AMCore.instance.proxy.particleManager.spawn(world, "sparkle", pos.getX() + 0.5 + (rand.nextDouble() * 0.2f - 0.1f), pos.getY() + 0.5, pos.getZ() + 0.5 + (rand.nextDouble() * 0.2f - 0.1f));
        if (particle != null){
            particle.setIgnoreMaxAge(false);
            particle.setMaxAge(10 + rand.nextInt(20));
            particle.AddParticleController(new ParticleFloatUpward(particle, 0f, -0.01f, 1, false));
            particle.AddParticleController(new ParticleGrow(particle, -0.005f, 1, false));
            particle.setRGBColorI(color);
        }
        super.randomDisplayTick(world, pos, state, rand);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if (!world.isRemote && player.getCurrentEquippedItem() != null){

			int id = OreDictionary.getOreID(player.getCurrentEquippedItem().getUnlocalizedName());
			if (id > -1){
				List<ItemStack> ores = OreDictionary.getOres(Integer.toString(id));
				for (ItemStack stack : ores){
					if (stack.getItem() == Items.dye){
						world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(player.getCurrentEquippedItem().getItemDamage() % 15), 2);
						break;
					}
				}
			}
		}

		return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
	}

	@Override
	public int getLightValue(){
		return 15;
	}

	@Override
	public int quantityDropped(Random random){
		return 0;
	}

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB(-0.2, -0.2, -0.2, 0.2, 0.2, 0.2);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return null;
    }

    @Override
    public boolean isAir(IBlockAccess world, BlockPos pos) {
        return false;
    }
}
