package am2.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3i;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import am2.AMCore;
import am2.buffs.BuffList;
import am2.entities.EntityBroom;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;

/**
 * This is my invisible utility block.  I use it for illumination (meta 0-2), as well as invisible walls (meta 3-10).
 * Meta 0: low illuminated
 * Meta 1: med illuminated
 * Meta 2: high illuminated
 * Meta 3: only collidable from +x
 * Meta 4: only collidable from -x
 * Meta 5: only collidable from +z
 * Meta 6: only collidable from -z
 * Meta 7: only collidable from +/- x
 * Meta 8: only collidable from +/- z
 * Meta 9: fully collidable
 * Meta 10: special illuminated (light sigil)
 *
 * @author Mithion
 */
public class BlockInvisibleUtility extends AMBlock {
	
	public static final PropertyEnum<EnumInvisibleBlocks> TYPE = PropertyEnum.create("type", EnumInvisibleBlocks.class);
	
	public BlockInvisibleUtility(){
		super(Material.glass);
		this.setBlockBounds(0, 0, 0, 0.01f, 0.01f, 0.01f);
		this.setTickRandomly(true);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, EnumInvisibleBlocks.LOW_ILLUMINATED));
	}

	@Override
	public int tickRate(World par1World){
		return 5;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World par1World, BlockPos pos, IBlockState state){
		EnumInvisibleBlocks bState = state.getValue(TYPE);
		if (bState.equals(EnumInvisibleBlocks.LOW_ILLUMINATED) || bState.equals(EnumInvisibleBlocks.MED_ILLUMINATED) || bState.equals(EnumInvisibleBlocks.HIGH_ILLUMINATED))
			return null;
		return new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(1, 1, 1);
	}

	@Override
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> collisionList, Entity entity){

		if (entity == null || world == null || entity instanceof EntityPlayer || entity instanceof EntityBroom)
			return;

		double distanceThreshold = 1.1;
		double shortDistanceThreshold = 0.1;

		boolean isCollided = false;
		Vec3i vec = new Vec3i(1.25, 1.6, 1.25);
		
		
		if (entity.width < 0.5 || entity.height < 0.5){
			distanceThreshold = 0.5f;
			shortDistanceThreshold = -0.2f;
		}
		
		EnumInvisibleBlocks collisionState = state.getValue(TYPE);
		
		switch (collisionState){
		case POSITIVE_X_COLLISION: //+x
			if (entity.posX > pos.getX() + distanceThreshold){
				collisionList.add(new AxisAlignedBB(pos, pos.add(vec)));
				isCollided = true;
			}
			break;
		case NEGATIVE_X_COLLISION: //-x
			if (entity.posX < pos.getX() - shortDistanceThreshold){
				collisionList.add(new AxisAlignedBB(pos, pos.add(vec)));
				isCollided = true;
			}
			break;
		case POSITIVE_Z_COLLISION: //+z
			if (entity.posZ > pos.getZ() + distanceThreshold){
				collisionList.add(new AxisAlignedBB(pos, pos.add(vec)));
				isCollided = true;
			}
			break;
		case NEGATIVE_Z_COLLISION: //-z
			if (entity.posZ < pos.getZ() - shortDistanceThreshold){
				collisionList.add(new AxisAlignedBB(pos, pos.add(vec)));
				isCollided = true;
			}
			break;
		case FULL_X_COLLISION: //+/- x
			if (entity.posX > pos.getX() + distanceThreshold || entity.posX < pos.getX() - shortDistanceThreshold){
				collisionList.add(new AxisAlignedBB(pos, pos.add(vec)));
				isCollided = true;
			}
			break;
		case FULL_Z_COLLISION: //+/- z
			if (entity.posZ > pos.getZ() + distanceThreshold || entity.posZ < pos.getZ() - shortDistanceThreshold){
				collisionList.add(new AxisAlignedBB(pos, pos.add(vec)));
				isCollided = true;
			}
			break;
		case FULL_COLLISION: //all
			collisionList.add(new AxisAlignedBB(pos, pos.add(vec)));
			isCollided = true;
			break;
		}
		
		if (world.isRemote && isCollided)
			spawnBlockParticles(world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity){
		if (world.isRemote){
			EnumInvisibleBlocks bState = state.getValue(TYPE);
			if (!bState.isIlluminated())
				spawnBlockParticles(world, pos.getX(), pos.getY(), pos.getZ());
		}
	}

	private void spawnBlockParticles(World world, int x, int y, int z){
		AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "symbols", x + 0.5, y + 0.5, z + 0.5);
		if (particle != null){
			particle.addRandomOffset(1, 1.6, 1);
			particle.setParticleScale(0.1f);
			particle.AddParticleController(new ParticleFloatUpward(particle, 0, 0.02f, 1, false));
			particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f));
			particle.setMaxAge(20);
			if (world.rand.nextBoolean()){
				particle.setRGBColorI(0x481bc8);
			}else{
				particle.setRGBColorI(0x891bc8);
			}
		}
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world,
			BlockPos pos, EntityPlayer player) {
		return null;
	}

	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion){
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos,
			EntityPlayer player) {
		return false;
	}

	@Override
	public boolean isAir(IBlockAccess world, BlockPos pos){
		return true;
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos){
		EnumInvisibleBlocks bState = world.getBlockState(pos).getValue(TYPE);
		switch (bState){
		case LOW_ILLUMINATED:
			return 8;
		case MED_ILLUMINATED:
			return 12;
		case HIGH_ILLUMINATED:
		case SPECIAL_ILLUMINATED:
			return 15;
		}
		return 0;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (!world.isRemote){
			world.scheduleBlockUpdate(pos, this, this.tickRate(world), 1);
		}
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
		EnumInvisibleBlocks meta = state.getValue(TYPE);
		if (meta.isIlluminated() && !meta.equals(EnumInvisibleBlocks.SPECIAL_ILLUMINATED)){
			float r = 1.5f;
			List<EntityLivingBase> ents = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.add(-r, -r, -r), pos.add(1 + r, 1 + r, 1 + r)));
			boolean buffNearby = false;
			for (EntityLivingBase ent : ents){
				buffNearby |= ent.isPotionActive(BuffList.illumination.id) ||
						(ent instanceof EntityPlayer &&
								((EntityPlayer)ent).inventory.getCurrentItem() != null &&
								((EntityPlayer)ent).inventory.getCurrentItem().getItem() == ItemsCommonProxy.wardingCandle);
			}
			if (!buffNearby && world.getBlockState(pos).equals(this))
				world.setBlockToAir(pos);

			world.scheduleBlockUpdate(pos, this, this.tickRate(world), 1);
		}
	}

	@Override
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand){
		EnumInvisibleBlocks meta = state.getValue(TYPE);
		if (world.rand.nextInt(10) < 3 && world.isRemote && meta.isIlluminated()){
			BlockPos pos1 = pos.add(-0.2, -0.2, -0.2);
			BlockPos pos2 = pos.add(1.2, 1.2, 1.2);
			List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos1, pos2));
			if (ents.size() > 0){
				spawnBlockParticles(world, pos.getX(), pos.getY(), pos.getZ());
			}
		}
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		EnumInvisibleBlocks meta = world.getBlockState(pos).getValue(TYPE);
		if (!meta.isIlluminated() || meta.equals(EnumInvisibleBlocks.SPECIAL_ILLUMINATED))
			return 100f;
		return super.getExplosionResistance(world, pos, exploder, explosion);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ArrayList<ItemStack>();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, EnumInvisibleBlocks.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).ordinal();
	}
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, TYPE);
	}
	
	public static enum EnumInvisibleBlocks implements IStringSerializable {
		LOW_ILLUMINATED,
		MED_ILLUMINATED,
		HIGH_ILLUMINATED,
		POSITIVE_X_COLLISION,
		NEGATIVE_X_COLLISION,
		POSITIVE_Z_COLLISION,
		NEGATIVE_Z_COLLISION,
		FULL_X_COLLISION,
		FULL_Z_COLLISION,
		FULL_COLLISION,
		SPECIAL_ILLUMINATED;

		@Override
		public String getName() {
			return name().toLowerCase();
		}
		
		public boolean isIlluminated() {
			return (this.equals(LOW_ILLUMINATED) || this.equals(MED_ILLUMINATED) || this.equals(MED_ILLUMINATED) || this.equals(SPECIAL_ILLUMINATED));
		}
	}
}
