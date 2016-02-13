package am2.blocks;

import am2.AMCore;
import am2.buffs.BuffList;
import am2.entities.EntityBroom;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class BlockInvisibleUtility extends AMBlock{

	public BlockInvisibleUtility(){
		super(Material.glass);
		this.setBlockBounds(0, 0, 0, 0.01f, 0.01f, 0.01f);
		this.setTickRandomly(true);
	}

	@Override
	public int tickRate(World par1World){
		return 5;
	}

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        if (meta == 0 || meta == 1 || meta == 2)
            return null;
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(1, 1, 1);
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> collisionList, Entity entity) {
        if (entity == null || world == null || entity instanceof EntityPlayer || entity instanceof EntityBroom)
            return;

        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        double distanceThreshold = 1.1;
        double shortDistanceThreshold = 0.1;

        boolean isCollided = false;

        if (entity.width < 0.5 || entity.height < 0.5){
            distanceThreshold = 0.5f;
            shortDistanceThreshold = -0.2f;
        }

        if (meta > 2 && meta < 10){
            switch (meta){
                case 3: //+x
                    if (entity.posX > pos.getX() + distanceThreshold){
                        collisionList.add(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.25, pos.getY() + 1.6, pos.getZ() + 1.25));
                        isCollided = true;
                    }
                    break;
                case 4: //-x
                    if (entity.posX < pos.getX() - shortDistanceThreshold){
                        collisionList.add(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.25, pos.getY() + 1.6, pos.getZ() + 1.25));
                        isCollided = true;
                    }
                    break;
                case 5: //+z
                    if (entity.posZ > pos.getZ() + distanceThreshold){
                        collisionList.add(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.25, pos.getY() + 1.6, pos.getZ() + 1.25));
                        isCollided = true;
                    }
                    break;
                case 6: //-z
                    if (entity.posZ < pos.getZ() - shortDistanceThreshold){
                        collisionList.add(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.25, pos.getY() + 1.6, pos.getZ() + 1.25));
                        isCollided = true;
                    }
                    break;
                case 7: //+/- x
                    if (entity.posX > pos.getX() + distanceThreshold || entity.posX < pos.getX() - shortDistanceThreshold){
                        collisionList.add(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.25, pos.getY() + 1.6, pos.getZ() + 1.25));
                        isCollided = true;
                    }
                    break;
                case 8: //+/- z
                    if (entity.posZ > pos.getZ() + distanceThreshold || entity.posZ < pos.getZ() - shortDistanceThreshold){
                        collisionList.add(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.25, pos.getY() + 1.6, pos.getZ() + 1.25));
                        isCollided = true;
                    }
                    break;
                case 9: //all
                    collisionList.add(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.25, pos.getY() + 1.6, pos.getZ() + 1.25));
                    isCollided = true;
                    break;
            }

            if (world.isRemote && isCollided)
                spawnBlockParticles(world, pos);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn.isRemote) {
            int meta = worldIn.getBlockState(pos).getBlock().getMetaFromState(worldIn.getBlockState(pos));
            if (meta > 2 && meta < 10)
                spawnBlockParticles(worldIn, pos);
        }
    }

	private void spawnBlockParticles(World world, BlockPos pos){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
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
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        return null;
    }

    @Override
	public boolean canDropFromExplosion(Explosion par1Explosion){
		return false;
	}

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean isAir(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos) {
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        switch (meta){
            case 0:
                return 8;
            case 1:
                return 12;
            case 2:
            case 10:
                return 15;
        }
        return 0;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote)
            worldIn.scheduleBlockUpdate(pos, this, this.tickRate(worldIn), 1); // priority??
    }

    @Override
	public boolean isOpaqueCube(){
		return false;
	}

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        if (meta < 3) {
            float r = 1.5f;
            List<EntityLivingBase> ents = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.getX() - r, pos.getY() - r, pos.getZ() - r, pos.getX() + 1 + r, pos.getY() + 1 + r, pos.getZ() + 1 + r));
            boolean buffNearby = false;
            for (EntityLivingBase ent : ents) {
                buffNearby |= ent.isPotionActive(BuffList.illumination.id) ||
                        (ent instanceof EntityPlayer &&
                                ((EntityPlayer) ent).inventory.getCurrentItem() != null &&
                                ((EntityPlayer) ent).inventory.getCurrentItem().getItem() == ItemsCommonProxy.wardingCandle);
            }
            if (!buffNearby && world.getBlockState(pos).getBlock() == this)
                world.setBlockToAir(pos);

            world.scheduleBlockUpdate(pos, this, this.tickRate(world), 1); // priority??
        }
    }

    @Override
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        if (world.rand.nextInt(10) < 3 && world.isRemote && meta >= 3 && meta < 10){
            List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX() - 0.2, pos.getY() - 0.2, pos.getZ() - 0.2, pos.getX() + 1.2, pos.getY() + 1.2, pos.getZ() + 1.2));
            if (ents.size() > 0){
                spawnBlockParticles(world, pos);
            }
        }
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        int meta = world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos));
        if (meta > 2)
            return 100f;
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<ItemStack>();
    }
}
