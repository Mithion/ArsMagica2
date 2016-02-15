package am2.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
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
import am2.AMCore;
import am2.api.math.AMVector3;
import am2.bosses.BossSpawnHelper;
import am2.damage.DamageSources;
import am2.particles.AMParticle;
import am2.particles.ParticleFloatUpward;

public class BlockInlay extends BlockRailBase{
	
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);
	public static final PropertyEnum<EnumRailDirection> SHAPE = PropertyEnum.<EnumRailDirection>create("shape", EnumRailDirection.class,
			EnumRailDirection.EAST_WEST,
			EnumRailDirection.NORTH_EAST,
			EnumRailDirection.NORTH_SOUTH,
			EnumRailDirection.NORTH_WEST,
			EnumRailDirection.SOUTH_EAST,
			EnumRailDirection.SOUTH_WEST);
	
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
		setDefaultState(blockState.getBaseState().withProperty(TYPE, type).withProperty(SHAPE, EnumRailDirection.EAST_WEST));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		EnumRailDirection direction = state.getValue(SHAPE);
		switch (direction) {
		case EAST_WEST: return 0;
		case NORTH_SOUTH: return 1;
		case NORTH_EAST: return 2;
		case NORTH_WEST: return 3;
		case SOUTH_EAST: return 4;
		case SOUTH_WEST: return 5;
		default:
			break;
		}
		return super.getMetaFromState(state);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumRailDirection direction = EnumRailDirection.EAST_WEST;
		switch (meta) {
			case 0: direction = EnumRailDirection.EAST_WEST;
			case 1: direction = EnumRailDirection.NORTH_SOUTH;
			case 2: direction = EnumRailDirection.NORTH_EAST;
			case 3: direction = EnumRailDirection.NORTH_WEST;
			case 4: direction = EnumRailDirection.SOUTH_EAST;
			case 5: direction = EnumRailDirection.SOUTH_WEST;
		}
		return getDefaultState().withProperty(SHAPE, direction).withProperty(TYPE, material);
	}
	
	@Override
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand){
		if (world.isRemote && world.getBlockState(pos.add(0, -1, 0)).getBlock().isAir(world, pos) && AMCore.config.FullGFX() && rand.nextInt(10) < 4){
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
	public float getRailMaxSpeed(World world, EntityMinecart cart, BlockPos pos) {
		int type = world.getBlockState(pos).getValue(TYPE);
		if (type == TYPE_REDSTONE)
			return 0.7f;
		return 0.4f;
	}
	
	
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placingEntity, ItemStack stack){
		super.onBlockPlacedBy(world, pos, state, placingEntity, stack);
		world.scheduleBlockUpdate(pos, this, tickRate(world), 1);
	}
	
	
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return new AxisAlignedBB(pos.add(minX, minY, minZ), pos.add(maxX, maxY, maxZ));
	}
	
	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos,
			IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list,
			Entity collidingEntity) {
		if (collidingEntity instanceof EntityMinecart)
			return;
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		super.onBlockAdded(world, pos, state);
		world.scheduleBlockUpdate(pos, this, tickRate(world), 0);
	}

	@Override
	public int tickRate(World par1World){
		return 60 + par1World.rand.nextInt(80);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
		if (!world.isRemote){
			if (state.getValue(SHAPE).equals(EnumRailDirection.SOUTH_WEST))
				checkPattern(world, pos);
			world.scheduleBlockUpdate(pos, this, tickRate(world), 1);
		}
	}

	private void checkPattern(World world, BlockPos pos){
		boolean allGood = true;
		for (int i = 0; i <= 2; ++i){
			for (int j = 0; j <= 2; ++j){
				if (i == 1 && j == 1) continue;
				allGood &= world.getBlockState(pos.add(i, 0, j)).getBlock().equals(this);
			}
		}

		if (allGood){
			if (!world.isRemote){
				if (!checkForIceEffigy(world, pos))
					checkForLightningEffigy(world, pos);
			}
		}
	}

	private boolean checkForIceEffigy(World world, BlockPos pos){
		if (world.getBlockState(pos.add(1, 0, 1)).equals(BlocksCommonProxy.AMOres.getDefaultState().withProperty(BlockAMOre.TYPE, BlockAMOre.META_BLUE_TOPAZ_BLOCK))){
			if (world.getBlockState(pos.add(1, 1, 1)).equals(BlocksCommonProxy.AMOres.getDefaultState().withProperty(BlockAMOre.TYPE, BlockAMOre.META_BLUE_TOPAZ_BLOCK))){
				if (world.getBlockState(pos.add(1, 2, 1)).equals(Blocks.ice.getDefaultState())){
					List<EntitySnowman> snowmen = world.getEntitiesWithinAABB(EntitySnowman.class, new AxisAlignedBB(pos.add(-9, -2, -9), pos.add(10, 4, 10)));
					//TODO Progress
					if (snowmen.size() >= 4) {
						for (int i = 0; i < 4; i++) {
							snowmen.get(i).attackEntityFrom(DamageSources.unsummon, 5000);
							AMCore.proxy.particleManager.BeamFromEntityToPoint(world, snowmen.get(i), pos.getX() + 1.5, pos.getY() + 2.5, pos.getZ() + 1.5);
						}
						BossSpawnHelper.instance.onIceEffigyBuilt(world, pos.getX() + 1, pos.getY(), pos.getZ() + 1);
					} else if (snowmen.size() > 0) {
						AMCore.proxy.particleManager.RibbonFromPointToPoint(world, pos.getX() + 1.5, pos.getY() + 2, pos.getZ() + 1.5, pos.getX() + 2, pos.getY() + 3, pos.getZ() + 2);
					}
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkForLightningEffigy(World world, BlockPos pos){

		if (!world.isRaining())
			return false;

		if (world.getBlockState(pos.add(1, 0, 1)).getBlock().equals(BlocksCommonProxy.manaBattery)){
			if (world.getBlockState(pos.add(1, 1, 1)).getBlock().equals(Blocks.iron_bars)){
				if (world.getBlockState(pos.add(1, 2, 1)).getBlock().equals(Blocks.iron_bars)){
					int fenceMeta = world.getBlockMetadata(x + 1, y + 2, z + 1);
					if (fenceMeta > 0){
						AMCore.proxy.particleManager.RibbonFromPointToPoint(world, x + 1.5, y + 2, z + 1.5, x + 2, y + 3, z + 2);
					}
					//TODO Progress
					fenceMeta++;
					if (!world.isRemote){
						for (int i = 0; i < 5; ++i)
							AMCore.proxy.particleManager.BoltFromPointToPoint(world, x + 1.5, y + 30, z + 1.5, x + 1.5, y + 2, z + 1.5);
						for (int i = 0; i < fenceMeta; ++i)
							world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "ambient.weather.thunder", 1.0f, world.rand.nextFloat() + 0.5f);
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
	public boolean canPlaceBlockAt(World par1World, BlockPos pos){
		Block block = par1World.getBlockState(pos.add(0, -1, 0)).getBlock();
		return block != null && block.isOpaqueCube();
	}

	@Override
	public int getRenderType(){
		return BlocksCommonProxy.commonBlockRenderID;
	}
	
	@Override
	public void onMinecartPass(World world, EntityMinecart cart, BlockPos pos){
		if (cart == null) return;

		long millisSinceLastTeleport = cart.getEntityData().getLong(minecartGoldInlayKey);

		EnumRailDirection meta = world.getBlockState(pos).getValue(SHAPE);
		if (this.material == TYPE_REDSTONE){
			float limit = 2f;
			if (meta.equals(EnumRailDirection.EAST_WEST)){
				if (cart.motionX > 0 && cart.motionX < limit)
					cart.motionX *= 1.1f;
				else if (cart.motionX < 0 && cart.motionX > -limit)
					cart.motionX *= 1.1f;
			}else if (meta.equals(EnumRailDirection.NORTH_SOUTH)){
				if (cart.motionZ > 0 && cart.motionZ < limit)
					cart.motionZ *= 1.1f;
				else if (cart.motionZ < 0 && cart.motionZ > -limit)
					cart.motionZ *= 1.1f;
			}
		}else if (this.material == TYPE_IRON){
			if (meta.equals(EnumRailDirection.EAST_WEST)){
				cart.motionX = -cart.motionX * 0.5f;
				if (cart.motionX < 0)
					cart.setPosition(pos.getX() - 0.02, cart.posY, cart.posZ);
				else if (cart.motionX > 0)
					cart.setPosition(pos.getX() + 1.02, cart.posY, cart.posZ);
			}else if (meta.equals(EnumRailDirection.NORTH_SOUTH)){
				cart.motionZ = -cart.motionZ * 0.5f;
				if (cart.motionZ < 0)
					cart.setPosition(cart.posX, cart.posY, pos.getZ() - 0.02);
				else if (cart.motionZ > 0)
					cart.setPosition(cart.posX, cart.posY, pos.getZ() + 1.02);
			}
		}else if (this.material == TYPE_GOLD){
			AMVector3 teleportLocation = null;
			if (meta.equals(EnumRailDirection.EAST_WEST)){
				if (cart.motionX > 0){
					for (int i = 1; i <= 8; ++i){
						if (world.getBlockState(pos.add(i, 0, 0)).getBlock().equals(BlocksCommonProxy.goldInlay)){
							teleportLocation = new AMVector3(pos.add(i, 0, 0));
							break;
						}
					}
				}else if (cart.motionX < 0){
					for (int i = 1; i <= 8; ++i){
						if (world.getBlockState(pos.add(-i, 0, 0)).getBlock().equals(BlocksCommonProxy.goldInlay)){
							teleportLocation = new AMVector3(pos.add(-i, 0, 0));
							break;
						}
					}
				}
			}else if (meta.equals(EnumRailDirection.NORTH_SOUTH)){
				if (cart.motionZ > 0){
					for (int i = 1; i <= 8; ++i){
						if (world.getBlockState(pos.add(0, 0, i)).getBlock().equals(BlocksCommonProxy.goldInlay)){
							teleportLocation = new AMVector3(pos.add(0, 0, i));
							break;
						}
					}
				}else if (cart.motionZ < 0){
					for (int i = 1; i <= 8; ++i){
						if (world.getBlockState(pos.add(0, 0, -i)).getBlock().equals(BlocksCommonProxy.goldInlay)){
							teleportLocation = new AMVector3(pos.add(0, 0, -i));
							break;
						}
					}
				}
			}
			EnumRailDirection teleportMeta = teleportLocation != null ? world.getBlockState(teleportLocation.toBlockPos()).getValue(SHAPE) : EnumRailDirection.ASCENDING_NORTH;
			long time = System.currentTimeMillis();
			boolean cooldownPassed = (time - millisSinceLastTeleport) > 5000;
			if (teleportLocation != null && (teleportMeta.equals(EnumRailDirection.NORTH_SOUTH) || teleportMeta.equals(EnumRailDirection.EAST_WEST)) && cooldownPassed){
				world.playSoundEffect(teleportLocation.x, teleportLocation.y, teleportLocation.z, "mob.endermen.portal", 1.0F, 1.0F);
				world.playSoundEffect(cart.posX, cart.posY, cart.posZ, "mob.endermen.portal", 1.0F, 1.0F);
				cart.setPosition(teleportLocation.x, teleportLocation.y, teleportLocation.z);
				cart.getEntityData().setLong(minecartGoldInlayKey, time);
			}
		}
	}

	@Override
	public IProperty<EnumRailDirection> getShapeProperty() {
		return SHAPE;
	}
}
