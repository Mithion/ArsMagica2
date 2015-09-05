package am2.blocks;

import am2.AMCore;
import am2.api.math.AMVector3;
import am2.bosses.BossSpawnHelper;
import am2.damage.DamageSources;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;
import am2.texture.ResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockInlay extends BlockRailBase{

	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

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

	@Override
	public void registerBlockIcons(IIconRegister IIconRegister){
		String[] textures = new String[]{"Inlay_Straight_Redstone", "Inlay_Straight_Iron", "Inlay_Straight_Gold", "Inlay_Corner_Redstone", "Inlay_Corner_Iron", "Inlay_Corner_Gold"};

		icons = new IIcon[textures.length];
		int count = 0;
		for (String s : textures){
			icons[count++] = ResourceManager.RegisterTexture(s, IIconRegister);
		}
	}

	@Override
	public IIcon getIcon(int side, int meta){
		if (side > 1) return null;

		boolean corner = meta >= 6;

		int index = material;
		if (corner) index += 3;

		return icons[index];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5){
		return getIcon(par5, par1iBlockAccess.getBlockMetadata(par2, par3, par4));
	}

	private void setValid(World world, int x, int y, int z, boolean valid){
		int meta = world.getBlockMetadata(x, y, z);
		if (valid) meta |= 0x8;
		else meta &= ~0x8;
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand){
		int meta = world.getBlockMetadata(x, y, z);

		if (world.isRemote && world.getBlock(x, y - 1, z).isAir(world, x, y, z) && AMCore.config.FullGFX() && rand.nextInt(10) < 4){
			AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, AMCore.config.FullGFX() ? "radiant" : "sparkle2", x + rand.nextFloat(), y, z + rand.nextFloat());
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
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
	}

	@Override
	public boolean canMakeSlopes(IBlockAccess world, int x, int y, int z){
		return false;
	}

	@Override
	public float getRailMaxSpeed(World world, EntityMinecart cart, int y, int x, int z){
		if (this.material == TYPE_REDSTONE)
			return 0.7f;
		return 0.4f;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placingEntity, ItemStack stack){
		super.onBlockPlacedBy(world, x, y, z, placingEntity, stack);
		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
		return AxisAlignedBB.getBoundingBox(par2 + this.minX, par3 + this.minY, par4 + this.minZ, par2 + this.maxX, par3 + this.maxY, par4 + this.maxZ);
	}

	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity){
		if (par7Entity instanceof EntityMinecart){
			return;
		}
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block blockID, int meta){
		checkNeighbors(world, x, y, z);
	}

	private void checkNeighbors(World world, int x, int y, int z){
		int myMeta = world.getBlockMetadata(x, y, z);
		if ((myMeta & 0x8) != 0){
			world.setBlockMetadataWithNotify(x, y, z, myMeta & ~0x8, 2);
			for (int i = -1; i <= 1; ++i){
				for (int j = -1; j <= 1; ++j){
					if (world.getBlock(x + i, y, z + j) == this && (world.getBlockMetadata(x + i, y, z + j) & 0x8) != 0){
						checkNeighbors(world, x + i, y, z + j);
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
	public void updateTick(World world, int x, int y, int z, Random rand){
		if (!world.isRemote){
			int meta = world.getBlockMetadata(x, y, z);
			if (meta == 6)
				checkPattern(world, x, y, z);
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		}
	}

	private void checkPattern(World world, int x, int y, int z){
		boolean allGood = true;
		for (int i = 0; i <= 2; ++i){
			for (int j = 0; j <= 2; ++j){
				if (i == 1 && j == 1) continue;
				allGood &= world.getBlock(x + i, y, z + j) == this;
			}
		}

		if (allGood){
			if (!world.isRemote){
				if (!checkForIceEffigy(world, x, y, z))
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
						List<EntitySnowman> snowmen = world.getEntitiesWithinAABB(EntitySnowman.class, AxisAlignedBB.getBoundingBox(x - 9, y - 2, z - 9, x + 10, y + 4, z + 10));
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
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4){
		Block block = par1World.getBlock(par2, par3 - 1, par4);
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
