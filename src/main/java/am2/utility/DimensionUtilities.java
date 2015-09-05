package am2.utility;

import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityAstralBarrier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;

public class DimensionUtilities{
	public static void doDimensionTransfer(EntityLivingBase entity, int dimension){

		if (entity instanceof EntityPlayerMP){
			EntityPlayerMP player = (EntityPlayerMP)entity;
			new AMTeleporter(player.mcServer.worldServerForDimension(dimension)).teleport(entity);
		}else{
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			int j = entity.dimension;
			WorldServer worldserver = minecraftserver.worldServerForDimension(j);
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimension);
			entity.dimension = dimension;
			entity.worldObj.removeEntity(entity);
			entity.isDead = false;

			minecraftserver.getConfigurationManager().transferEntityToWorld(entity, j, worldserver, worldserver1, new AMTeleporter(worldserver1));

			Entity e = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);

			if (e != null){
				e.copyDataFrom(entity, true);
				worldserver1.spawnEntityInWorld(e);
			}

			entity.isDead = true;

			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
		}
	}

	public static TileEntityAstralBarrier GetBlockingAstralBarrier(World world, int x, int y, int z, ArrayList<Long> keys){
		//check for Astral Barrier
		for (int i = -20; i <= 20; ++i){
			for (int j = -20; j <= 20; ++j){
				for (int k = -20; k <= 20; ++k){
					if (world.getBlock(x + i, y + j, z + k) == BlocksCommonProxy.astralBarrier){

						TileEntity te = world.getTileEntity(x + i, y + j, z + k);
						if (te == null || !(te instanceof TileEntityAstralBarrier)){
							continue;
						}
						TileEntityAstralBarrier barrier = (TileEntityAstralBarrier)te;

						long barrierKey = KeystoneUtilities.instance.getKeyFromRunes(barrier.getRunesInKey());
						if ((barrierKey != 0 && keys.contains(barrierKey)) || !barrier.IsActive()) continue;

						int dx = x - barrier.xCoord;
						int dy = y - barrier.yCoord;
						int dz = z - barrier.zCoord;

						int sqDist = (dx * dx + dy * dy + dz * dz);

						if (sqDist < (barrier.getRadius() * barrier.getRadius())) return barrier;
					}
				}
			}
		}
		return null;
	}
}
