package am2.blocks;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.bosses.BossSpawnHelper;
import am2.damage.DamageSources;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockInlay extends BlockRailBase{

	private final int material;

	public static final int TYPE_REDSTONE = 0;
	public static final int TYPE_IRON = 1;
	public static final int TYPE_GOLD = 2;

	private static final String minecartGoldInlayKey = "AM2GoldInlayTimer";


	public BlockInlay(int type){
		super(false);
		this.setHardness(1);
		this.setResistance(1);
		this.material = type;
		this.setBlockBounds(0f, 0f, 0f, 1f, 0.01f, 1f);
		this.setLightOpacity(0);
		this.setTickRandomly(true);
	}

	private void setValid(World world, BlockPos pos, boolean valid){
		int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
		if (valid) meta |= 0x8;
		else meta &= ~0x8;
		world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(meta), 2);
	}

    @Override
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));

        if (world.isRemote && world.isAirBlock(pos) && AMCore.config.FullGFX() && rand.nextInt(10) < 4){
            AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, AMCore.config.FullGFX() ? "radiant" : "sparkle2", pos.getX() + rand.nextFloat(), pos.getY(), pos.getZ() + rand.nextFloat());
            if (particle != null){
                particle.setMaxAge(20);
                particle.setParticleScale(AMCore.config.FullGFX() ? 0.015f : 0.15f);
                particle.AddParticleController(new ParticleFloatUpward(particle, 0.01f, -0.025f, 1, false));
                if (this == BlocksCommonProxy.redstoneInlay)
                    particle.setRGBColorF(1.0f, 0.4f, 0.4f);
                else if (this == BlocksCommonProxy.ironInlay)
                    particle.setRGBColorF(1.0f, 1.0f, 1.0f);
                else if (this == BlocksCommonProxy.goldInlay)
                    particle.setRGBColorF(1.0f, 1.0f, 0.2f);
            }
        }
    }

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
    }

    @Override
    public boolean canMakeSlopes(IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public float getRailMaxSpeed(World world, EntityMinecart cart, BlockPos pos) {
        if (this.material == TYPE_REDSTONE)
            return 0.7f;
        return 0.4f;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        worldIn.scheduleBlockUpdate(pos, this, tickRate(worldIn), 1);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        int par2 = pos.getX(), par3 = pos.getY(), par4 = pos.getZ();
        return new AxisAlignedBB(par2 + this.minX, par3 + this.minY, par4 + this.minZ, par2 + this.maxX, par3 + this.maxY, par4 + this.maxZ);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        if (collidingEntity instanceof EntityMinecart)
            return;
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        worldIn.scheduleBlockUpdate(pos, this, tickRate(worldIn), 1);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        checkNeighbors(worldIn, pos);
    }

    private void checkNeighbors(World world, BlockPos pos){ // FIXME this whole method is bad
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
		int myMeta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
		if ((myMeta & 0x8) != 0){
			world.setBlockState(pos, world.getBlockState(pos).getBlock().getStateFromMeta(myMeta & ~0x8), 2);
			for (int i = -1; i <= 1; ++i){
				for (int j = -1; j <= 1; ++j){
					if (world.getBlockState(new BlockPos(x + i, y, z + j)).getBlock() == this && (world.getBlockState(new BlockPos(x + i, y, z + j)).getBlock().getMetaFromState(world.getBlockState(pos)) & 0x8) != 0){
						checkNeighbors(world, new BlockPos(x + i, y, z + j));
					}
				}
			}
		}
	}

	@Override
	public int tickRate(World par1World){
		return 60 + par1World.rand.nextInt(80);
	}

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote){
            int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
            if (meta == 6)
                checkPattern(world, pos);
            world.scheduleBlockUpdate(pos, this, tickRate(world), 1);
        }
    }

	private void checkPattern(World world, BlockPos pos){
		boolean allGood = true;
		for (int i = 0; i <= 2; ++i){
			for (int j = 0; j <= 2; ++j){
				if (i == 1 && j == 1) continue;
				allGood &= world.getBlockState(new BlockPos(pos.getX() + i, pos.getY(), pos.getZ() + j)).getBlock()== this; // FIXME ew
			}
		}

		if (allGood){
			if (!world.isRemote){
				if (!checkForIceEffigy(world, pos.getX(), pos.getY(), pos.getZ()))
					checkForLightningEffigy(world, x, y, z);
			}
		}
	}

	private boolean checkForIceEffigy(World world, int x, int y, int z){
		if (world.getBlock(x + 1, y, z + 1) == BlocksCommonProxy.AMOres && world.getBlockMetadata(x + 1, y, z + 1) == BlockAMOre.META_BLUE_TOPAZ_BLOCK){
			if (world.getBlock(x + 1, y + 1, z + 1) == BlocksCommonProxy.AMOres && world.getBlockMetadata(x + 1, y + 1, z + 1) == BlockAMOre.META_BLUE_TOPAZ_BLOCK){
				if (world.getBlock(x + 1, y + 2, z + 1) == Blocks.ice){
					int iceMeta = world.getBlockMetadata(x + 1, y + 2, z + 1);
					if (iceMeta > 0){
						AMCore.proxy.particleManager.RibbonFromPointToPoint(world, x + 1.5, y + 2, z + 1.5, x + 2, y + 3, z + 2);
					}
					if (iceMeta >= 3){
						BossSpawnHelper.instance.onIceEffigyBuilt(world, x + 1, y, z + 1);
					}else{
						List<EntitySnowman> snowmen = world.getEntitiesWithinAABB(EntitySnowman.class, new AxisAlignedBB(x - 9, y - 2, z - 9, x + 10, y + 4, z + 10));
						if (snowmen.size() > 0){
							snowmen.get(0).attackEntityFrom(DamageSources.unsummon, 5000);
							iceMeta++;
							world.setBlockMetadataWithNotify(x + 1, y + 2, z + 1, iceMeta, 2);
							AMCore.proxy.particleManager.BeamFromEntityToPoint(world, snowmen.get(0), x + 1.5, y + 2.5, z + 1.5);
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkForLightningEffigy(World world, int x, int y, int z){
		if (!world.isRaining())
			return false;

		if (world.getBlock(x + 1, y, z + 1) == BlocksCommonProxy.manaBattery){
			if (world.getBlock(x + 1, y + 1, z + 1) == Blocks.iron_bars){
				if (world.getBlock(x + 1, y + 2, z + 1) == Blocks.iron_bars){
					int fenceMeta = world.getBlockMetadata(x + 1, y + 2, z + 1);
					if (fenceMeta > 0){
						AMCore.proxy.particleManager.RibbonFromPointToPoint(world, x + 1.5, y + 2, z + 1.5, x + 2, y + 3, z + 2);
					}
					fenceMeta++;
					world.setBlockMetadataWithNotify(x + 1, y + 2, z + 1, fenceMeta, 2);
					if (!world.isRemote){
						for (int i = 0; i < 5; ++i)
							AMCore.proxy.particleManager.BoltFromPointToPoint(world, x + 1.5, y + 30, z + 1.5, x + 1.5, y + 2, z + 1.5);
						for (int i = 0; i < fenceMeta; ++i)
							world.playSoundEffect(x, y, z, "ambient.weather.thunder", 1.0f, world.rand.nextFloat() + 0.5f);
					}
					if (fenceMeta >= 3){
						BossSpawnHelper.instance.onLightningEffigyBuilt(world, x + 1, y, z + 1);
					}
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos.down()).getBlock();
        return block != null && block.isOpaqueCube();
    }

    @Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}

	@Override
	public void onMinecartPass(World world, EntityMinecart cart, int x, int y, int z){
		if (cart == null) return;

		long millisSinceLastTeleport = cart.getEntityData().getLong(minecartGoldInlayKey);

		int meta = world.getBlockMetadata(x, y, z);
		if (this.material == TYPE_REDSTONE){
			float limit = 2f;
			if (meta == 1){
				if (cart.motionX > 0 && cart.motionX < limit)
					cart.motionX *= 1.1f;
				else if (cart.motionX < 0 && cart.motionX > -limit)
					cart.motionX *= 1.1f;
			}else if (meta == 0){
				if (cart.motionZ > 0 && cart.motionZ < limit)
					cart.motionZ *= 1.1f;
				else if (cart.motionZ < 0 && cart.motionZ > -limit)
					cart.motionZ *= 1.1f;
			}
		}else if (this.material == TYPE_IRON){
			if (meta == 1){
				cart.motionX = -cart.motionX * 0.5f;
				if (cart.motionX < 0)
					cart.setPosition(x - 0.02, cart.posY, cart.posZ);
				else if (cart.motionX > 0)
					cart.setPosition(x + 1.02, cart.posY, cart.posZ);
			}else if (meta == 0){
				cart.motionZ = -cart.motionZ * 0.5f;
				if (cart.motionZ < 0)
					cart.setPosition(cart.posX, cart.posY, z - 0.02);
				else if (cart.motionZ > 0)
					cart.setPosition(cart.posX, cart.posY, z + 1.02);
			}
		}else if (this.material == TYPE_GOLD){
			AMVector3 teleportLocation = null;
			if (meta == 1){
				if (cart.motionX > 0){
					for (int i = 1; i <= 8; ++i){
						if (world.getBlock(x + i, y, z) == BlocksCommonProxy.goldInlay){
							teleportLocation = new AMVector3(x + i, y, z);
							break;
						}
					}
				}else if (cart.motionX < 0){
					for (int i = 1; i <= 8; ++i){
						if (world.getBlock(x - i, y, z) == BlocksCommonProxy.goldInlay){
							teleportLocation = new AMVector3(x - i, y, z);
							break;
						}
					}
				}
			}else if (meta == 0){
				if (cart.motionZ > 0){
					for (int i = 1; i <= 8; ++i){
						if (world.getBlock(x, y, z + i) == BlocksCommonProxy.goldInlay){
							teleportLocation = new AMVector3(x, y, z + i);
							break;
						}
					}
				}else if (cart.motionZ < 0){
					for (int i = 1; i <= 8; ++i){
						if (world.getBlock(x, y, z - i) == BlocksCommonProxy.goldInlay){
							teleportLocation = new AMVector3(x, y, z - i);
							break;
						}
					}
				}
			}
			int teleportMeta = teleportLocation != null ? world.getBlockMetadata((int)teleportLocation.x, (int)teleportLocation.x, (int)teleportLocation.z) : -1;
			long time = System.currentTimeMillis();
			boolean cooldownPassed = (time - millisSinceLastTeleport) > 5000;
			if (teleportLocation != null && (teleportMeta == 1 || teleportMeta == 0) && cooldownPassed){
				world.playSoundEffect(teleportLocation.x, teleportLocation.y, teleportLocation.z, "mob.endermen.portal", 1.0F, 1.0F);
				world.playSoundEffect(cart.posX, cart.posY, cart.posZ, "mob.endermen.portal", 1.0F, 1.0F);
				cart.setPosition(teleportLocation.x, teleportLocation.y, teleportLocation.z);
				cart.getEntityData().setLong(minecartGoldInlayKey, time);
			}
		}
	}
}
