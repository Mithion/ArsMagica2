package am2.utility;

import am2.api.math.AMVector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class AMTeleporter extends Teleporter{

	private final WorldServer instance;

	public AMTeleporter(WorldServer par1WorldServer){
		super(par1WorldServer);
		instance = par1WorldServer;
	}

	public void teleport(EntityLivingBase entity){
		teleport(entity, instance);
	}

	// Move the Entity to the portal
	public void teleport(EntityLivingBase entity, World world){
		// Set Dimension
		if (entity.worldObj.provider.dimensionId != world.provider.dimensionId){
			AMVector3 teleportPos = clearTeleportPath(world, entity);

			entity.motionX = entity.motionY = entity.motionZ = 0.0D;
			entity.fallDistance = 0;
			entity.setPosition(teleportPos.x, teleportPos.y, teleportPos.z);

			MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)entity, world.provider.dimensionId, this);
		}

		//AMCore.log.info("Teleported to dim " + world.provider.dimensionId + ": " + teleportPos.x + "/" + teleportPos.y + "/" + teleportPos.z);
	}

	@Override
	public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8){
	}

	@Override
	public boolean placeInExistingPortal(Entity p_77184_1_, double p_77184_2_, double p_77184_4_, double p_77184_6_, float p_77184_8_){
		return false;
	}

	@Override
	public void removeStalePortalLocations(long p_85189_1_){
	}

	private AMVector3 clearTeleportPath(World world, EntityLivingBase entity){
		AMVector3 vec = new AMVector3(entity);
		vec.x /= world.provider.getMovementFactor();
		vec.y /= world.provider.getMovementFactor();
		vec.z /= world.provider.getMovementFactor();
		if (entity.dimension != -1){
			boolean canFindHigherGround = false;
			vec.y = (float)entity.posY;
			if (vec.y < 5 || vec.y >= world.getActualHeight() - 10)
				vec.y = 5;

			while (true){
				if (world.getBlock((int)vec.x, (int)vec.y, (int)vec.z) == Blocks.air || vec.y >= world.getActualHeight()){
					canFindHigherGround = true;
					break;
				}
				vec.y++;
			}

			if (canFindHigherGround){
				while (world.getBlock((int)vec.x, (int)vec.y - 1, (int)vec.z) == Blocks.air && vec.y > 0){
					vec.y--;
				}
			}else{
				if (vec.y < 5)
					vec.y = 5;
				if (vec.y > world.getActualHeight() - 10)
					vec.y = world.getActualHeight() - 10;

				for (int q = (int)Math.floor(vec.y) - 2; q < vec.y + 1; ++q){
					for (int i = (int)Math.floor(vec.x) - 1; i < vec.x + 1; ++i){
						for (int k = (int)Math.floor(vec.z) - 1; k < vec.z + 1; ++k){
							if (q == (int)Math.floor(vec.y - 2)){
								world.setBlock(i, q, k, Blocks.air);
							}
						}
					}
				}
			}
		}else if (entity.dimension == -1){
			boolean canFindHigherGround = false;
			while (true){
				if (world.getBlock((int)vec.x, (int)vec.y, (int)vec.z) == Blocks.air || vec.y >= 256){
					canFindHigherGround = true;
					break;
				}
				vec.y++;
			}

			if (!canFindHigherGround){
				for (int q = (int)Math.floor(vec.y) - 2; q < vec.y + 1; ++q){
					for (int i = (int)Math.floor(vec.x) - 1; i < vec.x + 1; ++i){
						for (int k = (int)Math.floor(vec.z) - 1; k < vec.z + 1; ++k){
							if (q == (int)Math.floor(vec.y - 2)){
								world.setBlock(i, q, k, Blocks.netherrack);
							}else{
								world.setBlock(i, q, k, Blocks.air);
							}
						}
					}
				}
			}
		}
		return vec;
	}
}
