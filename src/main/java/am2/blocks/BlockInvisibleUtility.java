package am2.blocks;

import am2.AMCore;
import am2.buffs.BuffList;
import am2.entities.EntityBroom;
import am2.items.ItemsCommonProxy;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleFloatUpward;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4){
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		if (meta == 0 || meta == 1 || meta == 2)
			return null;
		return new AxisAlignedBB(par2, par3, par4, par2 + 1, par3 + 1, par4 + 1).expand(1, 1, 1);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List collisionList, Entity entity){

		if (entity == null || world == null || entity instanceof EntityPlayer || entity instanceof EntityBroom)
			return;

		int meta = world.getBlockMetadata(x, y, z);
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
				if (entity.posX > x + distanceThreshold){
					collisionList.add(new AxisAlignedBB(x, y, z, x + 1.25, y + 1.6, z + 1.25));
					isCollided = true;
				}
				break;
			case 4: //-x
				if (entity.posX < x - shortDistanceThreshold){
					collisionList.add(new AxisAlignedBB(x, y, z, x + 1.25, y + 1.6, z + 1.25));
					isCollided = true;
				}
				break;
			case 5: //+z
				if (entity.posZ > z + distanceThreshold){
					collisionList.add(new AxisAlignedBB(x, y, z, x + 1.25, y + 1.6, z + 1.25));
					isCollided = true;
				}
				break;
			case 6: //-z
				if (entity.posZ < z - shortDistanceThreshold){
					collisionList.add(new AxisAlignedBB(x, y, z, x + 1.25, y + 1.6, z + 1.25));
					isCollided = true;
				}
				break;
			case 7: //+/- x
				if (entity.posX > x + distanceThreshold || entity.posX < x - shortDistanceThreshold){
					collisionList.add(new AxisAlignedBB(x, y, z, x + 1.25, y + 1.6, z + 1.25));
					isCollided = true;
				}
				break;
			case 8: //+/- z
				if (entity.posZ > z + distanceThreshold || entity.posZ < z - shortDistanceThreshold){
					collisionList.add(new AxisAlignedBB(x, y, z, x + 1.25, y + 1.6, z + 1.25));
					isCollided = true;
				}
				break;
			case 9: //all
				collisionList.add(new AxisAlignedBB(x, y, z, x + 1.25, y + 1.6, z + 1.25));
				isCollided = true;
				break;
			}

			if (world.isRemote && isCollided)
				spawnBlockParticles(world, x, y, z);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity){
		if (world.isRemote){
			int meta = world.getBlockMetadata(x, y, z);
			if (meta > 2 && meta < 10)
				spawnBlockParticles(world, x, y, z);
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
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
		return null;
	}

	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion){
		return false;
	}

	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta){
		return false;
	}

	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z){
		return true;
	}

	@Override
	public boolean canRenderInPass(int pass){
		return false;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z){
		int meta = world.getBlockMetadata(x, y, z);
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
	public void onBlockAdded(World world, int x, int y, int z){
		if (!world.isRemote){
			world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
		}
	}

	@Override
	public boolean isOpaqueCube(){
		return false;
	}

	@Override
	public boolean renderAsNormalBlock(){
		return true;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand){
		int meta = world.getBlockMetadata(x, y, z);
		if (meta < 3){
			float r = 1.5f;
			List<EntityLivingBase> ents = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(x - r, y - r, z - r, x + 1 + r, y + 1 + r, z + 1 + r));
			boolean buffNearby = false;
			for (EntityLivingBase ent : ents){
				buffNearby |= ent.isPotionActive(BuffList.illumination.id) ||
						(ent instanceof EntityPlayer &&
								((EntityPlayer)ent).inventory.getCurrentItem() != null &&
								((EntityPlayer)ent).inventory.getCurrentItem().getItem() == ItemsCommonProxy.wardingCandle);
			}
			if (!buffNearby && world.getBlock(x, y, z) == this)
				world.setBlockToAir(x, y, z);

			world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
		}
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand){
		int meta = world.getBlockMetadata(x, y, z);
		if (world.rand.nextInt(10) < 3 && world.isRemote && meta >= 3 && meta < 10){
			List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x - 0.2, y - 0.2, z - 0.2, x + 1.2, y + 1.2, z + 1.2));
			if (ents.size() > 0){
				spawnBlockParticles(world, x, y, z);
			}
		}
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ){
		int meta = world.getBlockMetadata(x, y, z);
		if (meta > 2)
			return 100f;
		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister){
	}

	@Override
	public ArrayList<ItemStack> getDrops(World arg0, int arg1, int arg2, int arg3, int arg4, int arg5){
		return new ArrayList<ItemStack>();
	}
}
