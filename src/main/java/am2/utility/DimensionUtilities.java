package am2.utility;

import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityAstralBarrier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;

public class DimensionUtilities{
	public static void doDimensionTransfer(EntityLivingBase entity, int dimension){

		if (entity instanceof EntityPlayerMP){
			EntityPlayerMP player = (EntityPlayerMP)entity;
			new AMTeleporter(player.mcServer.worldServerForDimension(dimension)).teleport(entity);
		}else{
			entity.worldObj.theProfiler.startSection("changeDimension");
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			int j = entity.dimension;
			WorldServer worldserver = minecraftserver.worldServerForDimension(j);
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimension);
			entity.dimension = dimension;
			entity.worldObj.removeEntity(entity);
			entity.isDead = false;
			entity.worldObj.theProfiler.startSection("reposition");
			minecraftserver.getConfigurationManager().transferEntityToWorld(entity, j, worldserver, worldserver1, new AMTeleporter(worldserver1));
			entity.worldObj.theProfiler.endStartSection("reloading");
			Entity e = EntityList.createEntityByName(EntityList.getEntityString(entity), worldserver1);

			if (e != null){
				e.copyDataFromOld(entity);
				worldserver1.spawnEntityInWorld(e);
			}

			entity.isDead = true;
			entity.worldObj.theProfiler.endSection();
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
			entity.worldObj.theProfiler.endSection();
		}
	}

	public static TileEntityAstralBarrier getBlockingAstralBarrier(World world, BlockPos pos, ArrayList<Long> keys){
		//check for Astral Barrier
		for (int i = -20; i <= 20; ++i){
			for (int j = -20; j <= 20; ++j){
				for (int k = -20; k <= 20; ++k){
					if (world.getBlockState(pos.add(i, j, k)).getBlock() == BlocksCommonProxy.astralBarrier){

						TileEntity te = world.getTileEntity(pos.add(i, j, k));
						if (te == null || !(te instanceof TileEntityAstralBarrier)){
							continue;
						}
						TileEntityAstralBarrier barrier = (TileEntityAstralBarrier)te;

						long barrierKey = KeystoneUtilities.instance.getKeyFromRunes(barrier.getRunesInKey());
						if ((barrierKey != 0 && keys.contains(barrierKey)) || !barrier.IsActive()) continue;

						int dx = pos.getX() - barrier.getPos().getX();
						int dy = pos.getY() - barrier.getPos().getY();
						int dz = pos.getZ() - barrier.getPos().getZ();

						int sqDist = (dx * dx + dy * dy + dz * dz);

						if (sqDist < (barrier.getRadius() * barrier.getRadius())) return barrier;
					}
				}
			}
		}
		return null;
	}
}
