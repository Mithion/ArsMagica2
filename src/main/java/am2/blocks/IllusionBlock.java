package am2.blocks;

import am2.api.math.AMVector3;
import am2.buffs.BuffList;
import am2.items.ItemsCommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class IllusionBlock extends AMBlock{

	//simple counter used for particle spawning
	private int tickCount;

	//The IIcon to show when "revealed"
	//private IIcon revealedIcon;

	//types represent ethereal or standard illusion blocks
	public static final String[] illusion_block_types = new String[]{"default", "nobarrier"};

	//how far to search for a block to mimic
	private static final byte SEARCH_MAX_DIST = 8;

	public IllusionBlock(){
		super(Material.wood);
		setTickRandomly(true);
		setLightOpacity(255);

		this.setHardness(3.0f);
		this.setResistance(3.0f);
	}


	@Override
	public int tickRate(World par1World){
		return 20;
	}

	/**
	 * Gets the direction the illusion block is facing
	 *
	 * @param blockAccess The block access instance, used to get metadata
	 * @param pos         The position of the block.
	 * @return ForgeDirection representing the forward vector
	 */
	private EnumFacing getFacing(IBlockAccess blockAccess, BlockPos pos){
		int meta = blockAccess.getBlockState(pos).getBlock().getMetaFromState(blockAccess.getBlockState(pos)) & 7;
		return EnumFacing.values()[meta];
	}

	/**
	 * Gets the location of the block to mimic
	 *
	 * @param blockAccess The block access instance, used to get metadata
	 * @param pos         The position of the block.
	 * @return the x,y,z coordinates of the mimic block, or null, if no mimic was found.
	 */
	public AMVector3 getMimicLocation(IBlockAccess blockAccess, BlockPos pos){
		EnumFacing dir = getFacing(blockAccess, pos);
		AMVector3 position = new AMVector3(pos.getX(), pos.getY(), pos.getZ());
		AMVector3 offset = new AMVector3(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ());
		int count = 0;

		while (count++ < SEARCH_MAX_DIST){
			position = position.add(offset);
			Block block = blockAccess.getBlockState(new BlockPos((int)position.x, (int)position.y, (int)position.z)).getBlock();
			if (block != Blocks.air && block != this){
				return position;
			}
		}

		return null;
	}

	/**
	 * Gets the actual block to mimic
	 *
	 * @param blockAccess The block access instance, used to get metadata
	 * @param position    The position of the block to mimic
	 * @return The block instance to mimic, or null if not found.
	 */
	public Block getMimicBlock(IBlockAccess blockAccess, AMVector3 position){
		return blockAccess.getBlockState(new BlockPos((int)position.x, (int)position.y, (int)position.z)).getBlock();
	}

	/**
	 * Is the block at the specified location always passable?
	 *
	 * @return True if the meta of the block has the 4th bit on meta set
	 */
	public boolean alwaysPassable(IBlockAccess blockAccess, BlockPos pos){
		int meta = blockAccess.getBlockState(pos).getBlock().getMetaFromState(blockAccess.getBlockState(pos));
		return (meta & 0x8) == 0x8;
	}

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack stack) {
        //get the nearest 90 degree angle from the placing entity's yaw, as a bit flag
        int yaw = MathHelper.floor_double((entityLiving.rotationYaw * 4F) / 360F + 0.5D) & 3;

        //get the pitch angle, again, as a bit flag
        Vec3 look = entityLiving.getLook(1.0f);
        int pitch = (int)(Math.round(look.yCoord * 0.6) + 1) & 3;

        int meta = 3;

        if (yaw == 0){
            meta = EnumFacing.SOUTH.ordinal();
        }else if (yaw == 1){
            meta = EnumFacing.WEST.ordinal();
        }else if (yaw == 2){
            meta = EnumFacing.NORTH.ordinal();
        }else if (yaw == 3){
            meta = EnumFacing.EAST.ordinal();
        }

        //pitch overrides, as at this point yaw wouldn't matter anyways
        if (pitch == 0){
            meta = EnumFacing.DOWN.ordinal();
        }else if (pitch == 2){
            meta = EnumFacing.UP.ordinal();
        }

        //ethereal blocks are always passable, indicated by the 4th bit of meta
        if (stack.getItemDamage() == 1){
            meta |= 0x8;
        }

        world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(meta), 2);
        super.onBlockPlacedBy(world, pos, state, entityLiving, stack);
    }

    @Override
    public float getBlockHardness(World world, BlockPos pos) {
        AMVector3 mimicLocation = getMimicLocation(world, pos);
        if (mimicLocation != null){
            Block mimicBlock = getMimicBlock(world, mimicLocation);
            if (mimicBlock != null && mimicBlock != Blocks.air){
                pos = new BlockPos((int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z);
                return mimicBlock.getBlockHardness(world, pos);
            }
        }
        return super.getBlockHardness(world, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        AMVector3 mimicLocation = getMimicLocation(world, pos);
        if (mimicLocation != null){
            Block mimicBlock = getMimicBlock(world, mimicLocation);
            if (mimicBlock != null && mimicBlock != Blocks.air){
                return mimicBlock.getExplosionResistance(world, new BlockPos((int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z), exploder, explosion);
            }
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public int damageDropped(IBlockState state) {
        if ((state.getBlock().getMetaFromState(state)& 0x8) == 0x8) return 1;
        return 0;
    }

    @Override
	protected boolean canSilkHarvest(){
		return false;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
	}

	/**
	 * Used in setting up recipes
	 */
	public Object[] GetRecipeComponents(boolean alwaysPassable){
		if (alwaysPassable){
			return new Object[]{
					"BRB", "RGR", "BRB",
					Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
					Character.valueOf('G'), Blocks.glass,
					Character.valueOf('B'), new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_CHIMERITE)
			};
		}else{
			return new Object[]{
					"BRB", "R R", "BRB",
					Character.valueOf('R'), new ItemStack(ItemsCommonProxy.rune, 1, ItemsCommonProxy.rune.META_BLACK),
					Character.valueOf('B'), new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_CHIMERITE)
			};
		}
	}

	public int GetCraftingQuantity(){
		return 4;
	}

    @Override
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random random) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        EntityPlayer closest = world.getClosestPlayer(5, 5, 5, 5);
        if (closest == null){
            world.markBlockForUpdate(pos);
            return;
        }else if (closest.isPotionActive(BuffList.trueSight.id)){
            world.markBlockForUpdate(pos);
            if (tickCount++ == 20){
                this.tickCount = 0;
                for (int x = 0; x < 10; ++x){
                    double movement = 2;
                    world.spawnParticle(EnumParticleTypes.REDSTONE,
                            i + ((random.nextDouble() * 2 - 1) * movement),
                            j + ((random.nextDouble() * 2 - 1) * movement),
                            k + ((random.nextDouble() * 2 - 1) * movement),
                            random.nextDouble() / 4 + 0.75,
                            0.5f,
                            random.nextDouble() / 2 + 0.75);
                }
            }
        }else{
            world.markBlockForUpdate(pos);
        }
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity entity) {
        if (alwaysPassable(world, pos))
            return;

        if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPotionActive(BuffList.trueSight.id)){
            return;
        }
        super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess blockAccess, BlockPos pos, int renderPass) {
        AMVector3 mimicLocation = getMimicLocation(blockAccess, pos);
        if (mimicLocation != null){
            Block mimicBlock = getMimicBlock(blockAccess, mimicLocation);
            if (mimicBlock != null && mimicBlock != Blocks.air){
                return mimicBlock.colorMultiplier(blockAccess, new BlockPos((int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z));
            }
        }
        return super.colorMultiplier(blockAccess, pos, renderPass);
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

	@Override
	public boolean isNormalCube(){
		return false;
	}

	/**
	 * Must not be opaque since the revealed IIcon is transparent; without this these blocks can be used as xray blocks
	 */
	@Override
	public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public int getLightOpacity(IBlockAccess world, BlockPos pos) {
        AMVector3 mimicLocation = getMimicLocation(world, pos);
        if (mimicLocation != null){
            Block mimicBlock = getMimicBlock(world, mimicLocation);
            if (mimicBlock != null && mimicBlock != Blocks.air){
                return mimicBlock.getLightOpacity(world, new BlockPos((int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z));
            }
        }
        return super.getLightOpacity(world, pos);
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        AMVector3 mimicLocation = getMimicLocation(world, pos);
        if (mimicLocation != null){
            Block mimicBlock = getMimicBlock(world, mimicLocation);
            if (mimicBlock != null && mimicBlock != Blocks.air){
                return mimicBlock.getLightValue(world, new BlockPos((int)mimicLocation.x, (int)mimicLocation.y, (int)mimicLocation.z));
            }
        }
        return super.getLightValue(world, pos);
    }

	@Override
	public float getAmbientOcclusionLightValue(){
		return 1.0f;
	}

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        return super.getMixedBrightnessForBlock(worldIn, pos);
    }
}
